(ns vanilla.add-widget-model
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))



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



(defn widget-card [name label img chosen-widget]
  ;(prn "widget-card " name
  ;  " //// label " label
  ;  " //// img " img)

  [:div.card {:class    (if (= @chosen-widget name) "is-selected" "")
              :style    {:background-color (if (= @chosen-widget name) "lightgreen" "")}
              :on-click #(do
                           ;(prn "widget-card " name
                           ;  " //// chosen-widget " @chosen-widget)

                           (reset! chosen-widget name))}
   [:div.image
    [:figure.image.is-128x128
     [:img {:src img}]]]
   [:div.card-content
    [:p.subtitle.is-7.has-text-centered label]]])



(defn widget-list [widgets s chosen-widget]
  (let [widget-cards (filter-widgets widgets s)]

    ;(prn "widget-list " widgets)
    ;  " //// selected " s)
    ;  " //// chosen-widget " @chosen-widget)
    ;  " //// cards " widget-cards)

    [:table>tbody
     [:tr
      (for [{:keys [name icon label]} widget-cards]
        ^{:key name} [:td
                      [widget-card name label icon chosen-widget]])]]))



(defn add-widget-modal [is-active]
  (let [services (rf/subscribe [:services])
        selected (rf/subscribe [:selected-service])
        chosen-widget (r/atom {})
        widget-cards (rf/subscribe [:all-widget-types])]
    (fn []
      [:div.modal (if @is-active {:class "is-active"})
       [:div.modal-background]
       [:div.modal-card
        [:header.modal-card-head
         [:p.modal-card-title "Add Data Source"]
         [:button.delete {:aria-label "close"
                          :on-click   #(reset! is-active false)}]]

        [:section.model-card-body
         [:p (str "selected " @selected)]
         [:p (str "widget " @chosen-widget)]]

        [:section.modal-card-body
         [service-list @services @selected]]

        [:section.modal-card-body
         [widget-list @widget-cards @selected chosen-widget]]

        [:footer.modal-card-foot
         [:button.button.is-success {:on-click #(do
                                                  ;(prn "picked widget " @chosen-widget @selected)
                                                  (add-widget @chosen-widget @selected)
                                                  (reset! is-active false))} "Add"]

         [:button.button {:on-click #(reset! is-active false)} "Cancel"]]]])))



(defn version-number []
  (let [version (rf/subscribe [:version])
        is-active (r/atom false)]
    (fn []
      [:container.level {:width "100%"}
       [:div.level-left.has-text-left
        [:h7.subtitle.is-6 @version]]
       [:div.level-right.has-text-right
        [:button.button.is-link {:on-click #(swap! is-active not)} "Add"]]
       ;[:button.button.is-link {:on-click #(add-canned-widget)} "widget"]]
       [add-widget-modal is-active]])))
