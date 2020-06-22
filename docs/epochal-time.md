# Handling Epochal Time


## What is 'Epochal Time'?

["Epochal Time"](https://www.dictionary.com/browse/epoch) can be defined as data which exists as a series
of snapshot across time. For example, a git repository could be thought of as "epochal" - it is organized as a
series of snapshots of the state of
a source code tree. Each ['commit'](https://www.atlassian.com/git/tutorials/saving-changes/git-commit) is a single
"epoch" - it denotes a specific dat/time when the state
of the immutable repository changed. Until the nex commit, the state of the tree is NOT changing.

A [Datomic database](https://docs.datomic.com/cloud/whatis/data-model.html#time-model)
could also be thought of as epochal since each  
transaction demotes a specific state of the entire database at a give date/time.

Epochal data is a key concept in the notion of "planning." To develop a plan for future activities in a
complex 'business' system, it is useful to look at the system as operating in a given steady-state
(an immutable value) which will be changed by the application of specific changes (configurations, new consumers,
new suppliers, etc.). Each configuration change did/will occur at a specific time.

"Epochal Time" data can be implemented using a form of [_Event Sourcing_.](https://dev.to/barryosull/event-sourcing-what-it-is-and-why-its-awesome)



## Materialized View of Epochs

In order to visualize such data for a user, we can't just show the events (changes) themselves; the user would
have to create a mental model of the entire system and the "play computer" to apply the events and understand
how they would change operating state of the system. This is a mch better job for the computer.

In Vanilla, we receive the data as events ("this thing started at this date/time", "that thing ended at that
date/time") and apply them to an initially-empty state. Each change alters the state. Once all the events at a
given date/time are applied, a "snapshot" is made so we can show the "steady-state" to the user.
The next epoch's initial state is the same as the prior epoch's _ending_ state.

To a user, planning is often easiest to consider as a collection of conditions that exist for a specific period
of time, from a 'start time" to an "end time." This is the most colloquial way to express planning data, and we
let the computer translate the more human-friendly format into something more detailed for the computer's use.

In Clojure terms, a simplified model of this operation might look something like this:

``` clojure

; the user thinks in terms of conditions, or scenarios:
(def plans [{:condition "something" :starts #inst "2020-01-01" :ends #inst "2020-02-01"}
            {:condition "something else" :starts #inst "2020-01-05" :ends #inst "2020-01-30"}])
             
; by defining
;       
; :condition -> :event                   
; :start     -> :add
; :end       -> :remove
;
; the system transforms these plans into the events: 
(def events [{:event "something" :add #inst "2020-0101"}
             {:event "something" :remove #inst "2020-02-01"}
             {:event "something else" :add #inst "2020-01-05"}
             {:event "something else" :remove #inst "2020-01-30"}])
             
; and then applies these events to "state" in date/time order:
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
if transacted into a single [Datascript](https://github.com/tonsky/datascript) database inside the server.

The key to working with the data is to understand that every entity in the Datascript DB is tied to one or more epochs,
the one that added it and all epochs from that point until it is removed (obviously the entity is NOT associated
with the ending epoch - it's been removed!) The intermediate epochs are important, and can be counter-intuitive to the user
because they tend to think most in terms of "when did this start" and they don't take the time to perform the
mentally taxing process of "playing computer" to do all the calculations. But once a condition has started, it exists
at every moment unit lit ends, and all those moments are represented by the intermediate epochs.

Look back at the example above. Notice how the `"something"` condition shows up in the `#inst "2020-01-05"` epoch? This is
because it started in the `#inst "2020-01-01"` epoch, but doesn't end until the `#inst "2020-02-01"` epoch. You can see it
is also in the `inst "2020-01-30"` epoch because `"something else"` stopped at that date/time, but `"something"` was
still going strong.

