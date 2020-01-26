(ns soul-talk.layout.order-track
  (:require
   [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.utils :as utils]
   [reagent.core :as r]
   [re-frame.core :as rf]))

(defn layout [head nav sider content foot]
  (r/with-let [active (subscribe [:active])]
    (fn []
      [:> js/antd.Layout
       [head [nav ]]
       [:> js/antd.Layout {:style {:padding "24px"}}
        [sider ]

        [:> js/antd.Layout.Content {:style {:background "#fff"
                                            :padding 24
                                            :margin 0
                                            :minHeight 280}}
         [content]]]
       [foot]])))
