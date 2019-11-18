(ns soul-talk.page.home-page
  (:require [soul-talk.page.layout :as layout]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            (soul-talk.model.account :refer [account record category])
            [soul-talk.date-utils :as du]
            [soul-talk.components.atom-fields :as field]
            [soul-talk.utils :refer [url->id]]
            [soul-talk.config :refer [source-pull source-new source-del source-update]]
            [soul-talk.pages.dash :refer [dash-page]]
            [soul-talk.components.base-layout :refer [content header nav footer siderbar]]))


(def atom-dash
  (r/atom
   {}))

(def atom-query
  (r/atom {})
  )


(def atom-record
  (r/atom {:cache {}
           :store {}
           :edit-vis false
           :new-vis false
           :show-vis false
           :delete-list []}))

(def atom-gear
  (r/atom {:cache {}
           :cache-vis false
           :store {}
           :store-vis false
           :delete-list []}))


(defn query-part []
  (r/with-let [selection (subscribe (category :data.all))]
    [field/field-select-new :category atom-record "账户类型" selection]
    )
  )




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

(defn columns [prototype ratom]
  (concat (for [[k item] (prototype :template)]
            {:title (:title item)
             :dataIndex (:dataIndex item)
             :key (:key item)
             :aligh "center"})

          [{:title "操作" :dataIndex "actions" :key "actions" :align "center"
            :render (render-parts prototype ratom)}]))

(defn selection [prototype]
  {:on-change (fn [sk sr]
                (dispatch (prototype :state.change :table-selection sk)))})

(println atom-record)

(defn modal-new []
  (r/with-let [selection (subscribe (category :data.all))]
    (when (:new-vis @atom-record)
      [:> js/antd.Modal
       {:title "账户输入"
        :visible (:new-vis @atom-record)
        :onCancel #(swap! atom-record assoc :new-vis false)
        :onOk #(do  (dispatch [source-new record  (:cache @atom-record)])
                    (swap! atom-record assoc :new-vis false))}
       [:> js/antd.Form
        [field/field-text-placeholder :name atom-record "账户名称"]
        [field/field-text-placeholder :quota atom-record "账户额度"]
        [field/field-select-new  :recordType atom-record  "work" selection]]])))

(defn modal-edit []
  (r/with-let [selection (subscribe (category :data.all))]
    (when (:edit-vis @atom-record)
      [:> js/antd.Modal
       {:title "账户修改"
        :visible (:edit-vis @atom-record)
        :onCancel #(swap! atom-record assoc :edit-vis false)
        :onOk #(do  (dispatch [source-update
                               record
                               (-> @atom-record (get-in [:cache :id])  str keyword)
                               (:cache @atom-record)])
                    (swap! atom-record assoc :edit-vis false))}
       [:> js/antd.Form
        [field/field-text-placeholder :name atom-record "账户名称"]
        [field/field-text-placeholder :quota atom-record "账户额度"]
        [field/field-select-new  :recordType atom-record  "work" selection]]])))

(defmethod content
  [:home :test]
  [x]
  (r/with-let [model (subscribe  (record :data.all))]
    (let [edited  (-> @model r/atom)]
      (when @model
        [:div  {:style {:padding 10}}
         [modal-new]
         [modal-edit]
         [:> js/antd.Row
          [:> js/antd.Col {:span 18}
           [:> js/antd.Row
            [:> js/antd.Col {:span 8}
             [:> js/antd.Button
              {:type "primary"
               :on-click  #(swap! atom-record assoc :new-vis true)}
              "新建记录"]
             [:> js/antd.Button
              ""]
             [:> js/antd.Button
              "批量操作"]]

            [:> js/antd.Col {:span 6 }  [query-part] ]
            [:> js/antd.Col {:span 6 }

             [:> js/antd.DatePicker.RangePicker]]]
           [:hr]
           [:> js/antd.Table   {:rowSelection (selection record)
                                :dataSource   (->> @(subscribe (record :data.all)) vals (sort-by :id))
                                :columns   (clj->js  (columns record  atom-record))
                                :rowKey "id"}]]

          [:> js/antd.Col {:span 6}
           [:> js/antd.Card
            {:style {:margin 5}}]]]]))))


