(ns soul-talk.page.admin
  (:require
   [reagent.core :as r]
   [reagent.ratom :as ratom]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.field :as field]
   [soul-talk.components.label-field :as label-field]
   [soul-talk.utils :as utils]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.util.model-utils :refer [metadata->dto]]
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
   [soul-talk.util.antd-utils :refer [input-value]]
   [soul-talk.util.subs :refer [sub>]]))

(defn admin-description [state  column item]
  [>Description
   {:title  "组织名称"}
   (doall
    (for [[k v]  item]
      ^{:key k}
      [:> js/antd.Descriptions.Item {:label (get-in @column [k :column_comment])}   v]))])

(defn  admin-input-form [state input-state column]
  [:div
   (doall
    (for [[k v] @column]
      ^{:key (str k v)}
      [>Row {:gutter 16}
       [>Col {:span 4}
        [:label (get-in  v  [:column_comment])]]
       [>Col {:span 20}
        [label-field/label-field v input-state]]]
      ;;
      ))]
  ;;
  )

(defn input-modal [state input-state column]
  [:> js/antd.Modal
   {:title "编辑界面"
    :visible (:visible @input-state)
    :onOk (fn [] (do
                   (swap! input-state assoc :visible false)))
    :onCancel  (fn [] (do
                        (swap! input-state assoc :visible false)))}

   [admin-input-form state input-state column]])

(defn  admin-edit-form [state edit-state column]
  [:div
   (doall
    (for [[k v] @column]
      ^{:key (str k v)}
      [>Row {:gutter 16}
       [>Col {:span 6}
        [:label (get-in  v  [:column_comment])]]
       [>Col {:span 14}
        [label-field/label-field v edit-state]]]
      ;;
      ))]
  ;;
  )

(defn edit-modal [state edit-state column item]
  [:> js/antd.Modal
   {:title "编辑界面"
    :visible (:visible @edit-state)
    :onOk (fn [] (do
                   (swap! edit-state assoc :visible false)))
    :onCancel  (fn [] (do
                        (swap! edit-state assoc :visible false)))}

   [admin-edit-form state edit-state column]])

(defn content [state  & _]
  (r/with-let
    [all-data (subscribe [:resource/all (:admin-active-model state)])
     column (subscribe [:resource/columns (:admin-active-model state)])
     view-column (subscribe [:resource/view.table-columns (:admin-active-model state)])
     page-state (r/atom {})
     input-state (r/cursor page-state [:input-state])
     edit-state (r/cursor page-state [:edit-state])]
     [>Layout
      [input-modal page-state input-state column]
      [edit-modal page-state edit-state column]


      [>Content
       [:div
        [>Button {:on-click (fn [] (swap! input-state assoc :visible true))}
         "新增"]]

       [>Table
        {:dataSource @all-data
         :columns (clj->js @view-column)
         :onRow
         (fn [record index]
           (let [item  (js->clj record  :keywordize-keys true)]
             #js {:onClick
                  (fn []
                    (swap! page-state assoc :select-item item)
                    (swap! edit-state assoc :before item)
                    (swap! edit-state assoc :after item)
                    (swap! edit-state assoc :visible true))}))

         :pagination {:defaultPageSize 5}}]
       [:hr]]

     ]
     ;;
     ))


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



