(ns soul-talk.components.modal
  (:require             [reagent.core :as r]
                        [re-frame.core :refer [dispatch dispatch-sync subscribe]]
                        [soul-talk.date-utils :refer [to-date]]
                        [soul-talk.date-utils :as du]
                        [soul-talk.components.common :as c]
                        (soul-talk.model.account :refer [account])
                        [soul-talk.route.utils :refer [logged-in?
                                                       context-url
                                                       href
                                                       navigate!
                                                       run-events
                                                       run-events-admin]]))

(defn account-input-modal []
  (r/with-let [db-state (subscribe [:db-state])
               model-key :model/account
               modal-key [model-key :modals :account/new-vis]
               cache-key [model-key :modals :account/new-item]
               account-cache (subscribe [:db/get-in cache-key])
               modal-state (subscribe (account :state.get :new-vis))
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

               state modal-state
               success-fn #(run-events [[:db/assoc-in cache-key @edited-model]
                                        [:server/new account @edited-model]
                                        (account :state.change :new-vis false)])
               cancel-fn #(run-events [(account :state.change :new-vis false)])]
    (c/form-modal title content state success-fn cancel-fn)))

(defn account-show-modal []
  (r/with-let [db-state (subscribe [:db-state])
               model-key :model/account
               modal-key [model-key :modals :account/show-vis]
               cache-key [model-key :modals :account/show-item]
               account-cache (subscribe [:db/get-in cache-key])
               item (subscribe [:db/get-in  [:model/account :modals :account/show-item]])
               modal-state (subscribe (account :state.get :show-vis))]

    (when @modal-state
      [:> js/antd.Modal
       {:title    "账户明细"
        :visible  @modal-state
        :onOk     #(run-events [[:db/assoc-in modal-key false]])
        :onCancel #(run-events [[:db/assoc-in modal-key false]])}

       [:label "账户名称"] [:> js/antd.Divider {:type "vertical"}]  [:label (:name @item)]
       [:p]
       [:label "额度"] [:> js/antd.Divider {:type "vertical"}]  [:label (:quota @item)]
       [:p]
       [:label "账户类型"] [:> js/antd.Divider {:type "vertical"}]  [:label (:accountType @item)]
       [:p]])))

(defn account-edit-modal []
  (r/with-let [db-state (subscribe [:db-state])
               model-key :model/account
               modal-key [:model/account :modals :account/edit-vis]
               cache-key [:model/account :modals :account/edit-cache]
               origin-key [:model/account :modals :account/edit-origin]
               modal-state (subscribe (account :state.get :edit-vis))]
    (when @modal-state
      (let [item (subscribe [:db/get-in  origin-key])
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
                       ;;(reset! edited (merge @item @edited))
                       (run-events
                        [[:server/update :model/account   (-> @edited :id str keyword) @edited]
                         [:db/assoc-in modal-key false]]))
          :onCancel #(run-events
                      [[:db/assoc-in modal-key false]])}

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

