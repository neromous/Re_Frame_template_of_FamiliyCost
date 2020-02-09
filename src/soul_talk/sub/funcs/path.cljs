(ns soul-talk.sub.funcs.path
  (:require
   [soul-talk.db :refer [model-register
                         value-register]]))

(def page-prefix [:views :pages])
(def value-prefix [:datas :values])
(def active-page  :active-page)

;; 集合所用的
(defn ->model [model-key]
  (let [model (get model-register model-key)]
    model))

(defn ->data-path [model-key]
  (let [model (->model model-key)]
    (get model :data-path)))


;; 集合视图状态
(defn ->view-path [model-key]
  (let [model (->model model-key)]
    (get model :view-path)))

(defn ->view-state [model-key state-key]
  (let [view-path (->view-path model-key)]
    (conj view-path state-key)))

;; 集合暂存状态
(defn ->cache-path [model-key]
  (let [model (->model model-key)]
    (get model :view-path)))

(defn ->cache-state [model-key cache-key]
  (let [cache-path (->cache-path model-key)]
    (conj cache-path cache-key)))

(defn ->model-name [model-key]
  (let [model (->model model-key)]
    (get model :model-name)))

(defn ->model-key [model-key]
  (let [model (->model model-key)]
    (get model :model-key)))

(defn ->item-id [model-key id]
  (let [data-path (->data-path model-key)]
    (conj data-path id)))

(defn ->pagination [model-key]
  (->view-state model-key :pagination))

(defn ->page_pre-page [model-key]
  (conj (->pagination  :pre-page)))

(defn ->page_offset [model-key]
  (conj (->pagination :offset)))

;; 页面所用的存储路径
(defn page->path [page-key]
  (conj page-prefix page-key))

(defn page->state [page-key state-key]
  (conj (page->path  page-key) state-key))

;;  单一值所用的存储路径
(defn value-model [value-key]
  (get value-register value-key))

(defn value->path [value-key]
  (get (value-model value-key) :data-path))

