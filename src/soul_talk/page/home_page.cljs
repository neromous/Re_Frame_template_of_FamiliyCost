(ns soul-talk.page.home-page
  (:require [soul-talk.page.layout :as layout]
            [soul-talk.route.utils :refer [navigate!]]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.home-page :refer [content header nav footer siderbar]]))

