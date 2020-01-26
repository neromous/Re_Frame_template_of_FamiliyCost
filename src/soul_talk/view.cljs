(ns soul-talk.views
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.page.table-manager :as table-manager]
            [soul-talk.page.field-detail :as field-detail]
            [soul-talk.page.order-track :as order-track]
            [soul-talk.page.cost :as order-cost]
            [soul-talk.page.cost-detail  :as cost-detail]

            ))

(defn router-parser [x]
  (let    [page (get-in x [:page])
           view (get-in x [:view])
           model (get-in x [:model])]
    (cond
      (= page nil) :default
      (= view nil) [page]
      (= model nil) [page view]
      :else [page view model])))

(defmulti pages router-parser)

(defmethod pages :default  [_] [:p "404 这里没有页面"])

(defmethod pages [:home :index] [_] [:p "这里是首页"])

(defmethod pages [:table :index]  [_]  [table-manager/table-home-page])
(defmethod pages [:table :field-detail]  [_]  [field-detail/home-page])

(defmethod pages [:order :track]  [_]  [order-track/home-page])
;; 成本相关页面路由
(defmethod pages [:cost :index]  [_]  [order-cost/home-page])
(defmethod pages [:cost :factory]  [_]  [:p "分工厂首页"])
(defmethod pages [:cost :workshop]  [_]  [:p "车间首页"])
(defmethod pages [:cost :detail]  [_]  [cost-detail/home-page])

(defn main-page []
  (r/with-let [ready? (subscribe [:initialised?])
               active-page (subscribe [:active])]
    (when @ready?
      (fn []
        [:div
         [pages @active-page]]))))

