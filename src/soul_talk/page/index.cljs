(ns soul-talk.page.index
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.global_components :as gbc]
   [soul-talk.utils :as utils]
   ))

(.locale js/moment  "zh-cn")

(defn order-calendar-form []
  [:> js/antd.Row

   [:> js/antd.Col

    [:> js/antd.Calendar  {:fullscreen false
                           :mode "month"}]]])

(defn side-bar []
  [:p])

(defn all-order-card []
  (r/with-let [all-order (subscribe [:sell-order/all])
               all-order-count (subscribe [:sell-order/all.count_orders])
               all-order-money (subscribe [:sell-order/all.sum_tax_money])
               all-order-weight (subscribe [:sell-order/all.order_detail_weight])
               all-order-distribution  (subscribe [:sell-order/all.job_order_weight])
               all-order-plan (subscribe [:sell-order/all.flow_plan_release])
               all-order-final (subscribe [:sell-order/all.flow_final_weight])
               all-energy (subscribe [:energy.oa_report/energy.values])]
    (let []
      [:> js/antd.Card
       {:title  "集团公司"

        :bodyStyle    {:height "240px"
                       :overflow "hidden"}
        :style        {:margin 5}
        :hoverable    true}
       [:div

        [:> js/antd.Row
         [:> js/antd.Col {:span 8}
          [:p "订单数量"]
          [:p @all-order-count]]

         [:> js/antd.Col {:span 8}
          [:p "订单金额"]
          [:p @all-order-money]]

         [:> js/antd.Col {:span 8}
          [:p "订单需求量"]
          [:p @all-order-weight]]]

        [:> js/antd.Row
         [:> js/antd.Col {:span 8}
          [:p "分配数量"]
          [:p @all-order-distribution]]

         [:> js/antd.Col {:span 8}
          [:p "计划生产重量"]
          [:p @all-order-plan]]
         [:> js/antd.Col {:span 8}
          [:p "完工重量"]
          [:p @all-order-final]]]

        [:> js/antd.Row
         [:> js/antd.Col {:span 8}
          [:p "用电量"]
          [:p (:energy_electric @all-energy)]]

         [:> js/antd.Col {:span 8}
          [:p "用水量"]
          [:p  (:energy_water @all-energy)]]
         [:> js/antd.Col {:span 8}
          [:p "蒸汽量"]
          [:p (:energy_steam @all-energy)]]]]])))

(defn tai_an-card []
  (r/with-let
    [;; 泰安
     tai_an_order (subscribe [:sell-order/tai_an])
     tai_an-order-count (subscribe [:sell-order/tai_an.count_orders])
     tai_an-order-money (subscribe [:sell-order/tai_an.sum_tax_money])
     tai_an-order-weight (subscribe [:sell-order/tai_an.order_detail_weight])
     tai_an-order-distribution  (subscribe [:sell-order/tai_an.job_order_weight])
     tai_an-order-plan (subscribe [:sell-order/tai_an.flow_plan_release])
     tai_an-order-final (subscribe [:sell-order/tai_an.flow_final_weight])]
    [:> js/antd.Card
     {:title  "泰安智能染色工厂"
      :bodyStyle    {:height "240px"
                     :overflow "hidden"}
      :style        {:margin 5}
      :hoverable    true}
     [:div

      [:> js/antd.Row
       [:> js/antd.Col {:span 8}
        [:p "订单数量"]
        [:p @tai_an-order-count]]

       [:> js/antd.Col {:span 8}
        [:p "订单金额"]
        [:p @tai_an-order-money]]

       [:> js/antd.Col {:span 8}
        [:p "订单需求量"]
        [:p @tai_an-order-weight]]]

      [:> js/antd.Row
       [:> js/antd.Col {:span 8}
        [:p "分配数量"]
        [:p @tai_an-order-distribution]]

       [:> js/antd.Col {:span 8}
        [:p "计划生产重量"]
        [:p @tai_an-order-plan]]
       [:> js/antd.Col {:span 8}
        [:p "完工重量"]
        [:p @tai_an-order-final]]]]]))

(defn xin_tai-card []
  (r/with-let [xin_tai_order (subscribe [:sell-order/xin_tai])
               xin_tai-order-count (subscribe [:sell-order/xin_tai.count_orders])
               xin_tai-order-money (subscribe [:sell-order/xin_tai.sum_tax_money])
               xin_tai-order-weight (subscribe [:sell-order/xin_tai.order_detail_weight])
               xin_tai-order-distribution (subscribe [:sell-order/xin_tai.job_order_weight])
               xin_tai-order-plan (subscribe [:sell-order/xin_tai.flow_plan_release])
               xin_tai-order-final (subscribe [:sell-order/xin_tai.flow_final_weight])]
    [:> js/antd.Card
     {:title  "新泰智能染色工厂"

      :bodyStyle    {:height "240px"
                     :overflow "hidden"}
      :style        {:margin 5}
      :hoverable    true}

     [:div

      [:> js/antd.Row
       [:> js/antd.Col {:span 8}
        [:p "订单数量"]
        [:p @xin_tai-order-count]]

       [:> js/antd.Col {:span 8}
        [:p "订单金额"]
        [:p @xin_tai-order-money]]

       [:> js/antd.Col {:span 8}
        [:p "订单需求量"]
        [:p @xin_tai-order-weight]]]

      [:> js/antd.Row
       [:> js/antd.Col {:span 8}
        [:p "分配数量"]
        [:p @xin_tai-order-distribution]]

       [:> js/antd.Col {:span 8}
        [:p "计划生产重量"]
        [:p @xin_tai-order-plan]]
       [:> js/antd.Col {:span 8}
        [:p "完工重量"]
        [:p @xin_tai-order-final]]]]]

    ;;
    ))

(defn factory-card []
  (r/with-let
    []

    (let []
      [:> js/antd.Layout.Content
       [:> js/antd.Row {:gutter 10}
        [:> js/antd.Col {:xs 24 :sm 24 :md 6 :lg 8}
         [all-order-card]]

        [:> js/antd.Col {:xs 24 :sm 24 :md 6 :lg 8}
         [tai_an-card]]

        [:> js/antd.Col {:xs 24 :sm 24 :md 6 :lg 8}
         [xin_tai-card]]]])))

(def columns
  (let [sample-keys
        [;;
         :contract_number
         :order_number
         :customer_name
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
                           (clj->js columns)}]])))

(defn storeage-card []
  (r/with-let []
    [:> js/antd.Card
     {:title  "泰安智能染色工厂"
      :bodyStyle    {:height "240px"
                     :overflow "hidden"}
      :style        {:margin 5}
      :hoverable    true}
     [:div]]))

(defn  customer-card []
  (r/with-let []
    [:> js/antd.Card
     {:title  "泰安智能染色工厂"
      :bodyStyle    {:height "240px"
                     :overflow "hidden"}
      :style        {:margin 5}
      :hoverable    true}
     [:div]]))





(defn cost-dashboard []
  (r/with-let []
    [:div
     [:> js/antd.Row {:gutter 16}
      [:> js/antd.Col
       [order-calendar-form]]
      ;;
      ]
     [:> js/antd.Divider]

     ;; 客户
     [:> js/antd.Row
      [:> js/antd.Col  {:span 6}  "dd"]
      [:> js/antd.Col {:span 6} "dd"]
      [:> js/antd.Col {:span 6} "dd"]
      [:> js/antd.Col  {:span 6}  "dd"]]
     [:hr]

     ;; 已接订单

     [:> js/antd.Row
      [:> js/antd.Col  {:span 6}  "dd"]
      [:> js/antd.Col {:span 6} "dd"]
      [:> js/antd.Col {:span 6} "dd"]
      [:> js/antd.Col  {:span 6}  "dd"]]

     ;; 已分配订单


     [:> js/antd.Row
      [:> js/antd.Col  {:span 6}  "dd"]
      [:> js/antd.Col {:span 6} "dd"]
      [:> js/antd.Col {:span 6} "dd"]
      [:> js/antd.Col  {:span 6}  "dd"]]

     ;; 已投产订单

     [:> js/antd.Row
      [:> js/antd.Col  {:span 6}  "dd"]
      [:> js/antd.Col {:span 6} "dd"]
      [:> js/antd.Col {:span 6} "dd"]
      [:> js/antd.Col  {:span 6}  "dd"]]]))

(defn cost-content []
  (r/with-let []
    [:> js/antd.Layout.Content
     [:> js/antd.Row {:gutter 16}
      [:> js/antd.Col {:span 19}
       [factory-card]
       [:p]
       [:p]
       [detail-board]]
      [:> js/antd.Col {:span 5}
       [cost-dashboard]

       ;;
       ]]]))



(defn home-page []
  (r/with-let [active (subscribe [:active-page])]
    (fn []
      [:> js/antd.Layout
       [gbc/head [gbc/nav ]]
       [:> js/antd.Layout {:style {:padding "24px"}}
        [side-bar ]

        [:> js/antd.Layout.Content {:style {:background "#fff"
                                            :padding 24
                                            :margin 0
                                            :minHeight 280}}
         [cost-content]]]
       [gbc/foot  ]])))




;; (defn home-page []
;;   (r/with-let [active (subscribe [:active])]
;;     (layout/layout
;;      gbc/head
;;      gbc/nav
;;      side-bar
;;      ;;layout/content
;;      cost-content
;;      gbc/foot)))


