(ns soul-talk.page.metadata
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.global_components :as gbc]
   [soul-talk.components.field :as field]
   [soul-talk.components.label-field :as label-field]
   [soul-talk.utils :as utils]
   [soul-talk.util.model-utils :refer [metadata->dto]]
   [soul-talk.components.antd-dsl
    :refer [>Input
            >InputNumber
            >Col
            >Row
            >List
            >AutoComplete
            >List_Item
            >Table
            >Content
            >Description
            >Description_Item
            >Cascader
            >Button
            >Header
            >Footer
            >Sider
            >Layout]]
   [soul-talk.util.antd-utils :refer [input-value]]
   [soul-talk.util.subs :refer [sub>]]))

(defonce   selected-column (-> {} r/atom))


(def sample (sub> [:metadata/column "erp_goods" "goods_name"]))


(defn table-name-list [table-names]
  (let [tb-cache (r/atom "")]
    (fn []
      [>Row {:gutter 16}
       [>Col {:span 23}
         ;; 搜索框
        [>Row {:gutter 8}
         [>Col {:span 12}
          [>Button {} "添加新表"]]
         [>Col  {:span 12}]]

        [>Input {:on-change  #(reset! tb-cache  (input-value %))
                 :value @tb-cache}]
       ;;下列列表


        [>List
         {:dataSource (filter
                       #(utils/word-filter % (or @tb-cache ""))
                       @table-names)
          :size "small"
          :pagination {:defaultPageSize 20

                       :size "small"}
          :renderItem
          #(let [n   %]
             (r/as-element
              [>List_Item
               {:on-click
                (fn [x] (do
                          (dispatch [:page-state :metadata-index :select-table n])
                          (reset! tb-cache n)))}

               n]))}]]]
        ;;
      )))

(defn  table-columns-table  [select-table column]
  (let [columns  [{:key "column_name"
                   :dataIndex "column_name"
                   :title "字段名"}
                  {:key "data_type"
                   :dataIndex "data_type"
                   :title "数据类型"}
                  {:key "intel_type"
                   :dataIndex "intel_type"
                   :title "信息类型"}]]
    (fn []
      [>Table
       {:dataSource  (clj->js @select-table)
        :columns (clj->js columns)
        :onRow (fn [record index]
                 (let [item    (-> record
                                   js->clj
                                   (get "column_name"))]
                   #js {:onClick
                        (fn []
                          (doall
                           (dispatch
                            [:page-state :metadata-index :select-column
                             (js->clj   item)]))
                          (reset! selected-column {}))}))}]

      ;;
      )))

(defn columns-edit-form [select-column  column]
  (let [table_name (r/cursor column [:table_name])
        column_name (r/cursor column [:column_name])
        column_type (r/cursor column [:column_type])
        data_type (r/cursor column [:data_type])
        column_default (r/cursor column [:column_default])
        value-fn  (fn []  (merge @select-column @column))
        df-fn  (fn [] (:column_default (value-fn)))
        df-in-fn #(reset! column_default (input-value %))]

    (fn []
      [:div {:style {:padding 8}}

       [>Input {:on-change #(reset! table_name  (input-value %))
                :value (:table_name (value-fn))
                :addonBefore (r/as-element [:label "实体名称"])}]
       [>Input {:on-change #(reset! column_name (input-value %))
                :value (:column_name (value-fn))
                :addonBefore (r/as-element [:label "字段名称"])}]

       [>Input {:on-change #(reset! column_type (input-value %))
                :value (:column_type (value-fn))
                :addonBefore (r/as-element [:label "字段类型"])}]

       [>Input {:on-change #(reset! data_type (input-value %))
                :value (:data_type (value-fn))
                :addonBefore (r/as-element [:label "数据类型"])}]

       [>Input {:on-change df-in-fn
                :value (df-fn)
                :addonBefore (r/as-element [:label "默认值"])}]
       ;;[>Cascader  {:options (sub>  [:metadata/cascader.option])}]


       [>Button
        {:on-click
         (fn []
           (let []
             (doall
              (dispatch [:metadata/update  {:table_name  (:table_name (value-fn))
                                            :column_name (:column_name (value-fn))}
                         (value-fn)])
              (reset! selected-column {}))
             ;;
             ))}

        "点击修改"]])))

(defn add-columns-form []
  (let [column (r/atom {})
        table-names (subscribe [:metadata/all.table_names])
        form-state (r/atom {})

        table_name (r/cursor column [:table_name])
        column_name (r/cursor column [:column_name])
        column_type (r/cursor column [:column_type])
        data_type (r/cursor column [:data_type])
        column_default (r/cursor column [:column_default])
        value-fn  (fn []  @column)
        column-list (fn [table_name]
                      (map :column_name (sub>  [:metadata/table.columns @table_name])))]

    (fn []
      [:div

       [:p (str @form-state)]

       [:p (str (column-list table_name))]
       [>Row {:gutter 16}
       [>Row 
        [>Col {:span 11}
         [>AutoComplete {:on-change #(reset! table_name  %)
                         :value (:table_name (value-fn))
                         :placeholder "输出或选择表名"
                         :defaultActiveFirstOption true
                         :dataSource (filter
                                      #(utils/word-filter % (or @table_name ""))
                                      @table-names)}]
         ]
        [>Col {:span 11}

         [>AutoComplete {:on-change #(reset! column_name  %)
                         :value (:column_name (value-fn))
                         :defaultActiveFirstOption true
                         :placeholder "输入或选择字段名"
                         :dataSource (filter
                                      #(utils/word-filter % (or @column_name ""))
                                      (column-list table_name))}]



         ]]

        [label-field/label-field sample form-state  ]

       ;; [>Input {:on-change #(reset! table_name  (input-value %))
       ;;          :value (:table_name (value-fn))
       ;;          :addonBefore (r/as-element [:label "实体名称"])}]

       ;; [>Input {:on-change #(reset! column_name (input-value %))
       ;;          :value (:column_name (value-fn))
       ;;          :addonBefore (r/as-element [:label "字段名称"])}]


       [>Input {:on-change #(reset! column_type (input-value %))
                :value (:column_type (value-fn))
                :addonBefore (r/as-element [:label "字段类型"])}]

       [>Input {:on-change #(reset! data_type (input-value %))
                :value (:data_type (value-fn))
                :addonBefore (r/as-element [:label "数据类型"])}]

       [>Input {:on-change #(reset! column_default (input-value %))
                :value (:column_default (value-fn))
                :addonBefore (r/as-element [:label "默认值"])}]

       [>Button
        {:on-click
         (fn []
           (let []
             (doall
              (dispatch [:metadata/new (metadata->dto (value-fn))])

              ;;
              )))}

        "新增"]]
       ]

       )))

(defn home-page []
  (r/with-let
    [select-column (subscribe [:metadata/select-column])
     select-table (subscribe  [:metadata/select-table])
     table-names (subscribe [:metadata/all.table_names])
     page-state [:page-state :metadata-index]]
    (fn []
      [>Layout
       [>Header]
       [>Layout
        [>Row {:gutter 8}
         [>Col {:span 3}
          [table-name-list table-names]]
         [>Col {:span 15}
          [table-columns-table select-table]]
         [>Col {:span 5}
          [columns-edit-form  select-column selected-column]
          [:> js/antd.Divider]
          [add-columns-form]]]]
       [>Footer]]
        ;;
      )))

;; (defn home-page
;;   []
;;   [:div
;;    [content]])
