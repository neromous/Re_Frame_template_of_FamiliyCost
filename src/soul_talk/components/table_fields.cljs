(ns soul-talk.components.table-fields
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [soul-talk.date-utils :as du]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [soul-talk.config :refer [source-pull source-new source-del source-update foreign-key]]
            [soul-talk.utils :refer [url->id]]
            [reagent.core :as r]))


;; 这里是对于form表单里常用的类型所使用的东西


(defmulti field (juxt :dtype :vtype))

(defmethod field :default [{:keys [prototype store-key] :as field}]
  (let [k (:inner-key field)]
    [:label    (clj->js @(subscribe (prototype :state.get store-key k)))]))

(defmethod field [:text :read] [{:keys [prototype store-key] :as field}]
  (let [k (:inner-key field)]
    [:div
     [:label (:title field)]
     [:> js/antd.Divider {:type "vertical"}]
     [:label    (clj->js @(subscribe (prototype :state.get store-key k)))]
     [:p]]))

(defmethod field [:date :read] [{:keys [prototype store-key] :as field}]
  (let [k (:inner-key field)
        default-time (subscribe (prototype :state.get store-key k))]
    [:div
     [:label (:title field)]
     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.DatePicker
      {:on-change #(dispatch
                    (prototype :state.change store-key k (du/antd-date-parse %)))
       :defaultValue  (new js/moment  @default-time)}]
     [:p]]))

(defmethod field [:select :read] [{:keys [prototype store-key] :as field}]
  (let [k (:inner-key field)
        relation  (-> (prototype :template) k :relation)
        data (subscribe
              [foreign-key
               (prototype :db.states store-key k)
               (relation :db.datasets)])

        ]
    [:div
     [:label (:title field)]
     [:> js/antd.Divider {:type "vertical"}]
     [:label    (clj->js    (:name @data))]
     [:p]]))

(defmethod field [:text :new] [{:keys [prototype store-key] :as field}]
  (let [k (:inner-key field)]
    [:div
     [:> js/antd.Input
      {:on-change #(dispatch
                    (prototype :state.change  store-key k (->  % .-target .-value)))
       :placeholder (clj->js (str "请输入:" (:title field)))}]
     [:p]]))

(defmethod field [:date :new] [{:keys [prototype store-key] :as field}]
  (let [k (:inner-key field)]
    [:div
     [:label (:title field)]

     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.DatePicker
      {:on-change #(dispatch
                    (prototype :state.change store-key k (du/antd-date-parse %)))}]
     [:p]]))

(defmethod field [:select :new] [{:keys [prototype store-key] :as field}]
  (let [k (:inner-key field)
        data-map (subscribe (prototype :data.all))
        relation  (-> (prototype :template) k :relation)
        relation-data (subscribe (relation :data.all))
        ]
    [:div
     [:label (:title field)]
     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.Cascader
      {:options (map #(-> {}
                          (assoc :label  (:name %))
                          (assoc :value  (:url %)))
                     (into #{}  (vals @relation-data)))
       :on-change  #(dispatch  (prototype :state.change store-key k  (first %)))}]
     [:p]]))

(defmethod field [:text :edit] [{:keys [prototype store-key cache-key] :as field}]
  (let [k (:inner-key field)
        store (subscribe (prototype :state.get store-key k))]
    [:> js/antd.Row
     [:> js/antd.Col {:span 4}
      [:label (:title field)] [:> js/antd.Divider {:type "vertical"}]]
     [:> js/antd.Col {:span 12 :type "flex" :justify "center"}
      [:> js/antd.Input
       {:on-change #(dispatch
                     (prototype :state.change  cache-key k  (->  % .-target .-value)))
        :defaultValue (clj->js @store)}]]

     [:p]]))

(defmethod field [:date :edit] [{:keys [prototype store-key cache-key] :as field}]
  (let [k (:inner-key field)
        default-time (subscribe (prototype :state.get store-key k))]
    [:div
     [:label (:title field)]
     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.DatePicker
      {:on-change #(dispatch
                    (prototype :state.change store-key k (du/antd-date-parse %)))
       :defaultValue  (new js/moment  @default-time)}]
     [:p]]))

(defmethod field [:select :edit] [{:keys [prototype store-key cache-key] :as field}]
  (let [k (:inner-key field)
        relation  (-> (prototype :template) k :relation)
        relation-data (subscribe (relation :data.all))
        store (subscribe (prototype :state.get store-key k))]

    [:div
     [:label (:title field)]
     [:> js/antd.Divider {:type "vertical"}]
     [:> js/antd.Cascader
      {:options (map #(-> {}
                          (assoc :label  (:name %))
                          (assoc :value  (:url %)))
                     (vals @relation-data))
       :on-change  #(dispatch  (prototype :state.change cache-key k  (first %)))
       :defaultValue (clj->js [@store])}]
     [:p]]))



