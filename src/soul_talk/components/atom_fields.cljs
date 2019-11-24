(ns soul-talk.components.atom-fields
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [soul-talk.date-utils :as du]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [soul-talk.config :refer [source-pull source-new source-del source-update]]
            [soul-talk.utils :refer [url->id]]
            [reagent.core :as r]))


(defn label-raw  [pk store title]
  [:label  (->  @store (get-in [:store pk])  str clj->js)])

(defn text-read  [pk store title]
  [:label  (->  @store (get-in [:store pk])  str clj->js)])

(defn text-placeholder  [pk store title]
  [:div
   [:> js/antd.Input
    {:on-change #(swap! store assoc-in [:cache pk]  (->  % .-target .-value))
     :placeholder (clj->js (str "请输入:" title))}]
   [:p]])

(defn text-edit  [pk store title]
  [:div
   [:label  (clj->js title)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.Input
    {:on-change #(swap! store assoc-in [:cache pk]  (->  % .-target .-value))
     :defaultValue (->  @store (get-in [:store pk])  str clj->js)}]
   [:p]])

(defn date-read  [pk store title]
  [:div
   [:label (clj->js title)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.DatePicker
    {:defaultValue  (new js/moment  (get-in @store  [:store pk]))
     :disabled true}]
   [:p]])

(defn date-placehold  [pk store title]
  [:div
   [:label (clj->js title)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.DatePicker
    {:on-change #(swap! store assoc-in [:cache pk]  (du/antd-date-parse %))
     :defaultValue  (new js/moment)}]
   [:p]])

(defn date-edit  [pk store title]
  [:div
   [:label (clj->js title)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.DatePicker
    {:on-change #(swap! store assoc-in [:cache pk]  (du/antd-date-parse %))
     :showToday true
     :defaultValue  (new js/moment  (get-in @store  [:store pk]))}]
   [:p]])

(defn select-read  [pk store title selection]
  [:div
   [:label (-> title str clj->js)]
   [:> js/antd.Divider {:type "vertical"}]
   [:> js/antd.Cascader
    {:options (map #(-> {}
                        (assoc :label  (:name %))
                        (assoc :value  (:url %)))
                   (into #{}  (vals @selection)))
     :defaultValue (clj->js [(get-in @store [:store pk])])
     }]
   [:p]])

(defn select-new  [pk store title selection]
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

(defn select-edit  [pk store title selection]
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


