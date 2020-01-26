(ns soul-talk.page.order-track
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.global_components :as gbc]
            [soul-talk.components.fields :as fields]
            [soul-talk.components.tables :as tables]
            [soul-talk.components.columns :as columns]
            [soul-talk.layout.order-track :as layout]
            [soul-talk.date-utils :as du]
            
            [soul-talk.utils :as utils]
            [soul-talk.route.utils :refer [logged-in?
                                           context-url
                                           href
                                           navigate!
                                           run-events
                                           run-events-admin]]))





;; (subscribe [:order-track/data.all])
;; (subscribe [:order-track/view.table-columns])



;; (subscribe [:order-track/columns.unique-key :customer_color] )

;; (count @(subscribe [:order-track/filter {:customer_id 1}  ]) )

;; (dispatch  [:order-track/delete  {:customer_id 1} ])
;; (dispatch  [:order-track/delete  {:customer_id 1} ])


(defn content []
  (r/with-let  [all-data (subscribe [:order-track/data.all])
                page-state (subscribe [:order-track/page-state])
                columns  (subscribe [:order-track/view.table-columns])]
    (let []

      [:div
       [:> js/antd.Table  {:dataSource @all-data
                           :columns  @columns}
        ;;
        ]])))

(defn home-page []
    (fn []
    (layout/layout
     gbc/head
     gbc/nav
     gbc/side-bar
     ;;layout/content
     content
     gbc/foot))

  )
