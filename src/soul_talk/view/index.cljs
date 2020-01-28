(ns soul-talk.view.index
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.page.index :as index]
   [soul-talk.page.product_order :as product_order]
   
   [soul-talk.view.pages :refer [pages]]))

(defmethod pages :home-page [state _] [(fn []  [:p "这里是主页"
                                                [:p (str state)]])])

(defmethod pages :index [state _] [ index/home-page  ])

(defmethod pages :index-detail [state _] [product_order/home-page])

(defmethod pages :material-raw [state _] [(fn []  [:p "这里是测试的订单跟踪页"
                                                   [:p (str state)]])])

(defmethod pages :material-raw-detail [state _] [(fn []  [:p "这里是测试的订单跟踪页"
                                                          [:p (str state)]])])







