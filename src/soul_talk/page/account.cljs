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
            :on-click #(run-events [(account :state.change :show-item clj-item)
                                    (account :state.change :show-vis true)])
            :target "_blank"}
           "查看明细"]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button
           {:icon   "edit"
            :size   "small"
            :target "_blank"
            :on-click #(run-events [(account :state.change :edit-item clj-item)
                                    (account :state.change :edit-vis true)])}

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
                          #(dispatch [:server/delete account (-> id str keyword)])
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
                (dispatch (account :state.change :table/selection sk)))})

(defn sample2columns [data]
  (for [k (-> data  keys)]
    {:title (name k)
     :dataIndex (name k)
     :key (name k)
     :align "center"}))

(defn account-input-modal []
  (r/with-let [account-cache (subscribe (account :state.get :new-cache))
               state (subscribe (account :state.get :new-vis))
               edited-model (r/atom {})
               title "创建账户"
               content [:> js/antd.Form
                        [:> js/antd.Input
                         {:on-change (fn [form]
                                       (swap! edited-model assoc :name  (->  form .-target .-value)))
                          :placeholder "请输入姓名信息"}]
                        [:> js/antd.Input
                         {:on-change (fn [form]
                                       (swap! edited-model assoc :quota  (-> form .-target .-value)))
                          :placeholder "请输入额度信息"}]

                        [:> js/antd.Input
                         {:on-change (fn [form]
                                       (swap! edited-model assoc :accountType  (-> form .-target .-value)))
                          :placeholder "请输入账户类型"}]]

               success-fn #(run-events [(account :state.change :new-cache @edited-model)
                                        [:server/new account @edited-model]
                                        (account :state.change :new-vis false)])
               cancel-fn #(run-events [(account :state.change :new-vis false)])]
    (c/form-modal title content state success-fn cancel-fn)))

(defn account-show-modal []
  (r/with-let [item (subscribe (account :state.get :show-item))
               modal-state (subscribe (account :state.get :show-vis))]

    (when @modal-state
      [:> js/antd.Modal
       {:title    "账户明细"
        :visible  @modal-state
        :onOk     #(run-events [(account :state.change :show-vis false)])
        :onCancel #(run-events [(account :state.change :show-vis false)])}

       [:label "账户名称"] [:> js/antd.Divider {:type "vertical"}]  [:label (:name @item)]
       [:p]
       [:label "额度"] [:> js/antd.Divider {:type "vertical"}]  [:label (:quota @item)]
       [:p]
       [:label "账户类型"] [:> js/antd.Divider {:type "vertical"}]  [:label (:accountType @item)]
       [:p]])))

(defn account-edit-modal []
  (r/with-let [modal-state (subscribe (account :state.get :edit-vis))]
    (when @modal-state
      (let [item (subscribe (account :state.get :edit-item))
            edited (-> @item
                       (update :id #(or % nil))
                       (update :name #(or % nil))
                       (update :quota #(or % nil))
                       (update :accountType #(or % nil))
                       r/atom)
            i-name (r/cursor edited [:name])
            i-quota (r/cursor edited [:quota])
            i-accounType (r/cursor edited [:accountType])]

        [:> js/antd.Modal
         {:title    "编辑账户"
          :visible  @modal-state
          :onOk     #(do
                       (reset! edited (merge @item @edited))
                       (run-events
                        [[:server/update account (-> @edited :id str keyword) @edited]
                         (account :state.change :edit-vis false)]))
          :onCancel #(run-events
                      [(account :state.change :edit-vis false)])}

         [:label "账户名称"]
         [:> js/antd.Divider {:type "vertical"}]
         [:> js/antd.Input
          {:defaultValue (:name @item)
           :on-change (fn [form]
                        (reset! i-name   (-> form .-target .-value)))}]

         [:p]
         [:label "额度"]
         [:> js/antd.Divider {:type "vertical"}]
         [:> js/antd.Input
          {:defaultValue (:quota @item)
           :on-change (fn [form]
                        (reset!  i-quota (-> form .-target .-value)))}]
         [:p]
         [:label "账户类型"]
         [:> js/antd.Divider {:type "vertical"}]
         [:> js/antd.Input
          {:defaultValue (:accountType @item)
           :on-change (fn [form]
                        (reset! i-accounType   (-> form .-target .-value)))}]
         [:p]]))))

;; (defmethod content
;;   [:table :index :account]
;;   [db]
;;   (r/with-let [_ (dispatch [:server/dataset-find-by account])
;;                data-map   (subscribe (account :data.all))]

;;     [:div
;;      [account-input-modal]
;;      [account-show-modal]
;;      [account-edit-modal]
;;      [:br]
;;      [:> js/antd.Button
;;       {:on-click #(dispatch  (account :state.change :new-vis true))
;;        :type "primary"
;;        :size "small"}
;;       "新增账户"]
;;      [:hr]
;;      [:> js/antd.Table   {:rowSelection (selection)
;;                           :dataSource   (vals @data-map)
;;                           :columns   (clj->js  (columns))
;;                           :rowKey "id"}]]))


