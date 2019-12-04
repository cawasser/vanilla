# History

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

> 0.2.0-SNAPSHOT. dashboard-clj has been pulled into the project for added flexibility
this version also supports '/version' which gets the version number from the server 

> 0.2.1-SNAPSHOT. supports '/services' which gets metadata on available data sources 
from the server and present this data in a model-card so the user can (one day) pick 
a new data source and widget to add to their "personalized" dashboard

> 0.2.1-DEMO. update layout to better support November 2019 Product Owner demo. No
new widgets, just revised default layout

