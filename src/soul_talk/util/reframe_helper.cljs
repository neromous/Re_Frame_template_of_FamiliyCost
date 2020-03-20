(ns soul-talk.util.reframe-helper
  (:require
   [reagent.core :as r]
   [soul-talk.util.date-utils :as du]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]))

(defn sub> [event-id & args]
  (subscribe (into [] (cons event-id args))))

(defn act> [event-id & args]
  (dispatch (into [] (cons event-id args))))

(defn dsub [sub-path]
  (subscribe [:d/get sub-path]))

(defn dget [sub-path form]
  (dispatch [:d/datalog sub-path  form]))

(defn dset [sub-path form]
  (dispatch [:d/transact sub-path  form]))

(def to-columns
  (map (fn [[k v]]
         {:key k
          :dataIndex k
          :title v})))

(defn field->float&2 [field]
  (map (fn [x]
         (update x field   #(/ (.round js/Math (* 100 %)) 100)))))

(defn fix-key
  [kf]
  (map (fn [x] (zipmap (map kf (keys x)) (vals x)))))

(defn fix-value
  [vf]
  (map (fn [x] (zipmap (keys x)  (map vf x)))))

(defn field->moment [field]
  (map (fn [x]
         (update x field   #(new js/moment %)))))

(defn field->date2str [field]
  (map (fn [x]
         (update x field #(str %)))))

(defn- remove-db_id* [item]
  (reduce-kv (fn [m k v]
               (cond
                 (= k :db/id)  (dissoc m :db/id)
                 (map? v) (let [value  (remove-db_id* v)]
                            (if (= (count value) 0)
                              m
                              (assoc m k value)))
                 (vector? v) (let [no-db_id  (map remove-db_id*)
                                   no_null  (filter #(not= (count %) 0))
                                   value (into [] (comp no-db_id no_null) v)]
                               (if (= (count value) 0)
                                 m
                                 (assoc m k value)))
                 :default (assoc m k v)))

             {} item))

(def remove-db_id
  (map remove-db_id*))

(defn- remove-db_id*-test []
  (remove-db_id* {:haha "dasd"  :db/id 323
                  :da "dfasd"
                  :aaaa {:db/id  "dasd"  :ll 'asd}
                  :hahaha [{:haha "dfasd" :db/id 22} {} {:db/id 454}]}))
