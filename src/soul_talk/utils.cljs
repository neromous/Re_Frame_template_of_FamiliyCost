(ns soul-talk.utils
  (:require
   [re-frame.core :refer [reg-sub
                          dispatch
                          reg-event-fx
                          reg-event-db
                          subscribe]]))

(defn word-filter [words word-filter]
  (let [word-filter (clojure.string/trim word-filter)
        fs (re-pattern  word-filter)
        result  (re-find fs words)
        ]
    (cond
      (= word-filter "") true
      (= word-filter nil) true
      :default result
      )
    ))


;; route 的工具函数


(defn url->id [url]
  (->  url
       (clojure.string/split  "/")
       last
       str
       keyword))

(defn url->id-str [url]
  (->  url
       (clojure.string/split  "/")
       last
       str))

(defn mapset2map [mapset] (into {} (for [x mapset] (hash-map  (-> x :id str keyword) x))))

(defn mapset2map-url [mapset] (into {} (for [x mapset] (hash-map  (-> x :url str) x))))

(defn query_filter [model-map query]
  (filter #(= query (select-keys % (keys query))) (vals model-map)))





