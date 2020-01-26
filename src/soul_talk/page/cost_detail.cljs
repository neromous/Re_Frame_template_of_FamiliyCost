(ns soul-talk.page.cost-detail
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.fields :as fields]
            [soul-talk.components.tables :as tables]
            [soul-talk.components.columns :as columns]
            [soul-talk.date-utils :as du]
            [soul-talk.components.global_components :as gbc]
            [soul-talk.utils :as utils]
            [soul-talk.route.utils :refer [logged-in?
                                           context-url
                                           href
                                           navigate!
                                           run-events
                                           run-events-admin]]))

;; (->> @(subscribe [:cost.material-raw/all] )
;;      ;;(filter #(-> % :order_detail_id (=  72))    )
;;      (map :dyelot_number)
;;      set
;;      count
;;      )

(dispatch [:cost.detail/material_craft-pull
           {"filters" [["="  "order_detail_id"   72]]
            "limit" 102400}])

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

    [:> js/antd.Table {:columns (clj->js columns)
                       :dataSource item
                       :size "small"
                       :bordered true}]))

(defn task-consum-description []
  (r/with-let [material_raw (subscribe [:cost.detail/material_raw])]
    (let [material-consum (->>
                           @material_raw
                           vals
                           (apply concat)
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


(-> @(subscribe [:cost.detail/material_craft])
    first)

(defn task-craft-table []
  (r/with-let [crafts  (subscribe [:cost.detail/material_craft])
               columns  [{:dataIndex  "dyelot_number" :key "dyelot_number" :title "缸号"}
                         {:dataIndex  "dye_zh_name" :key "dye_zh_name" :title "物料名称"}
                         {:dataIndex  "goods_id" :key "goods_id" :title "物料编号"}
                         {:dataIndex  "yl_unit" :key "yl_unit" :title "单位"}
                         {:dataIndex  "dosage" :key "dosage" :title "用量"}

                         ]]

    [:> js/antd.Table {:columns columns
                       :dataSource @crafts
                       :size "small"
                       :bordered true
                       }]))

(defn content []

  (r/with-let [orders  (subscribe [:cost.detail/order_detail-selected])
               tasks (subscribe [:cost.detail/product_task-selected])
               material (subscribe [:cost.detail/material_raw])]
    (let [item   (or (first @orders)  {})
          tasks @tasks]
      [:div
       [:> js/antd.Row {:gutter 24}
        [:> js/antd.Col {:span 16}
         [order-description item]

         [:> js/antd.Divider]
         [:h3 "生产订单分拆情况"]
         [task-description-table  tasks]

         [:> js/antd.Divider]
         [:h3 "染料用量情况"]
         [task-craft-table]

         ]

        [:> js/antd.Col {:span 8}
         [task-consum-description]]]])))

(defn home-page []
  (r/with-let [active (subscribe [:active])
               page-state (subscribe [:views/cost.detail])]
    (fn []
      (let []

        [:> js/antd.Layout
         [gbc/head [gbc/nav]]
         [:> js/antd.Layout {:style {:padding "24px"}}
          [gbc/side-bar]

          [:> js/antd.Layout.Content {:style {:background "#fff"
                                              :padding 24
                                              :margin 0
                                              :minHeight 280}}
           [content]]]
         [gbc/foot]]))))

