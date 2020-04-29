(ns vanilla.widgets.storm-diagram-widget
  (:require ["storm-react-diagrams" :refer [DiagramEngine DiagramModel DefaultNodeModel,
                                            LinkModel DiagramWidget DefaultLinkModel]]))


(def data {:nodes [{100 {:name "Node 1" :ports {:in [{:id 101 :label "In"}]
                                                :out [{:id 102 :label "Out"}
                                                      {:id 103 :label "Out"}]}}
                    200 {:name "Node 2" :ports {:in [{:id 201 :label "In"}]}}
                    300 {:name "Node 3" :ports {:in [{:id 301 :label "In"}]}}}]
           :links [[102 201] [103 301]]})


; TODO: how do I get the ports by id into a map I can use for look-up?
(defn- add-ports [node port-data]
  (if (not (empty? (:in port-data)))
    (map #(.addInPort node %) (:in port-data)))
  (if (not (empty? (:out port-data)))
    (map #(.addOutPort node %) (:out port-data))))



(defn- make-a-node [[id node-data]]
  (let [node (DefaultNodeModel. (:name node-data) "rgb(0,192,255)")
        ret {id {:draw-node node
                 :all-ports (map #(add-ports node %)
                              (:ports node-data))}}]
    ret))



(defn- lookup [nodes port-id]
  port-id)



(defn- make-a-link [nodes [from-port to-port]]
  (let [link (.link (lookup nodes from-port) (lookup nodes to-port))]
    link))


(defn- make-model [data]
  (let [model (DiagramModel.)
        nodes (map #(make-a-node %) (:nodes data))
        links (map #(make-a-link nodes %) (:links data))]

    (map #(.add model %) nodes)
    (map #(.add model %) links)

    model))



(defn make-widget [name data options]
  (let [engine (DiagramEngine.)
        model  (DiagramModel.)
        node1  (DefaultNodeModel. "Node 1" "rgb(0,192,255)")
        node2  (DefaultNodeModel. "Node 2", "rgb(192,255,0)")]

    (.installDefaultFactories engine)

    (.setPosition node1 100 100)
    (.setPosition node2 400 100)
    (let [port1  (.addOutPort node1 "Out")
          port2  (.addInPort node2 "In")
          link1  (.link port1 port2)]
      (.addLabel link1 "Hello World!")

      (.addAll model node1 node2 link1)

      (.setDiagramModel engine model)

      [:> DiagramWidget {:diagramEngine engine}])))
