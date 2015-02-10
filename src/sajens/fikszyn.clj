(ns sajens.fikszyn
  (:require [metrics.core :as clj-metrics])
  (:import [com.codahale.metrics Counting Counter]))


(definterface ICounter
  (update [^Number value] "Add a value to the count"))

(defn expire-entry [{:keys [time]} now period]
  (< (- now time) period))

(defn ->Spiral
  ([name period]
     (->Spiral name period clj-metrics/default-registry))
  ([name period registry]
     (if (get (.getCounters registry) name)
       (get (.getCounters registry) name)
       (let [counts (atom [])
             spiral (proxy [Counter ICounter] []
                      (getCount []
                        (let [ts (System/currentTimeMillis)]
                          (swap! counts (fn [entries]
                                          (filter #(expire-entry % ts period)
                                            entries))))
                        (reduce + (map :value @counts)))
                      (update [value]
                        (let [ts (System/currentTimeMillis)]
                          (swap! counts conj {:time ts :value value})
                          nil)))]
         (.register registry name spiral)))))
