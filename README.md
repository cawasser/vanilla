# vanilla

This version provides a number of graphical widgets based on [Highcharts](https://highcharts.com)

## History

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

## Development

- Start server `lein run`
- Start figwheel `lein figwheel`
- Go to http://localhost:5000

## Deployment

- `lein uberjar` will create the deployable jar.

#### [Deploy to heroku](https://devcenter.heroku.com/articles/deploying-clojure-applications-with-the-heroku-leiningen-plugin)
