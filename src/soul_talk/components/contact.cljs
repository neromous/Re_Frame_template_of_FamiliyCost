(ns soul-talk.components.contact
  (:require             [reagent.core :as r]
                        [re-frame.core :refer [dispatch dispatch-sync subscribe]]
                        [soul-talk.date-utils :refer [to-date]]
                        [soul-talk.date-utils :as du]
                        (reddit-viewer.chart :as chart)
                        [soul-talk.components.common :as c]
                        [soul-talk.route.utils :refer [logged-in?
                                                       context-url
                                                       href
                                                       navigate!
                                                       run-events
                                                       run-events-admin]]))

;; (defn selection []
;;   {:on-change (fn [sk sr]
;;                 (js/console.log "===" sk "===="  sr))})

;; (defn simple-columns [key-view]
;;   [{:title "id" :dataIndex "id"}
;;    {:title "姓名" :dataIndex "name", :key "name", :align "center"}
;;    {:title "淘宝id" :dataIndex "taobaoId", :key "taobaoId", :align "center"}
;;    {:title "地址" :dataIndex "address", :key "address", :align "center"}
;;    {:title "所在地" :dataIndex "area", :key "area", :align "center"}
;;    {:title "是否是供应商" :dataIndex "isSupplier", :key "isSupplier", :align "center"}
;;    {:title  "操作" :dataIndex "actions" :key "actions" :align "center"
;;     :render (fn [_ item]
;;               (r/as-element
;;                (let [{:keys [id]} (js->clj item :keywordize-keys true)]
;;                  [:div
;;                   [:> js/antd.Button {:size   "small"
;;                                       :target "_blank"
;;                                       :href   (str "#/posts/" id)}
;;                    "查看"]
;;                   [:> js/antd.Divider {:type "vertical"}]
;;                   [:> js/antd.Button {:icon   "edit"
;;                                       :size   "small"
;;                                       :target "_blank"
;;                                       :href   (str "#/posts/" id "/edit")}]
;;                   [:> js/antd.Divider {:type "vertical"}]
;;                   [:> js/antd.Button
;;                    {:type     "danger"
;;                     :icon     "delete"
;;                     :size     "small"
;;                     :on-click (fn []
;;                                 (println id)
;;                                 (r/as-element
;;                                  (c/show-confirm
;;                                   "删除"
;;                                   (str "你确认要删除这个实体？")
;;                                   #(dispatch [:server/delete "/Contact/" key-view (-> id str keyword)])
;;                                   #(js/console.log "cancel"))))}]
;;                   [:> js/antd.Divider {:type "vertical"}]])))}])

;; (defn  contact-input-modal [view-key]
;;   (r/with-let [contact (subscribe [:state/get view-key :cache-contact])]
;;     (fn []
;;       (let [edited-contact (->  (or @contact {}) (update :id #(or % nil))
;;                                 (update :name #(or % nil))
;;                                 (update :taobaoId #(or % nil))
;;                                 (update :address #(or % nil))
;;                                 (update :area #(or % nil))
;;                                 (update :isSupplier #(or % 0))
;;                                 r/atom)
;;             id (r/cursor edited-contact [:id])
;;             name (r/cursor edited-contact [:name])
;;             taobaoId (r/cursor edited-contact [:taobaoId])
;;             area (r/cursor edited-contact [:area])
;;             isSupplier (r/cursor edited-contact [:isSupplier])]
;;         [:> js/antd.Form
;;          [:> js/antd.Input
;;           {:on-change  #(let [val (-> % .-target .-value)]
;;                           (reset! id val))
;;            :placeholder "请输入id"}]
;;          [:hr]
;;          [:> js/antd.Input
;;           {:on-change  #(let [val (-> % .-target .-value)]
;;                           (reset! name val))
;;            :placeholder "请输入id"}]
;;          [:hr]
;;          [:> js/antd.Input
;;           {:on-change  #(let [val (-> % .-target .-value)]
;;                           (reset! taobaoId val))
;;            :placeholder "请输入taobaoId"}]
;;          [:hr]
;;          [:> js/antd.Input
;;           {:on-change  #(let [val (-> % .-target .-value)]
;;                           (reset! area val))
;;            :placeholder "请输入area"}]
;;          [:hr]
;;          [:> js/antd.Input
;;           {:on-change  #(let [val (-> % .-target .-value)]
;;                           (reset! isSupplier val))
;;            :placeholder "请输入是否是供应商"}]
;;          [:> js/antd.Button

;;           {:on-click #(dispatch [:state/assoc view-key :cotact-cache @edited-contact])} "新增"]]))))

;; (defmethod  content-register
;;   [:home-contact :table]
;;   [_]
;;   (r/with-let [pk [:home-contact :table]
;;                model (subscribe [:data/all pk])
;;                state (subscribe [:state/all pk])]
;;     (fn []
;;       [:div
;;        [:> js/antd.Row
;;         [:> js/antd.Col {:xs 22 :sm 22 :md 8 :lg 8}
;;          [:> js/antd.Card
;;           [chart/chart-posts-by-votes chart/posts]]]
;;         [:> js/antd.Col {:xs 22 :sm 22 :md 8 :lg 8}
;;          [:> js/antd.Card
;;           [chart/chart-posts-by-votes chart/posts]]]

;;         [:> js/antd.Row
;;          [:> js/antd.Col {:xs 22 :sm 22 :md 22 :lg 22}

;;           [:> js/antd.Button
;;            {:on-click #(dispatch [:state/assoc pk :model-create true])}
;;            "新增"]

;;           [:> js/antd.Modal {:visible (if (-> @state :model-create (= nil))
;;                                         false
;;                                         (-> @state :model-create))
;;                              :onOk  #(dispatch [:state/assoc pk :model-create false])
;;                              :onCancel #(dispatch [:state/assoc pk :model-create false])}
;;            [contact-input-modal pk]]

;;           [:> js/antd.Button
;;            {:on-click #(dispatch [:state/assoc pk :model-delete true])}
;;            "批量删除"]
;;           [:> js/antd.Modal {:visible (if (-> @state :model-delete (= nil))
;;                                         false
;;                                         (-> @state :model-delete))
;;                              :onOk  #(dispatch [:state/assoc pk :model-delete false])
;;                              :onCancel #(dispatch [:state/assoc pk :model-delete false])}
;;            "是否需要批量删除?"]

;;           [:> js/antd.DatePicker.RangePicker
;;            {:on-change #(dispatch [:state/assoc pk :date-rage (js->clj %)])}]
;;           [:> js/antd.Button "日期选择"]]]
;;         [:> js/antd.Row
;;          [:> js/antd.Col {:span 18}
;;           [:> js/antd.Table   {:rowSelection (selection)
;;                                :dataSource @model
;;                                :columns   (clj->js  (simple-columns pk))
;;                                :rowKey "id"}]]]]]
;;       ;;
;;       )))

;; (defmethod  content-register
;;   [:home-contact :list]
;;   [_]
;;   (r/with-let [pk [:home-contact :table]
;;                data (subscribe [:data/all pk])
;;                state (subscribe [:state/all pk])]
;;     (fn []
;;       [:> js/antd.List
;;        {:itemLayout "vertical"
;;         :dataSource  (if (= nil @data)
;;                        []
;;                        @data)
;;         :renderItem (fn  [items]
;;                       (let [item  (js->clj items :keywordize-keys true)]
;;                         (r/as-element
;;                          [:> js/antd.List.Item
;;                           [:a {:href (str "#/vvv" "/" (:id item))}
;;                            (:name item)]])))}])))

;; (defn item-detail [item]
;;   (fn []
;;     [:div
;;      (for [[x y] item]
;;        [:div [:p x] [:p y]])]))

;; (defmethod  content-register
;;   [:home-contact :detail]
;;   [view-keys id-key]
;;   (r/with-let [item (subscribe [:model/find-id :contact  id-key])]
;;     (item-detail @item)))


