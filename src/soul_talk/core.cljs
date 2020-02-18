(ns soul-talk.core
  (:require [reagent.core :as r]
            [soul-talk.ajax :refer [load-interceptors!]]
            [soul-talk.routes :refer [hook-browser-navigation! logged-in? navigate!]]
            [soul-talk.views :refer [main-page]]
            [re-frame.core :refer [dispatch-sync dispatch]]
<<<<<<< HEAD
    ;;初始化处理器和订阅器
            soul-talk.routes
            soul-talk.coeffects
            soul-talk.effects
            soul-talk.handlers
            soul-talk.subs))
=======
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
>>>>>>> kpn_ai

;; 挂载页面组件
(defn mount-component []
  (r/render [#'main-page]
            (js/document.getElementById "app")))

;; 初始化方法
(defn init! []
  (dispatch-sync [:initialize-db])
  (if (logged-in?) (dispatch [:run-login-events]))
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-component))