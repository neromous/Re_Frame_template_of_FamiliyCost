(ns soul-talk.components.columns
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [soul-talk.date-utils :as du]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [soul-talk.config :refer [source-pull source-new source-del source-update]]
            [soul-talk.utils :refer [url->id]]
            [soul-talk.components.common :as c]
            (soul-talk.model.account :refer [account record category])
            [reagent.core :as r]))




(defn text-raw [prototype field-key]
  {:title (prototype :field field-key :title)
   :key  (name field-key)
   :dataIndex (name field-key)})

(defn date-raw [prototype field-key]
  {:title (prototype :field field-key :title)
   :key  (name field-key)
   :dataIndex (name field-key)})

(defn number-raw [prototype field-key]
  {:title (prototype :field field-key :title)
   :key  (name field-key)
   :dataIndex (name field-key)})

(defn selection [prototype field-key]
  {:title (prototype :field field-key :title)
   :key  (name field-key)
   :dataIndex (name field-key)})

(defn action-parts [prototype ratom]
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








