(ns soul-talk.page.cost
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.fields :as fields]
            [soul-talk.components.tables :as tables]
            [soul-talk.components.columns :as columns]
            [soul-talk.date-utils :as du]
            [soul-talk.layouts.cost :as layout]
            [soul-talk.components.global_components :as gbc]
            [soul-talk.utils :as utils]
            [soul-talk.route.utils :refer [logged-in?
                                           context-url
                                           href
                                           navigate!
                                           run-events
                                           run-events-admin]]))

(defn home-page []
  (r/with-let [active (subscribe [:active])]
    (layout/layout
     gbc/head
     gbc/nav
     gbc/side-bar
     ;;layout/content
     (fn [] [:p])
     gbc/foot)))
