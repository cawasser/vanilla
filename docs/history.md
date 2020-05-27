# History


## 0.1.x

> 0.1.0-SNAPSHOT - initial baseline directly from [lein-dashboard](https://github.com/multunus/dashboard-clj/blob/master/docs/setting-up-dashboards.md)

> 0.1.1-SNAPSHOT - basic dashboard working. playing now with returning 
large value sets (as literals) to implement charting widgets.

> 0.1.2-SNAPSHOT - added line-chart using highcharts.js and a few simple 
styling things

> 0.1.3-SNAPSHOT - refactoring to allow bar and line to be embedded inside 
other widgets. also added pie-chart, dual-chart (vertically stacked line & bar), 
and side-by-side-chart (horizontally stacked bar and pie)

> 0.1.4-SNAPSHOT - removed cljsjs.highcharts to get uberjar working. just 
using externs.js and embedded highcharts-nnn.js files in index.html

> 0.1.5-SNAPSHOT - complete refactoring of the Highcharts implementation 
to make the code more streamlined and flexible

> 0.1.6-SNAPSHOT - added "dependency wheel", a variation on the sankey diagram. 
this adds another .js dependency. Also, slowed down the update speed of the services

> 0.1.7-SNAPSHOT. added maps and tweaked stoplight to use buttons (better looking). 
this incorporates gwp-map-widget and stoplight changes


## 0.2.x

> 0.2.0-SNAPSHOT. dashboard-clj has been pulled into the project for added flexibility
this version also supports '/version' which gets the version number from the server 

> 0.2.1-SNAPSHOT. supports '/services' which gets metadata on available data sources 
from the server and present this data in a model-card so the user can (one day) pick 
a new data source and widget to add to their "personalized" dashboard

> 0.2.1-DEMO. update layout to better support November 2019 Product Owner demo. No
new widgets, just revised default layout

> 0.2.2-SNAPSHOT. added scatter chart


## The road to Add/Remove Widgets

> 0.2.2-SQLITE-A/R 002. replacing dashboard-clj library with a more flexible widget mechanism, still based on react-grid-layout.
> also adds re-frame-10x

> 0.2.2-SQLITE-A/R 003. all the basic highcharts types can be added "manually" with the widget button. can't turn on the service
> updates yet - every update causes all the widgets to refresh. next - wire in the source/widget picker and remove the "widget" button

> 0.2.2-SQLITE-A/R 004. source/widget picker "works." many of the source/widget combos work, but there are
> some issues (Relationship (network) and Sankey fail, for example). This is due to the service descriptions not proving a VECTOR of
> :data-format types. Need to implement that in SQLite, among other updates

> 0.2.2-SQLITE-A.R 005. updates to get both [Figwheel](https://github.com/bhauman/lein-figwheel) and [Uberjar](https://stackoverflow.com/questions/11947037/what-is-an-uber-jar) working

## 0.3.x

> 0.3.0-SNAPSHOT. Basic working version with Add/Remove Widgets. Some of the widget/data-source combination don't work
> but there are such wide-spread changes, we need to draw the line somewhere

> 0.3.1-SNAPSHOT. clicking on a widget header allows for changing the header color. Similarly, clicking on the title
> text allow the user to change the color of the text

> 0.3.2-SNAPSHOT. the Stoplight and Time widget type is available once again. Also a new "table" widget

> 0.3.3-SNAPSHOT. Addition of widget layout saving and loading from the database

> 0.3.4-SNAPSHOT. New modal so user can "customize" a widget: banner color, title text, and title text color.

> 0.3.5-SNAPSHOT. Added World Map and Continental Australia map widgets.
