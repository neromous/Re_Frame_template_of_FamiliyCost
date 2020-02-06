(ns soul-talk.handler.page-state
  (:require [soul-talk.models :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe
                                   reg-sub]]
            [reagent.core :as r]
            [soul-talk.util.query-filter :as query-filter]))

(reg-event-db
 :page-state
 (fn [db [_ page-key k v]]
   (assoc-in db  [:views :page-state page-key  k] v)))

(defn set-current-page-state [k v]
  (r/with-let [current-page (subscribe)]
    (dispatch [:page-state  @current-page k v])))


