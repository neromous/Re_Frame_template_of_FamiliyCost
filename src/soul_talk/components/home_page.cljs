(ns soul-talk.components.home-page
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.antd-dsl :refer [>Input
                                                   >InputNumber
                                                   >Col
                                                   >Row
                                                   >List
                                                   >AutoComplete
                                                   >Table
                                                   >Content
                                                   >Description
                                                   >Cascader
                                                   >Button
                                                   >Header
                                                   >Footer
                                                   >Sider
                                                   >Layout
                                                   >Content]]

            [soul-talk.util.route-utils :refer [logged-in?
                                                context-url
                                                href
                                                navigate!
                                                run-events
                                                run-events-admin]]))

(defn side-bar [state]
  [>Sider {:className "sidebar"}
   [:> js/antd.Menu {:mode                "inline"
                     :className            "sidebar"
                     :theme               "light"
                     :default-select-keys ["user"]
                     :default-open-keys   ["account" "user"]
                     :selected-keys       ["sales-area"]}
    [:div]]])

(defn head [state  nav]

  [>Header
   [>Row
    [>Col {:xs 24 :sm 24 :md 2 :lg 4
           :on-click #(navigate! "#/")}
     [:h1 "康平纳成本管理平台"]]
    [>Col {:xs 24 :sm 24 :md 16 :lg 16
           :style {:text-align "left"}}
     nav]]])

(defn nav [state]
  [:> js/antd.Menu {:className         "home-nav"
                    :mode              "horizontal"
                    :theme "dark"
                    :defaultSelectKeys ["home"]
                    :selectedKeys      []}

   [:> js/antd.Menu.Item {:key      "index"
                          :on-click #(navigate! "#/todo-index")}
    "待办事项 "]
   [:> js/antd.Menu.Item {:key      "index"
                          :on-click #(navigate! "#/product-track")}
    "订单汇总页"]
   [:> js/antd.Menu.Item {:key      "index"
                          :on-click #(navigate! "#/product-detail/1")}
    "订单明细"]




   ])

(defn content []
  [:p])

(defn foot [state]
  [>Footer {:style {:text-align "center"
                    :background "#3e3e3e"}}
   [>Row
    [:h4 {:style {:color "#FFF"}}
     "Made with By "
     [:a
      {:type   "link"
       :href   "https://ant.design"
       :target "_blank"}
      "Ant Design"]
     " and  "]]])
