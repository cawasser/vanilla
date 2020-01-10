(ns vanilla.add-widget-modal
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cljsjs.react-color :as picker]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; events and subscriptions
;
;

(rf/reg-event-db
  :chosen-bg-color
  (fn-traced [db [_ color]]
    (assoc db :chosen-bg-color color)))

(rf/reg-event-db
  :chosen-txt-color
  (fn-traced [db [_ color]]
    (assoc db :chosen-txt-color color)))



(rf/reg-sub
  :chosen-bg-color
  (fn [db _]
    (:chosen-bg-color db)))

(rf/reg-sub
  :chosen-txt-color
  (fn [db _]
    (:chosen-txt-color db)))

;
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



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
  ;  " //// ret_types " (keyword (:ret_types selected)))

  (let [ret-val (filter #(if (some #{(keyword (:ret_types selected))}
                               (:ret_types %)) true false)
                  (vals widgets))]

    ;(prn (str "filter-widgets " selected
    ;       " //// ret_types " (keyword (:ret_types selected))
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
             [:td.is-7 name] [:td.is-7 doc_string]])))]]]])



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


(defn color-picker []
  (let [chosen-bg-color  @(rf/subscribe [:chosen-bg-color])
        chosen-txt-color @(rf/subscribe [:chosen-txt-color])
        swatch           {:style {:padding      "5px"
                                  :background   "#fff"
                                  :borderRadius "1px"
                                  :boxShadow    "0 0 0 1px rgba(0,0,0,.1)"
                                  :display      "inline-block"
                                  :cursor       "pointer"}
                          :on-click #()}]
    [:div swatch
     [:div {:style {:width        "36px"
                    :height       "14px"
                    :borderRadius "2px"
                    :background   (str "rgba(" (:r chosen-bg-color) "," (:g chosen-bg-color)
                                    "," (:b chosen-bg-color) "," (:a chosen-bg-color) ")")}}]
     [:p.is-7 "Title Color"]]))



(defn labeled-radio [group label]
  [:label [:radio-button {:ng-model group} group false label]
   (str label "    ")])

(defn add-widget-modal [is-active]
  (let [services      (rf/subscribe [:services])
        selected      (rf/subscribe [:selected-service])
        chosen-widget (r/atom {})
        widget-cards  (rf/subscribe [:all-widget-types])]
    (fn []
      [:div.modal (if @is-active {:class "is-active"})
       [:div.modal-background]
       [:div.modal-card
        [:header.modal-card-head
         [:p.modal-card-title "Add Data Source"]
         [:button.delete {:aria-label "close"
                          :on-click   #(reset! is-active false)}]]

        ;[:section.model-card-body
        ; [:p (str "selected " @selected)]
        ; [:p (str "widget " @chosen-widget)]]

        [:section.modal-card-body
         [service-list @services @selected]]

        [:section.modal-card-body
         [widget-list @widget-cards @selected chosen-widget]]

        [:footer.modal-card-foot
         [:div {:width "100%"}
          [:container.level {:width "100%"}
           [:div.level-left.has-text-left
            [:button.button.is-success {:on-click #(do
                                                     ;(prn "picked widget " @chosen-widget @selected)
                                                     (add-widget @chosen-widget @selected)
                                                     (reset! is-active false))} "Add"]

            [:button.button {:on-click #(reset! is-active false)} "Cancel"]]
           [:div.level-right.has-text-right
            [color-picker]
            [:div.control
             [:radio-button.radio {:ng-model "txt-color"} "txt-color" false "white"]
             [:radio-button.radio {:ng-model "txt-color"} "txt-color" false "black"]]]]]]]])))

             ;[:label.radio [:input {:type "radio" :name "text-color"}] "white"]
             ;[:label.radio [:input {:type "radio" :name "text-color" :checked true}] "black"]]]]]]]])))




(defn version-number []
  (let [version   (rf/subscribe [:version])
        is-active (r/atom false)]
    (fn []
      [:container.level {:width "100%"}
       [:div.level-left.has-text-left
        [:h7.subtitle.is-6 @version]]
       [:div.level-right.has-text-right
        [:button.button.is-link {:on-click #(swap! is-active not)} "Add"]]
       ;[:button.button.is-link {:on-click #(add-canned-widget)} "widget"]]
       [add-widget-modal is-active]])))
