# Adding New Widget Types

1. develop code necessary to implement the new widget type
2. add the widget definition data structure to [cljs/vanilla/widget-defs.cljs]()

## Adding New Data Sources

Sometime when adding a new widget type, it is also necessary to create a new 'data-source' to proive data in the
correct format for consumption by the new widget.

Adding a new data source requires the following steps:

1. add a new namespace for the new data-source
2. implement the "data providing function" in the new name space.
3. add a new row to the "service" data table, specifically a new clause in the `populate-services` function of [vanilla/db/core.clj](/src/clj/vanilla/db/core.clj)
4. using the REPL, execute the `(initialize-database)` function in the 'Rich Comment' block to actually add the row to the database





