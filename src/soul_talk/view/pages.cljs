(ns soul-talk.view.pages
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]))



(defmulti pages  (fn [global-state _]
                   (get global-state :active-page)))

(defmethod pages :default [_ _]
  [(fn []  [:p "没有这一页"])])

