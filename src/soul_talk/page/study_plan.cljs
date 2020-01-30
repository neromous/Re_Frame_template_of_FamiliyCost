(ns soul-talk.page.study-plan
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.global_components :as gbc]
   [soul-talk.components.field :as field]
   [soul-talk.layout.row-type :refer [input?>
                                      row2>
                                      row3>
                                      row4>]]

   [soul-talk.components.modal :as modal]
   [soul-talk.components.form :as form]
   [soul-talk.utils :as utils]))

(def base-skill
  {:reading "阅读"
   :writing "写作"
   :math "计算"
   :logic "逻辑"
   :music "音乐"
   :art   "美术"
   :coordination "协调感"
   :sports "运动"
   :physical "物理"
   :biological "生物"
   :chemical "化学"
   :financial "理财"
   :social "社交"
   ;;
   })

(def base-skill-selector-pairs
  (map (fn [x] {:value (key x)
                :label (val x)
                } )     base-skill)

  )


(defn  study-plan-input-form [item]

  (let [label-c {:span 4}
        input-c {:span 12}
        plan-name (r/cursor item [:plan-name])
        plan-start-date (r/cursor item [:plan-date])
        plan-end-date (r/cursor item [:plan-end-date])
        relate-skills (r/cursor item [:relate-skills])
        test-bool (r/cursor item [:test-bool])
        ;;
        ]
    [:div
     ;;标题
     [:h3 "计划录入"]
     [:p (str @item)]
     ;; 一列单独排列的
     [:> js/antd.Row
      [:> js/antd.Col {:span 3}
       "计划名称"]
      [:> js/antd.Col {:span 14}
       [field/text-input plan-name {}]]
      ;; 结束
      ]

     [:p]
     ;; 并列两个的
     [:> js/antd.Row
      ;; 第一对
      [:> js/antd.Col {:span 3} "起始日期"]
      [:> js/antd.Col {:span 6} [field/date-input plan-start-date {}]]
      ;; 第二对
      [:> js/antd.Col {:span 3} "计划结束日期"]
      [:> js/antd.Col {:span 6} [field/date-input plan-end-date {}]]
      ;;结束
      ]

     [:p]
     [:> js/antd.Row
      [:> js/antd.Col {:span 3}
       "相关计划"]
      [:> js/antd.Col {:span 14}
       [field/select-input relate-skills base-skill-selector-pairs  "1000" {}]]
      ;; 结束
      ]

     [:p]
     [:> js/antd.Row
      [:> js/antd.Col {:span 3}
       "相关计划"]
      [:> js/antd.Col {:span 14}
       [field/bool-change  test-bool {}]]
      ;; 结束
      ]
     ;;
     ]
     ;;
    ))

(defn study-plan-input-modal [item]
  (r/with-let []
    (fn []

      [:> js/antd.Modal



       ]


      )

    )

  )




(defn home-page
  []
  (r/with-let [active (subscribe [:active-page])]
    (let [item (-> {}
                   r/atom)]
      [:>  js/antd.Row {:gutter 16}
       [:> js/antd.Col {:span 12}
        [study-plan-input-form item]
        ]])))
