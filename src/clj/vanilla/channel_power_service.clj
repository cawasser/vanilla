(ns vanilla.channel-power-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))







(defn- get-data [sat-num]
  (let [ch-1 (sort-by first
               (d/q '[:find ?freq ?cp
                      :where
                      [?e :satellite-id ?sat-num]
                      [?e :freq ?freq]
                      [?e :channel-1-power ?cp]
                      :in $ ?sat-num]
                 @excel/conn sat-num))
        ch-2 (sort-by first
               (d/q '[:find ?freq ?cp
                      :where
                      [?e :satellite-id ?sat-num]
                      [?e :freq ?freq]
                      [?e :channel-2-power ?cp]
                      :in $ ?sat-num]
                 @excel/conn sat-num))
        ch-3 (sort-by first
               (d/q '[:find ?freq ?cp
                      :where
                      [?e :satellite-id ?sat-num]
                      [?e :freq ?freq]
                      [?e :channel-3-power ?cp]
                      :in $ ?sat-num]
                 @excel/conn sat-num))]

    [{:name "channel-1"
      :keys ["x" "y"]
      :data ch-1}
     {:name "channel-2"
      :keys ["x" "y"]
      :data ch-2}
     {:name "channel-3"
      :keys ["x" "y"]
      :data ch-3}]))


(defn fetch-data [sheet sat-num]
  (log/info "Channel Power " sheet)

  {:title       (str "Channel Power " sheet)
   :data-format :data-format/x-y
   :labels      ["Frequency" "Power"]
   :series      (get-data sat-num)})



(comment
  (vanilla.subscription-manager/refresh-source :channel-power-1000-service)
  (vanilla.subscription-manager/refresh-source :channel-power-2000-service)

  ())

(comment
  (def sat-num "SAT1")

  (get-data "SAT1")

  (def channel-1 (sort-by first
                   (d/q '[:find ?freq ?cp
                          :where
                          [?e :satellite-id ?sat-num]
                          [?e :freq ?freq]
                          [?e :channel-1-power ?cp]
                          :in $ ?sat-num]
                     @excel/conn sat-num)))
  (def channel-2 (sort-by first
                   (d/q '[:find ?freq ?cp
                          :where
                          [?e :satellite-id ?sat-num]
                          [?e :freq ?freq]
                          [?e :channel-2-power ?cp]
                          :in $ ?sat-num]
                     @excel/conn sat-num)))

  (take 10 channel-1)
  (= channel-1 channel-2)

  (d/q '[:find ?sat
         :where [?e :satellite ?sat]]
    @excel/conn)

  (sort-by first (d/q '[:find ?sat-id ?freq
                        :in $ ?sat-id
                        :where [?e :satellite-id ?sat-id]
                        [?e :freq ?freq]]
                   @excel/conn "3000"))




  (d/q '[:find ?sat-num ?freq
         :where [?e :sat ?sat-num]
         [?e :freq ?freq]]
    @excel/conn)


  ())