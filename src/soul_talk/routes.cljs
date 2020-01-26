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

(def state-sample {;;:order_id 1
                   :order_detail_id 72
                   ;;:flow_id -1
                   })

(dispatch [:views/cost.detail  state-sample])
(subscribe [:views/cost.detail])

(defn page-init [id]
  (run-events  [[:cost.detail/material_craft-pull
                 {"filters" [["="  "order_detail_id"   id]]
                  "limit" 102400}]
                [:cost.detail/human_resource-pull
                 {"filters" [["="  "order_detail_id"   id]]
                  "limit" 102400}]
                [:cost.detail/machine-pull
                 {"filters" [["="  "order_detail_id"   id]]
                  "limit" 102400}]]))


(page-init 72)



;; (run-events [
;;              [:order-track/server.pull {"limit" 115000}]
;;              [:base.org/server.pull {"limit" 500}]
;;              [:base.customer/server.pull {"limit" 500}]
;;              [:cost.material-raw/server.pull {"limit" 115000}]
;;              [:product.output/server.pull {"limit" 115000}]
;;              [:energy.oa_report/server.pull {"limit" 5000}]
;;              ]
;;             )




;; init events


(run-events  [[:cost.index/state.init]])

(dispatch [:cost.index/state.init])

;;(dispatch  [:cost.index/state.replace {}]  )
;; (dispatch [:cost.index/state {:flow_ids #{1 2 3}
;;                               :order_detail_ids #{1 2 3}
;;                               :order_ids #{1}
;;                               } ])
;; (-> @(subscribe [:order-track/data.all]) first println )


(subscribe [:cost.index/state])

;; (-> @(subscribe [:cost.index/flow_ids]))
;; (-> @(subscribe [:cost.material-raw/all])
;;     first

;;     )
;; (subscribe [:cost.material-raw/data.product_flow])

;; (subscribe [:order-track/state.data])


;; (dispatch [:table/server.pull])

;; (dispatch [:order-track/server.pull  {} ]  )

;; (dispatch [:org/server.pull {}])

;; (subscribe [:org/data.all])

;; (subscribe [:org/children-by-id 12 ])

;; [:cost.human/server.pull {"limit" 5000  }]
;; [:cost.material-craft/server.pull {"limit" 115000}]

;; (subscribe [:cost/order-track 5])

;; (first @(subscribe  [:cost.material-craft/all]))

;; (first @ (subscribe [:sell-order/all])
;; )
;;  (first @(subscribe [:order-track/data.all]))
;; (first @(subscribe [:sell-order/all]))
;; (first @(subscribe [:product-order/all]))
;; (first @(subscribe [:product-task/all]))
;; (subscribe [:sell-order/tai_an.sum_tax_money])

;; (count @(subscribe  [:order-track/data.re_dye]))

;; (first @(subscribe [:order-track/data.no_re_dye]))

;; (subscribe  [:product-task/material.output.sum])

;; (->> @(subscribe [:order-track/data.all])
;;      (map :inward_quantity)
;;      (map int)
;;      (apply +)
;;     )

;; (subscribe [:product-task/material.re_dye.sum])


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




