(ns vanilla.widgets.util
  (:require
    [cljs.core.match :refer-macros [match]]))





(defn line->bar [data options]
  (let [dats (get-in data [:data (get-in options [:src :extract])])
        num  (count dats)]

    ;(.log js/console (str "line->bar " data))

    (into []
          (for [n (range num)]
            {:name (get-in dats
                           [n (get-in options [:src :name] :name)]
                           (str "set " n))
             :data (into [] (get-in dats
                                    [n (get-in options [:src :values] :values)]))}))))


(defn combine [a b]
  (let [m-a (map? a)
        m-b (map? b)]
    (match [m-a m-b]
           [true true] (clojure.set/union a b)
           :else b)))



;;  Copyright (c) Jason Wolfe. All rights reserved.  The use and
;;  distribution terms for this software are covered by the Eclipse Public
;;  License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
;;  be found in the file epl-v10.html at the root of this distribution.  By
;;  using this software in any fashion, you are agreeing to be bound by the
;;  terms of this license.  You must not remove this notice, or any other,
;;  from this software.
;;
;;  map_utils.clj
;;
;;  Utilities for operating on Clojure maps.
;;
;;  jason at w01fe dot com
;;  Created 25 Feb 2009

; from: https://github.com/richhickey/clojure-contrib/blob/2ede388a9267d175bfaa7781ee9d57532eb4f20f/src/main/clojure/clojure/contrib/map_utils.clj

(defn deep-merge-with
  "Like merge-with, but merges maps recursively, applying the given fn
  only when there's a non-map at a particular level.
  (deepmerge + {:a {:b {:c 1 :d {:x 1 :y 2}} :e 3} :f 4}
               {:a {:b {:c 2 :d {:z 9} :z 3} :e 100}})
  -> {:a {:b {:z 3, :c 3, :d {:z 9, :x 1, :y 2}}, :e 103}, :f 4}"
  [f & maps]
  (apply
    (fn m [& maps]
      (if (every? map? maps)
        (apply merge-with m maps)
        (apply f maps)))
    maps))