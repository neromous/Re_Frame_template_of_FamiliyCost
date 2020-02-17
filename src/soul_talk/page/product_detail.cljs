(ns soul-talk.page.product-detail
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.common :as c]
   [soul-talk.util.date-utils :as du]
   [soul-talk.components.antd-dsl
    :refer [>Input  >InputNumber >Col  >Row >List >Card
            list-item  >AutoComplete  >Modal  >Table
            >Content   >Form  >Description  descrip-item
            >Cascader  >Button  >DatePicker >Divider  >Header
            >Select  >Footer  >Switch >Sider
            >Checkbox  >Checkbox_group  >Form  form-item
            >Layout >Content]]
   [soul-talk.utils :as utils]))

(defn Description-Static [page-state]
  (r/with-let []
    [>Card

     [>Description {:title "统计数据"
                    :column 1}

      [:> descrip-item {:label "原纱投入量"}]
      [:> descrip-item {:label "耗电量"}]
      [:> descrip-item {:label "耗水量"}]
      [:> descrip-item {:label "蒸汽量"}]
      [:> descrip-item {:label "总参与人数"}]
      [:> descrip-item {:label "参与用工人数"}]
      [:> descrip-item]]]))

(defn Quick-filter [page-state]
  (r/with-let []
    [:p "信息栏"]))

(defn Description-Header [page-state]
  (r/with-let []

    [:> js/antd.Descriptions  {:title "订单信息"}
     [:> descrip-item {:label "订单号"}]
     [:> descrip-item {:label "订单需求量"}]
     [:> descrip-item {:label "下单公司"}]
     [:> descrip-item {:label "客户色号"}]
     [:> descrip-item {:label "本厂色号"}]
     [:> descrip-item {:label "下单时间"}]
     [:> descrip-item {:label "接单工厂"}]
     [:> descrip-item {:label "是否已分配"}]]))

(defn Table-order_detail [page-state]
  (r/with-let []

    [:div
     [>Divider  "订单拆分列表"]
     [>Table]]))

(defn Table-raw_material [page-state]
  (r/with-let []

    [:div
     [>Divider  "原材料投入"]
     [>Table]]))

(defn Table-craft [page-state]
  (r/with-let []
    [:div
     [>Divider "染料与助剂投入"]
     [>Table]]))

(defn Table-human_resource [page-state]
  (r/with-let  []
    [:div
     [>Divider "人力资源投入"]
     [>Table]]))

(defn Table-machine_resource [page-state]
  (r/with-let []
    [:div
     [>Divider   "机械投入"]
     [>Table]]))

(defn Description-global [page-state]
  (r/with-let []
    [:div
     [>Description
      [:> descrip-item]
      [:> descrip-item]
      [:> descrip-item]
      [:> descrip-item]
      [:> descrip-item]
      [:> descrip-item]
      [:> descrip-item]]]))

(defn Selector-global [page-state]
  [:div ""])

(defn home-page [state & _]
  (r/with-let [active-page (:active-page state)
               page (:page-state state)
               page-state (r/atom {})]

    [>Layout
     [hpc/head state
      [hpc/nav state]]
     [>Layout {:style {:padding "24px"}}

      [>Content {:style {:background "#fff"
                         :padding 24
                         :margin 0
                         :minHeight 280}}

       [>Row {:gutter 24}
        [>Col {:span 4}
         [Description-Static page-state]]

        [>Col {:span 16}
         [Quick-filter page-state]
         [:p]

         [Description-Header page-state]
         [Table-order_detail page-state]
         [Table-raw_material page-state]
         [Table-human_resource page-state]
         [Table-machine_resource page-state]
         [Table-craft page-state]]]

       [>Col {:span 8}]]]

     [hpc/foot state]]))



