(ns soul-talk.layouts.home-layout
  (:require
   [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.utils :as utils]
   [reagent.core :as r]
   [re-frame.core :as rf]))

(defn form-table-selector []
  (r/with-let [table-names  (subscribe [:table/table-names])
               selected-table  (subscribe [:table/selected-table])
               page-state (subscribe [:table/page-state])
               selected-field (subscribe [:table/selected-field])]
    (let [tb-cache  (:tb-cache @page-state)]

      [:div
       [:> js/antd.Input
        {:on-change #(let [v (-> % .-target .-value)]
                         ;;(reset! tb-cache v)
                       (dispatch [:table/page-state {:tb-cache v}]))
         :placeholder tb-cache}]
       [:>  js/antd.List
        {:dataSource (filter  #(utils/word-filter % (or tb-cache ""))   @table-names)
         :renderItem #(let [n   %]
                        (r/as-element
                         [:> js/antd.List.Item
                          {:on-click
                           (fn []
                             (dispatch [:table/page-state {:tb-cache n
                                                           :selected-table (keyword n)}]))}

                          n]))}]]
        ;;
      )))

(defn table-side-bar
  []
  (r/with-let [table-names (subscribe [:table/table-names])
               page-state (subscribe [:table/page-state])
               selected-table  (subscribe [:table/selected-table])]
    (let [tb-cache  (:tb-cache @page-state)]
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

        [:div
         [form-table-selector]]]])))

(defn default-table-side-bar [nav]
  (r/with-let []
    (let []
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

        [:div]]])))

(defn table-top-head [nav]
  (r/with-let []
    (let []
      [:> js/antd.Layout.Header
       [:> js/antd.Row
        [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8
                         :on-click #(navigate! "#/")}

         [:h1 "测试管理系统"]]
        [:> js/antd.Col {:xs 24 :sm 24 :md 16 :lg 16
                         :style {:text-align "right"}}
         nav]]])))

(defn table-top-nav []
  (r/with-let [active (subscribe [:active])]
    (let [active-page (:page @active)]
      [:> js/antd.Menu {:className         "home-nav"
                        :mode              "horizontal"
                        :theme "dark"
                        :defaultSelectKeys ["home"]
                        :selectedKeys      [(key->js active-page)]}
       [:> js/antd.Menu.Item {:key      "home"
                              :on-click #(navigate! "#/")}
        "首页"]])))

(defn table-content []
  (r/with-let []
    (let []
      [:p])))

(defn table-foot []
  (r/with-let []
    (let []
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
         " and JIESOUL "]]])))

(defn  table-home-page [head nav sider content foot]
  (r/with-let [active (subscribe [:active])]
    [:> js/antd.Layout
     [head [nav]]
     [:> js/antd.Layout
      [sider]
      [:> js/antd.Layout.Content
       [content]]]
     [foot]]))

(defn table-field-detail-layout [head nav sider content foot]
  (r/with-let []
    [:> js/antd.Layout
     [head [nav]]
     [:> js/antd.Layout {:style {:padding "24px"}}
      [sider]

      [:> js/antd.Layout.Content {:style {:background "#fff"
                                          :padding 24
                                          :margin 0
                                          :minHeight 280}}

       [content]]]

     [foot]]))



