(ns soul-talk.components.misc
  (:require [soul-talk.routes :refer [navigate!]]
            [re-frame.core :refer [subscribe dispatch]]
            [reagent.core :as r]
            [soul-talk.components.common :as c]
            [re-frame.core :as rf]))

(defn controll-detail
  "控制用控件的组件"
  [model]
  (r/with-let []
    (fn [_ items]
      (r/as-element
       (let [{:keys [id publish] :as item} (js->clj items :keywordize-keys true)]
         [:div
          [:> js/antd.Button {:size   "small"
                              :target "_blank"
                              :href   (str "#/" (:name-str model) "/" id)}
           "查看"]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button {:icon   "edit"
                              :size   "small"
                              :target "_blank"
                              :href   (str "#/" (:name-str model) "/" id "/edit")}]
          [:> js/antd.Divider {:type "vertical"}]
          [:> js/antd.Button {:type     "danger"
                              :icon     "delete"
                              :size     "small"
                              :on-click (fn []
                                          (r/as-element
                                           (c/show-confirm
                                            "删除"
                                            (str "你确认要删除这个吗？")
                                            #(dispatch [(:db/delete model) id])
                                            #(js/console.log "cancel"))))}]
          [:> js/antd.Divider {:type "vertical"}]])))))
