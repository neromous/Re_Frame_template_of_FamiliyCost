(ns soul-talk.page.layout
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [re-frame.core :as rf]))

(defn layout-hcf [{:keys [header nav  content footer]}]
  (fn []
    [:> js/antd.Layout
     [header [nav]]
     [content]
     [footer]]))

(defn layout-hcfs-left [{:keys [header nav content footer sider ] :as item}]

  (r/with-let [active (subscribe [:active])]
    (fn []
      [:> js/antd.Layout
       [header @active [nav @active]]
       [:> js/antd.Layout
        [sider @active]
        [:> js/antd.Layout.Content
         [content @active]]]
       [footer @active]]))
)
(defn layout-hcfs-right [{:keys [header nav content footer sider]}]
  (fn []
    [:> js/antd.Layout
     [header [nav]]
     [:> js/antd.Layout
      [:> js/antd.Layout.Content
       [content]]
      [sider]]
     [footer]]))

(defn layout-hcfs-whole [{:keys [header nav content footer sider]}]
  (fn []
    [:> js/antd.Layout
     [sider]]
    [:> js/antd.Layout
     [header [nav]
      [content]
      [footer]]]))

(defn layout-hcfs-left-calc [{:keys [header nav main-table pivot-table  footer sider]}]
  (fn []
    [:> js/antd.Layout
     [header [nav]]
     [:> js/antd.Layout
      [sider]
      [:> js/antd.Layout.Content
       [main-table]
       [:hr]
       [pivot-table]]]
     [footer]]))






