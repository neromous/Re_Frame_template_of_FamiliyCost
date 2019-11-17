(ns soul-talk.components.atom-fields
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [soul-talk.date-utils :as du]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [soul-talk.config :refer [source-pull source-new source-del source-update foreign-key]]
            [soul-talk.utils :refer [url->id]]
            [reagent.core :as r]))

;; 这里是对于form表单里常用的类型所使用的东西

(defmulti field (juxt :dtype :vtype))

(defmethod field :default [{:keys [pk store title]}]
  [:label    (->  @store (get-in [:store pk])  str clj->js)])

(defmethod field [:text :read] [{:keys [pk store title]}]
  [:label    (->  @store (get-in [:store pk])  str clj->js)])

(defmethod field [:text :new] [{:keys [pk store title]}]
  [:div
   [:> js/antd.Input
    {:on-change #(swap! store assoc-in [:cache pk]  (->  % .-target .-value))
     :placeholder (clj->js (str "请输入:" title))}]
   [:p]])

(defmethod field [:text :edit] [{:keys [pk store title]}]
  [:div
   [:label  (clj->js title)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.Input
    {:on-change #(swap! store assoc-in [:cache pk]  (->  % .-target .-value))
     :defaultValue (->  @store (get-in [:store pk])  str clj->js)}]
   [:p]])

(defmethod field [:date :read] [{:keys [pk store title]}]
  [:div
   [:label (clj->js title)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.DatePicker
    {:defaultValue  (new js/moment  (get-in @store  [:store pk]))
     :disabled true}]
   [:p]])

(defmethod field [:date :new] [{:keys [pk store title]}]
  [:div
   [:label (clj->js title)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.DatePicker
    {:on-change #(swap! store assoc-in [:cache pk]  (du/antd-date-parse %))
     :defaultValue  (new js/moment)}]
   [:p]])

(defmethod field [:date :edit] [{:keys [pk store title]}]
  [:div
   [:label (clj->js title)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.DatePicker
    {:on-change #(swap! store assoc-in [:cache pk]  (du/antd-date-parse %))
     :showToday true
     :defaultValue  (new js/moment  (get-in @store  [:store pk]))}]
   [:p]])

(defmethod field [:select :read] [{:keys [pk store selection title]}]
  [:div
   [:label (-> title str clj->js)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.Cascader
    {:options (map #(-> {}
                        (assoc :label  (:name %))
                        (assoc :value  (:url %)))
                   (into #{}  (vals @selection)))
     :defaultValue (clj->js [(get-in @store [:store pk])])
     ;;:on-change  #(swap! store assoc-in [:cache pk] (-> % js->clj first))
     }]
   [:p]])

(defmethod field [:select :new] [{:keys [pk store selection title]}]
  [:div
   [:label (-> title str clj->js)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.Cascader
    {:options (map #(-> {}
                        (assoc :label  (:name %))
                        (assoc :value  (:url %)))
                   (into #{}  (vals @selection)))
     :on-change  #(swap! store assoc-in [:cache pk] (-> % js->clj first))}]
   [:p]])

(defmethod field [:select :edit] [{:keys [pk store selection title]}]
  [:div
   [:label (-> title str clj->js)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.Cascader
    {:options (map #(-> {}
                        (assoc :label  (:name %))
                        (assoc :value  (:url %)))
                   (into #{}  (vals @selection)))
     :defaultValue (clj->js [(get-in @store [:store pk])])
     :on-change  #(swap! store assoc-in [:cache pk] (-> % js->clj first))}]
   [:p]])


