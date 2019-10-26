(ns vanilla.widgets.sankey-chart
  (:require [reagent.core :as r]
          [reagent.ratom :refer-macros [reaction]]
          [dashboard-clj.widgets.core :as widget-common]
          [vanilla.widgets.basic-widget :as basic]
          [vanilla.widgets.util :as util]))

       ;:series      [{:type "sankey"
       ;               :keys (get-in data [:data (get-in options [:src :keys])])
       ;               :data (get-in data [:data (get-in options [:src :extract])])}]))

