(ns soul-talk.page.charts
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [cljsjs.chartjs]
            (soul-talk.model.account :refer [account record category])
            [soul-talk.date-utils :as du]
            [soul-talk.components.table-fields :refer [field]]
            [soul-talk.components.home-page :refer [content header nav footer siderbar]]))



(defn render-data [node data]
  (js/Chart.
   node
   (clj->js
    {:type    "bar"
     :data    {:labels   ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"]
               :datasets [
                          {
                           :label "# of Votes"
                           :data [12 19 3 5 2 3]
                           :backgroundColor [
                                             "rgba(255, 99, 132, 0.2)"
                                             "rgba(54, 162, 235, 0.2)"
                                             "rgba(255, 206, 86, 0.2)"
                                             "rgba(75, 192, 192, 0.2)"
                                             "rgba(153, 102, 255, 0.2)"
                                             "rgba(255, 159, 64, 0.2)"
                                             ]
                           :borderColor [
                                         "rgba(255, 99, 132, 1)"
                                         "rgba(54, 162, 235, 1)"
                                         "rgba(255, 206, 86, 1)"
                                         "rgba(75, 192, 192, 1)"
                                         "rgba(153, 102, 255, 1)"
                                         "rgba(255, 159, 64, 1)"
                                         ]
                           }
                           ]

                          }
     :options {:scales {:xAxes [{:display true}]}}})))

(defn destroy-chart [chart]
  (when @chart
    (.destroy @chart)
    (reset! chart nil)))

(defn render-chart [chart data]
  (fn [component]
    (when-not (empty? @data)
      (let [node (r/dom-node component)]
        (destroy-chart chart)
        (reset! chart (render-data node @data))))))


(defn chart-posts-by-votes [data]
  (let [chart (r/atom nil)]
    (r/create-class
     {:component-did-mount  (render-chart chart data)
      :component-did-update (render-chart chart data)
      :component-will-unmount (fn [_] (destroy-chart chart))
      :render               (fn [] (when @data [:canvas]))})))



;; (def posts
;;   ( ->
;;    (vals @(subscribe (record :data.all)))
;;     r/atom
;;    ))


(def data-temp
  ( ->
    [{:v 10 :c :Red} {:v 20 :c "Yello"}  {:v 30 :c "Blue"}]
   r/atom
   ))



(defn selection [prototype]
  {:on-change (fn [sk sr]
                (dispatch (prototype :state.change :table/selection sk)))})

(defn columns [prototype]
  (concat (for [[k item] (prototype :template)]
            {:title (:title item)
             :dataIndex (:dataIndex item)
             :key (:key item)
             :aligh "center"})

          []))



(defmethod content
  [:table :test :test]
  [db]
  (r/with-let [prototype record
               _ (dispatch [:server/dataset-find-by prototype])
               data-map   (subscribe (prototype :data.all))]
    [:div
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch (prototype :state.change :new-vis true))
       :type "primary"
       :size "small"}
      "新增账户类别"]
     [:hr]
     [chart-posts-by-votes data-temp]
     [:> js/antd.Table   {:rowSelection (selection prototype)
                          :dataSource   (->> @data-map vals (sort-by :id))
                          :columns   (clj->js  (columns prototype))
                          :rowKey "id"}]
     ]))
    


