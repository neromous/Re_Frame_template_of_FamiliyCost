(ns soul-talk.sub.study-plan
  (:require
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe reg-sub]]
            [soul-talk.components.common :as c]
            [soul-talk.utils :as utils]
            [soul-talk.util.query-filter :as query-filter]))

;; 技能模板说明,
;; 判断dg

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

(def scores-template
  {:skill-name  :reading
   :scores-begin 0
   :scores-plan 100
   :scores-current 0}
  ;;
  )

(def plan-template
  {:plan-name ""
   :plan-start-date ""
   :plan-end-date ""
   :actual-start-date ""
   :actual-end-date ""
   :relate-skills []})




