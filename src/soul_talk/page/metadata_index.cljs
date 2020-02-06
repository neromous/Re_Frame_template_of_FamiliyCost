(ns soul-talk.page.metadata-index
  (:require
   [reagent.core :as r]
   [reagent.ratom :as ratom]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.field :as field]
   [soul-talk.components.label-field :as label-field]
   [soul-talk.utils :as utils]
   [soul-talk.components.home-page :as hpc]
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

(defonce relation-id (r/atom 1))

(defn table-selector [page-state all-data]
  (r/with-let [selected-table  (r/cursor page-state [:selected-table])]

    [>AutoComplete
     {:on-change  #(reset! selected-table (-> %))
      :filterOption true
      :placeholder "选择实体名称"
      :defaultActiveFirstOption true
      :dataSource  (-> @all-data
                       keys
                       sort)
      ;;
      }]))

(defn column-selector [page-state all-data]
  (r/with-let [selected-table  (r/cursor page-state [:selected-table])
               selected-column  (r/cursor page-state [:selected-column])]

    [>AutoComplete
     {:on-change  #(reset! selected-column (-> %))
      :placeholder "选择字段名"
      :filterOption true
      :defaultActiveFirstOption true
      :dataSource  (-> (get-in @all-data [@selected-table])
                       keys
                       sort)
      ;;
      }]))

(defn fields-table-component [page-state all-data]
  (r/with-let

    [edit-column (r/cursor page-state [:edit-column])
     edit-column (r/cursor page-state [:edit-column])
     selected-table  (r/cursor page-state [:selected-table])
     selected-column  (r/cursor page-state [:selected-column])
     relation-new (r/cursor page-state [:relation-new])
     columns [{:key "column_name"
               :dataIndex "column_name"
               :title "字段名"}
              {:key "data_type"
               :dataIndex "data_type"
               :title "数据类型"}
              {:key "intel_type"
               :dataIndex "intel_type"
               :title "信息类型"}
              {:title  "操作"
               :dataIndex "actions"
               :key "actions"
               :align "center"
               :render  (fn [_ item]
                          (let [item (-> item
                                         (js->clj :keywordize-keys true))]
                            (r/as-element
                             [:div
                              [>Button  {:onClick (fn []
                                                    (swap! edit-column assoc :cache item)
                                                    (swap! edit-column assoc :visible true))}
                               "编辑"]
                              [>Button {:on-click #(swap! relation-new assoc :visible true)}
                               "新关系"]])))}]]

    [:div
     [>Table  {:dataSource (-> @all-data
                               (get-in [@selected-table])
                               vals)

               :columns columns
               :bordered true
               :size "smaill"
               :pagination {:defaultPageSize 5}
               :onRow
               (fn [record index]
                 (let [item (-> record
                                (js->clj :keywordize-keys true))]
                   #js   {:onClick (fn []
                                     (reset! selected-column (:column_name  item))
                                     ;;
                                     )}


                   ;
                   ))}]]

    ;;
    ))

(defn new-table_name-modal [state]
  [:> js/antd.Modal
   {:title "编辑界面"
    :visible (:visible @state)
    :onOk (fn [] (do
                   (dispatch [:metadata/table.new  (:table_name @state)])
                   (swap! state assoc :visible false)))
    :onCancel  (fn [] (do
                        (swap! state assoc :visible false)))}

   [>Input {:on-change  #(swap! state assoc :table_name  (-> % .-target .-value))}]])

(defn new-column-modal [page-state state all-data]
  (r/with-let [data_type_list (subscribe [:metadata/all.data_type])
               view_type_list (subscribe [:metadata/all.view_type])
               entity_type_list (subscribe [:metadata/all.entity_type])
               cache-data (r/cursor state [:cache])
               table_name (r/cursor state [:cache :table_name])
               column_name (r/cursor state [:cache :column_name])
               column_comment (r/cursor state [:cache :column_comment])
               data_type (r/cursor state [:cache :data_type])
               view_type (r/cursor state [:cache :view_type])
               entity_type (r/cursor state [:cache :entity_type])
               selected-table  (r/cursor page-state [:selected-table])
               selected-column  (r/cursor page-state [:selected-column])
               ;;
               ]

    [:> js/antd.Modal
     {:title "新增字段"
      :visible (:visible @state)
      :onOk (fn [] (do
                     (dispatch [:metadata/column.new
                                (or @selected-table @table_name)
                                @column_name
                                @cache-data])
                     (swap! state assoc :visible false)))
      :onCancel  (fn [] (do
                          (reset! cache-data {})
                          (swap! state assoc :visible false)))}
     ;;
     [:div
      [>AutoComplete
       {:on-change  #(reset! table_name (-> %))
        :filterOption true
        :value (or @selected-table @table_name)
        :defaultValue @selected-table
        :placeholder "选择实体名称"
        :defaultActiveFirstOption true
        :dataSource  (-> @all-data
                         keys
                         sort)}]
      [>Input {:on-change  #(reset! column_name   (-> % .-target .-value))}]

      [>AutoComplete
       {:on-change  #(reset! data_type (-> %))
        :filterOption true
        :placeholder "数据类型"
        :defaultActiveFirstOption true
        :dataSource  (-> (filter #(-> % nil? not)  @data_type_list)
                         sort)}]

      [>AutoComplete
       {:on-change  #(reset! view_type (-> %))
        :filterOption true
        :placeholder "视图类型"
        :defaultActiveFirstOption true
        :dataSource  (-> (filter #(-> % nil? not)  @view_type_list)
                         sort)}]

      [>AutoComplete
       {:on-change  #(reset! entity_type (-> %))
        :filterOption true
        :placeholder "实体类型"
        :defaultActiveFirstOption true
        :dataSource  (-> (filter #(-> % nil? not)  @entity_type_list)
                         sort)}]

      [>Input {:on-change  #(reset! column_comment  (-> % .-target .-value))}]
      ;;
      ]]))

(defn edit-column-modal [state all-data]
  (r/with-let [data_type_list (subscribe [:metadata/all.data_type])
               view_type_list (subscribe [:metadata/all.view_type])
               entity_type_list (subscribe [:metadata/all.entity_type])
               cache-data (r/cursor state [:cache])
               table_name (r/cursor state [:cache :table_name])
               column_name (r/cursor state [:cache :column_name])
               column_comment (r/cursor state [:cache :column_comment])
               data_type (r/cursor state [:cache :data_type])
               view_type (r/cursor state [:cache :view_type])
               entity_type (r/cursor state [:cache :entity_type])
               ;;
               ]

    [:> js/antd.Modal
     {:title "修改字段"
      :visible (:visible @state)
      :onOk (fn [] (do
                     (dispatch [:metadata/column.new
                                @table_name
                                @column_name
                                @cache-data])
                     (swap! state assoc :visible false)))
      :onCancel  (fn [] (do
                          (reset! cache-data {})
                          (swap! state assoc :visible false)))}
     ;;
     [:div
      [>AutoComplete
       {:on-change  #(reset! table_name (-> %))
        :value @table_name
        :filterOption true
        :placeholder "选择实体名称"
        :defaultActiveFirstOption true
        :dataSource  (-> @all-data
                         keys
                         sort)}]
      [>Input {:on-change  #(reset! column_name   (-> % .-target .-value))
               :value @column_name}]

      [>AutoComplete
       {:on-change  #(reset! data_type (-> %))
        :value @data_type
        :filterOption true
        :placeholder "数据类型"
        :defaultActiveFirstOption true
        :dataSource  (-> (filter #(-> % nil? not)  @data_type_list)
                         sort)}]

      [>AutoComplete
       {:on-change  #(reset! view_type (-> %))
        :value @view_type
        :filterOption true
        :placeholder "视图类型"
        :defaultActiveFirstOption true
        :dataSource  (-> (filter #(-> % nil? not)  @view_type_list)
                         sort)}]

      [>AutoComplete
       {:on-change  #(reset! entity_type (-> %))
        :value @entity_type
        :filterOption true
        :placeholder "实体类型"
        :defaultActiveFirstOption true
        :dataSource  (-> (filter #(-> % nil? not)  @entity_type_list)
                         sort)}]

      [>Input {:on-change  #(reset! column_comment  (-> % .-target .-value))}]
      ;;
      ]]))

(defn new-relation-modal [page-state state all-relation all-table]
  (r/with-let [cache-data (r/cursor state [:cache])
               origin-table (r/cursor state [:cache :origin_table])
               origin-column (r/cursor state [:cache :origin_column])
               target-table (r/cursor state [:cache :target_table])
               target-column (r/cursor state [:cache :target_column])
               relation (r/cursor state [:cache :relation])
               selected-table  (r/cursor page-state [:selected-table])
               selected-column  (r/cursor page-state [:selected-column])]
    [:> js/antd.Modal
     {:title "修改字段"
      :visible (:visible @state)
      :onOk (fn [] (do
                     (dispatch [:metadata/relation.new
                                (-> {:origin_table @selected-table
                                     :origin_column @selected-column}
                                    (merge @cache-data)
                                    (merge {:id (swap! relation-id inc)}))])

                     (swap! state assoc :visible false)))
      :onCancel  (fn [] (do
                          (swap! state assoc :visible false)))}

     [:div
      [>Row
       [>AutoComplete
        {:on-change  #(reset! origin-table (-> %))
         :defaultValue @selected-table
         :filterOption true
         :placeholder "选择起始表"
         :defaultActiveFirstOption true
         :dataSource  (-> @all-table
                          keys
                          sort)
         ;;
         }]

       [>AutoComplete
        {:on-change  #(reset! origin-column (-> %))
         :placeholder "选择起始字段名"
         :defaultValue @selected-column
         :filterOption true
         :defaultActiveFirstOption true
         :dataSource  (-> (get-in @all-table [@origin-table])
                          keys
                          sort)
         ;;
         }]]
      [:p]
      [>Row
       [>AutoComplete
        {:on-change  #(reset! relation (-> %))
         :placeholder "选择关系"
         :filterOption true
         :defaultActiveFirstOption true
         :dataSource  (->>  @all-relation
                            vals
                            (map :relation)
                            sort)
         ;;
         }]]

      [:p]
      [>Row
       [>AutoComplete
        {:on-change  #(reset! target-table (-> %))
         :filterOption true
         :placeholder "选择目标表"
         :defaultActiveFirstOption true
         :dataSource  (-> @all-table
                          keys
                          sort)
         ;;
         }]

       [>AutoComplete
        {:on-change  #(reset! target-column (-> %))
         :placeholder "选择目标字段"
         :filterOption true
         :defaultActiveFirstOption true
         :dataSource  (-> (get-in @all-table [@target-table])
                          keys
                          sort)
         ;;
         }]]]]))

(defn relation-table-component [page-state all-relation]
  (r/with-let  [columns  [{:key "origin_table"
                           :dataIndex "origin_table"
                           :title "起始表"}
                          {:key "origin_column"
                           :dataIndex "origin_column"
                           :title "起始字段"}
                          {:key "relation"
                           :dataIndex "relation"
                           :title "关系"}
                          {:key "target_table"
                           :dataIndex "target_table"
                           :title "目标表"}
                          {:key "target_column"
                           :dataIndex "target_column"
                           :title "目标字段"}]

                edit-column (r/cursor page-state [:edit-column])
                selected-table  (r/cursor page-state [:selected-table])
                selected-column  (r/cursor page-state [:selected-column])]

    [:div
     [>Table  {:dataSource (->>
                            @all-relation
                            vals
                            (filter (fn [x]
                                      (cond
                                        (= (:origin_table x) @selected-table) true
                                        (= (:target_table x) @selected-table) true
                                        (nil? @selected-table)  true))))

               :bordered true
               :columns columns
               :size "smaill"
               :pagination {:defaultPageSize 5}
               :onRow
               (fn [record index]
                 (let [item (-> record
                                (js->clj :keywordize-keys true))]
                   #js   {:onClick (fn []
                                     (swap! edit-column assoc :cache item)
                                     (swap! edit-column assoc :visible true))}
                   ;
                   ))}]]

    ;;
    ))

(defn content [state & _]
  (r/with-let  [all-data (subscribe [:metadata/all])
                all-relation (subscribe [:metadata/relation.all])
                page-state (r/atom {})
                atom-table_name (r/cursor page-state [:atom-table_name])
                create-column (r/cursor page-state [:new-column])
                relation-new (r/cursor page-state [:relation-new])
                edit-column (r/cursor page-state [:edit-column])]

    [>Layout
     [new-table_name-modal  atom-table_name]
     [new-column-modal page-state create-column all-data]
     [edit-column-modal  edit-column all-data]
     [new-relation-modal page-state  relation-new all-relation all-data]
     [>Content
      [>Row {:gutter 32}
        ;; table 自身的维护
       [>Col {:span 12}
         ;; 按钮
        [>Row {:gutter 32}
         [>Col {:span 6}
          [table-selector page-state all-data]]

         [>Col {:span 6}
          [>Button {:on-click #(swap! atom-table_name assoc :visible true)}
           "添加新表名"]]
         [>Col {:span 6}
          [>Button {:on-click #(swap! create-column assoc :visible true)}
           "添加新字段"]]]
        [fields-table-component  page-state all-data]]

        ;; table关系的维护

       [>Col {:span 12}
       ;; [>Button {:on-click #(swap! relation-new assoc :visible true)}
       ;;  "添加新关系"]
        [relation-table-component page-state all-relation]]]]]

    ;;
    ))

(defn home-page [state & _]
  (r/with-let [active (subscribe [:active-page])
               page-state (subscribe [:current-page-state])]

    [:> js/antd.Layout
     [hpc/head state
      [hpc/nav state]]
     [:> js/antd.Layout {:style {:padding "24px"}}
      [hpc/side-bar state]

      [:> js/antd.Layout.Content {:style {:background "#fff"
                                          :padding 24
                                          :margin 0
                                          :minHeight 280}}
       [content]]]
     [hpc/foot state]]))


