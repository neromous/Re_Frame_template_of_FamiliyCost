(ns soul-talk.sub.funcs.orm
  (:require [soul-talk.util.query-filter :as query-filter]
            [soul-talk.sub.funcs.path :as path]
            [soul-talk.db :refer [model-register]]))

(defn default [data [_]] data)

(defn sample  [db [_ model-key]]
  (let [data-path (path/->data-path model-key)]
    (-> (get-in db data-path)
        first
        val)))

(defn raw>  [db [_ model-key]]
  (let [data-path (path/->data-path model-key)]
    (get-in db data-path)))

(defn all> [db [_ model-key]]
  (let [data-path  (path/->data-path model-key)]
    (vals (get-in db data-path))))

(defn  find-by> [db [_ model-key query]]
  (let [data-path (path/->data-path model-key)]
    (->> (get-in db data-path)
         vals
         (filter  #(query-filter/is-part-of-query? % query)))))

(defn  find-id> [db [_ model-key id]]
  (let [path (path/->id  model-key id)]
    (->> (get-in db (conj path id))
         vals
         (filter  #(query-filter/is-part-of-query? %  {:id id})))))

(defn view-path> [db [_ model-key]]
  (let [view-path  (path/->view-path model-key)]
    (get-in db view-path)))

(defn view-state> [db [_ model-key state-key]]
  (let [view-state  (path/->view-state model-key state-key)]
    (get-in db view-state)))

(defn replace> [db [_ model-key response]]
  (let [data-path (path/->data-path model-key)
        view-path (path/->view-path model-key)
        pagination-path (path/->pagination model-key)
        dataset  (get-in response [:dataset])
        dataset (-> (group-by :id dataset)
                    ((fn [x] (zipmap (keys x)  (->> x vals   (map first))))))
        pagination  (get response :pagination)
       ]

    (-> db
        (assoc-in data-path  dataset)
        (assoc-in pagination-path pagination))))





