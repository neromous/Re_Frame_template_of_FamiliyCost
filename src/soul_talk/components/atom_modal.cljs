(ns soul-talk.components.atom-modal
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [soul-talk.date-utils :as du]
            [soul-talk.config :refer [source-pull source-new source-del source-update]]
            [soul-talk.components.table-fields :refer [field]]))


(defn modal-show []
  (r/with-let [selection (subscribe (category :data.all))]

    [:> js/antd.Modal
     {:title   "查看录入"
      :visible (:show-vis @atom-record)
      :onOk     #(swap! atom-record assoc :show-vis false)
      :onCancel #(swap! atom-record assoc :show-vis false)}
     [:> js/antd.Card
      [field/text-placeholder :name atom-record "账户名称"]
      [field/text-placeholder :quota atom-record "账户额度"]
      [field/select-new  :recordType atom-record  "work" selection]]]))

