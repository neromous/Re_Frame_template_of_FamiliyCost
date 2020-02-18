(ns soul-talk.views
<<<<<<< HEAD
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.routes :refer [logged-in? navigate!]]
            [soul-talk.components.common :as c]
            [soul-talk.pages.dash :as dash]
            [soul-talk.pages.home :as home]
            [soul-talk.pages.auth :as auth]
            [soul-talk.pages.users :as users]
            [soul-talk.components.post :as post]
            [soul-talk.pages.category :as category]
            [soul-talk.pages.blog :as blog]
            [soul-talk.pages.tag :as tag]
            [clojure.string :as str]
            ))

;;多重方法  响应对应的页面
(defmulti pages (fn [page _] page))

;;页面
(defmethod pages :home [_ _] [home/home-page])
(defmethod pages :login [_ _] [auth/login-page])
(defmethod pages :register [_ _] [auth/register-page])
(defmethod pages :blog/archives [_ _] [blog/blog-archives-page])
(defmethod pages :blog [_ _] [blog/blog-page])
(defmethod pages :posts/view [_ _] [post/post-view-page])

(defn admin [page]
  (r/with-let [user (subscribe [:user])]
    (if @user
      [page]
      (navigate! "#/admin"))))

;;后台页面
(defmethod pages :admin [_ _]
  (admin dash/dash-page))

(defmethod pages :change-pass [_ _]
  (admin users/change-pass-page))

(defmethod pages :user-profile [_ _]
  (admin users/user-profile-page))

(defmethod pages :users [_ _]
  (admin users/users-page))

(defmethod pages :categories [_ _]
  (admin category/categories-page))

(defmethod pages :categories-add [_ _]
  (admin category/add-page))

(defmethod pages :categories-edit [_ _]
  (admin category/edit-page))

(defmethod pages :posts [_ _]
  (admin post/posts-page))

(defmethod pages :posts/add [_ _]
  (admin post/add-post-page))

(defmethod pages :posts/edit [_ _]
  (admin post/edit-post-page))

;(defmethod pages :tags/add [_ _]
;  (admin tag/add-page))

(defmethod pages :default [_ _] [:div "页面未找到"])

;; 根据配置加载不同页面
(defn main-page []
  (r/with-let [ready? (subscribe [:initialised?])
               active-page (subscribe [:active-page])]
=======
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.page.todo-index :as todo-index]
   [soul-talk.page.home-page :as home-page]
   [soul-talk.page.product-track :as product-track]
   [soul-talk.page.product-detail :as product-detail]
   [soul-talk.page.price-index  :as price-index]

   ))

(defmulti pages  (fn [global-state & _]
                   (get global-state :active-page)))

(defmethod pages :default [_ _]
  [(fn []  [:p "没有这一页"])])

(defmethod pages :home-page [state _] [home-page/home-page state])
(defmethod pages :todo-index [state _] [todo-index/home-page state])
(defmethod pages :product-track [state _] [product-track/home-page state])

(defmethod pages :product-detail [state _] [product-detail/home-page state])
(defmethod pages :price-index [state _] [price-index/home-page state])

(defn main-page []
  (r/with-let [ready? (subscribe [:initialised?])
               view-state (subscribe [:get-view])]
>>>>>>> kpn_ai
    (when @ready?
      (fn []
        [:div
         [c/success-modal]
         [c/error-modal]
         (pages @active-page nil)]))))