(ns soul-talk.view.index
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.page.index :as index]
   [soul-talk.page.product-order :as product-order]
   [soul-talk.view.pages :refer [pages]]
   [soul-talk.page.study-plan :as study-plan]
   ))

(defmethod pages :home-page [state _] [(fn []  [:p "这里是主页"
                                                [:p (str state)]])])

(defmethod pages :index [state _] [ index/home-page  ])

(defmethod pages :index-detail [state _] [product-order/home-page])

(defmethod pages :material-raw [state _] [(fn []  [:p "这里是测试的订单跟踪页"
                                                   [:p (str state)]])])

(defmethod pages :material-raw-detail [state _] [(fn []  [:p "这里是测试的订单跟踪页"
                                                          [:p (str state)]])])


(defmethod pages :study-plan [state _] [ study-plan/home-page  ])








