(ns soul-talk.sub.funcs.path
  (:require
   [soul-talk.db :refer [model-register]]))

(defn ->model [model-key]
  (let [model (get model-register model-key)]
    model))

(defn ->data-path [model-key]
  (let [model (->model model-key)]
    (get model :data-path)))

(defn ->view-path [model-key]
  (let [model (->model model-key)]
    (get model :view-path)))

(defn ->view-state [model-key state-key]
  (let [view-path (->view-path model-key)]
    (conj (view-path state-key))))

(defn ->cache-path [model-key]
  (let [model (->model model-key)]
    (get model :view-path)))

(defn ->cache-state [model-key cache-key]
  (let [cache-path (->cache-path model-key)]
    (conj (view-path cache-key))))

(defn ->model-name [model-key]
  (let [model (->model model-key)]
    (get model :model-name)))

(defn ->model-key [model-key]
  (let [model (->model model-key)]
    (get model :model-key)))

(defn ->id [model-key id]
  (let [data-path (->data-path model-key)]
    (conj data-path id)))

(defn ->pagination [model-key]
  (->view-state model-key :pagination))













