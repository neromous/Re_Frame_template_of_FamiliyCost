(ns soul-talk.modules.mi.help-func
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [subscribe reg-event-db
                          dispatch reg-sub reg-event-fx]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]))

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



