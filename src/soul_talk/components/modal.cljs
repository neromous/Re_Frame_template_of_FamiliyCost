(ns soul-talk.components.modal
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [soul-talk.date-utils :as du]
            [soul-talk.components.table-fields :refer [field]]
            

            ))

