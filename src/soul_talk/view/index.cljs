(ns soul-talk.view.index
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.view.pages :refer [pages]]))

(defmethod pages :home-page [state _] [(fn []  [:p "这里是主页"
                                                [:p (str state)]])])

(defmethod pages :order-track [state _] [(fn []  [:p "这里是测试的订单跟踪页"
                                                  [:p (str state)]])])

(defmethod pages :order-track-detail [state _] [(fn []  [:p "这里是主页"
                                                         [:p (str state)]])])

(defmethod pages :material-raw [state _] [(fn []  [:p "这里是测试的订单跟踪页"
                                                   [:p (str state)]])])

(defmethod pages :material-raw-detail [state _] [(fn []  [:p "这里是测试的订单跟踪页"
                                                          [:p (str state)]])])







