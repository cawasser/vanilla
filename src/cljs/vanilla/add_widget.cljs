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
             (let [next-id (:next-id db)
                   widget-type (get-in db [:widget-types new-widget-type])
                   current-user @(rf/subscribe [:get-current-user]) ;;get current user
                   named-widget (assoc widget-type
                                  :key (str next-id)
                                  :data-source data-source
                                  :username current-user    ;;add username key value to widget map
                                  :data-grid {:x 0 :y 0 :w 5 :h 15})]

               (do
                 ;(prn ":add-widget " new-widget-type
                 ;  " //// widget-type " widget-type
                 ;  " //// named-widget " named-widget)
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
  ;(prn "add-widget " new-widget
  ;  " //// selected-source " selected-source
  ;  " //// keyword " (keyword (:keyword selected-source)))

  (rf/dispatch [:add-widget new-widget (keyword (:keyword selected-source))]))




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; SERVICES AND WIDGET PICKER
;
;
(defn- selected-service [services selected]
  (let [ret-val (first (filter #(= selected (:name %)) services))]

    ;(prn "selected-service " services
    ;  " //// selected " selected
    ;  " //// ret-val " ret-val)

    ret-val))


(defn- selected-widget [widgets selected]
  (let [ret-val (get widgets selected)]

    ;(prn "selected-widget " selected
    ;     " //// ret-val " ret-val)

    ret-val))


(defn- filter-widgets [widgets selected]
  ;(prn "filter-widgets " selected
  ;  " //// ret_types " (keyword (:ret_type selected)))

  (let [ret-val (filter #(if (some #{(keyword (:ret_type selected))}
                                   (:ret_types %)) true false)
                        (vals widgets))]

    ;(prn (str "filter-widgets " selected
    ;       " //// ret_types " (keyword (:ret_type selected))
    ;       " //// ret-val " ret-val))

    ret-val))




(defn service-list [services selected]
  (if (nil? selected)
    (rf/dispatch-sync [:init-selected-service]))

  ;(prn "service-list selected " services selected)

  [:div.container
   [:table-container
    [:table.is-hoverable
     [:thead
      [:tr [:th "Name"] [:th "Description"]]]
     [:tbody
      (doall
        (for [{:keys [name doc_string]} services]
          (do
            ^{:key name}
            [:tr {:class    (if (= (:name selected) name) "is-selected" "")
                  :style    {:background-color (if (= (:name selected) name) "lightgreen" "")}
                  :on-click #(do
                               (rf/dispatch [:selected-service (selected-service services name)]))}
             [:td name] [:td doc_string]])))]]]])



(defn widget-card [name label img chosen-widget widgets]
  ;(prn "widget-card " name
  ;     " //// label " label
  ;     " //// img " img
  ;     " //// " chosen-widget)

  [:div.card {:class    (if (= (:name chosen-widget) name) "is-selected" "")
              :style    {:background-color (if (= (:name chosen-widget) name) "lightgreen" "")}
              :on-click #(do
                           ;(prn "widget-card CLICKED " name
                           ;     " //// chosen-widget " chosen-widget)
                           (rf/dispatch [:selected-widget (selected-widget widgets name)]))}

   ;(reset! chosen-widget name))}
   [:div.image
    [:figure.image.is-128x128
     [:img {:src img}]]]
   [:div.card-content
    [:p.subtitle.is-7.has-text-centered label]]])



(defn widget-list [widgets s chosen-widget]
  (let [widget-cards (filter-widgets widgets s)]

    ;(prn "widget-list " widgets
    ;;  " //// selected " s)
    ;;  " //// chosen-widget " chosen-widget)
    ;  " //// cards " widget-cards)

    [:table>tbody
     [:tr
      (for [{:keys [name icon label]} widget-cards]
        ^{:key name} [:td
                      [widget-card name label icon chosen-widget widgets]])]]))



(defn add-widget-modal
  ""
  [is-active]
  (let [services (rf/subscribe [:services])
        selected (rf/subscribe [:selected-service])
        chosen-widget (rf/subscribe [:selected-widget])
        widget-cards (rf/subscribe [:all-widget-types])
        compatible-selection (rf/subscribe [:compatible-selections])]
    (fn []

      (modal {:is-active                    is-active
              :title                 "Add Data Source"
              :modal-body-list       [[service-list @services @selected]
                                      [widget-list @widget-cards @selected @chosen-widget]]
              :footer-button-enabled @compatible-selection
              :footer-button-fn      #(add-widget (:name @chosen-widget) @selected)
              :footer-button-text    "Add"}))))


      ;[modal/modal-start (if @is-active {:class "is-active"})
      ;;[:div.modal (if @is-active {:class "is-active"})]
      ; [modal/modal-background]
      ; [modal/modal-card
      ;  ;[:header.modal-card-head
      ;  ; [:p.modal-card-title "Add Data Source"]
      ;  ; [:button.delete {:aria-label "close"
      ;  ;                  :on-click   #(reset! is-active false)}]]
      ;  [modal/modal-header "Add Data Source" is-active]
      ;
      ;  ;[:section.model-card-body
      ;  ; [:p (str "selected " @selected)]
      ;  ; [:p (str "widget " @chosen-widget)]]
      ;
      ;  [modal/modal-body-section
      ;    [service-list @services @selected]]
      ;  [:section.modal-card-body
      ;   [widget-list @widget-cards @selected @chosen-widget]]))))
      ;
      ;
      ;  [modal/modal-footer  @compatible-selection
      ;                       #(add-widget (:name @chosen-widget) @selected)
      ;                       "Add"
      ;                       is-active]]])))
        ;[:footer.modal-card-foot
        ; [:button.button.is-success
        ;  {:disabled (not @compatible-selection)
        ;   :on-click #(do
        ;                                           ;(prn "picked widget " @chosen-widget @selected)
        ;                                           (add-widget (:name @chosen-widget) @selected))}
        ;                                           ;(reset! is-active false))
        ;   ;:on-success #(reset! is-active false)}
        ;
        ;  "Add"]
        ; [:button.button {:on-click #(reset! is-active false)} "Cancel"]]]])))


(defn add-widget-button
  "Creates a button that triggers a modal that allows the user to add a widget"
  []
  ;(let [services (rf/subscribe [:services])
  ;      selected (rf/subscribe [:selected-service])
  ;      chosen-widget (rf/subscribe [:selected-widget])
  ;      widget-cards (rf/subscribe [:all-widget-types])
  ;      compatible-selection (rf/subscribe [:compatible-selections])]
  ;    (fn []
  ;      [:div.has-text-left
  ;       [:button.button.is-link
  ;        {:on-click #(do
  ;                      ;(rf/dispatch [:set-modal-title "Add Data Source"])
  ;                      ;(rf/dispatch [:set-modal-body ([:section.modal-card-body
  ;                      ;                                [service-list @services @selected]]
  ;                      ;                               [:section.modal-card-body
  ;                      ;                                [widget-list @widget-cards @selected @chosen-widget]])])
  ;                      ;(rf/dispatch [:set-modal-footer-button-text "Add"])
  ;                      ;(rf/dispatch [:set-modal-footer-button-enabled @compatible-selection])
  ;                      ;(rf/dispatch [:set-modal-footer-button-fn (add-widget (:name @chosen-widget) @selected)])
  ;                      (rf/dispatch [:set-modal-active true]))} "Add"]])))



  (let [is-active (r/atom false)]
    (fn []
      [:div.has-text-left
       ;[:div.level-right.has-text-right
       [:button.button.is-link {:on-click #(swap! is-active not)} "Add"]
       ;[:button.button.is-link {:on-click #(add-canned-widget)} "widget"]]
       [add-widget-modal is-active]])))




(defn version-number
  "Returns the version number wrapped in a h6 element."
  []
  (let [version (rf/subscribe [:version])]
    (fn []
      ;[:div.level-left.has-text-left
      [:h6.subtitle.is-6 @version])))

;(defn version-number []
;  (let [version   (rf/subscribe [:version])
;        is-active (r/atom false)]
;    (fn []
;      [:div.container.level.is-fluid {:width "100%"}
;       [:div.level-left.has-text-left
;        [:h6.subtitle.is-6 @version]]
;       [:div.level-right.has-text-right
;        [:button.button.is-link {:on-click #(swap! is-active not)} "Add"]]
;       ;[:button.button.is-link {:on-click #(add-canned-widget)} "widget"]]
;       [add-widget-modal is-active]])))
