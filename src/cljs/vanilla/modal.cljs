(ns vanilla.modal
  (:require
    [re-frame.core :as rf]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [reagent.core :as r]))

; Refer to https://bulma.io/documentation/components/modal/
; for more about this modal


;;;;;;;;;;;;;;;;;;;;
;
;  Helper defs -
;      The declared defs below are used to replace hiccup to make future changes easier if
;      we need to change anything about the hiccup. Also they prevent typos throughout this namespace
;
;

(def modal-start
  :div.modal)

(def modal-background
  :div.modal-background)

(def modal-card
  :div.modal-card)

;;;;;;;;;;;


;;;;;;;;;;;
;
;   Modal Components
;        The functions below return the sections of the modal as dictated in their function name.
;        The idea is to break the modal into a header body and footer and handle each of them separately.
;

(defn modal-header
  [title is-active]
  [:header.modal-card-head
   [:p.modal-card-title title]
   [:button.delete {:aria-label "close"
                    :on-click   #(reset! is-active false)}]])

;; This is mostly a helper def
(def modal-body-section
  :section.modal-card-body)

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


;;;;;;;


(defn create-modal
  "This function returns a Bulma modal component that can be customized for each use case.

  More specifically this function takes in all the data used by a the modal components and
  creates the components and strings them together to return one fully built modal in return.

  *:is-active*(boolean r/atom)
                               The modal is not visible(or inactive) based on the r/atom boolean value.

  *:title*(string)             The title of the modal

  *:modal-body-list*(vector of hiccup)
                               This should be the body section of the modal. This is very specific for each use
                               so this can take in a list of hiccup.

  *:footer-button-enabled*(boolean)
                               Should the modal-footer-button show (True or false)?

  *:footer-button-fn*(fn)      What does the modal-footer-button do on-click?

  *:footer-button-text*(string)
                               What does the modal-button say? (ie \"submit\")"

  [{:keys [is-active
           title
           modal-body-list
           footer-button-enabled
           footer-button-fn
           footer-button-text] :as input}]
  [modal-start (if @is-active {:class "is-active"})
   [modal-background]
   [modal-card
    (modal-header title is-active)
    (for [item modal-body-list]
       [modal-body-section item])
    (modal-footer footer-button-enabled footer-button-fn footer-button-text is-active)]])


(defn lets-call-a-modal
 "This is an example"
  [is-active]
  [:div
   (create-modal {:is-active is-active
                  :title "Example example"
                  :modal-body-list [[:p "section one"]
                                    [:p "section two"]]
                  :footer-button-enabled :true
                  :footer-button-fn #(prn "You did it!")
                  :footer-button-text "Click here"})])

