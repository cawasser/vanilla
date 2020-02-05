(ns vanilla.widgets.configure-widget
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [cljsjs.react-color]))


;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; helpers
;

(def def-color {:r 150 :g 150 :b 150 :a 1})

(defn- rgba [{:keys [r g b a]}]
  (str "rgba(" r "," g "," b "," a ")"))

(defn- f [n w]
  (->> w
     (filter #(= (:key %) n))
     first)) ; returns a map

(defn- notf [n w]
  (->> w
       (filter #(not= (:key %) n)))) ; a seq of maps

(defn- modify-the-widget [m banner-color title-color title]
  (-> m
      (assoc-in [:options :viz/title] title)
      (assoc-in [:options :viz/banner-text-color] title-color)
      (assoc-in [:options :viz/banner-color] banner-color)))

(defn- get-part [id widget path default]
  (if (and (not= id "")
           (not (nil? id)))
    (get-in widget path default)
    default))


;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; event handlers and subscriptions
;

(rf/reg-event-db
  :configure-widget
  (fn-traced
    [db [_ widget-id]]
    (let [widget (f widget-id (:widgets db))]
      (assoc db
        :configure-widget widget-id
        :chosen-widget (if (= widget-id "") nil widget)
        :chosen-title (get-part widget-id widget [:options :viz/title] "")
        :chosen-banner-color (get-part widget-id widget [:options :viz/banner-color] def-color)
        :chosen-title-color (get-part widget-id widget [:options :viz/banner-text-color] def-color)
        :chosen-title-color (get-part widget-id widget [:options :viz/banner-text-color] def-color)))))



(rf/reg-event-db
  :chosen-title
  (fn-traced
    [db [_ val]]
    (assoc db :chosen-title val)))



(rf/reg-event-db
  :chosen-title-color
  (fn-traced
    [db [_ val]]
    (assoc db :chosen-title-color val)))


(rf/reg-event-db
  :chosen-banner-color
  (fn-traced
    [db [_ val]]
    (assoc db :chosen-banner-color val)))



(rf/reg-event-db
  :update-widget-config
  (fn-traced
    [db [_ widget-id]]
    (assoc db :widgets (into [] (conj (notf widget-id (:widgets db))
                                      (modify-the-widget (f widget-id (:widgets db))
                                                         (:chosen-banner-color db)
                                                         (:chosen-title-color db)
                                                         (:chosen-title db)))))))

(rf/reg-sub
  :chosen-widget
  (fn-traced
    [db [_]]
    (:chosen-widget db)))


(rf/reg-sub
  :chosen-banner-color
  (fn-traced
    [db [_]]
    (get db :chosen-banner-color def-color)))


(rf/reg-sub
  :chosen-title-color
  (fn-traced
    [db [_]]
    (get db :chosen-title-color def-color)))


(rf/reg-sub
  :chosen-title
  (fn-traced
    [db [_]]
    (get db :chosen-title "")))


(rf/reg-sub
  :configure-widget
  (fn [db _]
    (:configure-widget db)))



;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; PUBLIC component
;

(defn change-header [is-active]
  [:div.modal (if (and (not= @is-active "") (not (nil? @is-active)))
                {:class "is-active"})
   [:div.modal-background]
   [:div.modal-card
    [:header.modal-card-head
     [:p.modal-card-title "Change Tile and Text Color"]]
    [:section.modal-card-body
     [:p "Title: "]
     [:input.input
      {:type      :text
       :value     @(rf/subscribe [:chosen-title])
       :on-change #(do
                     (rf/dispatch-sync [:chosen-title (-> % .-target .-value)]))}]]
    [:section.modal-card-body
     [:p "Text Color: "]
     [:> js/ReactColor.CompactPicker
      {:style            {:top "5px" :left "10px"}
       :color            @(rf/subscribe [:chosen-title-color])
       :onChangeComplete (fn [color _]
                           (rf/dispatch-sync [:chosen-title-color (:rgb (js->clj color :keywordize-keys true))]))}]]
    [:section.modal-card-body
     [:p "Banner Color: "]
     [:> js/ReactColor.CompactPicker
      {:style            {:top "5px" :left "10px"}
       :color            @(rf/subscribe [:chosen-banner-color])
       :onChangeComplete (fn [color _]
                           (rf/dispatch-sync [:chosen-banner-color (:rgb (js->clj color :keywordize-keys true))]))}]]

    [:footer.modal-card-foot
     [:button.button.is-success {:on-click #(do
                                              (rf/dispatch-sync [:update-widget-config @is-active])
                                              (rf/dispatch-sync [:configure-widget ""])
                                              (.stopPropagation %))} "OK"]
     [:button.button {:on-click #(do
                                   (rf/dispatch-sync [:configure-widget ""])
                                   (.stopPropagation %))} "Cancel"]]]])








(comment
  (def b {:other "stuff" :widgets [{:name "1" :thing 2} {:name "2" :thing 5} {:name "50"}]})

  (def n "1")

  (defn f [n w]
    (->> w
         (filter #(= (:name %) n))
         first)) ; returns a map

  (defn notf [n w]
    (->> w
         (filter #(not= (:name %) n)))) ; a seq of maps


  (f "1" (:widgets b))
  (notf "1" (:widgets b))

  (assoc (f "1" (:widgets b)) :thing 100 :thing2 302589)

  (conj (notf "1" (:widgets b)) (assoc (f "1" (:widgets b)) :thing 100 :thing2 302589))

  (assoc b :widgets (into [] (conj (notf "1" (:widgets b))
                                   (assoc (f "1" (:widgets b)) :thing 100))))


  (defn modt [m banner-color title-color title]
    (-> m
        (assoc-in [:options :viz/title] title)
        (assoc-in [:options :viz/banner-text-color] title-color)
        (assoc-in [:options :viz/banner-color] banner-color)))

  (modt (f "1" (:widgets b)) "banner" "color" "title")

  ())
