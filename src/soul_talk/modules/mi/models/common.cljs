(ns soul-talk.modules.mi.models.common
  (:require
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [re-posh.core :as rd]
   [soul-talk.util.reframe-helper :refer
    [sub> act>  to-columns fix-key fix-value]]))

(rd/reg-query-sub
 :company.all.ids
 '[:find ?e
   :where
   [?e :org/eid]])

(rd/reg-query-sub
 :company.by-id
 '[:find ?e
   :in $ ?id
   :where
   [?e :org/eid ?id]])

(rd/reg-sub
 :company.all
 :<- [:company.all.ids]
 (fn [ids _]
   {:type :pull-many
    :pattern '[*]
    :ids (reduce into []  ids)}))

(rd/reg-query-sub
 :material.all.ids
 '[:find ?e
   :where
   [?e :material/eid]])

(rd/reg-sub
 :material.all
 :<- [:material.all.ids]
 (fn [ids _]
   {:type :pull-many
    :pattern '[*]
    :ids (reduce into []  ids)}))




;; (defn interface [id]
;;   [[:d/datalog
;;     :order.single
;;     {:query '[:find (pull ?order [*])
;;               :in $ [?tb-name ?id]
;;               :where
;;               [?order :order/eid ?id]
;;               [?task :task/ref.order ?order]
;;               [?process :process/ref.task ?task]
;;               [?cost1 :cost/ref.for-what ?task]
;;               [?cost2 :cost/ref.for-what ?process]]
;;      :input [:order/eid id]}]

;;    [:d/datalog
;;     :order.task {:query '[:find (pull ?task [*])
;;                           :in $ [?tb-name ?id]
;;                           :where
;;                           [?order :order/eid ?id]
;;                           [?task :task/ref.order ?order]
;;                           [?process :process/ref.task ?task]
;;                           [?cost1 :cost/ref.for-what ?task]
;;                           [?cost2 :cost/ref.for-what ?process]]
;;                  :input [:order/eid id]}]
;;    [:d/datalog
;;     :order.process {:query '[:find (pull ?process [*])
;;                              :in $ [?tb-name ?id]
;;                              :where
;;                              [?order :order/eid ?id]
;;                              [?task :task/ref.order ?order]
;;                              [?process :process/ref.task ?task]
;;                              [?cost1 :cost/ref.for-what ?task]
;;                              [?cost2 :cost/ref.for-what ?process]]
;;                     :input [:order/eid id]}]
;;    [:datalog
;;     :order.human&machine {:query '[:find (pull ?cost2 [*])
;;                                    :in $ [?tb-name ?id]
;;                                    :where
;;                                    [?order :order/eid ?id]
;;                                    [?task :task/ref.order ?order]
;;                                    [?process :process/ref.task ?task]
;;                                    [?cost1 :cost/ref.for-what ?task]
;;                                    [?cost2 :cost/ref.for-what ?process]]
;;                           :input [:order/eid id]}]
;;    [:d/datalog
;;     :order.material&craft {:query '[:find (pull ?cost1 [*])
;;                                     :in $ [?tb-name ?id]
;;                                     :where
;;                                     [?order :order/eid ?id]
;;                                     [?task :task/ref.order ?order]
;;                                     [?process :process/ref.task ?task]
;;                                     [?cost1 :cost/ref.for-what ?task]
;;                                     [?cost2 :cost/ref.for-what ?process]]
;;                            :input [:order/eid id]}]

;;    [:d/datalog
;;     :order.all {:query '[:find (pull ?order [*]) 
;;                          :where
;;                          [?order :order/eid]
;;                          [?order :order/ref.order_number  ?e ]
;;                          [?e :order/ref.order_company ?com]
;;                          [?e :order/ref.customer ?custom]
;;                          ]}]

;;    ;;
;;    ])

;; (defn init!  [id]
;;   (map (fn [[k v m]]
;;          (dispatch [k  m])) (interface id)))

;; (init! 200)


