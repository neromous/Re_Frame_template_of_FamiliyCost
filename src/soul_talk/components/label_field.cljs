(ns soul-talk.components.label-field
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [soul-talk.util.date-utils :as du]
            [soul-talk.utils :as utils]
            [reagent.core :as r]))

(defmulti label-field  (fn [column & _]  (or
                                          (get column :view_type)
                                          (get column :data_type))))

(defmethod label-field :default  [column & _]
  ;;(println (str "这个没有对应的定义"  column))
  [:p "这里缺少定义"]
  )

(defmethod label-field "varchar"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (utils/to-keyword  (get column :column_name))]

    [:div
     [:> js/antd.Input
      (merge
       {:on-change #(swap! form-state  assoc-in
                           [:after column_name]
                           (->  % .-target .-value))
        :value (get-in @form-state [:after column_name])

        :defaultValue (get-in @form-state [:before column_name])
        :placeholder (str "请输入:"  (get column :column_title))}
       addtion-config)]]))

(defmethod label-field "char"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (utils/to-keyword  (get column :column_name))]

    [:> js/antd.Input
     (merge
      {:on-change #(swap! form-state  assoc-in
                          [:after column_name]
                          (->  % .-target .-value))

       :value (get-in @form-state [:after column_name])
       :defaultValue (get-in form-state [:before column_name])
       :placeholder (str "请输入:"  (get column :column_title))}
      addtion-config)]))

(defmethod label-field "datetime"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (utils/to-keyword  (get column :column_name))]

    [:> js/antd.DatePicker  (merge
                             {:on-change #(swap! form-state  assoc-in
                                                 [:after column_name]
                                                 %)

                              :value (new js/moment
                                          (get-in @form-state [:after column_name]))

                              :defaultValue (new js/moment
                                                 (get-in form-state [:before column_name] 0))
                              :placeholder (str "请输入:"  (get column :column_title))}
                             addtion-config)]))

(defmethod label-field "date"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (utils/to-keyword  (get column :column_name))]

    [:> js/antd.DatePicker  (merge
                             {:on-change #(swap! form-state  assoc-in
                                                 [:after column_name]
                                                 %)
                              :value (new js/moment
                                          (get-in @form-state [:after column_name]))

                              :defaultValue (new js/moment
                                                 (get-in form-state [:before column_name] 0))
                              :placeholder (str "请输入:"  (get column :column_title))}
                             addtion-config)]))

(defmethod label-field "int"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (utils/to-keyword  (get column :column_name))]

    [:> js/antd.InputNumber
     (merge
      {:on-change #(swap! form-state  assoc-in
                          [:after column_name]
                          (->  %))
       :defaultValue (get-in form-state [:before column_name])

       :value (get-in @form-state [:after column_name])
       :placeholder (str "请输入:"  (get column :column_title))}
      addtion-config)]))

(defmethod label-field "bigint"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (utils/to-keyword  (get column :column_name))]

    [:> js/antd.InputNumber
     (merge {:on-change #(swap! form-state  assoc-in
                                [:after column_name]
                                (->  %))
             :defaultValue (get-in form-state [:before column_name])

             :value (get-in @form-state [:after column_name])
             :placeholder (str "请输入:"  (get column :column_title))}
            addtion-config)]))

(defmethod label-field "tinyint"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (utils/to-keyword  (get column :column_name))]

    [:> js/antd.InputNumber
     (merge
      {:on-change #(swap! form-state  assoc-in
                          [:after column_name]
                          (->  % int))
       :defaultValue (get-in form-state [:before column_name])

       :value (get-in @form-state [:after column_name])
       :placeholder (str "请输入:"  (get column :column_title))}
      addtion-config)]))

;; (defmethod label-field "smallint"
;;   [column  form-state  &  [addtion-config _]]
;;   (let [column_name (utils/to-keyword  (get column :column_name))]

;;     [:> js/antd.InputNumber  (merge
;;                               {:on-change #(swap! form-state  assoc-in
;;                                                   [:after column_name]
;;                                                   (->  %))

;;                                :value (get-in @form-state [:after column_name])
;;                                :defaultValue (get-in form-state [:before column_name])
;;                                :placeholder (str "请输入:"  (get column :column_title))}
;;                               addtion-config)]))

(defmethod label-field "float"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (utils/to-keyword  (get column :column_name))]

    [:> js/antd.InputNumber  (merge
                              {:on-change #(swap! form-state  assoc-in
                                                  [:after column_name]
                                                  (->  % .-target .-value))

                               :value (get-in @form-state [:after column_name] 0)
                               :placeholder (str "请输入:"  (get column :column_title))}
                              addtion-config)]))

(defmethod label-field "double"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (utils/to-keyword  (get column :column_name))]

    [:> js/antd.InputNumber  (merge
                              {:on-change #(swap! form-state  assoc-in
                                                  [:after column_name]
                                                  (->  % .-target .-value))

                               :value (get-in @form-state [:after column_name] 0)
                               :placeholder (str "请输入:"  (get column :column_title))}
                              addtion-config)]))


(defmethod label-field "decimal"
  [column  form-state  &  [addtion-config _]]
  (let [column_name (utils/to-keyword  (get column :column_name))]

    [:> js/antd.InputNumber  (merge
                              {:on-change #(swap! form-state  assoc-in
                                                  [:after column_name]
                                                  (->  % .-target .-value))

                               :value (get-in @form-state [:after column_name] 0)
                               :placeholder (str "请输入:"  (get column :column_title))}
                              addtion-config)]))


