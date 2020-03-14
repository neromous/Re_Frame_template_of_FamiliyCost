 (ns soul-talk.components.card

  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.components.table :as table]))

(defn fake-card
  "用来生成假数据图表的内容"
  [detail]
  (fn []
    [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8}
     [:> js/antd.Card {:activeTabKey "infordetails"
                       :title        "筛选后计算的合计值"
                       :bodyStyle    {:overflow "hidden"}
                       :style        {:margin 5}
                       :bordered     false
                       :hoverable    true}

        [:> js/antd.List
         {
          }]]]))



