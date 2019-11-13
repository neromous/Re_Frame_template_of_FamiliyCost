(ns soul-talk.views

  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.routes :refer [logged-in? navigate!]]
            [soul-talk.components.common :as c]
            [soul-talk.pages.home-page :as home]
            [clojure.string :as str]
            ))

;;多重方法  响应对应的页面
(defmulti pages (fn [page _] page))

;;页面
;;(defmethod pages :home [_ _] [home/home-page])



(defn admin [page]
  (r/with-let [user (subscribe [:user])]
    (if @user
      [page]
      (navigate! "#/admin"))))

;;后台页面
;; (defmethod pages :admin [_ _]
;;   (admin dash/dash-page))

;; (defmethod pages :change-pass [_ _]
;;   (admin users/change-pass-page))

;; (defmethod pages :user-profile [_ _]
;;   (admin users/user-profile-page))

;; (defmethod pages :users [_ _]
;;   (admin users/users-page))

;; (defmethod pages :categories [_ _]
;;   (admin category/categories-page))

;; (defmethod pages :categories-add [_ _]
;;   (admin category/add-page))

;; (defmethod pages :categories-edit [_ _]
;;   (admin category/edit-page))

;; (defmethod pages :posts [_ _]
;;   (admin post/posts-page))

;; (defmethod pages :posts/add [_ _]
;;   (admin post/add-post-page))

;; (defmethod pages :posts/edit [_ _]
;;   (admin post/edit-post-page))


(defmethod pages :default [_ _] [:div "页面未找到"])

;; 根据配置加载不同页面
(defn main-page []
  (r/with-let [ready? (subscribe [:initialised?])
               active-page (subscribe [:active-page])]
    (when @ready?
      (fn []
        [:div
         [c/success-modal]
         [c/error-modal]
         (pages @active-page nil)]))))
