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

> 0.1.7-SNAPSHOT. added maps and tweaked stoplight to use buttons (better looking). 
this incorporates gwp-map-widget and stoplight changes

> 0.2.0-SNAPSHOT. dashboard-clj has been pulled into the project for added flexibility
this version also supports '/version' which gets the version number from the server 

## Development


#### adding new Highcharts chart types

1.
1.
1.

#### adding new widget types

1. develop code necessary to implement the new widget type 
1. add the widget definition data structure to [cljs/vanilla/widget-defs.cljs]()

#### running in 'development mode' 

- Start server `lein run`
- Start figwheel `lein figwheel`
- Go to http://localhost:5000

## Deployment

Create a deployable uberjar:

    lein uberjar 

#### Testing the Uberjar

    java -jar target/uberjar/vanilla.jar` will run the application form the uberjar

go to http://localhost:5000

### Creating the Docker image

Build the Docker image (the dot _is_ important):

    docker build -t alloc-ui . 


Then, run the image in Docker:

    docker run -d -p 3500:5000 alloc-ui 

The `-d` flag tells docker to disconnect form the running 
process, so you will be sent back to the command prompt and
the `-p` flag sets up a port mapping from 3000 inside the 
container (where we designed our app to listen) and 3500 on 
your local machine, so you can connect to the app in the 
container

Check to see that it is running. run:

    docker ps 

...and look for your image in the list.

Finally, connect to the running image, but point the browser at:

    localhost:3500 
    
Notice that we use port 3500 to connect to the containerized app, 
and not 5000 like when we are developing on your local machine
