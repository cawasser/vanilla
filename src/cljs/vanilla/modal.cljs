(ns vanilla.modal
  (:require
    [re-frame.core :as rf]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [reagent.core :as r]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;        EVENTS
;
;

(rf/reg-event-db
  :set-modal-active
  (fn-traced [db [_ value]]
             (prn "Set modal-active")
             (assoc db :modal-active value)))

(rf/reg-event-db
  :set-modal-title
  (fn-traced [db [_ title]]
             (prn "Set modal header")
             (assoc db :modal-title title)))



(rf/reg-event-db
  :set-modal-body
  (fn-traced [db [_ _]]
             (prn "Set modal body")))


(rf/reg-event-db
  :set-modal-footer
  (fn-traced [db [_ _]]
             (prn "Set modal footer")))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;        SUBSCRIPTIONS
;
;



(rf/reg-sub
  :get-modal-active
  (fn [db _]
    (:modal-active db)))

(rf/reg-sub
  :get-modal-title
  (fn [db _]
    (:modal-title db)))

(rf/reg-sub
  :get-modal)






;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



(defn initialize-modal
  ""
  []
  (let [is-active (rf/subscribe :get-modal-active)
        modal-header-title (rf/subscribe :get-modal-title)
        modal-body-content (rf/subscribe :get-modal-body)
        modal-footer-button-text (rf/subscribe :get-modal-footer-button-text)
        modal-footer-button-on-click-fn (rf/subscribe :get-modal-footer-button-on-click-fn)
        modal-footer-button-enabled (rf/subscribe :get-modal-footer-button-enabled)]

       (fn []
         [:div.modal (if @is-active {:class "is-active"})
          [:div.modal-background]
          [:div.modal-card
           [:header.modal-card-head
            [:p.modal-card-title modal-header-title]
            [:button.delete {:aria-label "close"
                             :on-click   (rf/dispatch :set-modal-active false)}]]

           [:section.modal-card-body
            modal-body-content]

           [:footer.modal-card-foot
            [:button.button.is-success {:disabled (not modal-footer-button-enabled)
                                        :on-click #(do
                                                     modal-footer-button-on-click-fn
                                                     (rf/dispatch :set-modal-active false))} modal-footer-button-text]
            [:button.button {:on-click #(rf/dispatch :set-modal-active false)} "Cancel"]]]])))

