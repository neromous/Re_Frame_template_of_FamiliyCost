(ns soul-talk.views
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.page.index :as index]
   [soul-talk.page.product-order :as product-order]
   [soul-talk.page.study-plan :as study-plan]
   [soul-talk.page.metadata-index :as metadata]
   [soul-talk.page.admin :as admin]))

(defmulti pages  (fn [global-state & _]
                   (get global-state :active-page)))

(defmethod pages :default [_ _]
  [(fn []  [:p "没有这一页"])])

(defmethod pages :home-page [state _] [(fn []  [:p "这里是主页"])])

(defmethod pages :index [state _] [index/home-page state])

(defmethod pages :index-detail [state _] [product-order/home-page state])

(defmethod pages :material-raw [state _] [(fn []  [:p "这里是测试的订单跟踪页"
                                                   [:p (str state)]])])

(defmethod pages :material-raw-detail [state _] [(fn []  [:p "这里是测试的订单跟踪页"
                                                          [:p (str state)]])])

(defmethod pages :study-plan [state _] [study-plan/home-page])

(defmethod pages :metadata-index [state _] [metadata/home-page state])

(defmethod pages :admin-page [state & _]  [admin/home-page state])

(defn main-page []
  (r/with-let [ready? (subscribe [:initialised?])
               view-state (subscribe [:get-view])]
    (when @ready?
      (fn []
        [:div
         [pages  @view-state nil]]))))


