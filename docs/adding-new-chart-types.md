# Adding New Chart Types

Through 0.3.1-SNAPSHOT, Vanilla widgets takes advantage of the Javascript [Highcharts]() library.

Not all widgets use Highcharts, but a significant number do, including

- [Area Chart](../src/cljs/vanilla/widgets/area_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/area-basic)
- [Bar Chart](../src/cljs/vanilla/widgets/bar_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/bar-basic)
- [Bubble Chart](../src/cljs/vanilla/widgets/bubble_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/bubble)
- [Column Chart](../src/cljs/vanilla/widgets/column_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/column-basic)
- [Depenendcy Wheel Chart](../src/cljs/vanilla/widgets/dependency_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/dependency-wheel)
- [Heatmap Chart](../src/cljs/vanilla/widgets/heatmap_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/heatmap)
- [Line Chart](../src/cljs/vanilla/widgets/line_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/line-basic)
- [Network Chart](../src/cljs/vanilla/widgets/network_graph_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/network-graph)
- [Org Chart](../src/cljs/vanilla/widgets/org_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/organization-chart)
- [Pie Chart](../src/cljs/vanilla/widgets/pie_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/pie-basic)
- [Wind Rose Chart](../src/cljs/vanilla/widgets/rose_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/polar-wind-rose)
- [Scatter Plot Chart](../src/cljs/vanilla/widgets/scatter_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/scatter)
- [Vari-pir Chart](../src/cljs/vanilla/widgets/vari_pie_chart.cljs) [(see Highcharts)](https://www.highcharts.com/demo/variable-radius-pie)


For the remainder of this page, we will examine the implementation of [Bar Chart](../src/cljs/vanilla/widgets/bar_chart.cljs)

## Overview

Highcharts-based content is expressed as a single hash-map of keys relevant to the specific chart
type itself.

For example, Bar Chart is basically:

``` clojure
{:chart/type  :bar-chart
 :chart/supported-formats [:data-format/y :data-format/x-y]
 :chart                   {:type     "bar"
                           :zoomType "x"}
 :yAxis                   {:min    0
                           :title  {:align "high"}
                           :labels {:overflow "justify"}}}
```

Notice that the data is not defined in this structure. The data is merged in as part of the rendering
operation (see `:component-did-update` in [make-chart/make-chart](../src/cljs/vanilla/widgets/make_chart.cljs))


## API

Highcharts-based content is defined in Vanilla in a single namespace with 3 specific functions:

1. `(defn register-type [])` (required)
2. `(defn plot-option [chart-config data options])` (optional, multiple implementations supported)
3. `(defn conversion [ chart-config data options])` (optional, multiple implementations supported)

> Note: `(plot-options ...)` and `(conversion ...)` can actually be named _anything_ you like as they
> are attached to the registration as parameter, much like callbacks.

### register-type

`register-type` adds the basic chart definition to a registry, making it available for use in widget.
At a minimum, `register-type` *must* return a hash-map with the following keys:


 key              | content/usage
------------------|--------------
 `:chart-options` | hash-map of minimal keys to send to Highcharts. (see [minimum chart-options]())
 `:merge-plot-options` | hash-map defining "callout" functions to handle different `:/data-format` scenarios for creatins the correct `:plot-options`
 `:conversions` | hash-map defining "callout" functions to handle different `:/data-format` conversion scenarios


> The `register-type` function *MUST* defined.


### plot-options

`plot-options`, regardless of what you name it,


> The make-chart namespace provides a default implementation: `default-plot-options` which returns
> an empty hash-map if this is sufficient

> At a minimum, a function *must* be defined for the `:default` case

###


> The make-chart namespace provides a default implementation: `default-conversion` which returns
> an the unmodified value of `[:data :series]` for cases where the widget already understands and
> expects the data from the server to be in the correct format
> (see [Managing Data Formats](managing-data-formats.md))

> At a minimum, a function *must* be defined for the `:default` case
