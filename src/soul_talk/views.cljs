(ns soul-talk.views
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.page.todo-index :as todo-index]
   [soul-talk.page.home-page :as home-page]
   [soul-talk.page.product-track :as product-track]
   
   )
  )

(defmulti pages  (fn [global-state & _]
                   (get global-state :active-page)))

(defmethod pages :default [_ _]
  [(fn []  [:p "没有这一页"])])

(defmethod pages :home-page [state _] [home-page/home-page state])
(defmethod pages :todo-index [state _] [todo-index/home-page state])
(defmethod pages :product-track [state _] [product-track/home-page state])
 
(defn main-page []
  (r/with-let [ready? (subscribe [:initialised?])
               view-state (subscribe [:get-view])]
    (when @ready?
      (fn []
        [:div
         [pages  @view-state nil]]))))


