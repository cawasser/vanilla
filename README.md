# vanilla

[![contributors](https://img.shields.io/github/contributors/cawasser/vanilla)](https://github.com/cawasser/vanilla/graphs/contributors)
[![activity](https://img.shields.io/github/commit-activity/m/cawasser/vanilla)](https://github.com/cawasser/vanilla/pulse)
[![clojure](https://img.shields.io/badge/made%20with-Clojure-blue.svg?logo=clojure)](https://clojure.org/)
[![version](https://img.shields.io/github/v/tag/cawasser/vanilla)](https://github.com/cawasser/vanilla/tags)


This single-page application (SPA) provides a number of graphical widgets based on [Highcharts](https://highcharts.com)

## History

[look here](docs/history.md)

> 0.3.0-SNAPSHOT. Basic working version with Add/Remove Widgets. Some of the widget/data-source combination don't work
> but there are such wide-spread changes, we need to draw the line somewhere

> 0.3.1-SNAPSHOT. clicking on a widget header allows for changing the header color. Similarly, clicking on the title
> text allow the user to change the color of the text

## Roadmap

### Client-side

- [ ] improve chart conversions between the various :data-format/<> types
- [x] user picker for colors and title, using \[cljsjs/react-color "2.13.8-0"\] [link](http://casesandberg.github.io/react-color/)
- [ ] move some of the viz stuff (labels, etc) to the service definition/data-message to make more declarative (mix-in? where?)
- [ ] figure out how to let the user pick the charts in a stacked or side-by-side (DROPPED)
- [ ] migrate the widget defs to the server and load them dynamically (can this be done with a push from the server? that way the client will get the updates as soon as we put them on the server-farm)
- [ ] login
- [ ] generative testing of the client

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

### Auto-Documentation
[Marginalia](https://github.com/gdeer81/marginalia) and more specifically [lein-marginalia](https://github.com/gdeer81/lein-marginalia)has been added as a plugin to this project to keep a healthy level of documentation of our codebase and architecture. The plugin essentially scans through the project and creates an html "wiki" of how our project operates. It takes all our in line comments and can be added to easily to explain certain namespaces, functions, or design decisions.

 To run the plugin and generate the aforementioned wiki simply use:

 ```
 lein marg
 ```

 This will generate the file: 

 ```
 ./docs/uberdoc.html
 ```

Here is the document it generates:

[marginalia-uberdoc](./docs/uberdoc.html)


#### see also
- [cdk-clj](https://www.youtube.com/watch?v=TbDmupZyuXk)
- [Watch the video from the 2019 Conj!](https://github.com/StediInc/cdk-clj)


## User Guide


## FAQ


[Write the Docs](https://www.writethedocs.org)
