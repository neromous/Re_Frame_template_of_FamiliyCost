(ns soul-talk.page.table-relation-input
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.base-layout :refer [content header nav footer siderbar]]
            [soul-talk.components.fields :as fields]
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

(defonce page-state
  (r/atom {:select-table  nil
           :filters-work []
           :table-filter ""
           :relation-store []}))

(def select-table (r/cursor page-state [:select-table]))
(def table-filter (r/cursor page-state [:table-filter]))

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
      [:> js/antd.Input  {:on-change #(reset! table-filter  (-> % .-target .-value))}]
      (when @table-filter
        (doall
         (for [k (filter #(utils/word-filter % @table-filter) @table-names)]
           ^{:key (str "table_seris" "_" k)}
           [:> js/antd.Menu.Item {:key      k
                                  :icon     "user"
                                  :on-click #(reset! select-table  (keyword k))}
            k])))]]))

(def data-types
  ["数字" "类别" "主键" "时间" "时间戳" "字符串" "外键"])

(def columns
  [{:key "table_name"
    :dataIndex "table_name"
    :title "table_name"}
   {:key "column_name"
    :dataIndex "column_name"
    :title "column_name"}
   {:key "data_type"
    :dataIndex "data_type"
    :title "data_type"}
   {:key "column_commet"
    :dataIndex "column_commet"
    :title "column_commet"}])

(defmethod  content
  [:table :config]
  [db]

  (let [data (subscribe [:table/filter-by-table_name select-table])]

    (fn []

      [:> js/antd.Layout.Content
       [:> js/antd.Card
        [:> js/antd.Row {:gutter [16 {:xs 8, :sm 16, :md 24, :lg 32}]}
         [:h2 @select-table]
         [:> js/antd.Col {:span 12}
          



          ]

         [:> js/antd.Col {:span 12}
          [:> js/antd.Table  {:dataSource @data
                              :columns columns}]]]]

       ;;
       ]
      ;;
      )))



;; (doall
;;         (when @select-table
;;           (for [{:keys [table_name
;;                         column_name
;;                         column_type
;;                         data_type
;;                         column_comment] :as field}   @data]

;;             ^{:key (str table_name "_" column_name)}
;;             [:div
;;              [:div
;;               [:> js/antd.Row
;;                [:> js/antd.Col {:span 3}
;;                 [:> js/antd.Input  {:defaultValue column_name}]]

;;                [:> js/antd.Col {:span 2}
;;                 [:> js/antd.Input  {:defaultValue data_type}]]
;;                [:> js/antd.Col {:span 3}
;;                 [:> js/antd.Select
;;                  {:placeholder "选择数据类型"
;;                   :defaultValue @(subscribe [:table/get-field-attrb table_name column_name :field_type])
;;                   :on-change #(dispatch [:table/add-field-attrb table_name column_name :field_type %])
;;                   :style {:width 160}}
;;                  [:> js/antd.Select.Option  {:value ""}  "选择分类"]
;;                  (doall
;;                   (for [data-type data-types]
;;                     ^{:key  (str table_name "_" column_name "_" data-type )  }
;;                     [:> js/antd.Select.Option  {:value data-type} data-type]))]]
;;                [:> js/antd.Col {:span 2}
;;                 [:> js/antd.Input  {:defaultValue column_comment}]]
;;                [:> js/antd.Col  {:span 1}
;;                 [:> js/antd.Icon {:type "form"}]]]]]


;;             )
;;           ))
