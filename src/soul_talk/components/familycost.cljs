(ns soul-talk.components.familicost
  (:require             [reagent.core :as r]
                        [re-frame.core :refer [dispatch dispatch-sync subscribe]]
                        [soul-talk.date-utils :refer [to-date]]
                        [soul-talk.date-utils :as du]
                        [soul-talk.components.common :as c]
                        [soul-talk.components.home-page :refer [header content nav footer siderbar]]
                        [soul-talk.route.utils :refer [logged-in?
                                                       context-url
                                                       href
                                                       navigate!
                                                       run-events
                                                       run-events-admin]]))


(defn sample2columns [data]
  (for [k (-> data  keys)]
    {:title (name k)
     :dataIndex (name k)
     :key (name k)
     :align "center"}))

(defn selection []
  {:on-change (fn [sk sr]
                (js/console.log "===" sk "===="  sr))})

(defn account-table [template selection]
  (fn []
    (r/with-let [view-key [:familycost :account-table]
                 datas   (subscribe [:data/all view-key])]
      (println datas)
      [:> js/antd.Table   {:rowSelection (selection)
                           :dataSource   (if (= nil @datas)
                                           []
                                           @datas)
                           :columns   (clj->js  (sample2columns template))
                           :rowKey "id"}]
      ;;
      )))

(defn  account-modal [view-key cache-key  modal-key]
  (r/with-let [view-key [:familycost :accout-table]
               cache-key :cache-account
               modal-key :modal-account
               template-map  {:id nil
                              :name nil
                              :quota nil
                              :accountType nil
                              :url nil}
               state (subscribe [:state/all view-key])
               edited-model (-> template-map  r/atom)]
    (fn []
      [:div
         ;; 触发按钮
       [:> js/antd.Button
        {:on-click #(dispatch [:state/assoc-in [view-key modal-key] true])}
        "新增"]

         ;; 弹出框
       [:> js/antd.Modal {:visible (if (-> @state modal-key (= nil))
                                     false
                                     (-> @state modal-key))
                          :onOk #(do
                                   (dispatch [:state/assoc-in [view-key cache-key] @edited-model])
                                   (dispatch [:server/new "/Account/" view-key @edited-model])
                                   (dispatch [:state/assoc-in [view-key modal-key] false]))

                          :onCancel #(dispatch [:state/assoc-in [view-key modal-key] false])}
        [:> js/antd.Form
         [:> js/antd.Input
          {:on-change (fn [form]
                        (swap! edited-model :name  (-> form .-target .-value)))
           :placeholder "请输入账户名称"}]
         [:> js/antd.Input
          {:on-change (fn [form]
                        (swap! edited-model :quota  (-> form .-target .-value)))
           :placeholder "请输入额度信息"}]

         [:> js/antd.Input
          {:on-change (fn [form]
                        (swap! edited-model :accountType  (-> form .-target .-value)))
           :placeholder "请输入账户类型"}]
         ;;
         ]]])))
