(ns soul-talk.page.todo-index
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.common :as c]
   [soul-talk.components.todo :refer [description-modal
                                      create-modal
                                      modal-create-tag
                                      edit-modal]]
   [soul-talk.util.date-utils :as du]
   [soul-talk.components.antd-dsl
    :refer [>Input  >InputNumber >Col  >Row >List
            list-item  >AutoComplete  >Modal  >Table
            >Content   >Form  >Description  descrip-item
            >Cascader  >Button  >DatePicker >Divider  >Header
            >Select select-option  >Footer  >Switch >Sider
            >Checkbox  >Checkbox_group  >Form  form-item
            >Layout >Content]]

   [soul-talk.utils :as utils]))

(defn todo-table-column [page-state]
  (r/with-let [edit-todo (r/cursor page-state [:edit :cache-todo])
               edit-vis (r/cursor page-state [:edit :vis])
               metadata (subscribe [:metadata/views.table-columns :todos])
               selected-todo (r/cursor page-state [:show :cache-todo])
               show-vis (r/cursor page-state [:show :vis])]
    (conj
     [;;
      {:key "priority", :dataIndex "priority", :title "优先级"}
      {:key "title", :dataIndex "title", :title "标题"}
      {:key "content", :dataIndex "content", :title "内容"}
      {:key "is_done", :dataIndex "is_done", :title "完成"}
      {:key "plan_time", :dataIndex "plan_time", :title "计划完成时间"}
      {:key "create_time", :dataIndex "create_time", :title "创建时间"}
      ;;  {:key "tags_id", :dataIndex "tags_id", :title "tags_id"}
      {:key "done_time", :dataIndex "done_time", :title "完成时间"}
      {:key "note", :dataIndex "note", :title "备注"}
      ;;  {:key "modify_time", :dataIndex "modify_time", :title "modify_time"}
      ]
     (c/columns-with-do :todos [selected-todo show-vis]   [edit-todo edit-vis])
     ;;
     )))

(defn content [state  page-state & _]
  (r/with-let [all-todo (subscribe [:model/all :todos])
               todo-state (subscribe [:model/view-state :todos])
               select-todo (r/cursor page-state [:select-todo])
               tag-create-vis (r/cursor page-state [:create-tag :vis])
               input-vis (r/cursor page-state [:create  :vis])]
    [:div
     [>Divider {:type "vertical"}]
     [>Button  {:on-click #(reset! input-vis true)}  "新增"]
     [>Divider {:type "vertical"}]
     [>Button  {:on-click #(reset! tag-create-vis true)}  "新增tag"]

     [>Table  {:dataSource @all-todo
               :size "smaill"
               :columns (clj->js
                         (todo-table-column page-state))}]
     ;;
     ]))

(defn home-page [state & _]
  (r/with-let [active-page (:active-page state)
               page (:page-state state)
               page-state (r/atom {})]

    [>Layout
     [description-modal page-state]
     [create-modal page-state]
     [edit-modal page-state]
     [modal-create-tag page-state]
     [hpc/head state
      [hpc/nav state]]
     [>Layout {:style {:padding "24px"}}
      [hpc/side-bar state]

      [>Content {:style {:background "#fff"
                         :padding 24
                         :margin 0
                         :minHeight 280}}
       [content state page-state]]]
     [hpc/foot state]]))



