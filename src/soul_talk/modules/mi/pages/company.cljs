(ns soul-talk.modules.mi.pages.company
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.antd :as h]

   [datascript.core :as d]
   [soul-talk.db :refer  [conn]]
   [soul-talk.util.data-formatter :refer [parse-float
                                          parse-int
                                          round-number]]
   [soul-talk.db :refer  [unique-id]]

   [soul-talk.components.maps :as react-map]
   [soul-talk.modules.mi.models.sell-info :refer  [order->table_source
                                                   task->table_source]]
   [soul-talk.util.reframe-helper :refer
    [sub> act> field->float&2 to-columns fix-key fix-value
     field->moment field->date2str] :as rh]))

(def task->table
  (map (fn [x]
         (-> x
             (assoc  :workshop (get-in x [:task/ref.workshop
                                          :org/name]))
             (assoc  :material_name (get-in x [:task/ref.material
                                               :material/name]))
             ;;
             ))))

(defn data-static [dataset]
  {:money  (apply + (map :order/tax_money dataset))
   :weight (apply +  (map :order/yarn_weight dataset))
   :count  (count   (map :order/eid dataset))})

(defn sell-order-table []
  (r/with-let  [order  (sub> :order/all)]
    (let [order->table
          (map (fn [x]
                 (-> x
                     (assoc  :customer_name (get-in x [:order/ref.order_number
                                                       :order/ref.customer
                                                       :org/name]))
                     (assoc  :company_name (get-in x [:order/ref.order_number
                                                      :order/ref.order_company
                                                      :org/name]))
                     (assoc  :material_name (get-in x [:order/ref.material
                                                       :material/name]))
                     ;;
                     )))]
      [:> h/table
       {:size "small"
        :pagination {:defaultPageSize  5}
        :dataSource  (into []
                           (comp
                            order->table
                            (field->float&2 :order/yarn_weight)
                            (field->float&2 :order/tax_price)
                            (field->float&2 :order/tax_money))
                           @order)
        :columns (into [] to-columns [["eid" "id"]
                                      ["customer_name" "客户"]
                                      ["company_name" "生产企业"]
                                      ["color_number" "色号"]
                                      ["material_name" "材料名称"]
                                      ["yarn_weight" "重量"]
                                      ["tax_price" "单价"]
                                      ["tax_money" "价格"]])}])))
(defn sell-order-static []
  (r/with-let [order  (sub> :order/all)]
    (let [status   (data-static @order)]
      [:> js/antd.Card
       [:> h/descrip  {:column 1  :size "small"}
        [:> h/descrip-item {:label "订单总数"}  (:count status)]
        [:> h/descrip-item {:label "订单总价"}  (round-number
                                             (:weight status))]
        [:> h/descrip-item {:label "订单总重"} (round-number
                                            (:money status))]]])))

(defn series-table []
  (r/with-let [order  (sub> :order/all)]
    [:> h/card
     [:> h/row {:gutter 16}
      [:> h/col {:span 6}
       [sell-order-static]]
      [:> h/col {:span 18}
       [sell-order-table]]]]))

(defn task-table [state]
  (r/with-let  [order (rh/sub> :order/eid.task (:select-order state))]

    (let [order @order
          task->table
          (map (fn [x]
                 (-> x
                     (assoc  :workshop (get-in x [:task/ref.workshop
                                                  :org/name]))
                     (assoc  :material_name (get-in x [:task/ref.material
                                                       :material/name]))
                     ;;
                     )))]
      [:> h/table
       {:size "small"
        :pagination {:defaultPageSize  5}
        :dataSource  (into []
                           (comp
                            task->table
                            (field->float&2 :task/plan_weight))
                           order)
        :columns (into [] to-columns [["eid" "id"]
                                      ["dyelot_number" "缸号"]
                                      ["workshop" "生产车间"]
                                      ["material_name" "材料名称"]
                                      ["plan_weight" "重量"]])}])))

(defn resource-material-table [state]
  (r/with-let  [order  (sub> :order/eid.material (:select-order state))]
    (let [data->table (map (fn [x]
                             (-> x
                                 (assoc  :name (get-in x [:cost/ref.for-what
                                                          :task/name])))))]

      [:> h/table
       {:size "small"
        :pagination {:defaultPageSize  5}
        :dataSource  (into []
                           (comp
                            data->table
                            (field->date2str :cost/end-time)
                            (field->date2str :cost/start-time))
                           @order)
        :columns (into [] to-columns [["category" "类别"]
                                      ["unit" "单位"]
                                      ["quantity" "重量"]])}])))

(defn resource-human-table []
  (r/with-let  [order  (sub> :d/get :order.human&machine)]
    (let [data->table (map (fn [x]
                             (-> x
                                 (assoc  :name (get-in x [:cost/ref.for-what
                                                          :process/name])))))

          filter-machine (filter  #(= (:cost/category %) "人工"))]

      [:> h/table
       {:size "small"
        :pagination {:defaultPageSize  5}
        :dataSource  (into []
                           (comp
                            filter-machine
                            data->table
                            (field->float&2  :cost/quantity)
                            (field->date2str :cost/end-time)
                            (field->date2str :cost/start-time))
                           @order)
        :columns (into [] to-columns [["category" "类别"]
                                      ["name" "工序名称"]
                                      ["quantity" "量"]
                                      ["proportion" "百分比"]])}])))

(defn single-order [state]
  (r/with-let [cache  (sub> :select :order/eid  (:select-order state))
               tasks  (sub> :order/eid.task (:select-order state))
               processes (sub> :order/eid.process  (:select-order state))
               material (sub>  :order/eid.material (:select-order state))]

    (let [order  (first @cache)
          num (get-in order [:order/ref.order_number :order/order_number])
          money (get-in order [:order/tax_money])
          price (get-in order [:order/tax_price])
          weight (get-in order [:order/yarn_weight])
          color-number (get-in order [:order/color_number])
          ;;
          ]
      [:> js/antd.Card
       [:> h/descrip  {:column 2
                       :size "small"}
        [:> h/descrip-item {:label "订单编号"} num]
        [:> h/descrip-item {:label "订单单价"} (round-number price)]
        [:> h/descrip-item {:label "订单金额"} (round-number money)]
        [:> h/descrip-item {:label "订单重量"} (round-number weight)]
        [:> h/descrip-item {:label "色号"} color-number]
        [:> h/descrip-item {:label "生产任务数量"}
         (for [x (map :task/plan_weight  @tasks)]
           ^{:key (str "ooo"  unique-id)}
           [:span [:span  x]
            [:> h/divider {:type "vertical"}]])]

        [:> h/descrip-item {:label "工序数量"} (count @processes)]
        [:> h/descrip-item {:label "材料单条数"} (count @material)]
        ;;[:> h/descrip-item {:label "总消耗工序"} (count @consum)]
        ]])))

(defn  tai-an-orders []
  (let []
    (r/with-let [order (subscribe [:com.tai-an/orders])
                 tasks (subscribe [:com.tai-an/tasks])]
      [:> h/card
       [:h1 "泰安智能染色工厂概览"]
       [:> h/descrip {:size "small"}
        [:> h/descrip-item  {:label "订单个数"} (count @order)]
        [:> h/descrip-item  {:label "生产任务个数"} (count @tasks)]
        [:> h/descrip-item  {:label "订单重量"} (apply + (map :order/yarn_weight @order))]
        [:> h/descrip-item  {:label "plan_weight"} (apply + (map :task/plan_weight @tasks))]]
       [:> h/row
        {:gutter 16}
        [:> h/col

         {:span 12}
         [:h3 "销售订单列表"]
         [:> h/table
          {:pagination {:defaultPageSize  5}
           :size "small"
           :dataSource  (into [] order->table_source @order)
           :columns  (into [] to-columns [["eid" "id"]
                                          ["customer_name" "客户"]
                                          ["company_name" "生产企业"]
                                          ["color_number" "色号"]
                                          ["material_name" "材料名称"]
                                          ["yarn_weight" "重量"]
                                          ["tax_price" "单价"]
                                          ["tax_money" "价格"]])}]]

        [:> h/col
         {:span 12}
         [:h3 "生产订单列表"]
         [:> h/table
          {:pagination {:defaultPageSize  5}
           :size "small"
           :dataSource  (into [] task->table_source @tasks)
           :columns  (into [] to-columns [["eid" "id"]
                                          ["dyelot_number" "缸号"]
                                          ["company" "生产工厂"]
                                          ["workshop" "生产公司"]
                                          ["material_name" "材料名称"]
                                          ["plan_weight" "重量"]])}]]]])))

(defn  xin-tai-orders []
  (let []
    (r/with-let [order (subscribe [:com.xin-tai/orders])
                 tasks (subscribe [:com.xin-tai/tasks])]
      [:> h/card
       [:h1 "新泰能染色工厂概览"]
       [:> h/descrip {:size "small"}
        [:> h/descrip-item  {:label "订单个数"} (count @order)]
        [:> h/descrip-item  {:label "生产任务个数"} (count @tasks)]
        [:> h/descrip-item  {:label "订单重量"} (apply + (map :order/yarn_weight @order))]
        [:> h/descrip-item  {:label "plan_weight"} (apply + (map :task/plan_weight @tasks))]]
       [:> h/row
        {:gutter 16}
        [:> h/col

         {:span 12}
         [:h3 "销售订单列表"]
         [:> h/table
          {:pagination {:defaultPageSize  5}
           :size "small"
           :dataSource  (into [] order->table_source @order)
           :columns  (into [] to-columns [["eid" "id"]
                                          ["customer_name" "客户"]
                                          ["company_name" "生产企业"]
                                          ["color_number" "色号"]
                                          ["material_name" "材料名称"]
                                          ["yarn_weight" "重量"]
                                          ["tax_price" "单价"]
                                          ["tax_money" "价格"]])}]]

        [:> h/col
         {:span 12}
         [:h3 "生产订单列表"]
         [:> h/table
          {:pagination {:defaultPageSize  5}
           :size "small"
           :dataSource  (into [] task->table_source @tasks)
           :columns  (into [] to-columns [["eid" "id"]
                                          ["dyelot_number" "缸号"]
                                          ["company" "生产工厂"]
                                          ["workshop" "生产公司"]
                                          ["material_name" "材料名称"]
                                          ["plan_weight" "重量"]])}]]]])))


(defn  material-sample-orders []
  (let []
    (r/with-let [order (subscribe [:com.xin-tai/orders])
                 tasks (subscribe [:com.xin-tai/tasks])]
      [:> h/card
       [:h1 "新泰能染色工厂概览"]
       [:> h/descrip {:size "small"}
        [:> h/descrip-item  {:label "订单个数"} (count @order)]
        [:> h/descrip-item  {:label "生产任务个数"} (count @tasks)]
        [:> h/descrip-item  {:label "订单重量"} (apply + (map :order/yarn_weight @order))]
        [:> h/descrip-item  {:label "plan_weight"} (apply + (map :task/plan_weight @tasks))]]
       [:> h/row
        {:gutter 16}
        [:> h/col

         {:span 12}
         [:h3 "销售订单列表"]
         [:> h/table
          {:pagination {:defaultPageSize  5}
           :size "small"
           :dataSource  (into [] order->table_source @order)
           :columns  (into [] to-columns [["eid" "id"]
                                          ["customer_name" "客户"]
                                          ["company_name" "生产企业"]
                                          ["color_number" "色号"]
                                          ["material_name" "材料名称"]
                                          ["yarn_weight" "重量"]
                                          ["tax_price" "单价"]
                                          ["tax_money" "价格"]])}]]

        [:> h/col
         {:span 12}
         [:h3 "生产订单列表"]
         [:> h/table
          {:pagination {:defaultPageSize  5}
           :size "small"
           :dataSource  (into [] task->table_source @tasks)
           :columns  (into [] to-columns [["eid" "id"]
                                          ["dyelot_number" "缸号"]
                                          ["company" "生产工厂"]
                                          ["workshop" "生产公司"]
                                          ["material_name" "材料名称"]
                                          ["plan_weight" "重量"]])}]]]])))




(defn index-page [page-state]

  (r/with-let [state  (r/atom {})]
    (reset! state {:select-order 1})

    [:div
     [series-table]
     [single-order @state]
     ;[resource-human-table]
     [tai-an-orders]
     [xin-tai-orders]
     [resource-material-table @state]
     ;[resource-machine-table]
     [task-table  @state]
     [:div {:style   {:width 900  :height 500}}
      [:h2 "本市国资企业分布"]
      [:> (.-Map js/reactMap)    {:amapkey "14496d2c07234db4cb989b9c2549fe08"
                                  }]]


     ]))
