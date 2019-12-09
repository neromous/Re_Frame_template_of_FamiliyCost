(ns soul-talk.components.fields
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [soul-talk.date-utils :as du]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [soul-talk.config :refer [source-pull source-new source-del source-update]]
            [soul-talk.model.account :refer  [category record]]
            [soul-talk.utils :refer [url->id]]
            [reagent.core :as r]))
;; input
(def store (r/atom {:title "dddd"}))

(def samples {:vtype :new
              :prototype category
              :field-key :name
              :store store})

(defmulti fields (juxt #((:prototype %) :field (:field-key %) :dtype)   #(get % :vtype)))

(defmethod fields :default [_]   _)

(defmethod fields [:text :read-only]
  [{:keys [prototype field-key  store] :as item}]
  (let [template (prototype :field field-key)]
    [:label (->  @store (get-in [field-key])  str clj->js)]))

(defmethod fields [:text :new]
  [{:keys [prototype field-key  store] :as item}]
  (let [template (prototype :field field-key)]
    [:div
     [:> js/antd.Input
      {:on-change #(swap! store assoc  field-key (->  % .-target .-value))
       :placeholder (clj->js (str "请输入:" (:title template)))}]
     [:p]]))

(defmethod fields [:text :edit]
  [{:keys [prototype field-key  store] :as item}]
  (let [template (prototype :field field-key)]
    [:div
     [:> js/antd.Input
      {:on-change #(swap! store assoc  field-key (->  % .-target .-value))
       :defaultValue (-> @store (get field-key) clj->js)}]
     [:p]]))

(defmethod fields [:date :read-only]
  [{:keys [prototype field-key  store] :as item}]
  (let [template (prototype :field field-key)]
    [:div
     [:label (clj->js (:title template))]
     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.DatePicker
      {:defaultValue  (new js/moment  (get-in @store  [:store field-key]))
       :disabled true}]
     [:p]]))

(defmethod fields [:date :edit]
  [{:keys [prototype field-key  store] :as item}]
  (let [template (prototype :field field-key)]
    [:div
     [:label (clj->js (:title template))]
     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.DatePicker
      {:on-change #(swap! store assoc-in [field-key]  (du/antd-date-parse %))
       :showToday true
       :defaultValue  (new js/moment  (get-in @store  [field-key]))}]
     [:p]]))

(defmethod fields [:date :new]
  [{:keys [prototype field-key  store] :as item}]
  (let [template (prototype :field field-key)]
    [:div
     [:label (clj->js (:title template))]
     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.DatePicker
      {:on-change #(swap! store assoc-in [field-key]  (du/antd-date-parse %))
       :showToday true
       :disabled true}]
     [:p]]))

(defmethod fields [:select :read-only]
  [{:keys [prototype field-key  store] :as item}]
  (let [template (prototype :field field-key)
        selection  (subscribe  (prototype :relate.all field-key))]
    [:div
     [:label (-> template :title str clj->js)]
     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.Cascader
      {:options (map #(-> {}
                          (assoc :label  (:title %))
                          (assoc :value  (:url %)))
                     (into #{}  (vals @selection)))
       :defaultValue (clj->js [(get-in @store [field-key])])
       }]
     [:p]]))

(defmethod fields [:select :new]
  [{:keys [prototype field-key  store] :as item}]
  (let [template (prototype :field field-key)
        selection  (subscribe  (prototype :relate.all field-key))]
    [:div
     [:label (-> template :title str clj->js)]
     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.Cascader
      {:options (map #(-> {}
                          (assoc :label  (:title %))
                          (assoc :value  (:url %)))
                     (into #{}  (vals @selection)))
       :on-change  #(swap! store assoc-in [field-key] (-> % js->clj first))}]
     [:p]]))

(defmethod fields [:select :edit]
  [{:keys [prototype field-key  store] :as item}]
  (let [template (prototype :field field-key)
        selection  (subscribe  (prototype :relate.all field-key))]
    [:div
     [:label (-> template :title str clj->js)]
     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.Cascader
      {:options (map #(-> {}
                          (assoc :label  (:title %))
                          (assoc :value  (:url %)))
                     (into #{}  (vals @selection)))
       :defaultValue (clj->js [(get-in @store [field-key])])
       :on-change  #(swap! store assoc-in [field-key] (-> % js->clj first))}]
     [:p]]))
