(ns soul-talk.layouts.selector
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [soul-talk.components.global-header :refer [header]]
            ))
(defn category-select [category categories]
  [:> js/antd.Select {:value        {:key @category}
                      :labelInValue true
                      :style        {:width 120 :padding "5px"}
                      :on-change    #(let [val (:key (js->clj % :keywordize-keys true))]
                                       (reset! category val))}
   [:> js/antd.Select.Option {:value ""} "选择分类"]
   (doall
    (for [{:keys [id name]} @categories]
      ^{:key id} [:> js/antd.Select.Option {:value id} name]))])

