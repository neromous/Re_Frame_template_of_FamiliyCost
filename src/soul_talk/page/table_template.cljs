(ns soul-talk.page.table-template
  (:require [soul-talk.page.layout :as layout]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            (soul-talk.model.account :refer [account record category])
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
          [:> js/antd.Button {:icon   "edit"
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
                          #(dispatch [:server/delete prototype (-> id str keyword)])
                          #(js/console.log "cancel"))))}]
          [:> js/antd.Divider {:type "vertical"}]])))))

(defn columns [prototype]
  (concat (for [item (prototype :template.fields)]
            {:title (:title item)
             :dataIndex (:dataIndex item)
             :key (:key item)
             :aligh "center"})

          [{:title "操作" :dataIndex "actions" :key "actions" :align "center"
            :render (render-parts prototype)}]))

(defn selection [prototype]
  {:on-change (fn [sk sr]
                (dispatch (prototype :state.change :table/selection sk)))})

(defn input-form [prototype]
  [:> js/antd.Card
   [:> js/antd.Form
    (doall
     ;; react 不支持惰性序列  所以说需要doall包住
     (for [[k v] (-> (prototype :template.item) (dissoc :id))]
       ^{:key k}
       [:div
        [:p (str "请输入:"  v)]
        [:> js/antd.Input
         {:on-change #(dispatch
                       (prototype :state.change
                                  :new-cache
                                  k
                                  (->  % .-target .-value)))}]
        [:p]]))]])

(defn edit-form [prototype cache]
  [:> js/antd.Card
   [:> js/antd.Form
    (doall
     (for [[k v] (-> (prototype :template.item) (dissoc :id))]
       ^{:key k}
       [:div
        [:p (str "请输入" v)]
        [:> js/antd.Input
         {:on-change #(dispatch
                       (prototype :state.change
                                  :edit-cache
                                  k
                                  (->  % .-target .-value)))
          :defaultValue (clj->js (k @cache))}] [:p]]))]])

(defn input-modal [prototype]
  (r/with-let [state (subscribe (prototype :state.get :new-vis))
               cache (subscribe (prototype :state.get :new-cache))
               title "新建"
               content (input-form prototype)
               success-fn #(run-events [[:server/new prototype @cache]
                                        (prototype :state.change :new-vis false)])
               cancel-fn #(run-events [(prototype :state.change :new-vis false)])]
    (c/form-modal title content state success-fn cancel-fn)))

(defn edit-modal [prototype]
  (r/with-let [state (subscribe (prototype :state.get :edit-vis))
               cache (subscribe (prototype :state.get :edit-cache))
               title "修改"
               content [edit-form prototype cache]
               success-fn #(run-events
                            [[:server/update  prototype (-> @cache :id str keyword)  @cache]
                             (prototype :state.change :edit-vis false)])
               cancel-fn #(run-events [(prototype :state.change :edit-vis false)])]
    (c/form-modal title content state success-fn cancel-fn)))

(defn show-modal [prototype]
  (r/with-let [item (subscribe (prototype :state.get :show-item))
               modal-state (subscribe (prototype :state.get :show-vis))]

    [:> js/antd.Modal
     {:title    "账户明细"
      :visible  @modal-state
      :onOk     #(run-events [(prototype :state.change :show-vis false)])
      :onCancel #(run-events [(prototype :state.change :show-vis false)])}
     [:> js/antd.Card
      (doall
       (for [[k v] (-> (prototype :template.item) (dissoc :id))]
         ^{:key k}
         [:div
          [:label v]
          [:> js/antd.Divider {:type "vertical"}]
          [:label (-> @item
                      (get k)
                      clj->js)]
          [:p]]))]]))

(defn content-template
  [prototype]
  (r/with-let [_ (dispatch [:server/dataset-find-by prototype])
               data-map   (subscribe (prototype :data.all))]

    [:div
     [show-modal prototype]
     [input-modal prototype]
     [edit-modal prototype]
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch (prototype :state.change :new-vis true))
       :type "primary"
       :size "small"}
      "新增"]
     [:hr]
     [:> js/antd.Table   {:rowSelection (selection prototype)
                          :dataSource   (->> @data-map vals (sort-by :id))
                          :columns   (clj->js  (columns prototype))
                          :rowKey "id"}]]))

(defmethod content
  [:table :index :test]
  [db]
  (content-template record))

(defmethod content
  [:table :index :account]
  [db]
  (content-template account))

(defmethod content
  [:table :index :record]
  [db]
  (content-template record))



