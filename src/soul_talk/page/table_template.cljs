(ns soul-talk.page.table-template
  (:require [soul-talk.page.layout :as layout]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            (soul-talk.model.account :refer [account record category])
            [soul-talk.date-utils :as du]
            [soul-talk.components.table-fields :refer [field]]
            [soul-talk.utils :refer [url->id]]
            [soul-talk.config :refer [source-pull source-new source-del source-update]]
            [soul-talk.components.home-page :refer [content header nav footer siderbar]]))

(defn render-parts [prototype]
  (r/with-let []
    (fn [_ item]
      (r/as-element
       (let [{:keys [id] :as clj-item} (js->clj item :keywordize-keys true)]
         [:div
          [:> js/antd.Button
           {:size   "small"
            :on-click #(run-events [(prototype :state.change :show-item clj-item)
                                    (prototype :state.change :show-vis true)])
            :target "_blank"}
           "查看明细"]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button
           {:icon   "edit"
            :size   "small"
            :target "_blank"
            :on-click #(run-events [(prototype :state.change :edit-item clj-item)
                                    (prototype :state.change :edit-cache clj-item)
                                    (prototype :state.change :edit-vis true)])}
           "编辑"]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button
           {:type     "danger"
            :icon     "delete"
            :size     "small"
            :on-click (fn []
                        (r/as-element
                         (c/show-confirm
                          "删除"
                          (str "你确认要删除这个实体？")
                          #(dispatch [source-del prototype (-> id str keyword)])
                          #(js/console.log "cancel"))))}]
          [:> js/antd.Divider {:type "vertical"}]])))))

(defn columns [prototype]
  (concat (for [[k item] (prototype :template)]
            {:title (:title item)
             :dataIndex (:dataIndex item)
             :key (:key item)
             :aligh "center"})

          [{:title "操作" :dataIndex "actions" :key "actions" :align "center"
            :render (render-parts prototype)}]))

(defn selection [prototype]
  {:on-change (fn [sk sr]
                (dispatch (prototype :state.change :table-selection sk)))})

(defmethod content
  [:table :index :account]
  [db]
  (r/with-let [prototype account
               data-map   (subscribe (prototype :data.all))]
    [:div
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch (prototype :state.change :new-vis true))
       :type "primary"
       :size "small"}
      "新增账户"]
     [:hr]
     [:> js/antd.Table   {:rowSelection (selection prototype)
                          :dataSource   (->> @data-map vals (sort-by :id))
                          :columns   (clj->js  (columns prototype))
                          :rowKey "id"}]]))

(defmethod content
  [:table :index :record]
  [db]
  (r/with-let [prototype record
               data-map   (subscribe (prototype :data.all))]
    [:div
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch (prototype :state.change :new-vis true))
       :type "primary"
       :size "small"}
      "新增记录"]
     [:hr]
     [:> js/antd.Table   {:rowSelection (selection prototype)
                          :dataSource   (->> @data-map vals (sort-by :id))
                          :columns   (clj->js  (columns prototype))
                          :rowKey "id"}]]))

(defmethod content
  [:table :index :category]
  [db]
  (r/with-let [prototype category
               data-map   (subscribe (prototype :data.all))]
    [:div
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch (prototype :state.change :new-vis true))
       :type "primary"
       :size "small"}
      "新增账户类别"]
     [:hr]
     [:> js/antd.Table   {:rowSelection (selection prototype)
                          :dataSource   (->> @data-map vals (sort-by :id))
                          :columns   (clj->js  (columns prototype))
                          :rowKey "id"}]]))

(defmethod content
  [:test]
  [db]
  (r/with-let [prototype record
               _ (dispatch [source-pull prototype {:limit 50}])
               data-map   (subscribe (prototype :data.all))
               select (subscribe (prototype :state.get :table-selection))]
    [:div
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch (prototype :state.change :new-vis true))
       :type "primary"
       :size "small"}
      "新增账户类别"]

     [:> js/antd.Button
      {:on-click #(run-events [])

       :type "primary"
       :size "small"}
      "新增账户类别"]

     [:hr]
     [:> js/antd.Table   {:rowSelection (selection prototype)
                          :dataSource   (->> @data-map vals (sort-by :id))
                          :columns   (clj->js  (columns prototype))
                          :rowKey "id"}]]))


