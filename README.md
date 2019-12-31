# vanilla

This single-page application (SPA) provides a number of graphical widgets based on [Highcharts](https://highcharts.com)

## History

[look here](docs/history.md)

> 0.2.2-SQLITE-A/R 002. replacing dashboard-clj library with a more flexible widget mechanism, still based on react-grid-layout.
> also adds re-frame-10x

> 0.2.2-SQLITE-A/R 003. all the basic highcharts types can be added "manually" with the widget button. can't turn on the service
> updates yet - every update causes all the widgets to refresh. next - wire in the source/widget picker and remove the "widget" button

> 0.2.2-SQLITE-A/R 004. source/widget picker "works." many of the source/widget combos work, but there are
> some issues (Relationship (network) and Sankey fail, for example)


## Roadmap

> improve compatibility with the :data-formats

> user pick colors and title

> move some of the viz stuff (labels, etc) to the service definition/data-message to make more declarative

> figure out how to let the user pick the charts in a stacked or side-by-side

> migrate the widget defs to the server and load them dynamically (can this be done with a push from the server?
> that way the client will get the updates as soon as we put them on the server-farm)

> Login

> generative testing of the client


## Development

- [Adding new chart types](docs/adding-new-chart-types.md)
- [Adding new widget types](docs/adding-new-widget-types.md)
- [Running the Code in 'Development Mode'](docs/development-mode.md)
- [Deploy to Docker](docs/deploy-to-docker.md)
- [Deploy to AWS](docs/deploy-to-aws.md)

## Architecture



#### see also
- [cdk-clj](https://www.youtube.com/watch?v=TbDmupZyuXk)
- [Watch the video from the 2019 Conj!](https://github.com/StediInc/cdk-clj)


## User Guide


## FAQ


[Write the Docs](https://www.writethedocs.org)
