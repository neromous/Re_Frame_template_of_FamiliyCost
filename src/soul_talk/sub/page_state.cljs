(ns soul-talk.sub.page-state
  (:require [soul-talk.models :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe reg-sub]]
            [soul-talk.util.query-filter :as query-filter]))

(reg-sub
 :get-page-state
 (fn [db [_ page-key]]
   (get-in  db [:page-state page-key])))

