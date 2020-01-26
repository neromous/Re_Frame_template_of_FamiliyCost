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
        result  (re-find fs words)]
    (cond
      (= word-filter "") true
      (= word-filter nil) true
      :default result)))


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

(defn round-number
  [f]
  (/ (.round js/Math (* 100 f)) 100))

(defn filter->maerial-raw [vecs query-form]
  (let [flow_ids  [:flow_id (get query-form :flow_ids #{})]
        order_ids [:order_id (get  query-form :order_ids #{})]
        order_detail_ids [:order_detail_id (get query-form :order_detail_ids #{})]
        reduce-fn (fn
                    [x [k v]]
                    (if  (= (count v) 0)
                      x
                      (filter #(contains? (set v) (k  %))  x)))]

    (reduce reduce-fn  vecs [flow_ids
                             order_ids
                             order_detail_ids])))

(defn filter->order-track [vecs query-form]
  (let [flow_ids  [:flow_id (get query-form :flow_ids #{})]
        order_ids [:order_id (get  query-form :order_ids #{})]
        order_detail_ids [:order_detail_id (get query-form :order_detail_ids #{})]
        companys [:companys (get query-form :companys  #{})]
        factorys [:factorys (get query-form :factorys  #{})]
        workshops [:workshops (get query-form :workshops  #{})]
        customers [:customers (get query-form :customers  #{})]
        dyelot_numbers [:dyelot_numbers (get query-form :dyelot_numbers  #{})]
        color_numbers [:color_numbers (get query-form :color_numbers  #{})]

        reduce-fn (fn
                    [x [k v]]
                    (if  (= (count v) 0)
                      x
                      (filter #(contains? (set v) (k  %))  x)))]

    (reduce reduce-fn  vecs [flow_ids
                             order_ids
                             order_detail_ids
                             factorys
                             companys
                             factorys
                             workshops
                             customers
                             dyelot_numbers
                             color_numbers])))






