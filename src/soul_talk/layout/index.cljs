(ns soul-talk.layout.index
  (:require
   [soul-talk.util.route-utils :refer [run-events run-events-admin logged-in? navigate!]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.antd-dsl
    :refer [>Input
            >InputNumber
            >Col
            >Row
            >List
            >AutoComplete
            >List_Item
            >Table
            >Content
            >Description
            >Description_Item
            >Cascader
            >Button
            >Header
            >Footer
            >Sider
            >Layout]]
   [soul-talk.utils :as utils]
   [reagent.core :as r]
   [re-frame.core :as rf]))

(defn layout [head nav sider content foot]
  (r/with-let [active (subscribe [:active])]
    [:> js/antd.Layout
     [head [nav]]
     [:> js/antd.Layout {:style {:padding "24px"}}
      [sider]

      [:> js/antd.Layout.Content {:style {:background "#fff"
                                          :padding 24
                                          :margin 0
                                          :minHeight 280}}
       [content]]]
     [foot]]))
