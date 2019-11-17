(ns soul-talk.components.atom-modal
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [soul-talk.date-utils :as du]
            [soul-talk.config :refer [source-pull source-new source-del source-update]]
            [soul-talk.components.table-fields :refer [field]]))

(defn show-modal [prototype]
  (r/with-let [item (subscribe (prototype :state.get :show-item))
               modal-state (subscribe (prototype :state.get :show-vis))]

    [:> js/antd.Modal
     {:title   (str "查看: " (prototype :title))
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
               ;(assoc :cache-key :show-item)
               (assoc :vtype :read)
               )]))]]))

(defn input-form [prototype  atom  ]
  [:> js/antd.Form
   (doall
     ;; react 不支持惰性序列  所以说需要doall包住
    (for [[k item] (-> (prototype :template) (dissoc :id))]
      ^{:key k}
      [field
       (->  item
            (assoc :prototype prototype)
            (assoc :store-key :new-cache)
            (assoc :vtype :new)
            ;;(assoc :cache-key :new-cache)
            )]))])


(defn input-modal [prototype]
  (r/with-let [state (subscribe (prototype :state.get :new-vis))
               cache (subscribe (prototype :state.get :new-cache))
               edited (r/atom  {})
               title (str "新建: " (prototype :title))
               content (input-form prototype edited)
               success-fn #(run-events [[source-new prototype @cache]
                                        (prototype :state.change :new-vis false)])
               cancel-fn #(run-events [(prototype :state.change :new-vis false)])]
    (c/form-modal title content state success-fn cancel-fn)))


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

(defn edit-modal [prototype]
  (r/with-let [state (subscribe (prototype :state.get :edit-vis))
               cache (subscribe (prototype :state.get :edit-cache))
               title (str "修改: " (prototype :title))
               content [edit-form prototype cache]
               success-fn #(run-events
                            [[source-update  prototype (-> @cache :id str keyword)  @cache]
                             (prototype :state.change :edit-vis false)])
               cancel-fn #(run-events [(prototype :state.change :edit-vis false)])]
    (c/form-modal title content state success-fn cancel-fn)))


