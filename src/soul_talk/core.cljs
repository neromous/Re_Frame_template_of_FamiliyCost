(ns soul-talk.core
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch-sync dispatch]]
            ;;初始化处理器和订阅器
            soul-talk.db
            soul-talk.effects
            soul-talk.ajax
            soul-talk.handlers
            soul-talk.coeffects
            soul-talk.subs
            soul-talk.routes
            soul-talk.local-storage
            soul-talk.models
            [soul-talk.ajax :refer [load-interceptors!]]
            [soul-talk.route.utils :refer [logged-in? navigate!]]
            [soul-talk.routes :refer [hook-browser-navigation!]]
            [soul-talk.routes :refer [main-page]]))

;; 挂载页面组件
(defn mount-component []
  (r/render [#'main-page]
            (js/document.getElementById "app")))

;; ;; 挂载页面组件
;; (defn mount-component []
;;   (r/render [chart/chart-posts-by-votes chart/posts]
;;             (js/document.getElementById "app")))
;; 初始化方法

(defn init! []
  (dispatch-sync [:initialize-db])
  (if (logged-in?) (dispatch [:run-login-events]))
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-component))

