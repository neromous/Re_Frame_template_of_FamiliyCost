(ns soul-talk.components.table
  (:require [soul-talk.routes :refer [navigate!]]
            [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn sample-model-table
  "输入数据的索引和数据的数据的columns"
  [view-key model-key columns]
  (r/with-let [data (subscribe [:model/all model-key])
               ;;columns (subscribe [:view/get-model-columns view-key model-key])
               ]
    (fn []
      [:> js/antd.Table   {:dataSource @data
                           :columns   (clj->js  (columns))
                           :rowKey "id"}])))

(defn sample-table
  "输入数据的索引和数据的数据的columns"
  [model columns]

  (r/with-let [data    (subscribe [(:db-key model)])]
    (fn []
      [:> js/antd.Table   {:dataSource @data
                           :columns   (clj->js (columns model data))
                           :rowKey "id"}])))

(defn fake-table
  "输入数据的索引和数据的数据的columns"
  [datatype fakedata  columns]
  (fn []
    [:> js/antd.Table   {:dataSource fakedata
                         :columns   (clj->js (columns))
                         :rowKey "id"}]))

(defn fake-table-sum
  "输入数据的索引和数据的数据的columns"
  [x datatype fakedata]
  (r/with-let [data fakedata
               columns  (concat
                         [{:title x
                           :dataIndex x
                           :key x
                           :filters (for [item (map  #((keyword x) %) data)]       {:text item
                                                                                    :value item})
                           :onFilter (fn [value record]
                                       (= (-> record (js->clj :keywordize-keys true) (keyword x)) value))}]

                         (for [y (:calc datatype)
                               :when (contains? (set (map name (-> fakedata first keys))) y)]
                           {:title y
                            :dataIndex y
                            :key y}))]
    (fn []
      [:> js/antd.Layout.Content
       [:> js/antd.Table   {:dataSource data
                            :columns   (clj->js columns)
                            :rowKey "id"}]
       [:h2 (str "针对 " (-> data first ((keyword x))) " 的统计数据如下")]
       (for [item (:calc datatype)] [:p (str item "的合计值为" "  1000")])])))


;; (defn fake-table-sum
;;   "输入数据的索引和数据的数据的columns"
;;   [datatype fakedata]
;;   (r/with-let [data fakedata
;;                columns  (concat
;;                          (for [x (:group datatype)]
;;                            {:title x
;;                             :dataIndex x
;;                             :key x
;;                             :filters (for [item (map  #((keyword x) %) data)]       {:text item
;;                                                                                      :value item})
;;                             :onFilter (fn [value record]
;;                                         (= (-> record (js->clj :keywordize-keys true) (keyword x)) value))})

;;                          (for [y (:calc datatype)]
;;                            {:title y
;;                             :dataIndex y
;;                             :key y}))]
;;     (fn []
;;       [:> js/antd.Table   {:dataSource data
;;                            :columns   (clj->js columns)
;;                            :rowKey "id"}])))


