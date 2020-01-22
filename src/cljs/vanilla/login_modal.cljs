(ns vanilla.login-modal
  (:require
    [re-frame.core :as rf]
    [reagent.core :as r]))



(defn attempt-create-user
  ""
  [username pass]
  (prn username "///" pass)
  )

(defn attempt-login
  ""
  [username pass]
  (prn username "///" pass)
  )

(defn input-element
  "An input element that takes in what type of element it is, and the current value that will change with input."
  [id name type value]
  [:input {:id id
           :name name
           :class "form-control"
           :type type
           :required ""
           :value @value
           :on-change #(reset! value (-> % .-target .-value))
           }])


(defn login-pop-up                                          ;; Name should include modal instead of pop up
  "When the login button is clicked have this modal pop up"
  [is-active]
  (let [                                                    ;credentials (r/atom {:username "" :pass ""})
        username (r/atom nil)
        pass (r/atom nil)]
    (fn []
      [:div.modal (if @is-active {:class "is-active"})
       ;[:p "I was hiding"]
       [:div.modal-background]
       [:div.modal-card
        [:header.modal-card-head
         [:p.modal-card-title "Login"]
         [:button.delete {:aria-label "close"
                          :on-click   #(reset! is-active false)}]]


        [:section.modal-card-body
         [:label "Email:"
          [:div
           [input-element "email" "email" "email" username]]]


         [:label "Password:"
          [:div
           [input-element "password" "password" "password" pass]]]

         ]

        [:section.modal-card-body
         ]

        [:footer.modal-card-foot
         [:button.button.is-success {:on-click #(do
                                                  ;(prn "login")
                                                  (attempt-login @username @pass)
                                                  (reset! is-active false))} "Login"]
         [:button.button.is-success {:on-click #(do
                                                  ;(prn "picked widget " @chosen-widget @selected)
                                                  (attempt-create-user @username @pass )
                                                  (reset! is-active false))} "Sign-up"]
         [:button.button {:on-click #(reset! is-active false)} "Cancel"]]
        ]])
    )
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