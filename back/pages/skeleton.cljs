(ns soul-talk.pages.skeleton
  (:require [soul-talk.components.global-footer :refer [footer]]
            [soul-talk.components.global-header :refer [header]]
            [soul-talk.components.siderbar :refer [siderbar]]
            [soul-talk.routes :refer [
                                     logged-in?
                                     context-url
                                     href
                                     navigate!
                                     run-events
                                     run-events-admin
                                     ]]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn layout-hcf [head nav  content footer]
  (fn []
    [:> js/antd.Layout
     [header [nav]]
     [content]
     [footer]]))

(defn layout-hcfs-left [head nav content footer sider]
  (fn []
    [:> js/antd.Layout
     [header [nav]]
     [:> js/antd.Layout
      [sider]
      [:> js/antd.Layout.Content
       [content]]]
     [footer]]))

(defn layout-hcfs-right [head nav content footer sider]
  (fn []
    [:> js/antd.Layout
     [header [nav]]
     [:> js/antd.Layout
      [:> js/antd.Layout.Content
       [content]]
      [sider]]
     [footer]]))

(defn layout-hcfs-whole [head nav content footer sider]
  (fn []
    [:> js/antd.Layout
     [sider]]
    [:> js/antd.Layout
     [header [nav]
      [content]
      [footer]]]))

(defn layout-hcfs-left-calc [head nav main-table pivot-table  footer sider]
  (fn []
    [:> js/antd.Layout
     [header [nav]]
     [:> js/antd.Layout
      [sider]
      [:> js/antd.Layout.Content
       [main-table]
       [:hr]
       [pivot-table]
       ]]
     [footer]]))






