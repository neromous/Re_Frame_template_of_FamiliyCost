(ns soul-talk.routes
  (:require
   [reagent.core :as r]
   [goog.events :as events]
   [clojure.string :as str]
   [secretary.core :as secretary :refer-macros [defroute]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   soul-talk.subs
   soul-talk.handlers
   [accountant.core :as accountant]
   [soul-talk.util.route-utils :refer [run-events
                                       run-events-admin
                                       logged-in?
                                       navigate!]]
   [soul-talk.util.query-filter :as query-filter])

  (:import [goog History]
           [goog.History EventType]))

(defn init! []
  (run-events [;;[:item/server.pull :sell-order {:limit 10000}]
               ;;[:item/server.pull :product-track {:limit 10000}]
               ;;[:item/server.pull :relations {:limit 10000}]
               ]))
;;(init!)

(defroute  "/" []
  (run-events
   [;;[:set-active-page :state-capital-index]
    [:set-active-page :home-page]]))

;; (defroute  "/todsecretary.coreo-index" []
;;   (run-events
;;    [[:set-active-page :todo-index]
;;     [:model/server.pull :todos]
;;     [:model/server.pull :tags]
;;     [:model/server.pull :tag_type]]))


(defroute  "/product-track" []
  (run-events
   [[:set-active-page :product-track]
    [:metadata.get<-]
    [:sell-info.post<- {:limit 1000}]
    ;;[:http/get :metadata]
    ;;[:http/query :sell-info {:limit 1000 } ]
    ]))

(defroute  "/product-detail/:order_number" [order_number]
  (run-events
   [[:set-active-page :product-detail]
    [:page-state.set :product-detail :page-id order_number]
    ;;[:http/query :order_number {:order_number order_number}]
    ]))

(defroute  "/price-index" []
  (run-events
   [[:set-active-page :price-index]]))

(defroute  "/capital_index" []
  (run-events
   [[:set-active-page :state-capital-index]]))

(defroute  "/relations" []
  (run-events
   [[:set-active-page :relations]
    [:model/server.pull :tb_relation]
    [:item/server.get :relations]]))

(defroute  "/kpn/company" []
  (run-events
   [[:set-active-page :kpn.company]]))

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

