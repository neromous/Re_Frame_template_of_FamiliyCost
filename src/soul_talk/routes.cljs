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


(dispatch [:metadata/server.query])


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

