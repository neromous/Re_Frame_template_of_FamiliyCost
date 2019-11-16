(ns soul-talk.components.default-layout
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.base-layout :refer [content header nav footer siderbar]]
            [soul-talk.route.utils :refer [logged-in?
                                           context-url
                                           href
                                           navigate!
                                           run-events
                                           run-events-admin]]))

(defmethod header
  :default
  [db nav]

  (r/with-let [active-page (subscribe [:active])]

    [:> js/antd.Layout.Header
     [:> js/antd.Row
      [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8
                       :on-click #(navigate! "#/")}

       [:h1 "测试管理系统"]]
      [:> js/antd.Col {:xs 24 :sm 24 :md 16 :lg 16
                       :style {:text-align "right"}}
       nav]]]))

(defmethod nav
  :default
  [db]
  (r/with-let [active-page (subscribe [:active])]

    [:> js/antd.Menu {:className         "home-nav"
                      :mode              "horizontal"
                      :theme "dark"
                      :defaultSelectKeys ["home"]
                      :selectedKeys      [(key->js active-page)]}
     [:> js/antd.Menu.Item {:key      "home"
                            :on-click #(navigate! "#/")}
      "首页"]]))

(defmethod footer
  :default
  [db]
  (r/with-let [active-page (subscribe [:active])]

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
       " and JIESOUL "]]]))

(defmethod siderbar
  :default
  [db]
  (r/with-let [active-page (subscribe [:active])]

    [:> js/antd.Layout.Sider {:className "sidebar"}
     [:> js/antd.Menu {:mode                "inline"
                       :className            "sidebar"
                       :theme               "light"
                       :default-select-keys ["user"]
                       :default-open-keys   ["account" "user"]
                       :selected-keys       ["sales-area"]}
      [:> js/antd.Menu.Item {:key      "admin"
                             :on-click #(navigate! "#/admin")}
       [:span
        [:> js/antd.Icon {:type "area-chart"}]
        [:span "Dash"]]]
      [:> js/antd.Menu.SubMenu {:key   "account"
                                :title (r/as-element
                                        [:span
                                         [:> js/antd.Icon {:type "form"}]
                                         [:span "账户管理"]])}
       [:> js/antd.Menu.Item {:key      "account-table"
                              :icon     "user"
                              :on-click #(navigate! "#/v/table/index/account")}
        "账户列表"]
       [:> js/antd.Menu.Item {:key      "record-table"
                              :icon     "user"
                              :on-click #(navigate! "#/v/table/index/record")}
        "支出记录"]
       [:> js/antd.Menu.Item {:key      "category-table"
                              :icon     "user"
                              :on-click #(navigate! "#/v/table/index/category")}
        "账户类型管理"]]

      [:> js/antd.Menu.SubMenu {:key   "user"
                                :title (r/as-element
                                        [:span
                                         [:> js/antd.Icon {:type "form"}]
                                         [:span "个人管理"]])}
       [:> js/antd.Menu.Item {:key      "user-profile"
                              :icon     "user"
                              :on-click #(navigate! "#/user-profile")}
        "个人信息"]
       [:> js/antd.Menu.Item {:key      "change-pass"
                              :on-click #(navigate! "#/change-pass")} "密码修改"]]]]))

(defmethod  content
  :default
  [db]
  [:p "页面未找到"])






