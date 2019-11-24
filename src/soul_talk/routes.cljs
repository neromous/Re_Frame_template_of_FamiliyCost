(ns soul-talk.routes
  (:require
   [reagent.core :as r]
   [goog.events :as events]
   [clojure.string :as str]
   [secretary.core :as secretary :refer-macros [defroute]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [accountant.core :as accountant]
   soul-talk.handlers
   soul-talk.components.base-layout
   soul-talk.components.default-layout
   soul-talk.pages
   [soul-talk.page.layout :as layout]
   [soul-talk.model.account :refer [account]]
   [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
   [soul-talk.components.base-layout :as home-page]
   [soul-talk.components.common :as c]
   (soul-talk.model.account :refer [account record category gears])
   [soul-talk.config :refer [source-pull source-new source-del source-update]]
   [soul-talk.models :refer [preform-modals]])

  (:import [goog History]
           [goog.History EventType]))



;; 初始化所有数据
(run-events
 [[source-pull account {:limit 100}]
  [source-pull record {:limit 100}]
  [source-pull category {:limit 100}]
  [source-pull gears {:limit 100}]
  ])

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

(defn main-page []
  (r/with-let [ready? (subscribe [:initialised?])
               db-state (subscribe [:active])]
    (when @ready?
      (fn []
        [:div
         [c/success-modal]
         [c/error-modal]
         (preform-modals)
         [layout/layout-hcfs-left
          {:header  home-page/header
           :nav home-page/nav
           :content home-page/content
           :footer home-page/footer
           :sider home-page/siderbar}]]))))

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




