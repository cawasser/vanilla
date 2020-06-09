(ns vanilla.add-widget
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cljs-uuid-utils.core :as uuid]
            [vanilla.modal :refer [modal]]))


;;;; EVENTS
(rf/reg-event-db
  :add-widget
  (fn-traced [db [_ new-widget-type data-source]]
    (let [next-id      (:next-id db)
          widget-type  (get-in db [:widget-types new-widget-type])
          current-user @(rf/subscribe [:get-current-user])  ;;get current user
          named-widget (assoc widget-type
                         :key (str next-id)
                         :data-source data-source
                         :username current-user             ;;add username key value to widget map
                         :data-grid {:x 0 :y 0 :w 5 :h 15})]

      (do
        (assoc db
          :widgets (conj (:widgets db) named-widget)
          :next-id (uuid/uuid-string (uuid/make-random-uuid)))))))

(rf/reg-event-db
  :add-carousel
  (fn-traced [db]
    (prn ":add-carousel" (get-in db [:widget-types :carousel-widget]))

    (let [next-id      (:next-id db)
          widget-type  (get-in db [:widget-types :carousel-widget])
          current-user @(rf/subscribe [:get-current-user])  ;;get current user
          named-widget (assoc widget-type
                         :key (str next-id)
                         :data-source :carousel-service
                         :username current-user             ;;add username key value to widget map
                         :data-grid {:x 0 :y 0 :w 5 :h 15 :isResizable false})]  ;;carousel size is locked and cant be resized

      (do
        (assoc db
          :widgets (conj (:widgets db) named-widget)
          :next-id (uuid/uuid-string (uuid/make-random-uuid)))))))

(rf/reg-event-db
  :add-sankey-carousel
  (fn-traced [db]
    (prn ":add-sankey-carousel" (get-in db [:widget-types :sankey-carousel-widget]))
    (let [next-id      (:next-id db)
          widget-type  (get-in db [:widget-types :sankey-carousel-widget])
          current-user @(rf/subscribe [:get-current-user])  ;;get current user
          named-widget (assoc widget-type
                         :key (str next-id)
                         :data-source :carousel-service
                         :username current-user             ;;add username key value to widget map
                         :data-grid {:x 0 :y 0 :w 5 :h 15 :isResizable false})]  ;;carousel size is locked and cant be resized

      (do
        (assoc db
          :widgets (conj (:widgets db) named-widget)
          :next-id (uuid/uuid-string (uuid/make-random-uuid)))))))



(rf/reg-event-db
  :init-selected-service
  (fn-traced [db _]
    ;(prn (str ":init-selected-service " (first (:services db))))
    (assoc db :selected-service (first (:services db)))))


(rf/reg-event-db
  :selected-service
  (fn-traced [db [_ s]]
    ;(prn (str ":selected-service " s))
    (assoc db :selected-service s)))


(rf/reg-event-db
  :selected-widget
  (fn-traced [db [_ w]]
    ;(prn ":selected-widget " w))
    (assoc db :selected-widget w)))


(rf/reg-sub
  :compatible-selections
  :<- [:selected-service]
  :<- [:selected-widget]
  (fn [[selected-service selected-widget] _]
    (let [ret-val (if (some #{(keyword (:ret_type selected-service))}
                        (:ret_types selected-widget)) true false)]
      ret-val)))





(defn add-widget [new-widget selected-source]
  (rf/dispatch [:add-widget new-widget (keyword (:keyword selected-source))]))

(defn add-carousel []   ;;no args for now, this will change once we figure out a modal solution
  (rf/dispatch [:add-carousel]))




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; SERVICES AND WIDGET PICKER
;
;
(defn- selected-service
  "reruns the service that has been selected"

  [services selected]

  (let [ret-val (first (filter #(= selected (:name %)) services))]
    ret-val))


(defn- selected-widget
  "returns the widget that has been selected"

  [widgets selected]

  (let [ret-val (get widgets selected)]
    ret-val))


(defn- filter-widgets
  "filters the list of widgets to only those compatible with the selected-service"
  [widgets selected-service]

  (let [ret-val (filter #(if (some #{(keyword (:ret_type selected-service))}
                               (:ret_types %)) true false)
                  (vals widgets))]
    ret-val))



(defn- filter-services
  "filter the list of services to only those compatible with the chosen-widget"

  [services chosen-widget]

  (if (or (nil? chosen-widget) (empty? chosen-widget) (empty? services))
    []

    (do
      ;(prn
      ;  "filter-services " services
      ;  " //// (chosen-widget) " chosen-widget
      ;  " //// ret_types " (:ret_types chosen-widget))

      (let [ret-val (filter #(if (some #{(keyword (:ret_type %))}
                                   (:ret_types chosen-widget)) true false)
                      services)]

        ;(prn (str "filter-services " chosen-widget
        ;       " //// ret_types " (:ret_types chosen-widget)
        ;       " //// ret-val " ret-val))

        (into [] ret-val)))))





(defn service-list
  "returns a list of all the services (data-sources) available in the system"

  [services selected]

  (if (nil? selected)
    (rf/dispatch-sync [:init-selected-service])

  ;[:div.container
   [:div.table-container
    [:table.table.is-hoverable
     [:thead
      [:tr [:th "Name"] [:th "Description"]]]
     [:tbody
      (doall
        (for [{:keys [name doc_string]} services]
          (do
            ^{:key name}
            [:tr {:class    (if (= (:name selected) name) "is-selected" "")
                  :style    {:background-color (if (= (:name selected) name) "royalblue" "")
                             :color (if (= (:name selected) name) "white" "")}
                  :on-click #(do
                               (rf/dispatch [:selected-service (selected-service services name)]))}
             [:td name] [:td doc_string]])))]]]))


(defn compatible-service-list
  "returns a list of data-sources compatible with the chose-widget"

  [services chosen-widget selected]

  (let [compatible-services (filter-services services chosen-widget)]

    [:div.container
     [:div.table-container
      [:table.table.is-hoverable
       [:thead
        [:tr [:th "Name"] [:th "Description"]]]
       [:tbody
        (doall
          (for [{:keys [name doc_string]} compatible-services]
            (do
              ^{:key name}
              [:tr {:class    (if (= (:name selected) name) "is-selected" "")
                    :style    {:background-color (if (= (:name selected) name) "royalblue" "")}
                    :on-click #(do
                                 (rf/dispatch [:selected-service (selected-service compatible-services name)]))}
               [:td name] [:td doc_string]])))]]]]))




(defn widget-card
  "returns a UI 'card' component with a small thumbnail and a label, representing a given widget"

  [name label img chosen-widget widgets]

  [:div.card {:class    (if (= (:name chosen-widget) name) "is-selected" "")
              :style    {:background-color (if (= (:name chosen-widget) name) "royalblue" "")}
              :on-click #(rf/dispatch [:selected-widget (selected-widget widgets name)])}

   [:div.image
    [:figure.image.is-128x128
     [:img {:src img}]]]
   [:div.card-content
    [:p.subtitle.is-7.has-text-centered label]]])



(defn widget-list
  "component showing only the widgets compatible with the selected-service"

  [widgets selected-service chosen-widget]

  (let [widget-cards (filter-widgets widgets selected-service)]
    [:table>tbody
     [:tr
      (for [{:keys [name icon label]} widget-cards]
        ^{:key name} [:td
                      [widget-card name label icon chosen-widget widgets]])]]))

(defn all-widget-list
  "component showing ALL the widgets available"

  [widgets chosen-widget]

  [:table>tbody
   [:tr
    (for [{:keys [name icon label]} (vals widgets)]
      ^{:key name} [:td
                    [widget-card name label icon chosen-widget widgets]])]])


(defn filtered [services]
  "filters the services list shown in the modal"  ;only removes carousel currently due to specific carousel button addition
  (vec (remove #(= "carousel-service" (:keyword %)) services)))

(defn add-by-source-modal
  "modal to allow the user to pick new widgets by first picking the data source they want"
  [is-active]

  (let [services             (rf/subscribe [:services])
        selected             (rf/subscribe [:selected-service])
        chosen-widget        (rf/subscribe [:selected-widget])
        widget-cards         (rf/subscribe [:all-widget-types])
        compatible-selection (rf/subscribe [:compatible-selections])]
    (fn []
      (modal {:is-active             is-active
              :title                 "Add Data Source"
              :modal-body-list       [[service-list @services @selected]
                                      [widget-list @widget-cards @selected @chosen-widget]]
              :footer-button-enabled @compatible-selection
              :footer-button-fn      #(add-widget (:name @chosen-widget) @selected)
              :footer-button-text    "Add"}))))

;; this is currently not is use (replaced by add-carousel)
;; leaving it in case we want it back in the future
(defn add-by-widget-modal
  "modal to allow the user to pick a widget and then select a compatible data-source"

  [is-active]

  (let [services             (rf/subscribe [:services])
        selected             (rf/subscribe [:selected-service])
        chosen-widget        (rf/subscribe [:selected-widget])
        widget-cards         (rf/subscribe [:all-widget-types])
        compatible-selection (rf/subscribe [:compatible-selections])]
    (fn []

      (modal {:is-active             is-active
              :title                 "Add Widget"
              :modal-body-list       [[all-widget-list @widget-cards @chosen-widget]
                                      [compatible-service-list @services @chosen-widget @selected]]
              :footer-button-enabled @compatible-selection
              :footer-button-fn      #(add-widget (:name @chosen-widget) @selected)
              :footer-button-text    "Add"}))))



(defn add-widget-button
  "Creates a button that triggers a modal that allows the user to add a widget"

  []

  (let [is-source-active (r/atom false)
        is-widget-active (r/atom false)]
    (fn []
      [:div.has-text-left
       [:button.button.is-info {:on-click #(swap! is-source-active not)} "Add Source"]
       [:button.button.is-info {:on-click #(add-carousel)} "Add Carousel"]
       [add-by-source-modal is-source-active]])))
       ;[add-by-widget-modal is-widget-active]




(defn version-number
  "Returns the version number wrapped in a h6 element."

  []

  (let [version (rf/subscribe [:version])]
    (fn []
      [:div.control
       [:div.tags.has-addons
        [:span.tag.is-dark "version"]
        [:span.tag.is-dark @version]]])))



(comment
  (prn "ide integration")

  ())

(comment
  (def chosen-widget {:name :bubble-widget, :basis :chart, :type :bubble-chart,
                      :ret_types [:data-format/x-y-n
                                  :data-format/x-y
                                  :data-format/x-y-e
                                  :data-format/y]
                      :icon "/images/bubble-widget.png"})

  (def services [{:id "1000", :keyword "spectrum-traces", :name "Spectrum Traces",
                  :ret_type "data-format/x-y", :read_fn "vanilla.spectrum-traces-service/spectrum-traces"}
                 {:id "2000", :keyword "usage-data", :name "Usage Data",
                  :ret_type "data-format/label-y", :read_fn "vanilla.usage-data-service/usage-data"}
                 {:id "3000", :keyword "sankey-service", :name "Relationship Data",
                  :ret_type "data-format/from-to-n", :read_fn "vanilla.sankey-service/fetch-data"}])

  (:ret_types chosen-widget)
  (keyword (:ret_type (first services)))

  (some #{(keyword (:ret_type (first services)))}
    (:ret_types chosen-widget))


  (def ret (filter #(if (some #{(keyword (:ret_type %))}
                          (:ret_types chosen-widget)) true false)
             services))
  (into [] ret)


  (filter-services services chosen-widget)
  ())