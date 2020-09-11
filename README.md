# vanilla

[![contributors](https://img.shields.io/github/contributors/cawasser/vanilla)](https://github.com/cawasser/vanilla/graphs/contributors)
[![activity](https://img.shields.io/github/commit-activity/m/cawasser/vanilla)](https://github.com/cawasser/vanilla/pulse)
[![clojure](https://img.shields.io/badge/made%20with-Clojure-blue.svg?logo=clojure)](https://clojure.org/)
[![version](https://img.shields.io/github/v/tag/cawasser/vanilla)](https://github.com/cawasser/vanilla/tags)


This single-page application (SPA) provides a number of graphical widgets based on [Highcharts](https://highcharts.com)


## User Guide

### Quick start

Once you have pulled the code from the repository run the following in the command prompt
in the project directory 
  
```shell script
npm install
```
 
This installs all our dependencies.

Now we need to set up our development database:

To start you must first pull the official postgres docker image, this will be used
to create and run postgres containers.

```shell script
docker pull postgres
```

This pulls the official postgres docker image from the repo. Once you have the image
on your machine you don't have to run this again. To check if you already have this image, 
use 'docker images' and check if postgres is listed.

Once you have the postgres image, run the following command:

```shell script
docker run --name postgres -e POSTGRES_PASSWORD=Password -p 5432:5432 -d postgres
```

This will create a container running postgres at port 5432. After this you can always start the above container with:

```shell script
docker start postgres
```


After this you need to start a server by running

```shell script
lein run
```

This will spin up a server to host our code at localhost:5000. Keep the server running to keep the application going,
but you can exit the process by using CTRL + C.


Start up our client-side code with run
```shell script
shadow-cljs watch app
```

You now have a functioning client and server. Leave this command running for the front-end code to
maintain its hot-loading of code changes to cljs file. You can exit this command with CTRL + C.



To review a deeper dive on the above instructions, check out our docs:

- [Running a dev environment](docs/development-mode.md)
- [Database Management](docs/database_management.md)
- [Postgres db](docs/postgres-docker-db.md)



## History

[look here](docs/history.md)

> 0.5.0  - "Epochal time". This version supports data expressed as a series of "snapshots" over time. See [Handling Epochal Time](docs/epochal-time.md)
> for more details.



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
- [ ] [RabitMQ](https://www.rabbitmq.com) support using [Bunnicula](https://github.com/nomnom-insights/nomnom.bunnicula)
- [ ] [Qpid](https://qpid.apache.org) (AMQP) support
- [ ] [Kafka](https://kafka.apache.org) support

## Development

### Writing new code

- [Adding new chart types](docs/adding-new-chart-types.md)
- [Adding new widget types](docs/adding-new-widget-types.md)
- [Supporting different data formats](docs/managing-data-formats.md)
- [Modal Usage](docs/modal_useage.md)
- [Using Excel](docs/excel-and-datascript.md)
- [Using Datascript](docs/excel-and-datascript.md)
- [How Widgets Work](docs/how-widgets-work.md)
- Working at the REPL
  - [On the Server](docs/repl-driven-server.md)
  - [On the Client]()
- [Handling Epochal Time](docs/epochal-time.md)
- [Auto documentation](docs/auto-documentation.md)

### Running the application

- [Running the code in 'Development Mode'](docs/development-mode.md)
- [Deploy to Docker](docs/deploy-to-docker.md)
- [Deploy to AWS](docs/deploy-to-aws.md)
- [Working with AMQP (RabbitMQ/Bunnicula)]()
- [Working with Kafka]()
- [Database Management](docs/database_management.md)



## Architecture

### Generated Diagrams

Diagrams are generated "automatically" by ???

- [System Overview](diagrams/basicSysContx.png)
- [Containers](diagrams/basicContainer.png)
- [Components](diagrams/basicComponent.png)


#### see also
- [cdk-clj](https://www.youtube.com/watch?v=TbDmupZyuXk)
- [Watch the video from the 2019 Conj](https://github.com/StediInc/cdk-clj)
> Note: cdk-clj has been abandoned by the developers.






## FAQ


[Write the Docs](https://www.writethedocs.org)
