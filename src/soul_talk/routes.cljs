(ns soul-talk.routes
  (:require
   [reagent.core :as r]
   [goog.events :as events]
   [clojure.string :as str]
   [secretary.core :as secretary :refer-macros [defroute]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [accountant.core :as accountant]
   soul-talk.handlers
   soul-talk.pages
   soul-talk.effects
   ;;soul-talk.components.base-layout
   ;;soul-talk.components.default-layout
   soul-talk.pages
   ;;[soul-talk.page.layout :as layout]
   [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
   ;;[soul-talk.components.base-layout :as home-page]
   [soul-talk.components.common :as c]
   [soul-talk.config :refer [source-pull source-new source-del source-update]])

  (:import [goog History]
           [goog.History EventType]))

;; (dispatch [:table/server.pull])

;; (dispatch [:order-track/server.pull  {} ]  )

;; (dispatch [:org/server.pull {}])

;; (subscribe [:org/data.all])

;; (subscribe [:org/children-by-id 12 ])

(run-events [[:org/server.pull {"limit" 5}]
             [:cost.human/server.pull {"limit" 5}]
             [:cost.material-craft/server.pull {"limit" 5}]
             [:cost.material-raw/server.pull {"limit" 5}]
             [:product.output/server.pull {"limit" 5}]
             ]
 )



(defroute  "/" []
  (run-events
   [[:set-active {:page :home
                  :view :index}]]))

(defroute  "/test" []
  (run-events
   [[:set-active {:page :home
                  :view :test
                  :model :test}]]))

(defroute  "/v/:page" [page]
  (run-events
   [[:set-active {:page (keyword page)}]]))

(defroute  "/v/:page/:view" [page view]
  (run-events
   [[:set-active {:page (keyword page)
                  :view (keyword view)}]]))

(defroute  "/v/:page/:view/:model" [page view model]
  (run-events
   [[:set-active {:page (keyword page)
                  :view (keyword view)
                  :model (keyword model)}]]))

;; 根据配置加载不同页面

;; (defn main-page []
;;   (r/with-let [ready? (subscribe [:initialised?])
;;                db-state (subscribe [:active])]
;;     (when @ready?
;;       (fn []
;;         [:div
;;          [layout/layout-hcfs-left
;;           {:header  home-page/header
;;            :nav home-page/nav
;;            :content home-page/content
;;            :footer home-page/footer
;;            :sider home-page/siderbar}]]))))

;; 首页
;; 无登录下把事件加入登录事件


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




