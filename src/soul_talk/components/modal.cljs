(ns soul-talk.components.modal
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [soul-talk.date-utils :as du]
            [soul-talk.components.table-fields :refer [field]]
            ))

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
               title (str "新建: " (prototype :title))
               content (input-form prototype)
               success-fn #(run-events [[:server/new prototype @cache]
                                        (prototype :state.change :new-vis false)])
               cancel-fn #(run-events [(prototype :state.change :new-vis false)])]
    (c/form-modal title content state success-fn cancel-fn)))

(defn edit-modal [prototype]
  (r/with-let [state (subscribe (prototype :state.get :edit-vis))
               cache (subscribe (prototype :state.get :edit-cache))
               title (str "修改: " (prototype :title))
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
     {:title   (str "查看: " (prototype :title) )
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

