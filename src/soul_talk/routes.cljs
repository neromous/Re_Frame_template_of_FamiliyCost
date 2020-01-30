(ns soul-talk.routes
  (:require
   [reagent.core :as r]
   [goog.events :as events]
   [clojure.string :as str]
   [secretary.core :as secretary :refer-macros [defroute]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   soul-talk.resources
   soul-talk.subs
   soul-talk.sub.model
   soul-talk.handlers
   [accountant.core :as accountant]
   [soul-talk.util.route-utils :refer [run-events run-events-admin logged-in? navigate!]]
   [soul-talk.util.query-filter :as query-filter]
   )

  (:import [goog History]
           [goog.History EventType]))



;; (-> @(subscribe [:full-order/view.index-page])
;;     first
;;     :flow_id
;;     )
  
;;(dispatch [:resource/server.query  :order-track {} ])
;; (-> @(subscribe [:product-order/tai_an ])
;;     count
;;     )


;; (run-events [[:resource/server.query :human-resource {"filters" [["=" "order_detail_id" 72]]}]
;;              [:resource/server.query :order-track {
;;                                                    "limit" 15600

;;                                                    }]
;;              [:resource/server.query :energy-oa {}]
;;              [:resource/server.query :machine-resource {"limit" 10000}]
;;              [:resource/server.query :material-craft {"filters" [["=" "order_detail_id" 72]]}]
;;              [:resource/server.query :material-raw {"filters" [["=" "order_detail_id" 72]]}]])

;; (subscribe [:current-page-state])

;;(dispatch  [:set-page-state :index-detail :order_detail_id 72])

;; (subscribe [:product-task/view.index-page])

;; (def sample @(subscribe [:full-order/all] ))

;; (count sample)


;; (->
;;  (filter #(query-filter/is-part-of-query? % {:order_id 1}  )    sample  )
;;  first
;;  )

;; (dispatch [:resource/update :order-track {:order_id 1 }  {:ttt "dadsd"}  ])

;;(dispatch [:resource/delete :order-track  {:order_id 1}])

;; (count @(subscribe [:resource/all :order-track]))


;; 初始化所有数据


;; (run-events
;;  [[:set-views :admin-active-model :material-raw]])

;; (subscribe [:views])


(defroute  "/" []
  (run-events
   [[:set-active-page :home-page]]))

(defroute  "/home/:index-page" [index-page]
  (run-events
   [[:set-active-page (keyword index-page)]]))

(defroute  "/home/:index-page/:id" [index-page id]
  (run-events
   [[:set-active-page  (keyword (str index-page "-detail"))]
    [:set-views (keyword index-page) :id  id]]))

(defroute  "/admin/models/:model" [model]
  (run-events
   [[:set-active-page  (keyword model)]
    [:set-views :admin-active-model (keyword model)]]))

(defroute  "/admin/models/:model/:id" [model id]
  (run-events
   [[:set-active-page  (keyword model)]
    [:set-views :admin-active-model (keyword model)]
    [:set-views (keyword model) :id id]]))

(defroute "*" [])

(secretary/set-config! :prefix "#")

;; 使用浏览器可以使用前进后退 历史操作
(defn hook-browser-navigation! []
  (doto
   (History.)
    (events/listen EventType.NAVIGATE #(secretary/dispatch! (.-token %)))
    (.setEnabled true))
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))
    :reload-same-path? true})
  (accountant/dispatch-current!))

