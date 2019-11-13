(ns soul-talk.core
  (:require
             soul-talk.db
             soul-talk.models.init
             soul-talk.models.model
             soul-talk.models.view
             soul-talk.models.handler
            [reagent.core :as r]
            [soul-talk.ajax :refer [load-interceptors!]]
            [re-frame.core :refer [dispatch-sync dispatch]]
            ;;初始化处理器和订阅器
            soul-talk.coeffects
            soul-talk.effects
            soul-talk.subs
            soul-talk.handlers
            [soul-talk.routes :refer [logged-in? navigate!]]
            [soul-talk.routes :refer [hook-browser-navigation! ]]
            soul-talk.routes
            [soul-talk.views :refer [main-page]]
            ))

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

