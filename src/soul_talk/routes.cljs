(ns soul-talk.routes
  (:require
   [reagent.core :as r]
   [goog.events :as events]
   [clojure.string :as str]
   [secretary.core :as secretary :refer-macros [defroute]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   soul-talk.resources
   soul-talk.subs
   soul-talk.handlers
   [accountant.core :as accountant]
   [soul-talk.util.route-utils :refer [run-events run-events-admin logged-in? navigate!]]
   [soul-talk.util.query-filter :as query-filter])

  (:import [goog History]
           [goog.History EventType]))

(run-events [[:metadata/server.query]])


(defroute  "/" []
  (run-events
   [[:set-active-page :home-page]
    [:resource/server.query :order-track {"limit" 15600}]
    ]))

(defroute  "/home/index" []
  (run-events
   [[:set-active-page :index]
    [:resource/server.query :energy-oa {}]
    ]))

(defroute  "/home/index/detail/:id" [id]
  (run-events
   [[:set-active-page :index-detail]
    [:page-state :index-detail :order-detail-id  (int id)]

    [:resource/server.query :order-track {"filters" [["=" "order_detail_id" (int id)]]
                                          "limit" 15600}]
    [:resource/server.query :human-resource {"filters" [["=" "order_detail_id" (int id)]]
                                             "limit" 15600}]
    [:resource/server.query :energy-oa {"limit" 15600}]
    [:resource/server.query :machine-resource {"filters" [["=" "order_detail_id" (int id)]]
                                               "limit" 15600}]
    [:resource/server.query :material-craft {"filters" [["=" "order_detail_id" (int id)]]
                                             "limit" 15600}]
    [:resource/server.query :material-raw {"filters" [["=" "order_detail_id" (int id)]]
                                           "limit" 15600}]]))

(defroute  "/home/metadata-index" [index-page]
  (run-events
   [[:set-active-page :metadata-index]
    [:metadata/server.query]]))



;; (defroute  "/home/:index-page" [index-page]
;;   (run-events
;;    [[:set-active-page (keyword index-page)]]))

;; (defroute  "/home/:index-page/:id" [index-page id]
;;   (run-events
;;    [[:set-active-page  (keyword (str index-page "-detail"))]
;;     [:set-views (keyword index-page) :id  id]]))


(defroute  "/admin/models/:model" [model]
  (run-events
   [[:set-active-page  :admin-page]
    [:resource/server.query (keyword model) {"limit" 1000}]
    [:set-views :admin-active-model (keyword model)]]))

(defroute  "/admin/models/:model/:id" [model id]
  (run-events
   [[:set-active-page  :admin-detail-page]
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

