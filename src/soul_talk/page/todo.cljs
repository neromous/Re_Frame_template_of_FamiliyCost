(ns soul-talk.page.todo
  (:require [soul-talk.page.layout :as layout]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.base-layout :refer [content header nav footer siderbar]]
            ))



;;  主要目的, 为了记录所有的待办事项, 计划, 支出记录, 日程,
;;  综合事务管理功能
;; 包括 todo  project  tag系统,
;;   所有的东西都是记录, 或者虚拟记录.
;;  通过tag系统作为赘述
;;  记录存在上级和下级
;;  记录的字段是最全的, 是分场景呈现, 而不是不同的东西.
;;


(def record-sample
  {:是否完成 false
   :名称 "空"
   :优先级 0
   :紧急程度 0
   :实际发生日期 "1900-01-01"
   :计划发生日期 "1900-01-01"
   :循环类型 false
   :实际花费 0
   :计划花费 0
   :实际结束日期 "1900-01-01"})



(def record-datas
  [{:是否完成 false
   :名称 "空"
   :优先级 0
   :紧急程度 0
   :实际发生日期 "1900-01-01"
   :计划发生日期 "1900-01-01"
   :循环类型 false
   :实际花费 0
   :计划花费 0
   :实际结束日期 "1900-01-01"}] )


(defn render-parts [prototype ratom]
  (r/with-let []
    (fn [_ item]
      (r/as-element
       (let [{:keys [id] :as clj-item} (js->clj item :keywordize-keys true)]
         [:div
          [:> js/antd.Button
           {:size   "small"
            :on-click  #(do
                          (swap! ratom assoc :store clj-item)
                          (swap! ratom  assoc :show-vis true))
            :target "_blank"}
           "查看明细"]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button
           {:icon   "edit"
            :size   "small"
            :target "_blank"
            :on-click #(do
                         (swap! ratom  assoc :cache clj-item)
                         (swap! ratom  assoc :store clj-item)
                         (swap! ratom assoc :edit-vis true))}
           "编辑"]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button
           {:type     "danger"
            :icon     "delete"
            :size     "small"
            :on-click (fn []
                        (r/as-element
                         (c/show-confirm
                          "删除"
                          (str "你确认要删除这个实体？")
                          #(dispatch [source-del prototype (-> id str keyword)])
                          #(js/console.log "cancel"))))}]
          [:> js/antd.Divider {:type "vertical"}]])))))




(def record-columns
  (for [[k v]  record-sample]
    {:title (name k)
     :dataIndex (name k)
     :key (name k)
     :aligh "center"}))

(defmethod content
  [:home :todo]
  [x]
  [:> js/antd.Table   {
                       :dataSource   record-datas
                       :columns   record-columns
                       :rowKey "id"}]
  )




