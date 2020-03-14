(ns soul-talk.register
  (:require  [re-frame.core :refer [subscribe reg-event-db
                                    dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.handler.common :refer  [model-register]]))

(defn action> [& args]
  (dispatch (vec  args)))

(defn sub> [& args]
  (subscribe (vec args)))

(model-register
 :metadata
 {:root-path [:model :items :metadata]
  :url  "http://localhost:3000/api/kpn-metadata/all"})





