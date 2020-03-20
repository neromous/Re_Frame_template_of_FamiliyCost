(ns soul-talk.sub.datomic
  (:require  [re-frame.core :refer [inject-cofx
                                    dispatch
                                    dispatch-sync
                                    reg-event-db
                                    reg-event-fx
                                    subscribe reg-sub]]

             [re-posh.core :as rd]))
