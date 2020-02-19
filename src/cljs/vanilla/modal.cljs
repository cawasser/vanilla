(ns vanilla.modal
  (:require
    [re-frame.core :as rf]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [reagent.core :as r]))

;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;        EVENTS
;;
;;
;
;
;(rf/reg-event-db
;  :set-modal-active
;  (fn-traced [db [_ value]]
;             (prn "Set modal-active")
;             (assoc db :modal-active value)))
;
;
;
;;;;; Modal header
;(rf/reg-event-db
;  :set-modal-title
;  (fn-traced [db [_ title]]
;             (prn "Set modal header")
;             (assoc db :modal-title title)))
;
;
;;;;; Modal body
;(rf/reg-event-db
;  :set-modal-body
;  (fn-traced [db [_ body]]
;             (prn "Set modal body")
;             (assoc db :modal-body body)))
;
;
;;;;; Modal footer
;(rf/reg-event-db
;  :set-modal-footer-button-fn
;  (fn-traced [db [_ button-function]]
;             (prn "Set modal footer")
;             (assoc db :modal-footer-button-fn button-function)))
;
;(rf/reg-event-db
;  :set-modal-footer-button-enabled
;  (fn-traced [db [_ button-enabled]]
;             (prn "Set modal footer")
;             (assoc db :modal-footer-button-enabled button-enabled)))
;
;(rf/reg-event-db
;  :set-modal-footer-button-text
;  (fn-traced [db [_ button-text]]
;             (prn "Set modal footer button-text")
;             (assoc db :modal-footer-button-text button-text)))
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;        SUBSCRIPTIONS
;;
;;
;
;
;(rf/reg-sub
;  :get-modal-active
;  (fn [db _]
;    (get :modal-active db)))
;
;;;;; Modal Header
;(rf/reg-sub
;  :get-modal-title
;  (fn [db _]
;    (:modal-title db)))
;
;;;;; Modal Body
;(rf/reg-sub
;  :get-modal-body
;  (fn [db _]
;    (:modal-title db)))
;
;;;;; Modal Footer
;(rf/reg-sub
;  :get-modal-footer-button-on-click-fn
;  (fn [db _]
;    (:modal-footer-button-fn db)))
;
;(rf/reg-sub
;  :get-modal-footer-button-text
;  (fn [db _]
;    (:modal-footer-button-text db)))
;
;(rf/reg-sub
;  :get-modal-footer-button-enabled
;  (fn [db _]
;    (:modal-footer-button-enabled db)))
;
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;(defn initialize-modal-components
;  ""
;  []
;  (rf/dispatch [:set-modal-title ""])
;  (rf/dispatch [:set-modal-body ([:section.modal-card-body])])
;  (rf/dispatch [:set-modal-footer-button-text ""])
;  (rf/dispatch [:set-modal-footer-button-enabled true])
;  (rf/dispatch [:set-modal-footer-button-fn #(prn "hello, this seems broke")])
;  (rf/dispatch [:set-modal-active false]))



(def modal-body-section
  :section.modal-card-body)

(def modal-start
  :div.modal)

(def modal-background
  :div.modal-background)

(def modal-card
  :div.modal-card)





(defn modal-header
  [title is-active]
  [:header.modal-card-head
   [:p.modal-card-title title]
   [:button.delete {:aria-label "close"
                    :on-click   #(reset! is-active false)}]])


(defn modal-footer
  ""
  [enabled-button button-fn button-text is-active]
  [:footer.modal-card-foot
   [:button.button.is-success
    {:disabled (not enabled-button)
     :on-click #(do
                  (button-fn)
                  (reset! is-active false))}
    button-text]
   [:button.button {:on-click #(reset! is-active false)} "Cancel"]])




(defn create-modal
  ""
  [is-active title modal-body-list
   footer-button-enabled footer-button-fn footer-button-text]
  [modal-start (if @is-active {:class "is-active"})
   [modal-background]
   [modal-card
    (modal-header title is-active)
    (for [item modal-body-list]
       [modal-body-section item])
    (modal-footer footer-button-enabled footer-button-fn footer-button-text is-active)]])





;
;(defn initialize-modal
;  ""
;  []
;  (let [is-active (rf/subscribe :get-modal-active)
;        modal-header-title (rf/subscribe :get-modal-title)
;        modal-body-content (rf/subscribe :get-modal-body)
;        modal-footer-button-text (rf/subscribe :get-modal-footer-button-text)
;        modal-footer-button-on-click-fn (rf/subscribe :get-modal-footer-button-on-click-fn)
;        modal-footer-button-enabled (rf/subscribe :get-modal-footer-button-enabled)]
;
;       (fn []
;         [:div.modal (if @is-active {:class "is-active"})
;          [:div.modal-background]
;          [:div.modal-card
;           [:header.modal-card-head
;            [:p.modal-card-title modal-header-title]
;            [:button.delete {:aria-label "close"
;                             :on-click   (rf/dispatch :set-modal-active false)}]]
;
;           [modal-body-content]
;
;           [:footer.modal-card-foot
;            [:button.button.is-success
;             {:disabled (not modal-footer-button-enabled)
;              :on-click #(do
;                           modal-footer-button-on-click-fn
;                           (rf/dispatch :set-modal-active false))} modal-footer-button-text]
;            [:button.button {:on-click #(rf/dispatch :set-modal-active false)} "Cancel"]]]])))

