(ns soul-talk.sub.funcs.orm
  (:require [soul-talk.util.query-filter :as query-filter]
            [soul-talk.sub.funcs.path :as path]
            [soul-talk.util.data-utils :as data-utils]
            [soul-talk.utils :as utils]
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
  (let [path (path/->item-id  model-key id)]
    (->> (get-in db (conj path id))
         vals
         (filter  #(query-filter/is-part-of-query? %  {:id id})))))

;; 模型的视图的汇总
(defn view-state> [db [_ model-key]]
  (let [view-path  (path/->view-path model-key)]
    (get-in db view-path)))

;; (defn view-state> [db [_ model-key state-key]]
;;   (let [view-state  (path/->view-state model-key state-key)]
;;     (get-in db view-state)))

;; 中间件所用的
(defn replace> [db [_ model-key response]]
  (let [data-path (path/->data-path model-key)
        model-types (path/->model-types model-key)
        view-path (path/->view-path model-key)
        meta-path (path/->meta-path model-key)
        pagination-path (path/->pagination model-key)
        metadata (get-in response [:metadata])

        dataset  (get-in response [:dataset])
        dataset (utils/items-serial-apply dataset model-types   ) 
        ;;dataset (map #(data-utils/dto % model-types)  dataset)
        dataset (-> (group-by :id dataset)
                    ((fn [x] (zipmap (keys x)  (->> x vals (map first))))))

        metadata (-> (group-by :column_name metadata)
                     ((fn [x] (zipmap
                               (->> x keys (map keyword))
                               (->> x vals (map first))))))

        pagination  (get response :pagination)]
    (-> db
        (assoc-in data-path  dataset)
        (assoc-in meta-path metadata)
        (assoc-in pagination-path pagination))))

(defn add> [db [_ model-key response]]
  (let [data-path (path/->data-path model-key)
        model-types (path/->model-types model-key)
        view-path (path/->view-path model-key)
        data  (first (get-in response [:dataset]))
        data (utils/item-kv-apply data model-types)
        id (:id data)]
    (assoc-in  db (conj data-path id)  data)))

(defn del>  [db [_ model-key response]]
  (let [data-path (path/->data-path model-key)
        view-path (path/->view-path model-key)
        id   (get-in response [:dataset])]
    (update-in  db data-path  dissoc id)))

(defn update>  [db [_ model-key response]]

  (let [model-types (path/->model-types model-key)
        data (get-in response [:dataset])
        id (:id data)
        data (utils/item-kv-apply data model-types)
        item-path (path/->item-id model-key id)]
    (assoc-in  db item-path data)))


