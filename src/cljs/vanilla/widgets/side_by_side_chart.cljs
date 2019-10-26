(ns vanilla.widgets.side-by-side-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]
            [vanilla.widgets.pie-chart :as pie]
            [vanilla.widgets.chart :as line]
            [vanilla.widgets.bar-chart :as bar]))



