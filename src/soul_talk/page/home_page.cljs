(ns soul-talk.page.home-page
  (:require [soul-talk.page.layout :as layout]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            (soul-talk.model.account :refer [account record category])
            [soul-talk.date-utils :as du]
            [soul-talk.components.atom-fields :refer [field]]
            [soul-talk.utils :refer [url->id]]
            [soul-talk.config :refer [source-pull source-new source-del source-update]]
            [soul-talk.pages.dash :refer [dash-page]]
            [soul-talk.components.base-layout :refer [content header nav footer siderbar]]))

(def atom-account
  (r/atom {:cache {}
           :cache-vis false
           :store {:name "ddd"
                   :id 1}
           :store-vis false
           :delete-list []}))

(def atom-gear
  (r/atom {:cache {}
           :cache-vis false
           :store {}
           :store-vis false
           :delete-list []}))

(println @atom-account)

(defn create-modal []
  (when (:cache-vis @atom-account)
    [:> js/antd.Modal
     {:title "账户输入"
      :visible (:cache @atom-account)
      :onCancel #(swap! atom-account assoc :cache-vis false)
      :onOk #(dispatch [source-new account  (:cache @atom-account)])}
     [field {:dtype :date :vtype :new :pk :name :store atom-account :title "名称"
             :selection (subscribe (category :data.all))}]]))

(defmethod content
  [:home :test]
  [x]
  (r/with-let [model (subscribe  (account :data.all))]
    (let [edited  (-> @model r/atom)]
      (when @model
        [:div  {:style {:padding 10}}
         [create-modal]
         [:> js/antd.Row
          [:> js/antd.Col  {:span 8}
           [:> js/antd.Button
            {:type "primary"
             :on-click  #(swap! atom-account assoc :cache-vis true)}
            "新建记录"]
           [:> js/antd.Button
            ""]
           [:> js/antd.Button
            "批量操作"]]
          [:> js/antd.Col {:span 8}]
          [:> js/antd.Col {:span 8}
           [:> js/antd.DatePicker.RangePicker]]]
         [:> js/antd.Row  {:style {:height "50%"}}
          [:> js/antd.Col  {:span 8}
           [:div "ddddddddddddddddddd"]
           "00000000000000"]
          [:> js/antd.Col   {:span 8}
           "00000000000000"]
          [:> js/antd.Col  {:span 8}
           "00000000000000"]]]))))


