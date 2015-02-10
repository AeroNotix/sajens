(ns sajens.fikszyn
  (:require [metrics.core :as clj-metrics])
  (:import [com.codahale.metrics Metric Counting]))


(definterface ICounter
  (update [^Number value] "Add a value to the count"))

(definterface IExpirer
  (expire [] "Tells an IExpirer to expire its elements"))

(defn expire-entry [{:keys [time]} now period]
  (< (- now time) period))

(defn ->Spiral
  ([name period]
     (->Spiral name period clj-metrics/default-registry))
  ([name period registry]
     (let [counts (atom [])
           spiral (proxy [Metric Counting ICounter IExpirer] []
                    (expire []
                      (let [ts (System/currentTimeMillis)]
                        (swap! counts (fn [entries]
                                        (filter #(expire-entry % ts period)
                                          entries)))
                        nil))
                    (getCount []
                      (.expire this)
                      (reduce + (map :value @counts)))
                    (update [value]
                      (let [ts (System/currentTimeMillis)]
                        (swap! counts conj {:time ts :value value})
                        nil)))]
       (.register registry name spiral))))
