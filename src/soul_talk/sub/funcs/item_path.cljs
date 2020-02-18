(ns soul-talk.sub.funcs.item-path
  (:require
   [soul-talk.db :refer [model-register
                         item-register]]))


;; 集合所用的


(defn ->item [item-key]
  (let [item (get item-register item-key)]
    item))

(defn ->data-path [item-key]
  (let [item (->item item-key)]
    (get item :data-path)))

(defn ->url [item-key]
  (let [item (->item item-key)]
    (get item :url)))


