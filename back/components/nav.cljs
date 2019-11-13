(ns soul-talk.components.nav
  (:require [soul-talk.routes :refer [navigate!]]

            [re-frame.core :refer [dispatch dispatch-sync subscribe]]

            ))

(defn nav []
  [:> js/antd.Menu
   [:> js/antd.Menu.Item {:key "user-profile"
                          :on-click #(navigate! "#/user-profile")}
    [:> js/antd.Icon {:type "user"}]
    "个人信息"]
   [:> js/antd.Menu.Item {:key "change-pass"
                          :on-click #(navigate! "#/change-pass")}
    [:> js/antd.Icon {:type "setting"}]
    "密码修改"]
   [:> js/antd.Menu.Divider]
   [:> js/antd.Menu.Item {:key      "cancel"
                          :on-click #(dispatch [:logout])}
    [:> js/antd.Icon {:type "poweroff"}]
    "退出登录"]])


(defn nav-basic []
  [:> js/antd.Menu
   [:> js/antd.Menu.Item {:key "user-profile"
                          :on-click #(navigate! "#/user-profile")}
    [:> js/antd.Icon {:type "user"}]
    "个人信息"]
   [:> js/antd.Menu.Item {:key "change-pass"
                          :on-click #(navigate! "#/change-pass")}
    [:> js/antd.Icon {:type "setting"}]
    "密码修改"]
   [:> js/antd.Menu.Divider]
   [:> js/antd.Menu.Item {:key      "cancel"
                          :on-click #(dispatch [:logout])}
    [:> js/antd.Icon {:type "poweroff"}]
    "退出登录"]])

(defn nav-home [active-page]
  [:> js/antd.Menu {:className         "home-nav"
                    :mode              "horizontal"
                    :theme "dark"
                    :defaultSelectKeys ["home"]
                    :selectedKeys      [(key->js active-page)]}
   [:> js/antd.Menu.Item {:key      "home"
                          :on-click #(navigate! "#/")}
    "首页"]])

