(ns soul-talk.page.table-manager
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

(def page-state (r/atom
                 {:attrib_list #{}
                  :cache-table " "
                  :selected-table ""
                  :selected-field ""
                  :cache {:selected-table ""}}))

(def selected-table (r/cursor page-state [:selected-table]))
(def selected-field (r/cursor page-state [:selected-field]))

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

(defn form-table-selector []
  (r/with-let [table-names  (subscribe [:table/table-names])]
    (fn []
      (let [tb-cache  (r/cursor page-state [:cache :selected-table])]

        [:div
         [:> js/antd.Input
          {:on-change #(let [v (-> % .-target .-value)]
                         (reset! tb-cache v))
           :placeholder @tb-cache}]
         [:>  js/antd.List
          {:dataSource (filter  #(utils/word-filter % @tb-cache)   @table-names)
           :renderItem #(let [n   %]
                          (r/as-element
                           [:> js/antd.List.Item
                            {:on-click  (fn [x] (do
                                                  (reset! selected-table n)
                                                  (reset! tb-cache  n)))}
                            n]))}]]
        ;;
        ))))

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

      [:div
       [form-table-selector]]]]))


;; 录入table 内容

;;(subscribe  [:table/table.fields :erp_goods :goods_id]  )


(defn card-field-detail []
  (r/with-let [field-detail
               (fn [table field] (subscribe [:table/table.fields table  field]))]
    (fn []
      (let [field-origin   (field-detail
                     (keyword  @selected-table) (keyword @selected-field))
            field-edit  (-> @field-origin
                            r/atom
                            )
            ]
        (reset! selected-field "goods_id")
        [:> js/antd.Card
         [:div [:label    ]  [:label    ]  ]



          ]))))

(defn form-field-add-attrib []
  (r/with-let []
    (fn []
      (let [edit-field  (-> {:attrib-name ""
                             :attrib-value ""}
                            r/atom)
            attrib-name  (r/cursor edit-field [:attrib-name])
            attrib-value (r/cursor edit-field [:attrib-value])
            ;;
            ]

        [:> js/antd.Form
         [:> js/antd.Input
          {:on-change   #(let [v (-> % .-target .-value)]
                           (reset! attrib-name v))
           :placeholder "请输入属性名"}]
         [:> js/antd.Input
          {:on-change #(let [v (-> % .-target .-value)]
                         (reset! attrib-value v))
           :placeholder "请输入属性值"}]
         [:> js/antd.Button
          {:on-click  #(println "dddd")
           :type "primary"}
          "确定输入"]
         ;;
         ]
        ;;
        ;;
        ))))





;; 录入columns内容
;; 录入表关系内容


(defn form-table-add-attrib []
  (r/with-let []
    (fn []
      (let [edit-table  (-> {:attrib-name ""
                             :attrib-value ""}
                            r/atom)
            attrib-name  (r/cursor edit-table [:attrib-name])
            attrib-value (r/cursor edit-table [:attrib-value])
            ;;
            ]

        [:> js/antd.Form
         [:> js/antd.Input
          {:on-change   #(let [v (-> % .-target .-value)]
                           (reset! attrib-name v))
           :placeholder "请输入属性名"}]
         [:> js/antd.Input
          {:on-change #(let [v (-> % .-target .-value)]
                         (reset! attrib-value v))
           :placeholder "请输入属性值"}]
         [:> js/antd.Button
          {:on-click  #(println "dddd")
           :type "primary"}
          "确定输入"]
           ;;
         ]
          ;;
        ;;
        ))))

(defmethod  content
  [:table :config]
  [db]
  (let []
    (fn []
      [:> js/antd.Layout.Content
       [:> js/antd.Row
        [:> js/antd.Col {:span 11}
         [:p (str @page-state)]
         ;;[table-head-input-form]
         [form-table-add-attrib]
         ;;[form-table-selector]
         [:> js/antd.Row
          [:> js/antd.Col
           [card-field-detail]
           ;;[table-field-tables (data @selected-table)]
           ]]
         [:p]
         [:> js/antd.Row
          [:> js/antd.Col
           ;;[field-list (table-data  @selected-table)]
           ]]]
        [:> js/antd.Col {:span 11}]]])))


