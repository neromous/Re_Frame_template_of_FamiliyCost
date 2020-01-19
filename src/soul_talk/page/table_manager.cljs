(ns soul-talk.page.table-manager
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.fields :as fields]
            [soul-talk.components.tables :as tables]
            [soul-talk.components.columns :as columns]
            [soul-talk.layouts.home-layout :as home-layout]
            [soul-talk.date-utils :as du]
            [soul-talk.utils :as utils]
            [soul-talk.route.utils :refer [logged-in?
                                           context-url
                                           href
                                           navigate!
                                           run-events
                                           run-events-admin]]))
;; 两件事
;; 第一件事  定义字段
;; 第二件事  定义关系

(def uuid-key (r/atom 1000))
(defn get-uuid-key []
  (swap! uuid-key inc))

(defn list-item-field-tips [item]
  [:> js/antd.Card
   [:> js/antd.Row
    [:> js/antd.Col {:span 3}
     [:label (:column_name item)]]
    [:> js/antd.Col {:span 3}
     [:label (:data_type item)]]
    [:> js/antd.Col {:span 3}
     [:div  {:on-click #(do
                          (dispatch  [:table/page-state {:selected-table (keyword (:table_name item))
                                                         :selected-field (keyword (:column_name item))}])
                          (navigate!  "#/v/test/test1"))}

      [:> js/antd.Icon  {:type "setting"
                         :theme "filled"
                         :size "big"}]]]]

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

(defn  table-content
  []
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

(defn table-home-page
  []
  (r/with-let  []
    (fn []
      (let []
        [home-layout/table-home-page
         home-layout/table-top-head
         home-layout/table-top-nav
         home-layout/table-side-bar
         table-content
         ;;home-layout/table-content
         home-layout/table-foot]))))


(subscribe [:table/selected-field] )
