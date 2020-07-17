(ns vanilla.widgets.new-table-widget
  (:require [reagent.core :as r :refer [atom]]
            [reagent.dom :as rd]
            [re-frame.core :as rf]
            [vanilla.data-source-subscribe :as ds]
            [reagent-table.core :as rt]
            [vanilla.widgets.basic-widget :as basic]))

(ds/data-source-subscribe [:ka-beam-location-service]) ;:x-beam-location-service :terminal-location-service

(def ka-beams  (get-in @(rf/subscribe [:app-db :ka-beam-location-service])
                       [:data :data]))

(def ka-beams-small (r/atom
  [{:name "221200Z JUL 2020",
    :data {{:epoch "221200Z JUL 2020",
             :name "2-Ka-X3",
             :satellite-id "SAT2",
             :lat 39.74,
             :lon -85.04,
             :e {:diam 222000, :purpose "Y"}}
            {:epoch "221200Z JUL 2020",
             :name "1-Ka-X2",
             :satellite-id "SAT1",
             :lat 34.8,
             :lon -119.35,
             :e {:diam 222000, :purpose "B"}}
            {:epoch "221200Z JUL 2020",
             :name "1-Ka-X3",
             :satellite-id "SAT1",
             :lat 36.7,
             :lon -126.3,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "221200Z JUL 2020",
             :name "2-Ka-X2",
             :satellite-id "SAT2",
             :lat 39.44,
             :lon -79.42,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "221200Z JUL 2020",
             :name "2-Ka-X1",
             :satellite-id "SAT2",
             :lat 28.53,
             :lon -81.4,
             :e {:diam 222000, :purpose "O"}}
            {:epoch "221200Z JUL 2020",
             :name "1-Ka-X1",
             :satellite-id "SAT1",
             :lat 28.53,
             :lon -81.4,
             :e {:diam 222000, :purpose "Y"}}
            {:epoch "221200Z JUL 2020",
             :name "2-Ka-X1",
             :satellite-id "SAT2",
             :lat 36.7,
             :lon -126.3,
             :e {:diam 222000, :purpose "O"}}
            {:epoch "221200Z JUL 2020",
             :name "2-Ka-X4",
             :satellite-id "SAT2",
             :lat 34.81,
             :lon -86.45,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "221200Z JUL 2020",
             :name "2-Ka-X5",
             :satellite-id "SAT2",
             :lat 32.99,
             :lon -79.66,
             :e {:diam 222000, :purpose "B"}}
            {:epoch "221200Z JUL 2020",
             :name "2-Ka-X6",
             :satellite-id "SAT2",
             :lat 36.58,
             :lon -76.2,
             :e {:diam 222000, :purpose "R"}}}}
   {:name "221600Z JUL 2020",
    :data {{:epoch "221600Z JUL 2020",
             :name "2-Ka-X2",
             :satellite-id "SAT2",
             :lat 39.44,
             :lon -79.42,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "221600Z JUL 2020",
             :name "2-Ka-X5",
             :satellite-id "SAT2",
             :lat 32.99,
             :lon -79.66,
             :e {:diam 222000, :purpose "B"}}
            {:epoch "221600Z JUL 2020",
             :name "2-Ka-X1",
             :satellite-id "SAT2",
             :lat 36.7,
             :lon -126.3,
             :e {:diam 222000, :purpose "O"}}
            {:epoch "221600Z JUL 2020",
             :name "1-Ka-X1",
             :satellite-id "SAT1",
             :lat 28.53,
             :lon -81.4,
             :e {:diam 222000, :purpose "Y"}}
            {:epoch "221600Z JUL 2020",
             :name "1-Ka-X2",
             :satellite-id "SAT1",
             :lat 34.8,
             :lon -119.35,
             :e {:diam 222000, :purpose "B"}}
            {:epoch "221600Z JUL 2020",
             :name "2-Ka-X4",
             :satellite-id "SAT2",
             :lat 34.81,
             :lon -86.45,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "221600Z JUL 2020",
             :name "2-Ka-X3",
             :satellite-id "SAT2",
             :lat 39.74,
             :lon -85.04,
             :e {:diam 222000, :purpose "Y"}}
            {:epoch "221600Z JUL 2020",
             :name "2-Ka-X6",
             :satellite-id "SAT2",
             :lat 36.58,
             :lon -76.2,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "221600Z JUL 2020",
             :name "1-Ka-X3",
             :satellite-id "SAT1",
             :lat 36.7,
             :lon -126.3,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "221600Z JUL 2020",
             :name "2-Ka-X1",
             :satellite-id "SAT2",
             :lat 28.53,
             :lon -81.4,
             :e {:diam 222000, :purpose "O"}}}}
   {:name "222000Z JUL 2020",
    :data {{:epoch "222000Z JUL 2020",
             :name "2-Ka-X3",
             :satellite-id "SAT2",
             :lat 39.74,
             :lon -85.04,
             :e {:diam 222000, :purpose "Y"}}
            {:epoch "222000Z JUL 2020",
             :name "1-Ka-X1",
             :satellite-id "SAT1",
             :lat 28.53,
             :lon -81.4,
             :e {:diam 222000, :purpose "Y"}}
            {:epoch "222000Z JUL 2020",
             :name "1-Ka-X2",
             :satellite-id "SAT1",
             :lat 34.8,
             :lon -119.35,
             :e {:diam 222000, :purpose "B"}}
            {:epoch "222000Z JUL 2020",
             :name "2-Ka-X6",
             :satellite-id "SAT2",
             :lat 36.58,
             :lon -76.2,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "222000Z JUL 2020",
             :name "2-Ka-X5",
             :satellite-id "SAT2",
             :lat 32.99,
             :lon -79.66,
             :e {:diam 222000, :purpose "B"}}
            {:epoch "222000Z JUL 2020",
             :name "2-Ka-X2",
             :satellite-id "SAT2",
             :lat 39.44,
             :lon -79.42,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "222000Z JUL 2020",
             :name "2-Ka-X1",
             :satellite-id "SAT2",
             :lat 28.53,
             :lon -81.4,
             :e {:diam 222000, :purpose "O"}}
            {:epoch "222000Z JUL 2020",
             :name "1-Ka-X3",
             :satellite-id "SAT1",
             :lat 36.7,
             :lon -126.3,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "222000Z JUL 2020",
             :name "2-Ka-X4",
             :satellite-id "SAT2",
             :lat 34.81,
             :lon -86.45,
             :e {:diam 222000, :purpose "R"}}
            {:epoch "222000Z JUL 2020",
             :name "2-Ka-X1",
             :satellite-id "SAT2",
             :lat 36.7,
             :lon -126.3,
             :e {:diam 222000, :purpose "O"}}}}]))



;the column model
(def columns [{:path [:data :epoch]
               :header "Epoch"
               :key :epoch}  ; convention - use field name for reagent key
              {:path [:data :name]
               :header "Name"
               :key :name}
              {:path [:data :satellite-id]
               :header "Sat ID"
               :key :satellite-id}
              {:path [:data :lat]
               :header "Lat"
               ;:format #(format-number %)
               ;:attrs (fn [data] {:style {:text-align "right"
               ;                           :display "block"}})
               :key :lat}
              {:path [:data :lon]
               :header "Lon"
               ;:attrs (fn [data] {:style {:text-align "right"
               ;                           :display "block"}})
               :key :lon}
              {:path [:data :e :diam]
               :header "Diameter"
               ;:format #(if % "Stay Away!" "OK to stroke")
               :key :diam}
              {:path [:data :e :purpose]
               :header "Purpose"
               :key :purpose}])

(defn- row-key-fn
  "Return the reagent row key for the given row"
  [row row-num]
  (get-in row [:data :name]))

(defn- cell-data
  "Resolve the data within a row for a specific column"
  [row cell]
  (let [{:keys [path expr]} cell]
    (or (and path
             (get-in row path))
        (and expr
             (expr row)))))

(defn- cell-fn
  "Return the cell hiccup form for rendering.
   - render-info the specific column from :column-model
   - row the current row
   - row-num the row number
   - col-num the column number in model coordinates"
  [render-info row row-num col-num]
  (let [{:keys [format attrs]
         :or   {format identity
                attrs (fn [_] {})}} render-info
        data    (cell-data row render-info)
        content (format data)
        attrs   (attrs data)]
    [:span
     attrs
     content]))

(defn compare-vals
  "A comparator that works for the various types found in table structures.
  This is a limited implementation that expects the arguments to be of
  the same type. The :else case is to call compare, which will throw
  if the arguments are not comparable to each other or give undefined
  results otherwise.
  Both arguments can be a vector, in which case they must be of equal
  length and each element is compared in turn."
  [x y]
  (cond
    (and (vector? x)
         (vector? y)
         (= (count x) (count y)))
    (reduce #(let [r (compare (first %2) (second %2))]
               (if (not= r 0)
                 (reduced r)
                 r))
            0
            (map vector x y))

    (or (and (number? x) (number? y))
        (and (string? x) (string? y))
        (and (boolean? x) (boolean? y)))
    (compare x y)

    :else ;; hope for the best... are there any other possiblities?
    (compare x y)))

(defn- sort-fn
  "Generic sort function for tabular data. Sort rows using data resolved from
  the specified columns in the column model."
  [rows column-model sorting]
  (sort (fn [row-x row-y]
          (reduce
            (fn [_ sort]
              (let [column (column-model (first sort))
                    direction (second sort)
                    cell-x (cell-data row-x column)
                    cell-y (cell-data row-y column)
                    compared (if (= direction :asc)
                               (compare-vals cell-x cell-y)
                               (compare-vals cell-y cell-x))]
                (when-not (zero? compared)
                  (reduced compared))
                ))
            0
            sorting))
        rows))

(def table-state (atom {:draggable true}))

(defn make-widget [name data options]
  ;(rd/render
    [:div.container {:style {:font-size 16 :margin-top 10} :height "100%"}
     ;[:div.panel.panel-default
     ;[:div.panel-body
     [rt/reagent-table ka-beams-small {:table {:class "table table-hover table-striped table-bordered table-transition"
                                               :style {:border-spacing 0
                                                       :border-collapse "separate"}}
                                   :table-container {:style {:border "1px solid green"}}
                                   :th {:style {:border "1px solid white" :background-color "black"}}
                                   :table-state  table-state
                                   :scroll-height "80vh"
                                   :column-model columns
                                   :row-key      row-key-fn
                                   :render-cell  cell-fn
                                   :sort         sort-fn
                                   ;:caption [:caption "Test caption"]
                                   :column-selection {:ul {:li {:class "btn"}}}
                                   }]])
    ;]]
    ;(. js/document (getElementById "app"))))








(comment



  ())