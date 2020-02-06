(ns soul-talk.page.product-order
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as home-page-component]
   [soul-talk.utils :as utils]))

(defn machine-table [id]
  (r/with-let [machines  (subscribe
                          [:resource/find_by-order_detail_id  :machine-resource id])]
    (let [fields  {:crock_number "流水线号"
                   :machine_number "设备编号"
                   :process "工序名称"
                   :flow_id "flow_id"
                   :operation_time1  "操作时间1"
                   :operation_time2 "操作时间2"}
          columns (map (fn  [[k v]]  {:dataIndex (name k)
                                      :key (name k)
                                      :title v})   fields)]

      [:> js/antd.Table {:columns (clj->js columns)
                         :dataSource @machines
                         :pagination {:defaultPageSize	5}}])))

(defn human-table [id]
  (r/with-let [humans  (subscribe [:resource/find_by-order_detail_id :human-resource id])]
    (let [fields   {:order_detail_id "编号"
                    :flow_id "flow_id"
                    :worker_group_number "班组号"
                    :worker_name "工人名称"
                    :process "工序"
                    :create_time "工序开始时间"
                    :weigth "重量"
                    :proportion "比例"
                    :quantity "数量"}
          columns (map (fn  [[k v]]  {:dataIndex (name k)
                                      :key (name k)
                                      :title v})   fields)]

      [:> js/antd.Table  {:columns (clj->js columns)
                          :pagination {:defaultPageSize	5}
                          :dataSource @humans}])))

(defn  order-description [item]
  (let [{:keys [order_company_id finish_time tax_money order_saler_id
                color_number customer_color order_id order_time
                order_is_delete order_detail_id tax_price order_number
                goods_id contract_number customer_name
                order_detail_weight customer_id]
         :as order}  item]
    [:div

     [:> js/antd.Descriptions
      {:title "订单信息"
       ;;:bordered true
       }
      [:> js/antd.Descriptions.Item {:label "订单号"}   order_number]
      [:> js/antd.Descriptions.Item {:label "下单公司"}   order_company_id]
      [:> js/antd.Descriptions.Item {:label "下单时间"}   order_time]
      [:> js/antd.Descriptions.Item {:label "需求数量"}   order_detail_weight]
      [:> js/antd.Descriptions.Item {:label "单价"}  tax_price]
      [:> js/antd.Descriptions.Item {:label "总价"}   tax_money]
      [:> js/antd.Descriptions.Item {:label "客户名称"}   customer_name]
      [:> js/antd.Descriptions.Item {:label "客户色号"}   customer_color]
      [:> js/antd.Descriptions.Item {:label "本厂色号"}   color_number]
      ;;
      ]]))

(defn task-description-table [item]
  (let [columns [{:dataIndex "dyelot_number" :key "dyelot_number" :title "缸号"}
                 {:dataIndex "company_id" :key "company_id" :title "生产公司"}
                 {:dataIndex "factory_id" :key "factory_id" :title "生产工厂"}
                 {:dataIndex "workshop_id" :key "workshop_id" :title "生产车间"}
                 {:dataIndex "flow_plan_release" :key "flow_plan_release" :title "计划投料"}
                 {:dataIndex "flow_plan_release" :key "flow_plan_release" :title "计划投料"}
                 {:dataIndex "flow_final_weight" :key "flow_final_weight" :title "实际产出"}]]

    [:div
     [:> js/antd.Table {:columns (clj->js columns)
                        :dataSource item
                        :size "small"
                        :pagination {:defaultPageSize	5}
                        :bordered true}]]))

(defn task-consum-description [id]
  (r/with-let [material_raw
               (fn [id]
                 (subscribe
                  [:resource/find_by-order_detail_id :material-raw id]))]
    (let [material-consum (->>
                           @(material_raw id)
                           (map :ql_weight)
                           (apply +)
                           utils/round-number)]

      [:> js/antd.Card {:title "订单消耗汇总"}
       [:> js/antd.Descriptions {;;:title "订单消耗汇总"
                                 ;;:bordered true
                                 :size "small"}
        [:> js/antd.Descriptions.Item  {:label "耗电量"}  "ddd"]
        [:> js/antd.Descriptions.Item  {:label "用水量"}  "ddd"]
        [:> js/antd.Descriptions.Item  {:label "蒸汽量"}  "ddd"]
        [:> js/antd.Descriptions.Item  {:label "成品需求"}  "ddd"]
        [:> js/antd.Descriptions.Item  {:label "原纱计划消耗"}  "ddd"]
        [:> js/antd.Descriptions.Item  {:label "原纱实际消耗"}  material-consum]
        [:> js/antd.Descriptions.Item  {:label "参与人数"}  "ddd"]
        [:> js/antd.Descriptions.Item  {:label "使用机台数"}  "ddd"]
        [:> js/antd.Descriptions.Item  {:label "原纱消耗"}  "ddd"]]])))

(defn task-craft-table [id]
  (r/with-let [crafts  (subscribe
                        [:resource/find_by-order_detail_id :material-craft id])
               columns  [{:dataIndex  "dyelot_number" :key "dyelot_number" :title "缸号"}
                         {:dataIndex  "dye_zh_name" :key "dye_zh_name" :title "物料名称"}
                         {:dataIndex  "goods_id" :key "goods_id" :title "物料编号"}
                         {:dataIndex  "yl_unit" :key "yl_unit" :title "单位"}
                         {:dataIndex  "dosage" :key "dosage" :title "用量"}]]

    [:> js/antd.Table {:columns columns
                       :dataSource @crafts
                       :size "small"
                       :pagination {:defaultPageSize	5}
                       :bordered true}]))

(defn content [state & _]

  (r/with-let [id (subscribe [:page-state :index-detail :order-detail-id])
               orders  (fn [id] (subscribe [:sell-order/by-order_detail_id @id]))
               tasks  (fn [id] (subscribe [:product-task/by-order_detail_id @id]))]

    [:div
     [:> js/antd.Row {:gutter 24}

      [:> js/antd.Col {:span 16}
       [order-description  (first @(orders  id))]

       [:> js/antd.Divider]
       [:h3 "生产订单分拆情况"]
       [task-description-table  @(tasks id)]

         ;;
       [:> js/antd.Divider]
       [:h3 "人力状况"]
       [human-table @id]
         ;;
       [:> js/antd.Divider]
       [:h3 "机械状况"]
       [machine-table @id]
         ;;
       [:> js/antd.Divider]
       [:h3 "染料用量情况"]
       [task-craft-table @id]]
      [:> js/antd.Col {:span 8}
       [task-consum-description @id]]]]))

(defn home-page [state & _]
  (r/with-let [active (subscribe [:active-page])
               page-state (subscribe [:current-page-state])]

    [:> js/antd.Layout
     [home-page-component/head state
      [home-page-component/nav state]]
     [:> js/antd.Layout {:style {:padding "24px"}}

      [home-page-component/side-bar state]

      [:> js/antd.Layout.Content {:style {:background "#fff"
                                          :padding 24
                                          :margin 0
                                          :minHeight 280}}

       [content state]]]
     [home-page-component/foot state]]))

