(ns soul-talk.modules.kpn.global-order
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.common :as c]
   [soul-talk.util.date-utils :as du]
   [soul-talk.util.data-formatter :as formatter]
   [soul-talk.components.antd-dsl
    :refer [>Input  >InputNumber >Col  >Row >List >Card
            list-item  >AutoComplete  >Modal  >Table
            >Content   >Form  >Description  descrip-item
            >Cascader  >Button  >DatePicker >Divider  >Header
            >Select  >Footer  >Switch >Sider
            >Checkbox  >Checkbox_group  >Form  form-item
            >Layout >Content]]
   [soul-talk.utils :as utils]))

(defn Calendar-selectoer  [page-state]
  [:div                         {:style {:height "220px"}}

   [:> js/antd.Calendar {:size "small"
                         :fullscreen false}]])
(defn Quick-filter [page-state]
  (r/with-let []
    [:div
     [>Row
      [>Col {:span 11}
       [:div
        [:> js/antd.Tag {:color "magenta"} "集团公司"]
        [:> js/antd.Tag {:color "red"} "泰安染厂"]
        [:> js/antd.Tag {:color "green"} "新泰染厂"]]]

      [>Col {:span 13}
       [>Button "今天"]
       [>Divider {:type "vertical"}]
       [>Button "本月"]

       [>Divider {:type "vertical"}]
       [>Select
        {:style {:width 80}
         :defaultValue 1}
        [:> js/antd.Select.Option {:value 1} "今年"]
        [:> js/antd.Select.Option {:value 2} "上年"]
        [:> js/antd.Select.Option {:value 3} "至今"]]

       [>Divider {:type "vertical"}]
       [:> js/antd.DatePicker.RangePicker]]]

     [:p]]))

(defn Card-global [page-state]
  (r/with-let  [all-orders (subscribe [:metadata.data])  ]

    [>Card {:title (str  "康平纳集团公司" "  --  "  "基础指标")
            :bodyStyle    {:height "220px" :overflow "hidden"}}

     [:> js/antd.Descriptions
      {:bordered false}
      [:> descrip-item {:label  "订单个量(个)"}  (-> (:order_count @all-orders)
                                                     ) ]
      [:> descrip-item  {:label "订单金额(万元)"} (-> (:total_money @all-orders)
                                                      (* 0.0001)
                                                      formatter/round-number
                                                      ) ]
      [:> descrip-item  {:label  "色纱需求量(吨)"} (-> (:total_weight @all-orders)
                                                       (* 0.001)
                                                       formatter/round-number
                                                       ) ]
      [:> descrip-item  {:label "已分配需求量(吨)"}]
      [:> descrip-item {:label "已计划投产量(吨)"}]
      [:> descrip-item {:label "已完工需求量(吨)"}]
      [:> descrip-item {:label "用电量(度)"}]
      [:> descrip-item {:label "用水量(吨)"}]
      [:> descrip-item {:label "蒸气用量(立方)"}]
      [:> descrip-item {:label "产能利用状况"} [:div
                                          [:label "120吨"]
                                          [>Divider {:type "vertical"}]
                                          [:label "108.84吨"]]]
      [:> descrip-item]
      [:> descrip-item]]]))

(defn Card-Static [page-state]
  (r/with-let  []
    [>Card {:title (str  "康平纳集团公司" "  --  "  "统计指标")
            :bodyStyle    {:height "220px" :overflow "hidden"}}

     [>Description
      {:column 2}
      [:> descrip-item {:label "吨纱原纱投入(元)"}]
      [:> descrip-item {:label "吨纱能耗投入(元)"}]
      [:> descrip-item {:label "吨纱耗电量"}]
      [:> descrip-item {:label "吨纱耗水量"}]
      [:> descrip-item {:label "吨纱用蒸汽量"}]
      [:> descrip-item {:label "制成率"}]]]))

(defn Card-TaiAn [page-state]
  (r/with-let  []

    [>Card {:title "泰安智能染色工厂"
            :bodyStyle    {:height "220px" :overflow "hidden"}}]))

(defn Card-XinTai [page-state]
  (r/with-let  []

    [>Card {:title "新泰智能染色工厂"
            :bodyStyle    {:height "220px" :overflow "hidden"}}]))

(def table-columns
  [{:key "contract_number" :dataIndex "contract_number" :title "合同编号"}
   {:key "order_number" :dataIndex "order_number" :title "订单编号"}
   {:key "customer_name" :dataIndex "customer_name" :title "客户名称"}
   {:key "customer_color" :dataIndex "customer_color" :title "客户色号"}
   {:key "color_number" :dataIndex "color_number" :title "本厂色号"}

   {:key "order_time" :dataIndex "order_time" :title "下单时间"
    :render (fn [data _]
              (r/as-element  (-> data js/moment. (.format "YYYY-MM-DD"))))}

   {:key "finish_time" :dataIndex "finish_time" :title "计划完成时间"
    :render (fn [data _]
              (r/as-element  (-> data js/moment. (.format "YYYY-MM-DD"))))}

   {:key "yarn_weight" :dataIndex "yarn_weight" :title "订单重量"}
   {:key "tax_price" :dataIndex "tax_price" :title "单价"}
   {:key "tax_money" :dataIndex "tax_money" :title "总价"}])

(defn Table-product-order [page-state]
  (r/with-let [all-order (subscribe [:sell-info.data])]
    [>Table  {:dataSource  @all-order
              :columns table-columns}]))

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

       [>Row {:gutter 16}
        [>Col   {:span 5}
         [Calendar-selectoer page-state]
         [>Divider]]

        [>Col {:span 19}
         [Quick-filter page-state]
         [>Row {:gutter 24}
          [>Col {:span 16}

           [Card-global page-state]]
          [>Col {:span 8}
           [Card-Static page-state]]]

         [:p]
         [>Row
          [Table-product-order page-state]]


         ;;
         ]]]]

     [hpc/foot state]]))



