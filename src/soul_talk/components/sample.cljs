(ns soul-talk.components.sample
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [reagent.core :as r]
            [soul-talk.components.common :as c]
            [soul-talk.components.field :as field]
            [soul-talk.layout.row-type :as row-type]))

(defn  description-sample []
  ;; 描述控件
  [:> js/antd.Descriptions
   {:title "订单信息"
    :bordered true}
   ;; 开始
   [:> js/antd.Descriptions.Item {:label "订单号"}]
   ;; 结束
   ]
  ;;
  )

(defn new-form-sample [item]
  (let [i (r/cursor item [:haha])]
    [:div
     ;;标题
     [:h3 "测试的录入框"]
     ;; 一列单独排列的
     [:> js/antd.Row
      [:> js/antd.Col {:span 3}
       "dasdfas"]
      [:> js/antd.Col {:span 18}
       [field/text-input item {}]]]

     ;; 并列两个的
     [:> js/antd.Row
      ;; 第一对
      [:> js/antd.Col {:span 4}]
      [:> js/antd.Col {:span 8}]
      ;; 第二对
      [:> js/antd.Col {:span 4}]
      [:> js/antd.Col {:span 6}]
      ;; 结束
      ]

     [:p]

     ;;三个的
     [:> js/antd.Row
      ;; 第一对
      [:> js/antd.Col {:span 3}]
      [:> js/antd.Col {:span 5}]
      ;; 第二对
      [:> js/antd.Col {:span 3}]
      [:> js/antd.Col {:span 5}]
      ;; 第三对
      [:> js/antd.Col {:span 3}]
      [:> js/antd.Col {:span 5}]
      ;; 结束
      ]

     [:p]

     ;; 四个的
     [:> js/antd.Row
      ;; 第一对
      [:> js/antd.Col {:span 2}]
      [:> js/antd.Col {:span 4}]
      ;; 第二对
      [:> js/antd.Col {:span 2}]
      [:> js/antd.Col {:span 4}]
      ;; 第三对
      [:> js/antd.Col {:span 2}]
      [:> js/antd.Col {:span 4}]
      ;; 第四对
      [:> js/antd.Col {:span 2}]
      [:> js/antd.Col {:span 4}]
      ;; 结束
      ]

     ;;
     ]
    ;;
    ))

(defn change-form-sample [store origin]
  (let []
    [:div
     ;;标题
     [:h3 "测试的录入框"]
     ;; 一列单独排列的
     [:> js/antd.Row
      [:> js/antd.Col {:span 3}]
      [:> js/antd.Col {:span 18}
       []]]

     ;; 并列两个的
     [:> js/antd.Row
      ;; 第一对
      [:> js/antd.Col {:span 4} "dddd"]
      [:> js/antd.Col {:span 8} "adsads"]
      ;; 第二对
      [:> js/antd.Col {:span 4}]
      [:> js/antd.Col {:span 6}]]

     ;;三个的
     [:> js/antd.Row
      ;; 第一对
      [:> js/antd.Col {:span 3}]
      [:> js/antd.Col {:span 5}]
      ;; 第二对
      [:> js/antd.Col {:span 3}]
      [:> js/antd.Col {:span 5}]
      ;; 第三对
      [:> js/antd.Col {:span 3}]
      [:> js/antd.Col {:span 5}]]

     ;; 四个的
     [:> js/antd.Row
      ;; 第一对
      [:> js/antd.Col {:span 2}]
      [:> js/antd.Col {:span 4}]
      ;; 第二对
      [:> js/antd.Col {:span 2}]
      [:> js/antd.Col {:span 4}]
      ;; 第三对
      [:> js/antd.Col {:span 2}]
      [:> js/antd.Col {:span 4}]
      ;; 第四对
      [:> js/antd.Col {:span 2}]
      [:> js/antd.Col {:span 4}]]

     ;;
     ]
    ;;
    ))


