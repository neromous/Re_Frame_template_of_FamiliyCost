(ns soul-talk.components.base-layout
  (:require             [reagent.core :as r]
                        [re-frame.core :refer [dispatch dispatch-sync subscribe]]
                        [soul-talk.components.common :as c]
                        [soul-talk.route.utils :refer [logged-in?
                                                       context-url
                                                       href
                                                       navigate!

                                                       run-events
                                                       run-events-admin]]))

(defn router-parser [x]
  (let    [page (get-in x [ :page])
           view (get-in x [ :view])
           model (get-in x [:model])]
    (cond
      (= page nil) :default
      (= view nil) [page]
      (= model nil) [page view]
      :else [page view model])))

;; 组件模板
(defmulti content router-parser)
(defmulti header  router-parser)
(defmulti nav router-parser)
(defmulti footer router-parser)
(defmulti siderbar router-parser)

