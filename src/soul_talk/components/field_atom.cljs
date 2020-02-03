(ns soul-talk.components.field-atom
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [soul-talk.util.date-utils :as du]
            [reagent.core :as r]))






(defn text-input [column form-state config]
  (let [default-value {:on-change #(reset! store-value (->  % .-target .-value))}
        field-value (merge default-value config)]
    [:> js/antd.Input field-value]))


(defn number-input [store-value config]
  (let [default-value {:on-change #(reset! store-value  %)
                       :defaultValue 0}
        field-value (merge default-value config)]
    [:> js/antd.InputNumber field-value]))

(defn date-input [new-value  config]
  (let [default-value {:on-change #(reset! new-value  (du/antd-date-parse %))
                       :showToday true
                       :defaultValue  (new js/moment)}
        field-value (merge default-value  config)]
    [:> js/antd.DatePicker field-value]))


(defn bool-change [store-value config]
  (let [default-value {:checked  @store-value
                       :on-change #(reset!
                                    store-value
                                    (if (contains?  #{nil false} @store-value)
                                      true
                                      false))}
        field-value (merge default-value  config)]
    [:> js/antd.Switch  field-value]))

(defn select-input  [store-value relate-values key-seeds  config]
  (let [default-value  {:onChange #(reset! store-value %)
                        :style        {:width 120 :padding "5px"}
                        }
        field-config (merge default-value config)]
    [:> js/antd.Select  field-config
     (doall
      (for [option-config relate-values]
        ^{:key (str (get option-config :value) "_"  key-seeds)}
        [:> js/antd.Select.Option
         {:value (:value option-config)}
         (:label option-config)]))
     ;;
     ]))




