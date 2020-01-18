(ns soul-talk.page.table-relation-input
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.base-layout :refer [content header nav footer siderbar]]
            [soul-talk.components.fields :as fields]
            [soul-talk.components.tables :as tables]
            [soul-talk.components.columns :as columns]
            [soul-talk.date-utils :as du]
            [soul-talk.utils :as utils]
            [soul-talk.route.utils :refer [logged-in?
                                           context-url
                                           href
                                           navigate!
                                           run-events
                                           run-events-admin]]))
;; 两件事
;; 第一件事  定义字段
;; 第二件事  定义关系

(def uuid-key (r/atom 1000))
(defn get-uuid-key []
  (swap! uuid-key inc))

(def page-state (r/atom {:attrib_list #{}
                         :cache-table " "}))
(def attrib_list (r/cursor page-state [:attrib_list]))

(def cache-table (r/cursor page-state [:cache-table]))
(def attrib-input (r/cursor page-state [:attrib-input]))

(def selected-table (r/cursor page-state [:selected-table]))
(def selected-field (r/cursor page-state [:selected-field]))
(def tb-attr-cache (r/cursor page-state [:cache :table-kv]))
(def tb-attr-cache-k (r/cursor page-state [:cache :table-kv :k]))
(def tb-attr-cache-v (r/cursor page-state [:cache :table-kv :v]))

(def fd-attr-cache (r/cursor page-state [:cache :field-kv]))
(def fd-attr-cache-k (r/cursor page-state [:cache :field-kv :k]))
(def fd-attr-cache-v (r/cursor page-state [:cache :field-kv :v]))

(defmethod header
  [:table :config]
  [db nav]
  (fn []
    (r/with-let [active-page (subscribe [:active])]

      [:> js/antd.Layout.Header
       [:> js/antd.Row
        [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8
                         :on-click #(navigate! "#/")}

         [:h1 "测试管理系统"]]
        [:> js/antd.Col {:xs 24 :sm 24 :md 16 :lg 16
                         :style {:text-align "right"}}
         nav]]])))

(defmethod nav
  [:table :config]
  [db]
  (fn []
    (r/with-let [active-page (subscribe [:active])]

      [:> js/antd.Menu {:className         "home-nav"
                        :mode              "horizontal"
                        :theme "dark"
                        :defaultSelectKeys ["home"]
                        :selectedKeys      [(key->js active-page)]}
       [:> js/antd.Menu.Item {:key      "home"
                              :on-click #(navigate! "#/")}
        "首页"]])))

(defmethod footer
  [:table :config]
  [db]
  (fn []
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
         " and JIESOUL "]]])))

(defmethod siderbar
  [:table :config]
  [db]
  (r/with-let [table-names (subscribe [:table/table-names])]

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
      [:> js/antd.Input  {:on-change #(reset! cache-table  (-> % .-target .-value))}]
      (when @cache-table
        (doall
         (for [k (filter #(utils/word-filter % @cache-table) @table-names)]
           ^{:key (str "table_seris" "_" k)}
           [:> js/antd.Menu.Item
            {:key      k
             :icon     "user"
             :on-click #(reset! selected-table k)}
            k]))
        ;;
        )]]))

(def data-types
  ["数字" "类别" "主键" "时间" "时间戳" "字符串" "外键"])

(defn table-head-input-form []
  (let [edit-table-head (-> {:attrib ""
                             :value ""}
                            r/atom)
        attrib (r/cursor edit-table-head [:attrib])
        attrib-value (r/cursor edit-table-head [:value])
        table-head-input-fn  (fn []
                               (dispatch [:table/table.table-heads
                                          (keyword  @selected-table)
                                          @attrib
                                          @attrib-value]))]
    [:div
     [:p "请输入属性名"]
     [fields/text-input attrib {}]
     [:p "请输入属性值"]
     [fields/text-input attrib-value {}]
     [:p]
     [:> js/antd.Button  {:on-click table-head-input-fn
                          :type "primary"}  "输入表属性"]]))

(defn field-attrib-input-form []
  (let [edit-table-head (-> {:attrib ""
                             :value ""}
                            r/atom)
        attrib (r/cursor edit-table-head [:attrib])
        attrib-value (r/cursor edit-table-head [:value])]
    [:div
     [:p "请输入字段属性名"]
     [fields/text-input attrib {}]
     [:p "请输入字段属性值"]
     [fields/text-input attrib-value {}]
     [:> js/antd.Button  {:on-click #(println edit-table-head)}]]))

(defn table-field-tables  [table-data]
  (let [columns  [{:key "attrib"
                   :dataIndex "attrib"
                   :title "attrib"}
                  {:key "value"
                   :dataIndex "value"
                   :title "value"}]
        click-input #(dispatch [:table/attrib_name.cache [@attrib-input]])
        attrib-store (r/cursor page-state [:attrib-store])
        attrib_names (subscribe [:table/attrib_name.cache])
        table-head-input-fn  (fn []
                               (dispatch [:table/table.table-heads
                                          (keyword  @selected-table)
                                          @tb-attr-cache-k
                                          @tb-attr-cache-v]))]
    [:div
     [:p]

     [:div
      [:> js/antd.Table
       {:dataSource  @table-data
        :columns columns
        :pagination {:defaultPageSize 8}}]
      ;;
      ]]))

(defn field-list [table-data]

  (let [columns [{:key "column_name"
                  :dataIndex "column_name"
                  :title "column_name"}
                 {:key "table_name"
                  :dataIndex "table_name"
                  :title "table_name"}
                 {:key "data_type"
                  :dataIndex "data_type"
                  :title "data_type"}]]

    [:> js/antd.Table {:columns columns
                       :dataSource @table-data
                       :pagination {:defaultPageSize 5}}]))

(defn field-relation-form [field-data]
  (let []


    ))

(defmethod  content
  [:table :config]
  [db]
  (let [table-data (fn [x]  (subscribe [:table/table.table-fields (keyword x)]))
        data (fn [x]  (subscribe [:table/table.table-heads (keyword x)]))
        field-detail (fn [x y]
                       (subscribe [:table/field.table-field (keyword x) (keyword y)]))]

    (fn []
      [:> js/antd.Layout.Content
       [:> js/antd.Row
        [:> js/antd.Col {:span 11}
         [table-head-input-form]
         [:> js/antd.Row
          [:> js/antd.Col  [table-field-tables (data @selected-table)]]]
         [:p]
         [:> js/antd.Row
          [:> js/antd.Col [field-list (table-data  @selected-table)]]]]
        [:> js/antd.Col {:span 11}]]])))


