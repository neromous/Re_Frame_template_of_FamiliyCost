(ns soul-talk.components.maps
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [reagent.core :as r]
            [showdown]
            [hljs]))

(def map-tag  (.-Map js/reactMap))

(defn main-map [shape map-conf]
  [:div {:style  (merge {:width 700  :height 400} shape)}
   [:> map-tag   (merge {:amapkey "14496d2c07234db4cb989b9c2549fe08"} map-conf)]])
