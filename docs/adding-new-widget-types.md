## Files needed to be added and modified to add a new widget

# Files to be added/created
1. <widgetName>_service.clj - A file on the server side that stores the data of the widget
path of this file = project vanilla -> src -> clj -> vanilla -> widgets -> <widgetName>_service.clj

Sometime when adding a new widget type, it is also necessary to create a new 'data-source' to provide data in the
correct format for consumption by the new widget.

Adding a new data source requires the following steps:

1. add a new namespace for the new data-source
2. implement the "data providing function" in the new name space.

2. <widgetName>_chart/widget.cljs - A file on the client side to add the plot options and register type for the widget
path of this file =  project vanilla -> src -> cljs -> vanilla -> widgets -> <widgetName>_chart/widget.cljs

3. png file = A file that captures the icon of the widget when selecting that widget type inside of data type on the app
After making widget working take a screenshot of the widget, resize it in 128x128 pixels, and store it in its path file.
path of this file = project vanilla -> resources -> public -> images -> <widgetName>-widget.png

# Files to be modified 

1. widget-defs.cljs - Add a widget definition data structure with its name, type, data format, options etc. that gives the display of the widget on the client side in this file at [cljs/vanilla/widget-defs.cljs]()
path of this file = project vanilla -> src -> cljs -> vanilla -> widgets -> widget_defs.cljs

2. core.cljs - Add a register type of the widget that is created inside of start-dashboard function in this file 
path of this file = project vanilla -> src -> cljs -> vanilla -> widgets -> core.cljs

3. core.clj - Add a new row to the "service" data table, specifically a new clause in the `populate-services` function of [vanilla/db/core.clj](/src/clj/vanilla/db/core.clj)
and in the create-services functions to initialize the database. 
Using the REPL, execute the `(initialize-database)` function in the 'Rich Comment' block to actually add the row to the database
path of this file = project vanilla -> src -> clj -> vanilla -> db -> core.clj 
 

# Important Note

It is very important do get the "names' to match between in the service table and
in service-def, and that the :data-format(s) need to match between the services table 
and widget-defs so you can add new services/widgets to the UI; If you change the :data-format \
of an existing service, we need to change the vanilla_default too by running vanilla.db.core using REPL
and then push the revised vanilla_default to the repo.