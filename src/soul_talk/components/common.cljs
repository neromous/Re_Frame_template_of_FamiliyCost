(ns soul-talk.components.common
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [reagent.core :as r]
            [showdown]
            [hljs]))

(defn to-time [date]
  (str (.toDateString (js/Date. date))))

(defn loading-modal []
  (r/with-let [loading? (subscribe [:loading?])]
    (fn []
      (when @loading?
        [:> js/antd.antd.Spin {:tip  "加载中。。。。"
                               :size "large"}]))))

(defn spin-loading []
  (r/with-let [loading? (subscribe [:loading?])]
    (when @loading?
      (js/antd.message.loading "正在加载中。。。。"))))

(defn success-modal []
  (r/with-let [success (subscribe [:success])]
    (when @success
      (js/antd.message.success @success)
      (dispatch [:clean-success]))))

(defn show-confirm
  [title content ok-fun cancel-fun]
  (js/antd.Modal.confirm
   (clj->js {:centered true
             :title    title
             :content  content
             :onOk     ok-fun
             :onCancel cancel-fun})))

(defn error-modal []
  (r/with-let [error (subscribe [:error])]
    (when @error
      (js/antd.message.error @error)
      (dispatch [:clean-error]))))

(defn form-modal [title content state success-fn cancel-fn]
  [:> js/antd.Modal
   {:title    title
    :visible  state
    :onOk     success-fn
    :onCancel cancel-fn}
   content])

(defn breadcrumb-component []
  (r/with-let [items (subscribe [:breadcrumb])]
    (fn []
      [:> js/antd.Breadcrumb
       (for [item @items]
         ^{:key item}
         [:> js/antd.Breadcrumb.Item item])])))

(defn validation-modal [title errors]
  [:> js/antd.Modal {:is-open (boolean @errors)}
   [:> js/antd.ModalHeader title]
   [:> js/antd.ModalBody
    [:ul
     (doall
      (for [[_ error] @errors]
        ^{:key error}
        [:li error]))]]
   [:> js/antd.ModalFooter
    [:button.btn.btn-sm.btn-danger
     {:on-click #(reset! errors nil)}
     "Close"]]])

