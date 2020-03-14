(ns soul-talk.modules.relations.page
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.common :as c]
   [soul-talk.util.date-utils :as du]
   [soul-talk.components.antd-dsl
    :refer [>Input  >InputNumber >Col  >Row >List >Card
            list-item  >AutoComplete  >Modal  >Table
            >Content   >Form  >Description  descrip-item
            >Cascader  >Button  >DatePicker >Divider  >Header
            >Select  >Footer  >Switch >Sider
            >Checkbox  >Checkbox_group  >Form  form-item
            >Layout >Content]]
   [soul-talk.utils :as utils]))

(def entity_tag
  (->   ["product_order"
         "order_detail"
         "job_order"
         "flow"
         "sheet"
         "job_notice"
         "下单公司"
         "生产车间"
         "生产公司"
         "goods"
         "create_time"
         "update_time"]
        sort))

(def relation_tag
  ["is"
   "not"])

(defn Select-table-name [page-state]
  (r/with-let [table-names (subscribe [:relations/table.all])
               select-table (r/cursor page-state [:cache :r :table_nm])
               select-column (r/cursor page-state [:cache :r :column_nm])
               select-entity (r/cursor page-state [:cache :r :entity_nm])
               select-relation (r/cursor page-state [:cache :r :relation_nm])
               input-cache (r/cursor page-state [:cache :r])
               tb_relation (subscribe [:model/all :tb_relation])
               all-columns (subscribe [:relations/all])]

    [>Form {:labelCol {:span 4}
            :wapperCol {:span 12}}

     [:> js/antd.Form.Item {:label "表名"}
      [>Select {:style {:width "220px"}
                :showSearch true
                :filterOption true
                :onSelect (fn [x _]
                            (reset! select-table x))}

       (doall
        (for [table-name @table-names]
          ^{:key table-name}
          [:> js/antd.Select.Option {:value table-name}  table-name]))]]
     ;;
     [:> js/antd.Form.Item {:label "实体名称"}

      [>Select {:style {:width "220px"}
                :showSearch true
                :filterOption true
                :onSelect (fn [x _]
                            (reset! select-column x))}

       (doall
        (for [column (->> @all-columns
                          (filter #(= (get % :table_name) @select-table))
                          (map :column_name))]

          ^{:key column}
          [:> js/antd.Select.Option {:value column}  column]))]]

     [:> js/antd.Form.Item {:label "实体关系"}
      [>Select {:style {:width "220px"}
                :showSearch true
                :filterOption true
                :onSelect (fn [x _]
                            (reset! select-relation x))
                :placeholder @select-relation}

       (doall
        (for [item relation_tag]

          ^{:key (str "relation_" item)}
          [:> js/antd.Select.Option {:value item}  item]))]]

     [:> js/antd.Form.Item {:label "实体名称"}
      [>Select {:style {:width "220px"}
                :showSearch true
                :filterOption true
                :placeholder @select-entity
                :onSelect (fn [x _]
                            (reset! select-entity x))}

       (doall
        (for [item entity_tag]

          ^{:key (str "entity_" item)}
          [:> js/antd.Select.Option {:value item}  item]))]]]))

(defn Modal-input [page-state form]
  (r/with-let [input-vis (r/cursor page-state [:input-vis])
               input-cache (r/cursor page-state [:cache :r])]
    [>Modal {:centered true
             :visible  @input-vis
             :title    "关系录入"
             :onOk     #(do
                          (dispatch  [:model/server.add :tb_relation  @input-cache])
                          (reset! input-vis false)
                          (reset! input-cache {}))
             :onCancel #(reset! input-vis false)}
     [form page-state]]))

(def column-relation
  [{:key "table_nm"  :dataIndex "table_nm" :title "表名"}
   {:key "column_nm" :dataIndex "column_nm" :title "字段名"}
   {:key "relation_nm" :dataIndex "relation_nm" :title "关系名"}
   {:key "entity_nm"  :dataIndex "entity_nm" :title "实体名称"}
   {:title  "操作" :dataIndex "actions" :key "actions" :align "center"
    :render (fn [_ item]
              (r/as-element
               (let [{:keys [id]}  (js->clj item :keywordize-keys true)]
                 [:div
                  [:> js/antd.Button
                   {:size "small"
                    :target "_blank"
                    :on-click (fn []
                                (r/as-element
                                 (c/show-confirm
                                  "关系删除"
                                  (str "你确认要删除这个关系?")
                                  #(dispatch [:model/server.del :tb_relation id])
                                  #(js/console.log "cancel"))))}
                   "删除"]]
                 ;;
                 )))}])

(defn Table-relation [page-state]
  (r/with-let [relation (subscribe [:model/all :tb_relation])

               filter-table_name (r/cursor page-state [:filter :table_name])
               filter-column_name (r/cursor page-state [:filter :column_name])]

    [>Table {:dataSource (if (nil? @filter-table_name)
                           @relation
                           (filter #(= (get % :table_nm) @filter-table_name)  @relation))

             :columns column-relation}]))

(defn home-page [state & _]
  (r/with-let [active-page (:active-page state)
               page (:page-state state)
               page-state (r/atom {})
               input-vis (r/cursor page-state [:input-vis])
               input-cache (r/cursor page-state [:cache :r])
               filter-table_name (r/cursor page-state [:filter :table_name])
               filter-column_name (r/cursor page-state [:filter :column_name])
               table-names (subscribe [:relations/table.all])
               all-columns (subscribe [:relations/all])]

    [>Layout
     [hpc/head state
      [hpc/nav state]]
     [>Layout {:style {:padding "24px"}}

      [Modal-input  page-state Select-table-name]
      [>Content {:style {:background "#fff"
                         :padding 24
                         :margin 0
                         :minHeight 280}}

       [>Button  {:on-click #(reset! input-vis true)
                  :type "primary"}
        "输入实体关系"]
       [>Divider {:type "vertical"}]
       ;;
       [>Select {:style {:width "220px"}
                 :showSearch true
                 :filterOption true
                 :onSelect (fn [x _]
                             (reset! filter-table_name x))}

        (doall
         (for [table-name @table-names]
           ^{:key table-name}
           [:> js/antd.Select.Option {:value table-name}  table-name]))]

       [>Row {:gutter 24}
        [>Col {:span 8}
         [>Description {:column 1
                        :size "small"
                        :bordered true}
          (doall

           (for [column (->> @all-columns
                             (filter #(= (get % :table_name) @filter-table_name))
                             (map :column_name))]

             [:> js/antd.Descriptions.Item  column]))]]

        [>Col {:span 16}

         [Table-relation page-state]]]

       ;;
       ]]

     [hpc/foot state]]))



