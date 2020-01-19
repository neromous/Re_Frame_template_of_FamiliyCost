(ns soul-talk.page.table-manager-head
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.base-layout :refer [content header nav footer siderbar]]
            [soul-talk.components.fields :as fields]
            [soul-talk.components.tables :as tables]
            [soul-talk.components.columns :as columns]
            [soul-talk.date-utils :as du]
            [soul-talk.utils :as utils]
            [soul-talk.route.utils :refer [logged-in?
                                           context-url
                                           href
                                           navigate!
                                           run-events
                                           run-events-admin]]))

(def uuid-key (r/atom 1000))
(defn get-uuid-key []
  (swap! uuid-key inc))

(defn form-table-selector []
  (r/with-let [table-names  (subscribe [:table/table-names])
               selected-table  (subscribe [:table/selected-table])
               page-state (subscribe [:table/page-state])
               selected-field (subscribe [:table/selected-field])
               ;;
               ]
    (fn []
      (let [tb-cache  (:tb-cache @page-state)]

        [:div
         [:> js/antd.Input
          {:on-change #(let [v (-> % .-target .-value)]
                         ;;(reset! tb-cache v)
                         (dispatch [:table/page-state {:tb-cache v}]))
           :placeholder tb-cache}]
         [:>  js/antd.List
          {:dataSource (filter  #(utils/word-filter % (or tb-cache ""))   @table-names)
           :renderItem #(let [n   %]
                          (r/as-element
                           [:> js/antd.List.Item
                            {:on-click
                             (fn []
                               (dispatch [:table/page-state {:tb-cache n
                                                             :selected-table (keyword n)}]))}

                            n]))}]]
        ;;
        ))))

(defmethod siderbar
  [:manager :head]
  [db]
  (r/with-let [table-names (subscribe [:table/table-names])
               page-state (subscribe [:table/page-state])
               selected-table  (subscribe [:table/selected-table])]
    (fn []
      (let [tb-cache  (:tb-cache @page-state)]
        [:> js/antd.Layout.Sider {:className "sidebar"}
         [:> js/antd.Menu {:mode                "inline"
                           :className            "sidebar"
                           :theme               "light"
                           :default-select-keys ["user"]
                           :default-open-keys   ["account" "user"]
                           :selected-keys       ["sales-area"]}
          [:> js/antd.Menu.Item {:key      "admin"
                                 :on-click
                                 #(dispatch [:table/page-state {:tb-cache ""}])}
           [:span
            [:> js/antd.Icon {:type "area-chart"}]

            [:span
             "Dash"]]]

          [:div
           [form-table-selector]]]]))))

(defn list-item-field-tips [item]
  [:> js/antd.Card
   [:> js/antd.Row
    [:> js/antd.Col {:span 6}
     [:label (:column_name item)]]
    [:> js/antd.Col {:span 6}
     [:label (:data_type item)]]
    [:> js/antd.Col {:span 6}
     [:a  {:href "#/v/test/detail" } "dddddddddddddddd"]
     [:> js/antd.Button
      {:type "primary"
       :on-click #(dispatch  [:set-active {:page :test
                                           :view :detail
                                           } ])}

      "配置"]]]

   [:p]
   [:> js/antd.Row
    [:> js/antd.Col {:span 6}]]])

(defn field-list [page-state select-table]
  [:>  js/antd.List
   {:dataSource (or (->> select-table  vals  (sort-by :column_name))  [])
    :renderItem
    (fn [field]
      (let [{:keys [column_name
                    data_type
                    column_comment] :as field}  (js->clj field :keywordize-keys true)]
        (r/as-element
         [list-item-field-tips field])))}])

(defmethod  content
  [:manager :head]
  [db]
  (r/with-let [active-page (subscribe [:active])
               page-state (subscribe [:table/page-state])
               select-table (subscribe [:table/selected-table])]

    (fn []
      (let []
        [:div
         [:h1 (first @select-table)]
         ;; [:p (str @(table-fields (keyword @selected-table)  ))]
         [:p]
         [:> js/antd.Row {:gutter 10}
          [:> js/antd.Col
           [:> js/antd.Card
            [field-list @page-state  (second @select-table)]]
           [:p]]]]))))


(subscribe [:active])
