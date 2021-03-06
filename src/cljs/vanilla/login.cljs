(ns vanilla.login
  (:require
    [vanilla.update-layout :as layout]
    [cljs.core.async :as a]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [dashboard-clj.core :as d]
    ["toastr" :as toastr]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [ajax.core :as ajax :refer [GET POST]]))


;;;;;;;;;;;;;;;;;
;;EVENTS
;;;;;;;;;;;;;;;;;
(rf/reg-event-db
  :set-current-user
  (fn-traced [db [_ username]]
             (prn "Set current user" username)
             (layout/get-layout username)
             (assoc db :current-user username
                       :new-login true)))


(rf/reg-event-db
  :logout
  (fn-traced [db _]
    (prn "Logging out" (:current-user db))

    ; we can just launch this and assume the server cleans up.
    ; if there are any problems, the server will clean up when we
    ; close the web page anyway
    ;
    (POST "/logout"
      {:format          (ajax/json-request-format {:keywords? true})
       :response-format (ajax/json-response-format {:keywords? true})
       :params          {:user (:current-user db)}
       :handler         #()
       :error-handler   #()})

    (layout/get-layout nil)                                 ;;clear page of widgets
    (dissoc db :current-user :data-sources :services)))

(rf/reg-event-db
  :login-message
  (fn-traced [db [_ response message]]
             (if (= (:status response) 200)
               (toastr/success message)
               (toastr/error message))
             db))

;;;;;;;;;;;;;;;;;;;;;

(defn attempt-create-user
  "This is called when the user tries to sign up a new username and password"
  [credentials]
  ;(prn "Attempting to create this user:" credentials)
  (POST "/create-new-user"
        {:format          (ajax/json-request-format {:keywords? true})
         :response-format (ajax/json-response-format {:keywords? true})
         :params          credentials
         :handler         #(rf/dispatch [:login-message % "Account created!"])
         :error-handler   #(rf/dispatch [:login-message % "Failed to create account, please try again."])}))

; TODO  When a user fails to login, nothing happens. Trigger a pop up or modal.
(defn login-failed-pop-up
  ""
  []
;; This does not show up? The post call returns this so maybe have to work something out there
  (let [show-pop-up (r/atom true)]
    (fn []
      [:div.modal (if @show-pop-up {:class "is-active"})
       [:div.modal-background]
       [:div.modal-card
        [:header.modal-card-head
         [:p.modal-card-title "Login failed"]
         [:button.delete {:aria-label "close"
                          :on-click   #(reset! @show-pop-up false)}]]
        [:section.modal-card-body
         [:p "Invalid username and password, please try again."]]
        [:footer.modal-card-foot
         [:button.button {:on-click #(reset! @show-pop-up false)} "Cancel"]]]])))




(defn login-successful?
  "This handles whether the login attempt from the user was successful"
  [bool-val username]
  (prn username "login was" bool-val)
  (if (= bool-val true)
    (do
      (rf/dispatch [:login-message {:status 200} "Welcome to Vanilla!"])
      (rf/dispatch-sync [:set-current-user username])
      (layout/get-services)
      (d/connect-to-data-sources))
    (do
      (rf/dispatch [:login-message {:status 500} "Login failed, try again"])
      [login-failed-pop-up])))



(defn attempt-login
  "Handles login, runs a verify call then dispatches an event based on the verification"
  [credentials]
  ;(prn "login "credentials)
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
        :handler #(prn "get all users successful")}))



(defn input-element
  "An input element that takes in what type of element it is, and the current value that will change with input."
  [id-name-type value]
  [:input.input {:id id-name-type
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
         [:label "Username:"
          [:div
           [input-element "username" username]]]
         [:span {:style {:display "inline-block" :width "70px"}}]
         [:label "Password:"
          [:div
           [input-element "password"  pass]]]]
        [:footer.modal-card-foot
         [:button.button.is-info {:on-click #(do
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
      [:div.has-text-left
        ;[:p (str (attempt-get-all-users))] ;; This prints all users to lein run, but crashes UI
        [:div.level-right.has-text-right
          [:button.button.is-info {:on-click #(swap! is-active not)} "Login"]]
        [login-pop-up is-active]])))




(defn logout-pop-up
  "This calls a pop up modal that double checks that the user wants to log out,
  if they do they click the button to confirm."
  [is-active]
  [:div.modal (if @is-active {:class "is-active"})
   ;[:p "I was hiding"]
   [:div.modal-background]
   [:div.modal-card
    [:header.modal-card-head
     [:p.modal-card-title "Are you sure you want to log out?"]
     [:button.delete {:aria-label "close"
                      :on-click   #(reset! is-active false)}]]
    [:section.modal-card-body
     [:p "Are you sure you want to log out?"]]
    [:footer.modal-card-foot
     [:button.button.is-danger {:on-click #(do
                                             (rf/dispatch-sync [:logout])
                                             (reset! is-active false))} "Logout"]

     [:button.button {:on-click #(reset! is-active false)} "Cancel"]]]])


(defn logout-button
  "This creates a logout button that the user can click to trigger the clearing of current-user"
  []
  (let [is-active (r/atom false)]
    (fn []
      [:div.level-left.has-text-left
      ; [:div.level-right.has-text-right
        [:button.button.is-danger {:on-click #(swap! is-active not)} "Logout"]
       [logout-pop-up is-active]])))


;
;(defn determine-login-or-logout
;  "Determines whether the user is logged in, and displays either a login or logout button"
;  []
;  (let [current-user (rf/subscribe [:get-current-user])]
;    (fn []
;      ;[:p (str (some? current-user))]
;      (if (some? @current-user)
;        [logout-button]
;        [login-button]))))

