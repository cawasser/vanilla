(ns vanilla.login-modal
  (:require
    [re-frame.core :as rf]
    [reagent.core :as r]))



(defn attempt-create-user
  ""
  [])

(defn attempt-login
  ""
  [])

(defn login-pop-up                                          ;; Name should include modal instead of pop up
  "When the login button is clicked have this modal pop up"
  [is-active]
  [:div.modal (if @is-active {:class "is-active"})
   ;[:p "I was hiding"]
   [:div.modal-background]
   [:div.modal-card
    [:header.modal-card-head
     [:p.modal-card-title "Login"]
     [:button.delete {:aria-label "close"
                      :on-click   #(reset! is-active false)}]]


    [:section.modal-card-body
     ]

    [:section.modal-card-body
     ]

    [:footer.modal-card-foot
     [:button.button.is-success {:on-click #(do
                                              ;(prn "picked widget " @chosen-widget @selected)
                                              ;(add-widget @chosen-widget @selected)
                                              (reset! is-active false))} "Sign-in"]
     [:button.button {:on-click #(reset! is-active false)} "Cancel"]]]
   ]
  )



(defn login-button
  "Is called from page load to summon a login button, which the rest of this ns creates the functionality for"
  []
  (let [is-active (r/atom false)]
    (fn []
      [:div
        ;[:p "Good bye"]
        [:div.level-right.has-text-right
          [:button.button.is-link {:on-click #(swap! is-active not)} "Login"]]
        [login-pop-up is-active]
       ]
      )))