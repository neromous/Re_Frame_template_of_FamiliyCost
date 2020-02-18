(ns soul-talk.components.todo
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.common :as c]
   [soul-talk.util.date-utils :as du]
   [soul-talk.components.antd-dsl
    :refer [>Input  >InputNumber >Col  >Row >List
            list-item  >AutoComplete  >Modal  >Table
            >Content   >Form  >Description  descrip-item
            >Cascader  >Button  >DatePicker >Divider  >Header
            >Select select-option >Footer  >Switch >Sider
            >Checkbox  >Checkbox_group  >Form  form-item
            >Layout >Content]]
   [soul-talk.utils :as utils]))

(defn description-modal [page-cache]
  (r/with-let [selected-todo (r/cursor page-cache [:show :cache-todo])
               show-vis (r/cursor page-cache [:show :vis])]

    [>Modal
     {:title    "待办明细表"
      :visible  @show-vis
      :onOk     #(reset! show-vis false)
      :onCancel #(reset! show-vis false)
      :width "70%"}
     [>Description
      {:bordered true
       :size "small"
       :column 3}
      [:> descrip-item {:label "待办id"} (:id @selected-todo)]
      [:> descrip-item {:label "完成"} (:is_done @selected-todo)]
      [:> descrip-item {:label "优先级"} (:priority @selected-todo)]
      [:> descrip-item {:label "标题"} (:title @selected-todo)]
      [:> descrip-item {:label "内容"} (:content @selected-todo)]
      [:> descrip-item {:label "计划完成时间"} (-> @selected-todo :plan_time)]
      [:> descrip-item {:label "创建时间"} (:create_time @selected-todo)]
      [:> descrip-item {:label "实际完成时间"} (:done_time @selected-todo)]
      [:> descrip-item {:label "备注"} (:note @selected-todo)]
      [:> descrip-item {:label "修改时间"} (:modify_time @selected-todo)]
      ;;
      ]]))

(defn create-modal [page-cache]
  (r/with-let [todo (r/cursor page-cache [:create :cache-todo])
               create-vis (r/cursor page-cache [:create :vis])
               tags (subscribe [:model/all :tags])]

    [>Modal
     {:title    "待办明细表"
      :visible  @create-vis
      :onOk     #(do
                   (dispatch  [:model/server.add  :todos @todo])
                   (reset! create-vis false))

      :onCancel #(do (reset! create-vis false)
                     (reset! todo {}))}

     [:div
      [>Form
       {:labelCol {:span 6}
        :wrapperCol {:span 16}}
       ;;
       [:> form-item {:label "标题"}
        [>Input {:on-change #(swap! todo assoc :title (-> % .-target .-value))}]]
       ;;
       [:> form-item {:label "待办内容"}
        [:> js/antd.Input.TextArea
         {:rows 2
          :on-change #(swap! todo assoc :content (-> % .-target .-value))}]]


       ;;


       [:> form-item {:label "完成状态"}
        [>Switch {:on-change  #(swap! todo assoc :is_done %)
                  :checkedChildren "完成"
                  :unCheckedChildren "未完成"}]]
       ;;
       [:> form-item {:label "优先等级"}
        [>Select  {:on-change #(swap! todo assoc :priority %)
                   :placeholder "选择优先级"
                   :style {:width 120}}
         [:> js/antd.Select.Option  {:value 1} "1"]
         [:> js/antd.Select.Option  {:value 2} "2"]
         [:> js/antd.Select.Option  {:value 3} "3"]
         [:> js/antd.Select.Option  {:value 4} "4"]]]
       ;;
       [:> form-item  {:label "计划日期"}
        [>DatePicker  {:on-change #(swap! todo assoc :plan_time
                                          (du/moment->datetime %))}]]
       ;;
       [:> form-item  {:label "完成日期"}
        [>DatePicker  {:on-change #(swap!
                                    todo
                                    assoc
                                    :done_time (du/moment->datetime %))}]]
       ;;
       [:> form-item  {:label "完成日期"}
        [>DatePicker  {:on-change #(swap!
                                    todo
                                    assoc
                                    :plan_date (du/moment->datetime %))}]]
       ;;
       [:> form-item {:label "备注"}
        [:> js/antd.Input.TextArea
         {:rows 2
          :on-change #(swap! todo assoc :note (-> % .-target .-value))}]]
      ;;
       ]]]))

(defn modal-create-tag [page-cache]
  (r/with-let [tag (r/cursor page-cache [:create-tag :cache-tag])
               create-vis (r/cursor page-cache [:create-tag :vis])
               tag_type (subscribe [:model/all :tag_type])]

    [>Modal
     {:title    "待办明细表"
      :visible  @create-vis
      :onOk     #(do
                   (dispatch  [:model/server.add  :tags @tag])
                   (reset! create-vis false))

      :onCancel #(do (reset! create-vis false)
                     (reset! tag {}))}

     [:div
      [>Form
       {:labelCol {:span 6}
        :wrapperCol {:span 16}}
       ;;
       [:> form-item {:label "标签名称"}
        [>Input {:on-change #(swap! tag assoc :tag_name (-> % .-target .-value))}]]
       ;;
       [:> form-item {:label "标签类别"}
        [>Select
         (for [item @tag_type]
           ^{:key (:id item)}
           [:> select-option {:value (:id item)} (:type_name item)])]]
       ;;
       ]]]))

(defn edit-modal [page-cache]
  (r/with-let [todo (r/cursor page-cache [:edit :cache-todo])
               vis (r/cursor page-cache [:edit :vis])]

    [>Modal
     {:title    "待办明细表"
      :visible  @vis
      :onOk     #(do
                   (dispatch  [:model/server.update  :todos
                               (into {} (filter (fn [x]  (not (-> x val  nil?))) @todo))])

                   (reset! vis false))

      :onCancel #(reset! vis false)}

     [:div
      [:p (str @todo)]
      [>Form
       {:labelCol {:span 6}
        :wrapperCol {:span 16}}
       [:> form-item {:label "待办id"}
        [>InputNumber {:on-change  #(swap! todo assoc :id  %)
                       :addonBefore "id"
                       :value (:id @todo)}]]
       [:> form-item {:label "完成状态"}
        [>Switch {:on-change  #(swap! todo assoc :is_done %)
                  :checkedChildren "完成"
                  :value (:is_done @todo)
                  :unCheckedChildren "未完成"}]]
       [:> form-item {:label "优先等级"}

        [>Select  {:on-change #(swap! todo assoc :priority %)
                   :placeholder "选择优先级"
                   :value (:priority @todo)
                   :style {:width 120}}
         [:> js/antd.Select.Option  {:value 1} "1"]
         [:> js/antd.Select.Option  {:value 2} "2"]
         [:> js/antd.Select.Option  {:value 3} "3"]
         [:> js/antd.Select.Option  {:value 4} "4"]]]
       [:> form-item {:label "标题"}
        [>Input {:on-change #(swap! todo assoc :title (-> % .-target .-value))
                 :value (:title @todo)}]]

       [:> form-item {:label "待办内容"}
        [:> js/antd.Input.TextArea
         {:rows 2
          :on-change #(swap! todo assoc :content (-> % .-target .-value))
          :value (:content @todo)}]]

       [:> form-item  {:label "计划日期"}
        [>DatePicker  {:on-change #(swap! todo assoc :plan_time  %)
                       :value (new js/moment (:plan_time @todo))}]]

       [:> form-item  {:label "完成日期"}
        [>DatePicker  {:on-change #(swap!
                                    todo
                                    assoc
                                    :done_time %)
                       :value (new js/moment (:done_time @todo))}]]

       [:> form-item  {:label "完成日期"}
        [>DatePicker  {:on-change #(swap!
                                    todo
                                    assoc
                                    :plan_date %)
                       :value (new js/moment (:plan_date @todo))}]]

       [:> form-item {:label "备注"}
        [:> js/antd.Input.TextArea
         {:rows 2
          :on-change #(swap! todo assoc :note (-> % .-target .-value))}]]
      ;;
       ]]]))





