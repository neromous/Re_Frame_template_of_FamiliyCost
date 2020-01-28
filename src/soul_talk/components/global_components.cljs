(ns soul-talk.components.global_components
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.util.route-utils :refer [logged-in?
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
        [:div]]])

(defn head [nav]

      [:> js/antd.Layout.Header
       [:> js/antd.Row
        [:> js/antd.Col {:xs 24 :sm 24 :md 2 :lg 4
                         :on-click #(navigate! "#/")}

         [:h1 "康平纳成本管理系统"]]
        [:> js/antd.Col {:xs 24 :sm 24 :md 16 :lg 16
                         :style {:text-align "left"}}
         nav]]])

(defn nav []
        [:> js/antd.Menu {:className         "home-nav"
                          :mode              "horizontal"
                          :theme "dark"
                          :defaultSelectKeys ["home"]
                          :selectedKeys      []}

         [:> js/antd.Menu.Item {:key      "cost"
                                :on-click #(navigate! "#/home/index")}
          "集团 1+N "]



         [:> js/antd.Menu.Item {:key      "order-track"
                                :on-click #(navigate! "#/v/order/track")}
          "客户信息"]
         [:> js/antd.Menu.Item {:key      "order-track"
                                :on-click #(navigate! "#/v/order/track")}
          "物料信息"]
         [:> js/antd.Menu.Item {:key      "order-track"
                                :on-click #(navigate! "#/v/order/track")}
          "供应商信息"]
         [:> js/antd.Menu.Item {:key      "order-track"
                                :on-click #(navigate! "#/home/index/detail")}
          "订单明细"]



         ])

(defn content []
      [:p])

(defn foot []
      [:> js/antd.Layout.Footer {:style {:text-align "center"
                                         :background "#3e3e3e"}}
       [:> js/antd.Row
        [:h4 {:style {:color "#FFF"}}
         "Made with By "
         [:a
          {:type   "link"
           :href   "https://ant.design"
           :target "_blank"}
          "Ant Design"]
         " and JIESOUL "]]])
