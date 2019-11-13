(ns soul-talk.components.siderbar
  (:require             [reagent.core :as r]
                        [soul-talk.routes :refer [
                                                 logged-in?
                                                 context-url
                                                 href
                                                 navigate!
                                                 run-events
                                                 run-events-admin
                                                 ]]
 
                        [re-frame.core :as rf]))

(defn siderbar []
  (r/with-let []

    (fn []
      [:> js/antd.Layout.Sider {:className "sidebar"}
       [:> js/antd.Menu {:mode                "inline"
                         :className            "sidebar"
                         :theme               "light"
                         :default-select-keys ["admin"]
                         :default-open-keys   ["blog" "user"]
                     ;:selected-keys       [(key->js @active-page)]
                         }
        [:> js/antd.Menu.Item {:key      "admin"
                               :on-click #(navigate! "#/admin")}
         [:span
          [:> js/antd.Icon {:type "area-chart"}]
          [:span "Dash"]]]
        [:> js/antd.Menu.SubMenu {:key   "sales-main"
                                  :title (r/as-element [:span
                                                        [:> js/antd.Icon {:type "form"}]
                                                        [:span "销售客户分析母表"]])}
         [:> js/antd.Menu.Item {:key      "sales-area"
                                :on-click #(navigate! "#/sales-main")}
          "母表"]
         [:> js/antd.Menu.Item {:key      "sales-customer"
                                :on-click #(navigate! "#/sales")}
          "客户分析表"]
         [:> js/antd.Menu.Item {:key      "sales-detail"
                                :on-click #(navigate! "#/sales-place")}
          "地区分析表"]]

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
                                :on-click #(navigate! "#/change-pass")} "密码修改"]]]])))
(defn sidebar-basic [active-page]
  [:> js/antd.Layout.Sider {:className "sidebar"}
   [:> js/antd.Menu {:mode                "inline"
                     :className            "sidebar"
                     :theme               "light"
                     :default-select-keys ["admin"]
                     :default-open-keys   ["blog" "user"]
                     :selected-keys       [(key->js @active-page)]}
    [:> js/antd.Menu.Item {:key      "admin"
                           :on-click #(navigate! "#/admin")}
     [:span
      [:> js/antd.Icon {:type "area-chart"}]
      [:span "Dash"]]]
    [:> js/antd.Menu.SubMenu {:key   "blog"
                              :title (r/as-element [:span
                                                    [:> js/antd.Icon {:type "form"}]
                                                    [:span "文章管理"]])}
     [:> js/antd.Menu.Item {:key      "categories"
                            :on-click #(navigate! "#/categories")}
      "分类"]
     [:> js/antd.Menu.Item {:key      "posts"
                            :on-click #(navigate! "#/posts")}

      "文章"]]

    [:> js/antd.Menu.SubMenu {:key   "user"
                              :title (r/as-element
                                      [:span
                                       [:> js/antd.Icon {:type "user"}]
                                       [:span "个人管理"]])}
     [:> js/antd.Menu.Item {:key      "user-profile"
                            :icon     "user"
                            :on-click #(navigate! "#/user-profile")}
      "个人信息"]
     [:> js/antd.Menu.Item {:key      "change-pass"
                            :on-click #(navigate! "#/change-pass")} "密码修改"]]]])

