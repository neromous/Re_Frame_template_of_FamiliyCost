(ns soul-talk.routes
  (:require 
            [goog.events :as events]
            [secretary.core :as secretary :refer-macros [defroute]]
            [accountant.core :as accountant]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.pages.home-page :as home]
            )

  (:import [goog History]
           [goog.History EventType]))

;; 初始化全局多态方法
(defmethod pages :home [_ _] [home/home-page])

;; 判断是否登录


(defn logged-in? []
  @(subscribe [:user]))

(defn context-url [url]
  (str url))

(defn href [url]
  {:href (str url)})

(defn navigate! [url]
  (accountant/navigate! (context-url url)))

;; 加载多个事件
(defn run-events
  [events]
  (doseq [event events]
    (dispatch event)))

(defn run-events-admin
  [events]
  (doseq [event events]
    (if (logged-in?)
      (dispatch event)
      (dispatch [:add-login-event event]))))

(defn home-page-events [& events]
  (.scrollTo js/window 0 0)
  (run-events (into
               [[:load-categories]
                [:load-tags]
                [:set-active-page :home]]
               events)))

;; 首页
(defroute "/" []
  (run-events
   [;;[:load-posts {:page 1 :pre-page 3}]
    ;;[:model/contact.server.all  {:format "json"}]
    [:set-active-page :home]]))

 (defroute "/sales-motherform" []
  (run-events
   [;;[:load-posts {:page 1 :pre-page 3}]
    ;;[:model/contact.server.all  {:format "json"}]
    [:set-active-page :sales-motherform]]))

(defroute "/blog" []
  (let [pagination {:page     1
                    :pre-page 20}]
    (run-events
     [[:load-posts pagination]
      [:load-posts-archives]
      [:set-active-page :blog]])))

;; 无登录下把事件加入登录事件
(defn admin-page-events [& events]
  (.scrollTo js/window 0 0)
  (run-events-admin (into
                     [[:set-active-page :admin]]
                     events)))

;; 后台管理
(defroute admin "/admin" []
  (run-events [[:set-breadcrumb ["面板"]]
               [:set-active-page :admin]]))

(defroute "/change-pass" []
  (run-events [[:set-breadcrumb ["个人管理" "修改密码"]]
               [:set-active-page :change-pass]]))

(defroute "/user-profile" []
  (run-events [[:set-breadcrumb ["个人管理" "个人信息"]]
               [:set-active-page :user-profile]]))

(defroute "/users" []
  (run-events [[:set-breadcrumb ["用户" "清单"]]
               [:admin/load-users]
               [:set-active-page :users]]))

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


;; (ns soul-talk.routes
;;   (:require [goog.events :as events]
;;             soul-talk.models.init
;;             soul-talk.models.model
;;             soul-talk.models.handler
;;             soul-talk.models.view
;;             soul-talk.handlers
;;             [secretary.core :as secretary :refer-macros [defroute]]
;;             [soul-talk.utils :refer [
;;                                      logged-in?
;;                                      context-url
;;                                      href
;;                                      navigate!
;;                                      run-events
;;                                      run-events-admin
;;                                      ]]
;;             [accountant.core :as accountant]
;;             [reagent.core :as r]
;;             [re-frame.core :refer [subscribe dispatch]]
;;             [soul-talk.components.common :as c]
;;             [soul-talk.pages.home-page :as home]
;;             [clojure.string :as str]
;;             [re-frame.core :refer [dispatch dispatch-sync subscribe]])
;;   (:import [goog History]
;;            [goog.History EventType]))


;; (defmulti pages (fn [page _] page))
;; (defroute "*" [])
;; ;; 首页
;; (defmethod pages :home [_ _] [home/home-page])
;; (defroute "/" []
;;   (run-events
;;    [[:model/init :contact "Contact"]
;;     [:view/init :contact/data]
;;     [:server/sync-pull-replace :contact/data :contact]
;;     [:set-active-page :home]]))





;; ;; 无登录下把事件加入登录事件

;; ;; 判断是否登录
;; (defn home-page-events [& events]
;;   (.scrollTo js/window 0 0)
;;   (run-events (into
;;                [[:load-categories]
;;                 [:load-tags]
;;                 [:set-active-page :home]]
;;                events)))


;; (defn admin-page-events [& events]
;;   (.scrollTo js/window 0 0)
;;   (run-events-admin (into
;;                      [[:set-active-page :admin]]
;;                      events)))

;; ;; 后台管理


;; (secretary/set-config! :prefix "#")

;; ;; 使用浏览器可以使用前进后退 历史操作
;; (defn hook-browser-navigation! []
;;   (doto
;;    (History.)
;;     (events/listen EventType.NAVIGATE #(secretary/dispatch! (.-token %)))
;;     (.setEnabled true))
;;   (accountant/configure-navigation!
;;    {:nav-handler
;;     (fn [path]
;;       (secretary/dispatch! path))
;;     :path-exists?
;;     (fn [path]
;;       (secretary/locate-route path))
;;     :reload-same-path? true})
;;   (accountant/dispatch-current!))

;; ; 透视表相关


;; (defn admin [page]

;;   (r/with-let [user (subscribe [:user])]
;;     (if @user
;;       [page]
;;       (navigate! "#/admin"))))

;; ;;后台页面

;; (defmethod pages :default [_ _] [:div "页面未找到"])

;; ;; 根据配置加载不同页面
;; (defn main-page []
;;   (r/with-let [ready? (subscribe [:initialised?])
;;                active-page (subscribe [:active-page])]
;;     (when @ready?
;;       (fn []
;;         [:div
;;          [c/success-modal]
;;          [c/error-modal]
;;          (pages @active-page nil)]))))


