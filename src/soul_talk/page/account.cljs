(ns soul-talk.page.account
  (:require [soul-talk.page.layout :as layout]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            (soul-talk.model.account :refer [account])
            [soul-talk.components.home-page :refer [content header nav footer siderbar]]))

(defn render-parts []
  (r/with-let []
    (fn [_ item]
      (r/as-element
       (let [{:keys [id] :as clj-item} (js->clj item :keywordize-keys true)]
         [:div
          [:> js/antd.Button
           {:size   "small"
            :on-click #(run-events [(account :state.change :show-item clj-item )
                                    (account :state.change :show-vis true)
                                    ;[:pk/set :account/states :show-item clj-item]
                                    ;;[:pk/set :account/states :show-vis true]
                                    ])
            :target "_blank"}
           "查看明细"]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button
           {:icon   "edit"
            :size   "small"
            :target "_blank"
            :on-click #(run-events [(account :state.change :edit-item clj-item)
                                   (account :state.change :edit-vis true)] )}

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
                          #(dispatch (account :data.delete  (-> id str keyword)))
                          #(js/console.log "cancel"))))}]
          [:> js/antd.Divider {:type "vertical"}]])))))

(defn columns []
  [{:title "id" :dataIndex "id" :key "id" :align "center"}
   {:title "账户名称" :dataIndex "name" :key "name" :align "center"}
   {:title "额度" :dataIndex "quota" :key "quota" :align "center"}
   {:title "账户类型" :dataIndex "accountType" :key "accountType" :align "center"}
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
  [:table :index :account]
  [db]
  (r/with-let [ _ (dispatch [:server/dataset-find-by account])
               model-key :model/account
               data-map   (subscribe (account :data.all))]

    [:div
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch  (account :state.change :new-vis true))
       :type "primary"
       :size "small"}
      "新增账户"]
     [:hr]
     [:> js/antd.Table   {:rowSelection (selection)
                          :dataSource   (vals @data-map)
                          :columns   (clj->js  (columns))
                          :rowKey "id"}]]))




