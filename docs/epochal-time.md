# Handling Epochal Time


## What is 'Epochal Time'?

["Epochal Time"](https://www.dictionary.com/browse/epoch) can be defined as data which exists as a series
of snapshot across time. For example, a git repository could be thought of as "epochal" - it is organized as a
series of snapshots of the state of
a source code tree. Each ['commit'](https://www.atlassian.com/git/tutorials/saving-changes/git-commit) is a single
"epoch" - it denotes a specific date/time when the state
of the immutable repository changed. Until the next commit, the state of the tree does NOT change.

A [Datomic database](https://docs.datomic.com/cloud/whatis/data-model.html#time-model)
could also be thought of as epochal, since each
transaction defines a specific state of the entire database at a give date/time.

Epochal data is a key concept in the notion of "planning." To develop a plan for future activities in a
complex 'business' system, it is useful to look at the system as operating in a given steady-state
(an immutable value) which will be changed by the application of specific changes (configurations, new consumers,
new suppliers, etc.) at certain time thrpughput history. Each configuration change did/will occur at a specific time.

"Epochal Time" data can be implemented using a form of [_Event Sourcing_.](https://dev.to/barryosull/event-sourcing-what-it-is-and-why-its-awesome)



## Materialized View of Epochs

In order to visualize such data for a user, we can't just show the events (changes) themselves; the user would
have to create a mental model of the entire system and the "play computer" to apply the events and understand
how they would change the operating state of the system. This is a mch better job for the computer.

In Vanilla, we receive the data as plans ("this thing started at this date/time", "that thing ended at that
date/time"), convert ehm into events, and apply the events to an initially-empty state. Each change alters the state.
Once all the events at a
given date/time are applied, a "snapshot" is made so we can show the "steady-state" to the user by epoch.
The next epoch's initial state is the same as the prior epoch's _ending_ state.

To a user, planning is often easiest to consider as a collection of conditions that exist for a specific period
of time, from a "start time to an "end time." This is the most colloquial way to express planning data, and we
let the computer translate the more human-friendly format into something more detailed for the computer's use.

In Clojure terms, a simplified model of this operation might look something like this:

``` clojure

; the user thinks in terms of plans made up of conditions or scenarios:
(def plans [{:condition "something" :starts #inst "2020-01-01" :ends #inst "2020-02-01"}
            {:condition "something else" :starts #inst "2020-01-05" :ends #inst "2020-01-30"}])
             
; by defining
;       
; :condition -> :event                   
; :start     -> :add
; :end       -> :remove
;
; the system transforms these plans into events: 
(def events [{:event "something" :add #inst "2020-0101"}
             {:event "something" :remove #inst "2020-02-01"}
             {:event "something else" :add #inst "2020-01-05"}
             {:event "something else" :remove #inst "2020-01-30"}])
             
; and then applies these events to "state" in date/time order ro produce:
(def materialized-view
      {#inst "2019-12-31" #{}}                             ; start empty                               
      {#inst "2020-01-01" #{"something"}}                  ; :add "something"
      {#inst "2020-01-05" #{"something" "something else"}} ; :add "something else"
      {#inst "2020-01-30" #{"something"}}                  ; :remove "something else"
      {#inst "2020-02-01" #{}})                            ; :emove "something
```

Notice how each "epoch" gathers together all the relevant events, essentially "playing" them onto the state from the
prior epoch.



## Querying Epochal Data

To simplify dealing with the epochal data when developing the Vanilla Server, all data about all the epochs
is transacted into a single [Datascript](https://github.com/tonsky/datascript) database inside the server.

The key to working with the data is to understand that every entity in the Datascript DB is tied to one or more epochs -
the one that added it and all epochs from that point until it is removed (obviously the entity is NOT associated
with the ending epoch - it's been removed!) The intermediate epochs are important, and can be counter-intuitive to the user
because they tend to think most in terms of "when did this start" and they shouldn't have to take the time to perform the
mentally taxing process of "playing computer" to do all the calculations. But once a condition has started, it exists
at every moment until it ends, and all those moments are represented by the intermediate epochs.

Look back at the example above. Notice how the `"something"` condition shows up in the `#inst "2020-01-05"` epoch? This is
because it started in the `#inst "2020-01-01"` epoch but doesn't end until the `#inst "2020-02-01"` epoch. You can see it
is also in the `#inst "2020-01-30"` epoch, because `"something else"` stopped at that date/time so we have a change in epoch,
but `"something"` was still going strong.

The `vanilla.db.materialized-view` namespace provides a helper function, `query-thread`, to help you manage the transformations
needed to get good data out of the database. `query-thread` takes a single map of up to 3 keys:

key          | purpose
-------------|--------
:q-fn        | A Datascript query function to fetch out your raw data. This is typically a simple query of 1 or 2 bindings to select the correct values and use "pull syntax" to fetch out all the relevant attributes, but you are free to construct your query in any way you see fit.
:map-fn      | A function to map over the query results and extract the relevant attributes. This function is used when the pull returns more data than is really necessary, or in cases where you need to "rename" some of the attributes before sending them to a client. This value is OPTIONAL. If you do not pass a value, the system will `identity` for the extraction, which simply returns all the attributes from the query.
:merge-fn    | A function which returns a hash-map that combines all the values in give epoch together, keyed by the epochs. This value if OPTIONAL. In many cases you can use the default implements (just leave out the k/v pair) which gathers all the values of each epoch into a single-level Clojure `set` (`#{}`).

Of course, use of `query-thread` is optional; you are free to manage the query transformation pipeline yourself.

### Examples

Examples of using `query-thread` can be found in

function        | namespace                    | usage
----------------|------------------------------|------------
`get-beam-data` | [beam-location-service](../src/clj/vanilla/beam_location_service.clj) | Uses custom :map-fn to re-label beam-ids and default for for the merge function (`:merge-fn`).
`get-signal-path-data` | [signal-path-service](../src/clj/vanilla/signal_path_service.clj) | Custom implementations of both `:map-fn` and `:merge-fn`.
`get-mission-data` | [task-service](../src/clj/vanilla/task_service.clj) | Does NOT use `query-thread`. This is a fully custom query and transformation.
`get-terminal-location-data` | [terminal-location-service](../src/clj/vanilla/terminal_location_service.clj) | This is also a fully custom implementation. In this case, we need to combine the results of multiple queries into a single collection for return.

