(ns soul-talk.components.global_components
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
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

(defn head [nav]

      [:> js/antd.Layout.Header
       [:> js/antd.Row
        [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8
                         :on-click #(navigate! "#/")}

         [:h1 "测试管理系统"]]
        [:> js/antd.Col {:xs 24 :sm 24 :md 16 :lg 16
                         :style {:text-align "right"}}
         nav]]])

(defn nav []
        [:> js/antd.Menu {:className         "home-nav"
                          :mode              "horizontal"
                          :theme "dark"
                          :defaultSelectKeys ["home"]
                          :selectedKeys      [(key->js active-page)]}
         [:> js/antd.Menu.Item {:key      "home"
                                :on-click #(navigate! "#/")}
          "首页"]
         [:> js/antd.Menu.Item {:key      "order-track"
                                :on-click #(navigate! "#/v/order/track")}
          "订单追踪"]
         [:> js/antd.Menu.Item {:key      "cost"
                                :on-click #(navigate! "#/")}
          "成本追踪"]])

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
