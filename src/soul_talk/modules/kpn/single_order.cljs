(ns soul-talk.modules.kpn.single-order
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.common :as c]
   [soul-talk.util.date-utils :as du]
   [soul-talk.util.db :as udb]
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
  (r/with-let [data (subscribe [:order_number/get :sell-info])
               columns (utils/vecs->columns [["contract_number" "合同编号"]
                                             ["custom_name" "客户名称"]
                                             ["color_number" "色号"]
                                             ["order_company" "接单公司"]
                                             ["order_number" "订单号"]
                                             ["order_detail_id" "子订单编号"]
                                             ["order_detail_weight" "子订单重量"]
                                             ["tax_price" "单价"]
                                             ["tax_money" "合计金额"]
                                             ["order_time" "下单时间"]
                                             ["finish_time" "计划完成时间"]])]

    [:div
     [>Divider  "订单拆分列表"]
     [>Table  {:dataSource @data
               :columns columns}]]))

(defn Table-product_detail [page-state]
  (r/with-let [data (subscribe [:order_number/get :flow-info])
               columns (utils/vecs->columns [["company_name" "生产公司"]
                                             ["factory_name" "生产工厂"]
                                             ["workshop_name" "生产车间"]
                                             ["plan_number" "计划轴数"]
                                             ["plan_release" "计划数量"]
                                             ["final_number" "产出轴数"]
                                             ["final_release" "产出重量"]
                                             ["finish_time" "完成时间"]])]

    [:div
     [>Divider  "订单生产了列表"]
     [>Table  {:dataSource (->>
                            @data
                            (map (fn [{:keys [finish_time] :as body}]
                                   (assoc body :finish_time (du/str->date finish_time)))))

               :columns columns}]]))

(defn Table-raw_material [page-state]
  (r/with-let [data (subscribe [:order_number/get :raw-material])
               columns (utils/vecs->columns [["goods_code" "物料编码"]
                                             ["goods_name"  "物料名称"]
                                             ["ql_weight" "前络产成数"]
                                             ["user_time" "使用时间"]
                                             ["yarn_weight"  "需求数量"]])]

    [:div
     [>Divider  "原材料投入"]
     [>Table {:dataSource @data
              :columns columns}]]))

(defn Table-craft [page-state]
  (r/with-let [data (subscribe [:order_number/get :craft-material])
               columns (utils/vecs->columns [["dyelot_number" "缸号"]
                                             ["color_number" "色号"]
                                             ["dye_zh_name" "染料颜色"]
                                             ["yl_unit" "单位"]
                                             ["dosage" "染料消耗"]
                                             ["weight" "原纱重量"]])]

    [:div
     [>Divider "染料与助剂投入"]
     [>Table {:dataSource @data
              :columns columns}]]))

(defn Table-human_resource [page-state]
  (r/with-let [data (subscribe [:order_number/get :human])
               columns (utils/vecs->columns [["order_number" "订单号"]
                                             ["dyelot_number" "缸号"]
                                             ["process" "工序"]
                                             ["quantity" "重量"]
                                             ["proportion" "比例"]])]

    [:div
     [>Divider "人力资源投入"]
     [>Table  {:dataSource @data
               :columns columns}]]))

(defn Table-machine_resource [page-state]
  (r/with-let [data (subscribe [:order_number/get :machine])
               columns (utils/vecs->columns [["crock_number" "机台号"]
                                             ["create_name" "操作班组"]
                                             ["machine_number" "设备号"]])]

    [:div
     [>Divider   "机械投入"]
     [>Table  {:dataSource @data
               :columns columns}]]))

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
         [Table-product_detail page-state]
         [Table-raw_material page-state]
         [Table-human_resource page-state]
         [Table-machine_resource page-state]
         [Table-craft page-state]]]

       [>Col {:span 8}]]]

     [hpc/foot state]]))



