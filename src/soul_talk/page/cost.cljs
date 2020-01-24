(ns soul-talk.page.cost
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.fields :as fields]
            [soul-talk.components.tables :as tables]
            [soul-talk.components.columns :as columns]
            [soul-talk.date-utils :as du]
            [soul-talk.layout.cost :as layout]
            [soul-talk.components.global_components :as gbc]
            [soul-talk.utils :as utils]
            [soul-talk.route.utils :refer [logged-in?
                                           context-url
                                           href
                                           navigate!
                                           run-events
                                           run-events-admin]]))



(defn side-bar []
  
  [:> js/antd.Layout.Sider {:className "sidebar"}
   [:> js/antd.Menu {:mode                "inline"
                     :className            "sidebar"
                     :theme               "light"
                     :default-select-keys ["user"]
                     :default-open-keys   ["account" "user"]
                     :selected-keys       ["sales-area"]}
    [:> js/antd.Menu.Item {:key      "admin"
                           :on-click
                           #(dispatch [:table/page-state {:tb-cache ""}])}
     [:span
      [:> js/antd.Icon {:type "area-chart"}]

      [:span
       "Dash"]]]

    [:div]]])



(defn factory-card []
  (r/with-let [page-state (subscribe [:cost.index/state])
               all-order (subscribe [:sell-order/all])
               all-order-count (subscribe [:sell-order/all.count_orders])
               all-order-money (subscribe [:sell-order/all.sum_tax_money])
               tai_an_order (subscribe [:sell-order/tai_an])
               tai_an-order-count (subscribe [:sell-order/tai_an.count_orders])
               tai_an-order-money (subscribe [:sell-order/tai_an.sum_tax_money])
               xin_tai_order (subscribe [:sell-order/xin_tai])
               xin_tai-order-count (subscribe [:sell-order/xin_tai.count_orders])
               xin_tai-order-money (subscribe [:sell-order/xin_tai.sum_tax_money])]
    (let []
      [:> js/antd.Layout.Content
       [:> js/antd.Row {:gutter 10}
        [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8}
         [:> js/antd.Card
          {:title  "集团公司"

           :bodyStyle    {:height "150px"
                          :overflow "hidden"}
           :style        {:margin 5}
           :hoverable    true}

          [:> js/antd.Row
           [:> js/antd.Col {:span 6}
            [:p "订单数量"]
            [:p @all-order-count]]

           [:> js/antd.Col {:span 6}
            [:p "订单金额"]
            [:p @all-order-money]]]]]

        [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8}
         [:> js/antd.Card
          {:title  "泰安智能染色工厂"

           :bodyStyle    {:height "150px"
                          :overflow "hidden"}
           :style        {:margin 5}
           :hoverable    true}

          [:> js/antd.Row
           [:> js/antd.Col {:span 6}
            [:p "订单数量"]
            [:p  @tai_an-order-count]]

           [:> js/antd.Col {:span 6}
            [:p "订单金额"]
            [:p @tai_an-order-money]]]]]

        [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8}
         [:> js/antd.Card
          {:title  "新泰智能染色工厂"

           :bodyStyle    {:height "150px"
                          :overflow "hidden"}
           :style        {:margin 5}
           :hoverable    true}

          [:> js/antd.Row
           [:> js/antd.Col {:span 6}
            [:p "订单数量"]
            [:p  @xin_tai-order-count]]

           [:> js/antd.Col {:span 6}
            [:p "订单金额"]
            [:p @xin_tai-order-money]]]]]]])))

(def columns
  (let [sample-keys
        [;;
         :contract_number
         :order_number
         :custom_name
         :customer_color
         :color_number
         :order_time
         :finish_time
         :order_detail_weight
         :tax_price
         :tax_money
         ;;:order_detail_id 1,
         ;;:custom_id 57,
         ;;:company_id 3,
         ;;:order_id 1,
         ;;
         ]
        sample-columns (map name sample-keys)]
    (map (fn [x] {:dataIndex x
                  :key x
                  :title x})  sample-columns)))

(defn detail-board []
  (r/with-let [sell-orders  (subscribe  [:sell-order/all])]
    (let []
      [:div
       [:> js/antd.Table  {:dataSource
                           ;;@sell-orders
                           (clj->js @sell-orders)
                           :columns
                           ;;columns
                           (clj->js columns)
                           }]])))
(defn home-page []
  (r/with-let [active (subscribe [:active])]
    (layout/layout
     gbc/head
     gbc/nav
     side-bar
     ;;layout/content
     (fn [] [:p "测试内容"
             [factory-card]
             [detail-board]])

     gbc/foot)))

(subscribe [:energy.oa_report/all])


