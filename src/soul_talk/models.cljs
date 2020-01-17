(ns soul-talk.models
  (:require [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [re-frame.core :refer [inject-cofx dispatch dispatch-sync reg-event-db reg-event-fx subscribe reg-sub]]
            [soul-talk.db :refer [Env]]))

;; (doseq [[k v] @Env  ]
;;   (table-content-register  v k)
;;   )

