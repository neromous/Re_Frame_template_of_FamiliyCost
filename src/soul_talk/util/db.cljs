(ns soul-talk.util.db
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [soul-talk.util.date-utils :as du]
             [soul-talk.util.serializer :refer [serializer]]
             [reagent.core :as r]
             [soul-talk.db :refer [model-register
                                   item-register]]))
;; 集合所用的
(defn model-info [model-key & args]
  (let [db-register  (deref (subscribe [:register]))
        register (or db-register item-register)
        item (get-in register (cons model-key args))]
    item))

;; 序列化器
(defn dataset-serializer [valid_names]
  (map (fn [init]
         (reduce-kv (fn [m field [field-tag default-value]]
                      (update m field (fn [x]
                                        (if (nil? x)
                                          default-value
                                          (serializer field-tag x)))))
                    init
                    valid_names))))

(defn model-serializer [model-key]
  (dataset-serializer (model-info model-key :validator)))

(defn get* [model-key]
  (deref (subscribe [:get model-key])))

(defn set* [model-key value]
  (dispatch [:set model-key value]))

(defn new! [model-key {:keys [id] :as body}]
  (dispatch [:new model-key id body]))

(defn delete! [model-key id]
  (dispatch [:delete model-key id]))

(defn update! [model-key id query]
  (dispatch [:update model-key id query]))

(defn server-get* [model-key query]
  (dispatch [:http/get model-key query]))

(defn server-set* [model-key query]
  (dispatch [:http/set model-key query]))


