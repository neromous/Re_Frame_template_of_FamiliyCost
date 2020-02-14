(ns soul-talk.components.label-field
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [soul-talk.util.date-utils :as du]
            [soul-talk.utils :as utils]
            [reagent.core :as r]))


;; (defn table-selector [state  dataSource]
;;   (r/with-let []

;;     [>AutoComplete
;;      {:on-change  #(reset! state (-> %))
;;       :filterOption true
;;       :placeholder "选择实体名称"
;;       :defaultActiveFirstOption true
;;       :dataSource  (-> @dataSource
;;                        keys
;;                        sort)
;;       ;;
;;       }]))


