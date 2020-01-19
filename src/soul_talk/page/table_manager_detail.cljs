(ns soul-talk.page.table-manager-detail
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

(defmethod  content
  [:test :detail]
  [db]
  (r/with-let [active-page (subscribe [:active])
               page-state (subscribe [:table/page-state])
               select-table (subscribe [:table/selected-table])]

    (fn []
      (let []
        [:div
         [:p "ddddddddddddddddddddddddd"]
         [:h1 (first @select-table)]
         ;; [:p (str @(table-fields (keyword @selected-table)  ))]
         [:p]
         [:> js/antd.Row {:gutter 10}
          [:> js/antd.Col
           [:> js/antd.Card
           ;; [field-list @page-state  (second @select-table)]

            ]
           [:p]]]]))))


