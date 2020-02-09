(ns soul-talk.page.study-plan
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as gbc]
   [soul-talk.components.field :as field]
   [soul-talk.layout.row-type :refer [input?>
                                      row2>
                                      row3>
                                      row4>]]

   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.form :as form]
   [soul-talk.utils :as utils]))



(defn content [state  page-state & _]
  (r/with-let []
    [:p]))

(defn home-page [state & _]
  (r/with-let [active (subscribe [:active-page])
               page-state (subscribe [:current-page-state])]

    [:> js/antd.Layout
     [hpc/head state
      [hpc/nav state]]
     [:> js/antd.Layout {:style {:padding "24px"}}
      [hpc/side-bar state]

      [:> js/antd.Layout.Content {:style {:background "#fff"
                                          :padding 24
                                          :margin 0
                                          :minHeight 280}}
       [content state]]]
     [hpc/foot state]]))



