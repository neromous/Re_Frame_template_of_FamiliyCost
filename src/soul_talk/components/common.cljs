(ns soul-talk.components.common
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [soul-talk.components.antd-dsl
             :refer [>Input  >InputNumber >Col  >Row >List
                     list-item  >AutoComplete  >Modal  >Table
                     >Content   >Form  >Description  descrip-item
                     >Cascader  >Button  >DatePicker >Divider  >Header
                     >Select  >Footer  >Switch >Sider
                     >Checkbox  >Checkbox_group  >Form  form-item
                     >Layout >Content]]
            [reagent.core :as r]
            [showdown]
            [hljs]
            ))

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

<<<<<<< HEAD
;;高亮代码 循环查找结节
(defn highlight-code [node]
  (let [nodes (.querySelectorAll (r/dom-node node) "pre code")]
    (loop [i (.-length nodes)]
      (when-not (neg? i)
        (when-let [item (.item nodes i)]
          (.highlightBlock js/hljs item))
        (recur (dec i))))))

;; 处理 markdown 转换
(defn markdown-preview []
  (let [md-parser (js/showdown.Converter.)]
    (r/create-class
      {:component-did-mount
       #(highlight-code (r/dom-node %))
       :component-did-update
       #(highlight-code (r/dom-node %))
       :reagent-render
       (fn [content]
         [:div
          {:dangerouslySetInnerHTML
           {:__html (.makeHtml md-parser (str content))}}])})))

(defn page-nav [handler]
  (r/with-let
    [pagination (subscribe [:admin/pagination])
     prev-page (r/cursor pagination [:previous])
     next-page (r/cursor pagination [:next])
     page (r/cursor pagination [:page])
     pre-page (r/cursor pagination [:pre-page])
     total-pages (r/cursor pagination [:total-pages])
     total (r/cursor pagination [:total])
     paginate-params @pagination]
    (fn []
      (let [start (max 1 (- @page 5))
            end (inc (min @total-pages (+ @page 5)))]
        [:nav
         [:ul.pagination.justify-content-center.pagination-sm
          [:li.page-item
           {:class (if (= @page 1) "disabled")}
           [:a.page-link
            {:on-click  #(dispatch [handler (assoc paginate-params :page @prev-page)])
             :tab-index "-1"}
            "Previous"]]
          (doall
            (for [p (range start end)]
              ^{:key p}
              [:li.page-item
               {:class (if (= p @page) "active")}
               [:a.page-link
                {:on-click #(dispatch [handler (assoc paginate-params :page p)])}
                p]]
              ))
          [:li.page-item
           {:class (if (> @next-page @total-pages) "disabled")}
           [:a.page-link
            {:on-click #(dispatch [handler (assoc paginate-params :page @next-page)])}
            "Next"]]]]))))
=======
(defn columns-with-do [item-key [show-item show-vis]    [edit-item edit-vis]]

  {:title  "操作" :dataIndex "actions" :key "actions" :align "center"
   :render
   (fn
     [_ input-item]
     (r/as-element
      (let [{:keys [id]  :as item} (js->clj input-item :keywordize-keys true)]
        [:div
         [>Button {:size   "small"
                   :target "_blank"
                   :on-click #(do (reset! show-item  item)
                                  (reset! show-vis true))}

          "查看"]
         [:> js/antd.Divider {:type "vertical"}]
         [>Button {:icon   "edit"
                   :size   "small"
                   :target "_blank"
                      ;;:href   (str "#/todos/" id "/edit")
                   :on-click #(do (reset! edit-item item)
                                  (reset! edit-vis true))}

          "编辑"]
         [:> js/antd.Divider {:type "vertical"}]
         [>Button {:type     "danger"
                   :icon     "delete"
                   :size     "small"
                   :on-click (fn []
                               (r/as-element
                                (show-confirm
                                 "文章删除"
                                 (str "你确认要删除这篇文章吗？")
                                 #(dispatch [:model/server.del item-key id])
                                 #(js/console.log "cancel"))))}]])))})
>>>>>>> kpn_ai

