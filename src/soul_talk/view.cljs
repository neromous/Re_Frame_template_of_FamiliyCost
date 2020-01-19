(ns soul-talk.views
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.page.table-manager :as table-manager]))

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

(defmethod pages :default  [_] [:p "ddddddd"])
(defmethod pages [:home :index] [_] [:p "这里是首页"])

(defmethod pages [:table :manager]  [_]  [table-manager/table-home-page])
(defmethod pages [:test :ttt]  [_]    [:p "aaaaaaaaaaaa"])

(defmethod pages [:test :test1] [_] [:p "跳转的母表"])
(defmethod pages [:test :test2] [_] [:a  {:href "#/v/test/test1"}  "测试点击跳转"])

(defn main-page []
  (r/with-let [ready? (subscribe [:initialised?])
               active-page (subscribe [:active])]
    (when @ready?
      (fn []
        [:div
         [pages @active-page]]))))

