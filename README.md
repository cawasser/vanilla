# vanilla

[![contributors](https://img.shields.io/github/contributors/cawasser/vanilla)](https://github.com/cawasser/vanilla/graphs/contributors)
[![activity](https://img.shields.io/github/commit-activity/m/cawasser/vanilla)](https://github.com/cawasser/vanilla/pulse)
[![clojure](https://img.shields.io/badge/made%20with-Clojure-blue.svg?logo=clojure)](https://clojure.org/)
[![version](https://img.shields.io/github/v/tag/cawasser/vanilla)](https://github.com/cawasser/vanilla/tags)


This single-page application (SPA) provides a number of graphical widgets based on [Highcharts](https://highcharts.com)

## History

[look here](docs/history.md)

> 0.2.2-SQLITE-A/R 002. replacing dashboard-clj library with a more flexible widget mechanism, still based on react-grid-layout.
> also adds re-frame-10x

> 0.2.2-SQLITE-A/R 003. all the basic highcharts types can be added "manually" with the widget button. can't turn on the service
> updates yet - every update causes all the widgets to refresh. next - wire in the source/widget picker and remove the "widget" button

> 0.2.2-SQLITE-A/R 004. source/widget picker "works." many of the source/widget combos work, but there are
> some issues (Relationship (network) and Sankey fail, for example). This is due to the service descriptions not proving a VECTOR of
> :data-format types. Need to implement that in SQLite, among other updates

> 0.2.2-SQLITE-A.R 005. updates to get both [Figwheel](https://github.com/bhauman/lein-figwheel) and [Uberjar](https://stackoverflow.com/questions/11947037/what-is-an-uber-jar) working


## Roadmap

### Client-side

- improve chart conversions between the various :data-format/<> types
- user picker for colors and title, using \[cljsjs/react-color "2.13.8-0"\] [link](http://casesandberg.github.io/react-color/)
- move some of the viz stuff (labels, etc) to the service definition/data-message to make more declarative (mix-in? where?)
- figure out how to let the user pick the charts in a stacked or side-by-side
- migrate the widget defs to the server and load them dynamically (can this be done with a push from the server? that way the client will get the updates as soon as we put them on the server-farm)
- login
- generative testing of the client

### Services-side
- login
- RabitMQ support using [Bunnicula](https://github.com/nomnom-insights/nomnom.bunnicula)
- Kafka support

## Development

- [Adding new chart types](docs/adding-new-chart-types.md)
- [Adding new widget types](docs/adding-new-widget-types.md)
- [Running the code in 'Development Mode'](docs/development-mode.md)
- [Deploy to Docker](docs/deploy-to-docker.md)
- [Deploy to AWS](docs/deploy-to-aws.md)
- [Working with AMQP (RabbitMQ/Bunnicula)]()
- [Working with Kafka]()

## Architecture



#### see also
- [cdk-clj](https://www.youtube.com/watch?v=TbDmupZyuXk)
- [Watch the video from the 2019 Conj!](https://github.com/StediInc/cdk-clj)


## User Guide


## FAQ


[Write the Docs](https://www.writethedocs.org)
