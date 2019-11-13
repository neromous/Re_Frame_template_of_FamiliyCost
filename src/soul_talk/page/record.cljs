(ns soul-talk.page.record
  (:require [soul-talk.page.layout :as layout]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]

            [soul-talk.components.home-page :refer [content header nav footer siderbar]]))

(defn render-parts []
  (r/with-let []
    (fn [_ item]
      (r/as-element
       (let [{:keys [id] :as clj-item} (js->clj item :keywordize-keys true)]
         [:div
          [:> js/antd.Button
           {:size   "small"
            :on-click #(run-events [[:db/assoc-in [:st/modals :record/show-item] clj-item]
                                    [:db/assoc-in [:st/modals :record/show] true]])
            :target "_blank"}
           "查看明细"]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button {:icon   "edit"
                              :size   "small"
                              :target "_blank"
                              :on-click #(run-events [[:db/assoc-in [:st/modals :record/show-item] clj-item]
                                                      [:db/assoc-in [:st/modals :record/edit] true]])}

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
                          #(dispatch [:server/delete :model/record (-> id str keyword)])
                          #(js/console.log "cancel"))))}]
          [:> js/antd.Divider {:type "vertical"}]])))))

(defn columns []
  [{:title "id" :dataIndex "id" :key "id" :align "center"}
   {:title "账户名称" :dataIndex "name" :key "name" :align "center"}
   {:title "交易额" :dataIndex "costValue" :key "costValue" :align "center"}
   {:title "账单录入日期" :dataIndex "billTime" :key "billTime" :align "center"}
   {:title "操作" :dataIndex "actions" :key "actions" :align "center"
    :render (render-parts)}])

(defn selection []
  {:on-change (fn [sk sr]
                (js/console.log "===" sk "===="  sr))})

(defn sample2columns [data]
  (for [k (-> data  keys)]
    {:title (name k)
     :dataIndex (name k)
     :key (name k)
     :align "center"}))

(defmethod content
  [:table :index :record]
  [db]
  (r/with-let [_ (dispatch [:server/dataset-find-by :model/record])
               model-key :model/record
               data-map   (subscribe [:db/get-in [model-key :datasets]])]

    [:div
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch [:db/assoc-in [:st/modals :account/input] true])
       :type "primary"
       :size "small"}
      "新增账户"]
     [:hr]
     [:> js/antd.Table   {:rowSelection (selection)
                          :dataSource   (vals @data-map)
                          :columns   (clj->js  (columns))
                          :rowKey "id"}]]))


