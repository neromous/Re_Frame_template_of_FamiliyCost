(ns soul-talk.page.product-track
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.common :as c]
   [soul-talk.util.date-utils :as du]
   [soul-talk.components.antd-dsl
    :refer [>Input  >InputNumber >Col  >Row >List
            list-item  >AutoComplete  >Modal  >Table
            >Content   >Form  >Description  descrip-item
            >Cascader  >Button  >DatePicker >Divider  >Header
            >Select  >Footer  >Switch >Sider
            >Checkbox  >Checkbox_group  >Form  form-item
            >Layout >Content]]
   [soul-talk.utils :as utils]))

(def table-columns
  [{:key "contract_number" :dataIndex "contract_number" :title "合同编号"}
   {:key "order_number" :dataIndex "order_number" :title "订单编号"}
   {:key "customer_name" :dataIndex "customer_name" :title "客户名称"}
   {:key "customer_color" :dataIndex "customer_color" :title "客户色号"}
   {:key "order_time" :dataIndex "order_time" :title "下单时间"
    :render (fn [data _]
              (r/as-element  (-> data js/moment. (.format "YYYY-MM-DD"))))}

   {:key "finish_time" :dataIndex "finish_time" :title "计划完成时间"
    :render (fn [data _]
             (r/as-element  (-> data js/moment. (.format "YYYY-MM-DD"))))}

   {:key "order_detail_weight" :dataIndex "order_detail_weight" :title "订单重量"}
   {:key "tax_price" :dataIndex "tax_price" :title "单价"}
   {:key "tax_money" :dataIndex "tax_money" :title "总价"}])

(defn table-product-order [page-state]
  (r/with-let [all-order (subscribe [:item/all :product-track])]
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
      ;;[hpc/side-bar state]

      [>Content {:style {:background "#fff"
                         :padding 24
                         :margin 0
                         :minHeight 280}}

       [table-product-order page-state]]]

     [hpc/foot state]]))



