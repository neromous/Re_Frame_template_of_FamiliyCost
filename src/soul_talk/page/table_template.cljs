(ns soul-talk.page.table-template
  (:require [soul-talk.page.layout :as layout]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            (soul-talk.model.account :refer [account record category])
            [soul-talk.date-utils :as du]
            [soul-talk.components.table-fields :refer [field]]
            [soul-talk.components.home-page :refer [content header nav footer siderbar]]))

(defn render-parts [prototype]
  (r/with-let []
    (fn [_ item]
      (r/as-element
       (let [{:keys [id] :as clj-item} (js->clj item :keywordize-keys true)]
         [:div
          [:> js/antd.Button
           {:size   "small"
            :on-click #(run-events [(prototype :state.change :show-item clj-item)
                                    (prototype :state.change :show-vis true)])
            :target "_blank"}
           "查看明细"]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button {:icon   "edit"
                              :size   "small"
                              :target "_blank"
                              :on-click #(run-events [(prototype :state.change :edit-item clj-item)
                                                      (prototype :state.change :edit-cache clj-item)
                                                      (prototype :state.change :edit-vis true)])}
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
                          #(dispatch [:server/delete prototype (-> id str keyword)])
                          #(js/console.log "cancel"))))}]
          [:> js/antd.Divider {:type "vertical"}]])))))

(defn columns [prototype]
  (concat (for [[k item] (prototype :template)]
            {:title (:title item)
             :dataIndex (:dataIndex item)
             :key (:key item)
             :aligh "center"})

          [{:title "操作" :dataIndex "actions" :key "actions" :align "center"
            :render (render-parts prototype)}]))

(defn selection [prototype]
  {:on-change (fn [sk sr]
                (dispatch (prototype :state.change :table/selection sk)))})

(defn input-form [prototype]
  [:> js/antd.Form
   (doall
     ;; react 不支持惰性序列  所以说需要doall包住
    (for [[k item] (-> (prototype :template) (dissoc :id))]
      ^{:key k}
      [field
       (->  item
            (assoc :prototype prototype)
            (assoc :sotre-key :new-cache)
            (assoc :cache-key :new-cache))]))])

(defn edit-form [prototype cache]
  [:> js/antd.Form
   (doall
    (for [[k item] (-> (prototype :template) (dissoc :id))]
      ^{:key k}
      [field
       (->  item
            (assoc :prototype prototype)
            (assoc :store-key :edit-item)
            (assoc :cache-key :edit-cache)
            (assoc :vtype :edit))]))])

(defn input-modal [prototype]
  (r/with-let [state (subscribe (prototype :state.get :new-vis))
               cache (subscribe (prototype :state.get :new-cache))
               title (str "新建" (prototype :title))
               content (input-form prototype)
               success-fn #(run-events [[:server/new prototype @cache]
                                        (prototype :state.change :new-vis false)])
               cancel-fn #(run-events [(prototype :state.change :new-vis false)])]
    (c/form-modal title content state success-fn cancel-fn)))

(defn edit-modal [prototype]
  (r/with-let [state (subscribe (prototype :state.get :edit-vis))
               cache (subscribe (prototype :state.get :edit-cache))
               title (str "修改" (prototype :title))
               content [edit-form prototype cache]
               success-fn #(run-events
                            [[:server/update  prototype (-> @cache :id str keyword)  @cache]
                             (prototype :state.change :edit-vis false)])
               cancel-fn #(run-events [(prototype :state.change :edit-vis false)])]
    (c/form-modal title content state success-fn cancel-fn)))

(defn show-modal [prototype]
  (r/with-let [item (subscribe (prototype :state.get :show-item))
               modal-state (subscribe (prototype :state.get :show-vis))]

    [:> js/antd.Modal
     {:title    "账户明细"
      :visible  @modal-state
      :onOk     #(run-events [(prototype :state.change :show-vis false)])
      :onCancel #(run-events [(prototype :state.change :show-vis false)])}
     [:> js/antd.Card
      (doall
       (for [[k item] (-> (prototype :template) (dissoc :id))]
         ^{:key k}
         [field
          (->  item
               (assoc :prototype prototype)
               (assoc :store-key :show-item)
               (assoc :cache-key :show-item)
               (assoc :vtype :read))]))]]))

;; (defn content-template
;;   [prototype]
;;   (r/with-let [_ (dispatch [:server/dataset-find-by prototype])
;;                data-map   (subscribe (prototype :data.all))]
;;     [:div
;;      [show-modal prototype]
;;      [input-modal prototype]
;;      [edit-modal prototype]

;;      [:br]
;;      [:> js/antd.Button
;;       {:on-click #(dispatch (prototype :state.change :new-vis true))
;;        :type "primary"
;;        :size "small"}
;;       "新增"]
;;      [:hr]
;;      [:> js/antd.Table   {:rowSelection (selection prototype)
;;                           :dataSource   (->> @data-map vals (sort-by :id))
;;                           :columns   (clj->js  (columns prototype))
;;                           :rowKey "id"}]]))

(defmethod content
  [:table :index :account]
  [db]
  (r/with-let [prototype account
               _ (dispatch [:server/dataset-find-by prototype])
               data-map   (subscribe (prototype :data.all))]
    [:div
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch (prototype :state.change :new-vis true))
       :type "primary"
       :size "small"}
      "新增账户"]
     [:hr]
     [:> js/antd.Table   {:rowSelection (selection prototype)
                          :dataSource   (->> @data-map vals (sort-by :id))
                          :columns   (clj->js  (columns prototype))
                          :rowKey "id"}]
     ;; 注册modal 要在下面 避免出现出发不及时的状态 由于刷新是从上到下刷新的
     [show-modal prototype]
     [input-modal prototype]
     [edit-modal prototype]]))

(defmethod content
  [:table :index :record]
  [db]
  (r/with-let [prototype record
               _ (dispatch [:server/dataset-find-by prototype])
               data-map   (subscribe (prototype :data.all))]
    [:div
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch (prototype :state.change :new-vis true))
       :type "primary"
       :size "small"}
      "新增记录"]
     [:hr]
     [:> js/antd.Table   {:rowSelection (selection prototype)
                          :dataSource   (->> @data-map vals (sort-by :id))
                          :columns   (clj->js  (columns prototype))
                          :rowKey "id"}]
     [show-modal prototype]
     [input-modal prototype]
     [edit-modal prototype]]))

(defmethod content
  [:table :index :category]
  [db]
  (r/with-let [prototype category
               _ (dispatch [:server/dataset-find-by prototype])
               data-map   (subscribe (prototype :data.all))]
    [:div
     [:br]
     [:> js/antd.Button
      {:on-click #(dispatch (prototype :state.change :new-vis true))
       :type "primary"
       :size "small"}
      "新增账户类别"]
     [:hr]
     [:> js/antd.Table   {:rowSelection (selection prototype)
                          :dataSource   (->> @data-map vals (sort-by :id))
                          :columns   (clj->js  (columns prototype))
                          :rowKey "id"}]

     [show-modal prototype]
     [input-modal prototype]
     [edit-modal prototype]]))




