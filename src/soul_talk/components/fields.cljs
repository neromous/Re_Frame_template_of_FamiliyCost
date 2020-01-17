(ns soul-talk.components.fields
  (:require [reagent.core :as r]
            [soul-talk.date-utils :as du]))

(defn text-read [store-value config]
  (let [default-value  {}
        field-value (merge default-value config)]
    [:label  field-value  @store-value]))

(defn text-input [store-value config]
  (let [default-value {:on-change #(reset! store-value (->  % .-target .-value))}
        field-value (merge default-value config)]
    [:> js/antd.Input field-value]))

(defn text-change [store-value  origin-value config]
  (let [default-value {:on-change #(reset! store-value (->  % .-target .-value))
                       :defaultValue (-> @origin-value clj->js)}
        field-value (merge default-value config)]
    [:> js/antd.Input field-value]))

(defn number-read [store-value config]
  (let [default-value {:disabled true
                       :defaultValue 0}
        field-value (merge default-value config)]
    [:> js/antd.InputNumber field-value]))

(defn number-input [store-value config]
  (let [default-value {:on-change #(reset! store-value  %)
                       :defaultValue 0}
        field-value (merge default-value config)]
    [:> js/antd.InputNumber field-value]))

(defn number-change [store-value origin-value config]
  (let [default-value {:on-change #(reset! store-value %)
                       :defaultValue @origin-value}
        field-value (merge default-value config)]
    [:> js/antd.InputNumber field-value]))

(defn date-read [store-value  config]
  (let [default-value {:disabled true
                       :defaultValue (new js/moment @store-value)}
        field-value (merge default-value config)]
    [:> js/antd.DatePicker field-value]))

(defn date-change [store-value origin-value  config]
  (let [default-value {:on-change #(reset! store-value  (du/antd-date-parse %))
                       :showToday true
                       :defaultValue  (new js/moment  @origin-value)}
        field-value (merge default-value config)]
    [:> js/antd.DatePicker field-value]))

(defn date-input [new-value  config]
  (let [default-value {:on-change #(reset! new-value  (du/antd-date-parse %))
                       :showToday true
                       :defaultValue  (new js/moment)}
        field-value (merge default-value  config)]
    [:> js/antd.DatePicker field-value]))

(defn bool-read [store-value config]
  (let [default-value {:checked  @store-value
                       :disabled true}
        field-value (merge default-value  config)]
    [:> js/antd.Switch  field-value]))

(defn bool-change [store-value config]
  (let [default-value {:checked  @store-value
                       :on-change #(reset!
                                    store-value
                                    (if (contains?  #{nil false} @store-value)
                                      true
                                      false))}
        field-value (merge default-value  config)]
    [:> js/antd.Switch  field-value]))

;; (defn category-read [store-value options config]
;;   (let [default-config {:options (map #(-> {}
;;                                            (assoc :label  (:title %))
;;                                            (assoc :value  (:url %)))
;;                                       (into #{}  (vals @selection)))
;;                         :on-change  #(swap! store assoc-in [field-key]
;;                                             (-> % js->clj first))}

;;         field-config (merge default-config config)]

;;     [:> js/antd.Cascader field-config]))

(defn category-input [])

(defn category-change [])


