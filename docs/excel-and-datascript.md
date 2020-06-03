# Excel and Datascript

Vanilla can import data from specially-crafted Microsoft Excel files, using
the [DocJure](https://github.com/mjul/docjure) library and make them
available to data services through then use of the [Datascript]() Clojure library


## Importing from Excel

DocJure is used to access worksheets, rows, columns, and cells to extract data from
a spreadsheet, format it as Clojure data, and make it possible to transact into Datascript

> See also https://github.com/mjul/docjure#usage

For example, "Mission" data is loaded form the "Missions" worksheet:

``` clojure
; open the workbook called "sample.xlsx"
(with-open [workbook (load-workbook-from-resource "sample.xlsx")])
```

Since we are doing this inside the Vanilla server, we expect the spreadsheet to be stored inside the
`resources` folder.

Next we load the data form the "Mission" sheet. Columns A - D are the only ones relevant here, so we
map them to Clojure keywords to make them both easier to work with (every sheet has these same columns) and
to make them semantically meaningful inside the app:

``` clojure
(load-data workbook 
    "Missions" 
    {:A :task-name
     :B :organization
     :C :start-time
     :D :end-time})
```

This call loads the locates the sheet, extracts all the data from the referenced columns
and loads it into a Datascript in-memory database

## Loading Data Into Datascript

From the [readme](https://github.com/tonsky/datascript/blob/master/README.md):

#### What if creating a database would be as cheap as creating a Hashmap?

> "DataScript databases are immutable and based on persistent data structures. In fact, they’re
> more like data structures than databases (think Hashmap). Unlike querying a real SQL DB, when you
> query DataScript, it all comes down to a Hashmap lookup. Or series of lookups. Or array iteration.
> There’s no particular overhead to it. You put a little data in it, it’s fast. You put in a lot of data,
> well, at least it has indexes. That should do better than you filtering an array by hand anyway.
> The thing is really lightweight."

Datascript is, in almost all meaningful ways, an in-memory [Datomic](https://docs.datomic.com/cloud/index.html), except  
there is no transactor (not really meaningful for Vanilla); you can 'transact' (add) data, and you
can query using [Datalog](https://docs.datomic.com/on-prem/query.html#queries)

Let's query the data we just imported from Excel: our Missions. Let's find all the Task Names (ie. `:task-name`):

``` clojure
(datascript.core/q 
  '[:find ?task-name
    :where [_ :task-name ?task-name]]
    @db)

=> #{["Mission-015"]
     ["Mission-011"]
     ["Mission-013"]
     ["Mission-007"]
     ... }
```

Datalog works like a pattern-matcher, finding 'datoms' (facts) in the @db which make
all the 'where' clauses true. In this case, we only care about `:task-name` facts, specifically
their 'values'. We use the `?` to denote names for 'binding' of the values (the '_' means
we don't care about that property of the fact, in this case the 'entity-id', we just want the
value of the `:task-name` fact)

For most of the queries in Vanilla, we want all the relevant data about a given 'entity': its name, start-time, etc.
We *could* just include each attribute in the query, like this:

``` clojure
(datascript.core/q 
  '[:find ?task-name ?organization ?start-time ?stop-time
    :where [?e :task-name ?task-name]
           [?e :organization ?organization]
           [?e :start-time ?start-time]
           [?e :stop-time ?stop-time]]
    @db)
```
> Notice we now _need_ the entity-id, it's what ties all the facts about a given Mission together (like the row in the
> original spreadsheet)

This is fine for a few, small queries, but gets tiresome when things get bigger or there are many more of them.
In Vanilla's case. we usually want all the attributes anyway, so Datascript (and Datomic) provide a simpler
syntax, called 'pull-syntax', to get all the relevant attributes.

Where queries are more like pattern-matching, pull-syntax is more like graph-navigation; given a starting
point, work your way around the "graph" of entities and values and gather up the ones you want.

To use pull-syntax, we change the query to:

``` clojure
(datascript.core/q 
  '[:find [(pull ?e [*]) ...]
    :where [?e :task-name _]]
    @db)
    
=> [{:db/id 2409,
     :end-time #inst"2020-02-25T17:00:00.000-00:00",
     :organization "Orlando",
     :start-time #inst"2020-01-03T18:00:00.000-00:00",
     :task-name "Mission-008"}
    {:db/id 89,
     :end-time #inst"2020-12-25T17:00:00.000-00:00",
     :organization "San Diego",
     :start-time #inst"2020-02-03T18:00:00.000-00:00",
     :task-name "Mission-009"}
     ...]
```

This query says: "find any entities with a `:task-name` attribute, and with the entity-id (?e)
'pull' all the other attributes it has. The `[*]` is the usual wildcard for "all" the attributes.
The `...` means get them all the results. If you leave it off, the query just returns the first
one found.

> Pull Syntax is quite a bit more powerful that what is described here. It can actually do things kind of
> like joins between entities, etc. This discussion is restricted to how we use pull-syntax in Vanilla.
>
>