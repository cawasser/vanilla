(ns vanilla.channel-power-service
  (:require [clojure.tools.logging :as log]
            [vanilla.db.excel-data :as excel]
            [datascript.core :as d]))




(def column-map {:A :satellite
                 :B :freq
                 :C :channel-1-power
                 :D :channel-2-power
                 :E :channel-3-power})
(def post-fn (fn [x] x))



(defn- get-data [sheet sat-num]
  (excel/load-data excel/filename sheet column-map post-fn)

  (let [ch-1 (sort-by first
               (d/q '[:find ?freq ?cp
                      :where
                      [?e :satellite ?sat-num]
                      [?e :freq ?freq]
                      [?e :channel-1-power ?cp]
                      :in $ ?sat-num]
                 @excel/conn sat-num))
        ch-2 (sort-by first
               (d/q '[:find ?freq ?cp
                      :where
                      [?e :satellite ?sat-num]
                      [?e :freq ?freq]
                      [?e :channel-2-power ?cp]
                      :in $ ?sat-num]
                 @excel/conn sat-num))
        ch-3 (sort-by first
               (d/q '[:find ?freq ?cp
                      :where
                      [?e :satellite ?sat-num]
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
   :series      (get-data sheet sat-num)})



(comment
  (def sheet "Sat-Power-1000")
  (def sat-num "3000")


  (def channel-1 (sort-by first
                   (d/q '[:find ?freq ?cp
                          :where
                          [?e :satellite ?sat-num]
                          [?e :freq ?freq]
                          [?e :channel-1-power ?cp]
                          :in $ ?sat-num]
                     @excel/conn sat-num)))
  (def channel-2 (sort-by first
                   (d/q '[:find ?freq ?cp
                          :where
                          [?e :satellite ?sat-num]
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
                        :where [?e :satellite ?sat-id]
                        [?e :freq ?freq]]
                   @excel/conn "3000"))




  (d/q '[:find ?sat-num ?freq
         :where [?e :sat ?sat-num]
         [?e :freq ?freq]]
    @excel/conn)

  (def sat-3000 (let [ch-1 (sort-by first
                             (d/q '[:find ?freq ?cp
                                    :where
                                    [?e :satellite ?sat-num]
                                    [?e :freq ?freq]
                                    [?e :channel-1-power ?cp]
                                    :in $ ?sat-num]
                               @excel/conn sat-num))
                      ch-2 (sort-by first
                             (d/q '[:find ?freq ?cp
                                    :where
                                    [?e :satellite ?sat-num]
                                    [?e :freq ?freq]
                                    [?e :channel-2-power ?cp]
                                    :in $ ?sat-num]
                               @excel/conn sat-num))
                      ch-3 (sort-by first
                             (d/q '[:find ?freq ?cp
                                    :where
                                    [?e :satellite ?sat-num]
                                    [?e :freq ?freq]
                                    [?e :channel-3-power ?cp]
                                    :in $ ?sat-num]
                               @excel/conn sat-num))]
                  [ch-1 ch-2 ch-3]))

  (def sat-4000 (let [ch-1 (sort-by first
                             (d/q '[:find ?freq ?cp
                                    :where
                                    [?e :satellite ?sat-num]
                                    [?e :freq ?freq]
                                    [?e :channel-1-power ?cp]
                                    :in $ ?sat-num]
                               @excel/conn "4000"))
                      ch-2 (sort-by first
                             (d/q '[:find ?freq ?cp
                                    :where
                                    [?e :satellite ?sat-num]
                                    [?e :freq ?freq]
                                    [?e :channel-2-power ?cp]
                                    :in $ ?sat-num]
                               @excel/conn "4000"))
                      ch-3 (sort-by first
                             (d/q '[:find ?freq ?cp
                                    :where
                                    [?e :satellite ?sat-num]
                                    [?e :freq ?freq]
                                    [?e :channel-3-power ?cp]
                                    :in $ ?sat-num]
                               @excel/conn "4000"))]
                  [ch-1 ch-2 ch-3]))

  (= sat-3000 sat-4000)

  ())