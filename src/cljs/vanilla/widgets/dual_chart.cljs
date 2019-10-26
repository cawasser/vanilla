(ns vanilla.widgets.dual-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [dashboard-clj.widgets.core :as widget-common]
            [vanilla.widgets.basic-widget :as basic]
            [vanilla.widgets.util :as util]
            [vanilla.widgets.chart :as line]
            [vanilla.widgets.column-chart :as column]
            [vanilla.widgets.make-chart :as mc]
            [vanilla.widgets.widget-base :as wb]))



