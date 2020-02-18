(ns soul-talk.page.home-page
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.antd-dsl
    :refer [>Layout
            >Content]]

   [soul-talk.utils :as utils]))

(defn content [state  page-state & _]
  (r/with-let []
    [:p]))

(defn home-page [state & _]
  (r/with-let [active-page (:active-page state)
               page-state (:page-state state)
               page (r/atom {})]

    [>Layout
     [hpc/head state
      [hpc/nav state]]
     [>Layout {:style {:padding "24px"}}
      [hpc/side-bar state]

      [>Content {:style {:background "#fff"
                         :padding 24
                         :margin 0
                         :minHeight 280}}
       [content state]]]
     [hpc/foot state]]))



