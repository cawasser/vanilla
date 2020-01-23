(ns vanilla.login-modal
  (:require
    [re-frame.core :as rf]
    [reagent.core :as r]
    [ajax.core :as ajax :refer [GET POST]]))



(defn attempt-create-user
  ""
  [credentials]
  (prn "Attempting to create this user:" credentials)             ;; This runs
  (POST "/create-new-user"
        {
         ;; These two lines blow things up
         ;:headers         {"Accept" "application/transit+json"}
         ;:response-format (ajax/json-response-format {:keywords? true})



         :headers         {"Accept" "application/json"}     ;; This does not blow up - not sure if it does anything

         ;; This works and returns the handlers response
         :format          :json
         :params          credentials
         :handler         #(prn "User created!")            ;; This needs to call a function?
         :on-error        #(prn "ERROR creating the user! " %)}))




(defn login-successful?
  ""
  [bool-val username]
  (prn username "login was" bool-val)
  (if (= bool-val true)
    (rf/dispatch-sync [:set-current-user username])
    (prn "login failed"))) ; This should just bring up a new modal that says login failed, no need to call a new function



(defn attempt-login
  "Handles login, runs a verify call then dispatches an event based on the verification"
  [credentials]
  (prn "login "credentials)
  (GET "/verify-user"
       {
        :headers         {"Accept" "application/json"}
        :response-format (ajax/json-response-format {:keywords? true})
        :params          credentials
        :handler #( login-successful? (% :verified-user) (credentials :username))}))






(defn attempt-get-all-users
  "Used mostly to determine database content, made for dev logs"
  []
  (GET "/return-all-users"
       {
        :headers         {"Accept" "application/json"}
        :response-format (ajax/json-response-format {:keywords? true})
        ;:handler #(fn [response] (prn :key response))
        :handler #(prn "get all users successful")}))



(defn input-element
  "An input element that takes in what type of element it is, and the current value that will change with input."
  [id-name-type value]
  [:input {:id id-name-type
           :name id-name-type
           :class "form-control"
           :type id-name-type
           :required ""
           :value @value
           :on-change #(reset! value (-> % .-target .-value))}])



(defn login-pop-up                                          ;; Name should include modal instead of pop up
  "When the login button is clicked have this modal pop up"
  [is-active]
  (let [username (r/atom nil)
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
           [input-element "email" username]]]


         [:label "Password:"
          [:div
           [input-element "password"  pass]]]]



        [:section.modal-card-body]


        [:footer.modal-card-foot
         [:button.button.is-success {:on-click #(do
                                                  ;(prn "login")
                                                  (attempt-login {:username @username
                                                                  :pass @pass})
                                                  (reset! is-active false))} "Login"]
         [:button.button.is-success {:on-click #(do

                                                  (attempt-create-user {:username @username
                                                                        :pass @pass})

                                                  (reset! is-active false))} "Sign-up"]
         [:button.button {:on-click #(reset! is-active false)} "Cancel"]]]])))







(defn login-button
  "Is called from page load to summon a login button, which the rest of this ns creates the functionality for"
  []
  (let [is-active (r/atom false)]
    (fn []
      [:div
        ;[:p (str (attempt-get-all-users))] ;; This prints all users to lein run, but crashes UI
        [:div.level-right.has-text-right
          [:button.button.is-link {:on-click #(swap! is-active not)} "Login"]]
        [login-pop-up is-active]])))

