(ns soul-talk.modules.mi.pages.company
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.antd :as h]

   [soul-talk.util.data-formatter :refer [parse-float
                                          parse-int
                                          round-number]]
   [soul-talk.db :refer  [unique-id]]
   [soul-talk.util.reframe-helper :refer
    [sub> act>   dsub dget dset field->float&2 to-columns fix-key fix-value
     field->moment field->date2str]]))

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
  (r/with-let  [order  (sub> :d/get :order.all)]
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
      [:> js/antd.Table
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
(defn task-table []
  (r/with-let  [order  (sub> :d/get :order.task)]
    (let [task->table (map (fn [x]
                             (-> x
                                 (assoc  :workshop (get-in x [:task/ref.workshop
                                                              :org/name]))
                                 (assoc  :material_name (get-in x [:task/ref.material
                                                                   :material/name]))
                     ;;
                                 )))]
      [:> js/antd.Table
       {:size "small"
        :pagination {:defaultPageSize  5}
        :dataSource  (into []
                           (comp
                            task->table
                            (field->float&2 :task/plan_weight))
                           @order)
        :columns (into [] to-columns [["eid" "id"]
                                      ["dyelot_number" "缸号"]
                                      ["workshop" "生产车间"]
                                      ["material_name" "材料名称"]
                                      ["plan_weight" "重量"]])}])))

(defn resource-machine-table []
  (r/with-let  [order  (sub> :d/get :order.human&machine)]
    (let [data->table (map (fn [x]
                             (-> x
                                 (assoc  :name (get-in x [:cost/ref.for-what
                                                          :process/name])))))

          filter-machine (filter  #(= (:cost/category %) "机械"))]

      [:> js/antd.Table
       {:size "small"
        :pagination {:defaultPageSize  5}
        :dataSource  (into []
                           (comp
                            filter-machine
                            data->table
                            (field->date2str :cost/end-time)
                            (field->date2str :cost/start-time))
                           @order)
        :columns (into [] to-columns [["category" "类别"]
                                      ["machine_number" "机台号"]
                                      ["name" "工序名称"]
                                      ["start-time" "开始时间"]
                                      ["end-time" "结束时间"]])}])))

(defn resource-human-table []
  (r/with-let  [order  (sub> :d/get :order.human&machine)]
    (let [data->table (map (fn [x]
                             (-> x
                                 (assoc  :name (get-in x [:cost/ref.for-what
                                                          :process/name])))))

          filter-machine (filter  #(= (:cost/category %) "人工"))]

      [:> js/antd.Table
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

(defn sell-order-static []
  (r/with-let [order  (dsub :order.all)]
    (let [status   (data-static @order)]
      [:> js/antd.Card
       [:> js/antd.Descriptions  {:column 1  :size "small"}
        [:> js/antd.Descriptions.Item {:label "订单总数"}  (:count status)]
        [:> js/antd.Descriptions.Item {:label "订单总价"}  (round-number
                                                        (:weight status))]
        [:> js/antd.Descriptions.Item {:label "订单总重"} (round-number
                                                       (:money status))]]])))

(defn series-table []
  (r/with-let [order  (dsub :order.all)]
    [:> h/card
     [:> h/row {:gutter 16}
      [:> h/col {:span 6}
       [sell-order-static]]
      [:> h/col {:span 18}
       [sell-order-table]]]]))

(defn single-order []
  (r/with-let [cache  (sub> :d/get :order.single)
               tasks  (sub> :d/get :order.task)
               processes (sub> :d/get :order.process)
               material (sub> :d/get  :order.material&craft)
               consum (sub> :d/get  :order.human&machine)]
    (let [order  (first @cache)
          num (get-in order [:order/ref.order_number :order/order_number])
          money (get-in order [:order/tax_money])
          price (get-in order [:order/tax_price])
          weight (get-in order [:order/yarn_weight])
          color-number (get-in order [:order/color_number])
          ;;
          ]
      [:> js/antd.Card
       [:> js/antd.Descriptions  {:column 2
                                  :size "small"}
        [:> js/antd.Descriptions.Item {:label "订单编号"} num]
        [:> js/antd.Descriptions.Item {:label "订单单价"} (round-number price)]
        [:> js/antd.Descriptions.Item {:label "订单金额"} (round-number money)]
        [:> js/antd.Descriptions.Item {:label "订单重量"} (round-number weight)]
        [:> js/antd.Descriptions.Item {:label "色号"} color-number]
        [:> js/antd.Descriptions.Item {:label "生产任务数量"}
         (for [x (map :task/plan_weight  @tasks)]
           ^{:key (str "ooo"  unique-id)}
           [:span [:span  x]
            [:> h/divider {:type "vertical"}]])]

        [:> js/antd.Descriptions.Item {:label "工序数量"} (count @processes)]
        [:> js/antd.Descriptions.Item {:label "材料单条数"} (count @material)]
        [:> js/antd.Descriptions.Item {:label "总消耗工序"} (count @consum)]]])))

(defn index-page [page-state]
  (r/with-let [sell-order  (dsub :order.single)
               task  (dsub :order.task)
               process (dsub :order.process)
               material (dsub :order.material&craft)
               consume  (dsub :order.human&machine)]
    [:div
     [series-table]
     [single-order]
     [resource-human-table]
     [resource-machine-table]
     [task-table]]))

