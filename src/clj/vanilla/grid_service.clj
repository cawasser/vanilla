(ns vanilla.grid-service)

(defn header []
  [{:key "name"
    :name "Name"
    :editable true}
   {:key "country"
    :name "Country"
    :editable true}
   {:key "email"
    :name "Email"
    :editable true}
   {:key "operating-since"
    :name "Operating Since"
    :editable true}])

(defn grid-data []
  [{:name "user one"
    :country "USA"
    :email "user_one@none.com"
    :operating-since "2000"}
   {:name "user two"
    :country "USA"
    :email "user_two@none.com"
    :operating-since "2001"}
   {:name "user three"
    :country "USA"
    :email "user_three@none.com"
    :operating-since "2002"}])

(defn fetch-data []
  (prn "grid-widget service")

  {:title "Grid Data"
   :data-format :data-format/entities
   :meta-data   (header)
   :series  (grid-data)})