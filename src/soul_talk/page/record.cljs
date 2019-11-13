(ns soul-talk.page.record
  (:require [soul-talk.page.layout :as layout]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            (soul-talk.model.account :refer [record])
            [soul-talk.components.home-page :refer [content header nav footer siderbar]]))

(defn render-parts []
  (r/with-let []
    (fn [_ item]
      (r/as-element
       (let [{:keys [id] :as clj-item} (js->clj item :keywordize-keys true)]
         [:div
          [:> js/antd.Button
           {:size   "small"
            :on-click #(run-events [(record :state.change :show-item clj-item)
                                    (record :state.change :show-vis true)])
            :target "_blank"}
           "查看明细"]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button {:icon   "edit"
                              :size   "small"
                              :target "_blank"
                              :on-click #(run-events [(record :state.change :edit-item clj-item)
                                                      (record :state.change :edit-vis true)])}

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
                          #(dispatch [:server/delete record (-> id str keyword)])
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

(defn input-modal []
  (r/with-let [record-cache (subscribe (record :state.get :new-cache))
               state (subscribe (record :state.get :new-vis))
               edited-model (r/atom {})
               title "创建账户"
               content [:> js/antd.Form
                        [:> js/antd.Input
                         {:on-change (fn [form]
                                       (swap! edited-model assoc :name  (->  form .-target .-value)))
                          :placeholder "请输入交易记录名称"}]
                        [:> js/antd.Input
                         {:on-change (fn [form]
                                       (swap! edited-model assoc :costValue  (-> form .-target .-value)))
                          :placeholder "请输入交易额"}]

                        [:> js/antd.Input
                         {:on-change (fn [form]
                                       (swap! edited-model assoc :billTime  (-> form .-target .-value)))
                          :placeholder "请输入账单日期"}]]

               success-fn #(run-events [(record :state.change :new-cache @edited-model)
                                        [:server/new record @edited-model]
                                        (record :state.change :new-vis false)])
               cancel-fn #(run-events [(record :state.change :new-vis false)])]
    (c/form-modal title content state success-fn cancel-fn)))

(defn show-modal []
  (r/with-let [item (subscribe (record :state.get :show-item))
               modal-state (subscribe (record :state.get :show-vis))]

    (when @modal-state
      [:> js/antd.Modal
       {:title    "账户明细"
        :visible  @modal-state
        :onOk     #(run-events [(record :state.change :show-vis false)])
        :onCancel #(run-events [(record :state.change :show-vis false)])}

       [:label "记录名称"] [:> js/antd.Divider {:type "vertical"}]  [:label (:name @item)]
       [:p]
       [:label "交易额"] [:> js/antd.Divider {:type "vertical"}]  [:label (:costValue @item)]
       [:p]
       [:label "记录发生日期"] [:> js/antd.Divider {:type "vertical"}]  [:label (:billTime @item)]
       [:p]])))

(defn edit-modal []
  (r/with-let [modal-state (subscribe (record :state.get :edit-vis))]
    (when @modal-state
      (let [item (subscribe (record :state.get :edit-item))
            edited (-> @item
                       (update :id #(or % nil))
                       (update :costValue #(or % nil))
                       (update :billTime #(or % nil))
                       (update :recordType #(or % nil))
                       r/atom)
            i-name (r/cursor edited [:name])
            i-quota (r/cursor edited [:costValue])
            i-recordype (r/cursor edited [:billTime])]

        [:> js/antd.Modal
         {:title    "编辑账户"
          :visible  @modal-state
          :onOk     #(do
                       (reset! edited (merge @item @edited))
                       (run-events
                        [[:server/update record (-> @edited :id str keyword) @edited]
                         (record :state.change :edit-vis false)]))
          :onCancel #(run-events
                      [(record :state.change :edit-vis false)])}

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
          {:defaultValue (:costValue @item)
           :on-change (fn [form]
                        (reset!  i-quota (-> form .-target .-value)))}]
         [:p]
         [:label "账户类型"]
         [:> js/antd.Divider {:type "vertical"}]
         [:> js/antd.Input
          {:defaultValue (:billTime @item)
           :on-change (fn [form]
                        (reset! i-recordype   (-> form .-target .-value)))}]
         [:p]]))))

;; (defmethod content
;;   [:table :index :record]
;;   [db]
;;   (r/with-let [_ (dispatch [:server/dataset-find-by record])
;;                data-map   (subscribe (record :data.all))]

;;     [:div
;;      [edit-modal]
;;      [show-modal]
;;      [input-modal]
;;      [:br]
;;      [:> js/antd.Button
;;       {:on-click #(dispatch (record :state.change :new-vis true))
;;        :type "primary"
;;        :size "small"}
;;       "新增交易记录"]
;;      [:hr]
;;      [:> js/antd.Table   {:rowSelection (selection)
;;                           :dataSource   (vals @data-map)
;;                           :columns   (clj->js  (columns))
;;                           :rowKey "id"}]]))

