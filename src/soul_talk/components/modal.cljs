(ns soul-talk.components.modal
  (:require             [reagent.core :as r]
                        [re-frame.core :refer [dispatch dispatch-sync subscribe]]
                        [soul-talk.date-utils :refer [to-date]]
                        [soul-talk.date-utils :as du]
                        [soul-talk.components.common :as c]
                        (soul-talk.model.account :refer [account])
                        [soul-talk.route.utils :refer [logged-in?
                                                       context-url
                                                       href
                                                       navigate!
                                                       run-events
                                                       run-events-admin]]))
