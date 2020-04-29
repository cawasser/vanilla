(ns vanilla.server
    (:require [dashboard-clj.core :as dash]
              [vanilla.environment]

              [vanilla.bubble-service]
              [vanilla.heatmap-service]
              [vanilla.network-service]
              [vanilla.sankey-service]
              [vanilla.scatter-service]
              [vanilla.stoplight-service]
              [vanilla.usage-12-hour-service]
              [vanilla.spectrum-traces-service]
              [vanilla.usage-data-service]
              [vanilla.power-data-service]
              [vanilla.current-time-service]
              [vanilla.table-service]
              [vanilla.continent-map-service]
              [vanilla.australia-map-service]
              [vanilla.arearange-service]
              [vanilla.energy-use-service]
              [vanilla.task-service]
              [vanilla.carousel-service]

              [vanilla.service-deps :as deps]
              [clojure.tools.logging :as log]
              [trptcolin.versioneer.core :as version])

    (:gen-class))



(defn start-dashboard[]
  (log/info "server starting version: " (version/get-version
                                          "vanilla"
                                          "vanilla"
                                          "version number not found"))
  (dash/start deps/datasources))

(defn -main [& [port]]
  (start-dashboard))



(comment
  (def data {:nodes {100 {:name "Node 1" :ports {:in [{:id 101 :label "In"}]
                                                 :out [{:id 102 :label "Out"}
                                                       {:id 103 :label "Out"}]}}
                     200 {:name "Node 2" :ports {:in [{:id 201 :label "In"}]}}
                     300 {:name "Node 3" :ports {:in [{:id 301 :label "In"}]}}}
             :links [[102 201] [103 301]]})

  (:nodes data)
  (def path [:ports :in])
  (def id 200)
  (def node id)
  (def node-data (get-in data [:nodes id]))
  (def port-data (get-in node-data [:ports]))


  (defn get-ports [node-data path]
    (map #(:id %) (get-in node-data path)))

  (defn addInPort [node port]
    (str "In-" (:id port)))
  (defn addOutPort [node port]
    (str "Out-" (:id port)))

  (defn ports? [port-data]
    (not (or
           (nil? port-data)
           (empty? port-data))))

  (defn add-ports [node port-data]
    (remove nil?
      [(if (ports? (:in port-data))
         (map #(addInPort node %) (:in port-data)))
       (if (ports? (:out port-data))
         (map #(addOutPort node %) (:out port-data)))]))

  (defn make-node [[id node-data]]
    {id {:draw-node "here is the JS draw node"
         :draw-ports (add-ports id (get-in node-data [:ports]))
         :all-ports (flatten
                      (conj (get-ports node-data [:ports :in])
                        (get-ports node-data [:ports :out])))}})

  (make-node [id node-data])

  (map #(make-node %) (:nodes data))


  ())