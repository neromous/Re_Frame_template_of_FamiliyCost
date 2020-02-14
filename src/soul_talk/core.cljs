(ns soul-talk.core
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch-sync dispatch]]
            ;;初始化处理器和订阅器
            soul-talk.handlers
            soul-talk.db
            soul-talk.effects
            soul-talk.ajax
            soul-talk.subs
            soul-talk.coeffects
            soul-talk.routes
            soul-talk.local-storage
            soul-talk.views
            [soul-talk.ajax :refer [load-interceptors!]]
            [soul-talk.util.route-utils :refer [logged-in? navigate!]]
            [soul-talk.routes :refer [hook-browser-navigation!]]
            [soul-talk.views :refer [main-page]]
            ))

;; 挂载页面组件
(defn mount-component []
  (r/render [#'main-page]
            (js/document.getElementById "app")))


(defn init! []
  (dispatch-sync [:initialize-db])
  (if (logged-in?) (dispatch [:run-login-events]))
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-component))

