(ns soul-talk.page.price-index
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

(def fake-data
  {:prices  [{:id 1
              :goods_id 1
              :goods_name "原纱32支"
              :price_available 7000
              :last_buy_price 5000
              :last_sell_price 6000
              :mean_price 6500}
             {:id 2
              :goods_id 2
              :goods_name "原纱36支"
              :price_available 7000
              :last_buy_price 5000
              :last_sell_price 6000
              :mean_price 6500}]})

(defn  Modal-input-new-price [page-state]
  (r/with-let [vis (r/cursor page-state [:input-price :vis])]
    [:> js/antd.Modal
     {:title    "修改价格"
      :visible  @vis
      :onOk     #(reset! vis false)
      :onCancel #(reset! vis false)}

     [>Form {:labelCol {:span 6}
             :wapperCol {:span 16}}
      [:> js/antd.Form.Item {:label "物料id"}
       [>Select {:style {:width 120}}]]
      ;;
      [:> js/antd.Form.Item {:label "初始价格"}
       [>InputNumber  {:disabled true}]]

      ;;
      [:> js/antd.Form.Item {:label "生效价格"}
       [>InputNumber]]
      ;;
      [:> js/antd.Form.Item {:label "修改原因"}
       [>Select {:style {:width 120}}
        [:> js/antd.Select.Option {:value 1}  "定期更新"]
        [:> js/antd.Select.Option {:value 2}  "最新询价"]
        [:> js/antd.Select.Option {:value 3}  "其他"]]]

      [:> js/antd.Form.Item {:label "修改说明"}
       [:> js/antd.Input.TextArea  {:style {:width 320}}]]

      [:> js/antd.Form.Item {:label "上传相关审批单"}
       [:> js/antd.Upload
        [>Button {}  [:> js/antd.Icon {:type "upload"}]]]]

      [:> js/antd.Form.Item {:label "价格变更人"}
       [:label   "集团财务"]]





      ;;
      ]]))

(def Table-columns-price
  [{:key "goods_id" :dataIndex "goods_id" :title "物料id"}
   {:key "goods_name" :dataIndex "goods_name" :title "物料名称"}
   {:key "price_available" :dataIndex "price_available" :title "生效价格"}
   {:key "last_buy_price" :dataIndex "last_buy_price" :title "最后一次买入价"}
   {:key "last_sell_price" :dataIndex "last_sell_price" :title "最后一次卖出价"}
   {:key "mean_price" :dataIndex "mean_price" :title "平均价"}])

(def Table-columns-price-log
  [{:key "price_id" :dataIndex "price_id" :title "关联价格id"}
   {:key "event_title" :dataIndex "event_title" :title "事件名称"}
   {:key "event_publisher" :dataIndex "event_publisher" :title "价格变更登记者"}
   {:key "last_buy_price" :dataIndex "last_buy_price" :title "合同编号"}
   {:key "last_sell_price" :dataIndex "last_sell_price" :title "合同编号"}])

(defn Table-price [page-state]
  (r/with-let [all-order (subscribe [:item/all :price-index])
               vis (r/cursor page-state [:input-price :vis])]
    [:div
     [>Button  {:on-click #(reset! vis true)}  "修改材料价格"]
     [>Table  {:dataSource  (or @all-order (:prices  fake-data))
               :columns  Table-columns-price
               :rowSelection {:on-change (fn [i item]
                                           (println (js->clj item) "==========" i))}}]]))

(defn Table-price-log [page-state]
  (r/with-let []
    [:div

     ;;
     ]))

(defn home-page [state & _]
  (r/with-let [active-page (:active-page state)
               page (:page-state state)
               page-state (r/atom {})]

    [>Layout
     [Modal-input-new-price page-state]
     [hpc/head state
      [hpc/nav state]]
     [>Layout {:style {:padding "24px"}}

      [>Content {:style {:background "#fff"
                         :padding 24
                         :margin 0
                         :minHeight 280}}

       [Table-price page-state]

       ;;
       ]]

     [hpc/foot state]]))



