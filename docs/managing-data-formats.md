# Managing Data Formats


Data formats are used to describe both the data returned from a data service and the data
formats acceptable for visualization by UI widgets.

Data formats are Clojure [keywords](https://clojure.org/guides/learn/syntax#_symbols_and_idents) within the code, but due to incompatibilities in
storing keywords in SQL databases (specifically SQLite), they are stored as simple strings
and do _*NOT*_ include the ':' character.

> This allows the use of the [(keyword \<string\>)](https://clojuredocs.org/clojure.core/keyword) function to reconstitute


Specifically, data formats take the form `:data-format/<format encoding>`, so they in the "namespace" `data-format`


### Encoding

- 'x' is used to denote numbers that are mapped to the "x axis" of a chart
- 'y' is used to denote numbers that are mapped to the "y axis" of a chart
- 'from' denotes the "from" element of a relation, typically expressed as a \<string\>
- 'to' denotes the "to" element of a relation, typically expressed as a \<string\>
- 'n' is used to denote numbers that represent the magnitude of the value at coordinate "C"
- 'e' denotes that the magnitude data is taken from an "entity," provided in a hash-map

> where "C" can be 'x-y', 'grid', etc.


#### Compound Formats

- 'x-y' indicates that the data will be stored as a vector of 2 numbers (i.e., \[ \<x\> \<y\> \])
- 'grid' denotes data stored in \[ \<x\> \<y\> \] format, but with specific semantics _not_ supported by the more general widgets
- 'rose' is used to denote semantic meaning for data categorized by the 12 or 24 hour clock (see [Rose Widget](widget-docs/rose-widget.md))



### Widget Data Format Support


Widget Name | Default Data Format | Other Format | -e Format | -y Format
------------|---------------------|--------------|-----------|-----------
Area        | x-y                 | x-y-n        | x-y-e     | y
Bar         | x-y                 | x-y-n        | x-y-e     | y
Column      | x-y                 | x-y-n        | x-y-e     | y
Line        | x-y                 | x-y-n        | x-y-e     | y
Scatter     | x-y                 | x-y-n        | x-y-e     | y
Bubble      | x-y-n               | x-y-n        | x-y-e     | y
Heatmap     | grid-n              |              | grid-e    |
Network     | from-to             | from-to-n    | from-to-e |
Org         | from-to             | from-to-n    | from-to-e |
Dependency  | from-to-n           | from-to      | from-to-e |
Sankey      | from-to-n           | from-to      | from-to-e |
Pie         | label-y             |              |           |
Vari-pie    | label-y-n           | label-y      | label-y-e |
Rose        | rose-y-n            |              | rose-y-e  |




### Data Source Format Support

(as of 0.3.1-SNAPSHOT)

Data Source         | Data Format
--------------------|------------
Spectrum Traces     | x-y
Power Data          | x-y
Scatter Data        | x-y
Relationship Data   | from-to-n
Bubble Data         | x-y-n
Heatmap Data        | grid-n
Network Data        | from-to
Usage Data          | label-y
12-hour Usage Data  | rose-y-n
Health and Status   | entities (see branch [new-widgets](https://github.com/cawasser/vanilla/tree/new-widgets))
Time                | string (see branch [new-widgets](https://github.com/cawasser/vanilla/tree/new-widgets))
Grid                | entities (see branch [new-widgets](https://github.com/cawasser/vanilla/tree/new-widgets))


