# vanilla

[![contributors](https://img.shields.io/github/contributors/cawasser/vanilla)](https://github.com/cawasser/vanilla/graphs/contributors)
[![activity](https://img.shields.io/github/commit-activity/m/cawasser/vanilla)](https://github.com/cawasser/vanilla/pulse)
[![clojure](https://img.shields.io/badge/made%20with-Clojure-blue.svg?logo=clojure)](https://clojure.org/)
[![version](https://img.shields.io/github/v/tag/cawasser/vanilla)](https://github.com/cawasser/vanilla/tags)


This single-page application (SPA) provides a number of graphical widgets based on [Highcharts](https://highcharts.com)

## History

[look here](docs/history.md)

> 0.3.3-SNAPSHOT. Addition of widget layout saving and loading from the database

> 0.3.4-SNAPSHOT. New modal so user can "customize" a widget: banner color, title text, and title text color.

> 0.3.5-SNAPSHOT. Added World Map and Continental Australia map widgets.

## Roadmap

### Client-side

- [x] improve chart conversions between the various :data-format/\<\> types
- [x] user picker for colors and title, using [\[cljsjs/react-color "2.13.8-0"\]](http://casesandberg.github.io/react-color/)
- [ ] move some of the viz stuff (labels, etc) to the service definition/data-message to make more declarative (mix-in? where?)
- [ ] allow widgets to publish/subscribe user selection events so multiple widgets can work together
- [x] login
- [x] store widget layout for each user (and restore on next login)
- [ ] `spec` the data structures (`:data-format/*`)
- [ ] generative testing of the client
- [ ] migrate the widget defs to the server and load them dynamically (can this be done with a push from the server? that way the client will get the updates as soon as we put them on the server-farm)

- [ ] ~~figure out how to let the user pick the charts in a stacked or side-by-side~~ (DROPPED)


### Services-side
- [x] login
- [x] reload widgets on user login
- [ ] RabitMQ support using [Bunnicula](https://github.com/nomnom-insights/nomnom.bunnicula)
- [ ] Kafka support

## Development

### Writing new code

- [Adding new chart types](docs/adding-new-chart-types.md)
- [Adding new widget types](docs/adding-new-widget-types.md)
- [Supporting different data formats](docs/managing-data-formats.md)
- [Modal Usage](docs/modal_useage.md)

### Running the application

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


### Generated Diagrams

Diagrams are generated "automatically" by ???

- [System Overview](diagrams/basicSysContx.png)
- [Containers](diagrams/basicContainer.png)
- [Components](diagrams/basicComponent.png)




#### see also
- [cdk-clj](https://www.youtube.com/watch?v=TbDmupZyuXk)
- [Watch the video from the 2019 Conj!](https://github.com/StediInc/cdk-clj)


## User Guide


## FAQ


[Write the Docs](https://www.writethedocs.org)
