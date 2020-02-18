(ns soul-talk.sub.item
  (:require  [re-frame.core :refer [inject-cofx
                                    dispatch
                                    dispatch-sync
                                    reg-event-db
                                    reg-event-fx
                                    subscribe reg-sub]]
             [soul-talk.util.query-filter :as query-filter]
             [soul-talk.sub.funcs.item-path :as item-path]))

(reg-sub
 :item/raw
 (fn [db [_ item-key]]
   (get-in db (item-path/->data-path item-key))))

(reg-sub
 :item/all
 (fn [db [_ item-key]]
   (get-in db (item-path/->data-path item-key))))



