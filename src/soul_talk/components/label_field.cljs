(ns soul-talk.components.label-field
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [soul-talk.util.date-utils :as du]
            [reagent.core :as r]))

(defmulti label-field  (fn [column & _]  (get column :data_type)))

(defmethod label-field "varchar"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              (->  % .-target .-value))
           :defaultValue (get-in form-state [:before :column_name])
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.Input  (merge (default-value) addtion-config)]))

(defmethod label-field "char"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              (->  % .-target .-value))
           :defaultValue (get-in form-state [:before :column_name])
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.Input  (merge (default-value) addtion-config)]))



(defmethod label-field "datetime"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              %)
           :defaultValue (new js/moment
                              (get-in form-state [:before :column_name]))
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.Input  (merge (default-value) addtion-config)]))

(defmethod label-field "date"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              %)
           :defaultValue (new js/moment
                              (get-in form-state [:before :column_name]))
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.Input  (merge (default-value) addtion-config)]))


(defmethod label-field "int"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              (->  % ))
           :defaultValue (get-in form-state [:before :column_name])
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.InputNumber  (merge (default-value) addtion-config)]))

(defmethod label-field "bigint"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              (->  % ))
           :defaultValue (get-in form-state [:before :column_name])
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.InputNumber  (merge (default-value) addtion-config)]))

(defmethod label-field "tinyint"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              (->  % int))
           :defaultValue (get-in form-state [:before :column_name])
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.InputNumber  (merge (default-value) addtion-config)]))

(defmethod label-field "smallint"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              (->  % ))
           :defaultValue (get-in form-state [:before :column_name])
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.InputNumber  (merge (default-value) addtion-config)]))

(defmethod label-field "float"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              (->  % ))
           :defaultValue (get-in form-state [:before :column_name])
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.InputNumber  (merge (default-value) addtion-config)]))


(defmethod label-field "double"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              (->  % ))
           :defaultValue (get-in form-state [:before :column_name])
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.InputNumber  (merge (default-value) addtion-config)]))

(defmethod label-field "decimal"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (get column :column_name)
        default-value
        (fn []
          {:on-change #(swap! form-state  assoc-in
                              [:after column_name]
                              (->  % ))
           :defaultValue (get-in form-state [:before :column_name])
           :placeholder (str "请输入:"  (get column :column_title))})]

    [:> js/antd.InputNumber  (merge (default-value) addtion-config)]))










;; (defmethod label-field :datetime
;;   [event-id
;;    [layout-name config-1 config-2]
;;    [store-value config]]
;;   (let [default-value {:on-change #(reset! store-value  (du/antd-date-parse %))
;;                        :showToday true
;;                        :defaultValue  (new js/moment)}

;;         field-value (merge default-value config)
;;         config-1 (merge {:span 6}  config-1)
;;         config-2 (merge {:span 16} config-2)]

;;     [:> js/antd.Row {:gutter 16}
;;      [:> js/antd.Col config-1
;;       [:label layout-name]]
;;      [:> js/antd.Col config-2
;;       [:> js/antd.DatePicker field-value]]]))

;; (defmethod label-field :int
;;   [event-id
;;    [layout-name config-1 config-2]
;;    [store-value config]]
;;   (let [default-value {:on-change  #(reset! store-value  %)
;;                        :defaultValue  0}

;;         field-value (merge default-value config)
;;         config-1 (merge {:span 6} config-1)
;;         config-2 (merge  {:span 16} config-2)
;;         ;;
;;         ]

;;     [:> js/antd.Row {:gutter 16}
;;      [:> js/antd.Col config-1
;;       [:label layout-name]]
;;      [:> js/antd.Col config-2
;;       [:> js/antd.InputNumber field-value]]]))

;; (defmethod label-field :view.select
;;   [event-id
;;    [layout-name config-1 config-2]
;;    [store-value relate-values  config key-seeds]]
;;   (let [default-value {:onChange #(reset! store-value %)
;;                        :style        {:width 120 :padding "5px"}}
;;         field-value (merge default-value config)
;;         config-1 (merge {:span 6} config-1)
;;         config-2 (merge {:span 16} config-2)
;;         ;;
;;         ]

;;     [:> js/antd.Row {:gutter 16}
;;      [:> js/antd.Col config-1
;;       [:label layout-name]]
;;      [:> js/antd.Col config-2
;;       [:> js/antd.Select  field-value
;;        (doall
;;         (for [option-config relate-values]
;;           ^{:key (str (get option-config :value) "_"  key-seeds)}
;;           [:> js/antd.Select.Option
;;            {:value (:value option-config)}
;;            (:label option-config)]))
;;            ;;
;;        ]]]))

;; (defmethod label-field :view.cascader
;;   [event-id
;;    [layout-name config-1 config-2]
;;    [store-value config]]
;;   (let [default-value {:onChange #(reset! store-value %)
;;                        :style    {:width 400
;;                                   :padding "10px"}}
;;         field-value (merge default-value config)
;;         config-1 (merge {:span 4} config-1)
;;         config-2 (merge {:span 20} config-2)
;;         ;;
;;         ]

;;     [:> js/antd.Row {:gutter 16}
;;      [:> js/antd.Col config-1
;;       [:label layout-name]]
;;      [:> js/antd.Col config-2

;;       [:> js/antd.Cascader field-value]
;;       ;;
;;       ]]))







