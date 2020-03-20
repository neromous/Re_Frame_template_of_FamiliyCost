(ns soul-talk.modules.mi.model.common
  (:require
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.util.reframe-helper :refer
    [sub> act> dsub dget dset to-columns fix-key fix-value]]))


(defn interface [id]
  [[:d/datalog
    :order.single
    {:query '[:find (pull ?order [*])
              :in $ [?tb-name ?id]
              :where
              [?order :order/eid ?id]
              [?task :task/ref.order ?order]
              [?process :process/ref.task ?task]
              [?cost1 :cost/ref.for-what ?task]
              [?cost2 :cost/ref.for-what ?process]]
     :input [:order/eid id]}]

   [:d/datalog
    :order.task {:query '[:find (pull ?task [*])
                          :in $ [?tb-name ?id]
                          :where
                          [?order :order/eid ?id]
                          [?task :task/ref.order ?order]
                          [?process :process/ref.task ?task]
                          [?cost1 :cost/ref.for-what ?task]
                          [?cost2 :cost/ref.for-what ?process]]
                 :input [:order/eid id]}]
   [:d/datalog
    :order.process {:query '[:find (pull ?process [*])
                             :in $ [?tb-name ?id]
                             :where
                             [?order :order/eid ?id]
                             [?task :task/ref.order ?order]
                             [?process :process/ref.task ?task]
                             [?cost1 :cost/ref.for-what ?task]
                             [?cost2 :cost/ref.for-what ?process]]
                    :input [:order/eid id]}]
   [:datalog
    :order.human&machine {:query '[:find (pull ?cost2 [*])
                                   :in $ [?tb-name ?id]
                                   :where
                                   [?order :order/eid ?id]
                                   [?task :task/ref.order ?order]
                                   [?process :process/ref.task ?task]
                                   [?cost1 :cost/ref.for-what ?task]
                                   [?cost2 :cost/ref.for-what ?process]]
                          :input [:order/eid id]}]
   [:d/datalog
    :order.material&craft {:query '[:find (pull ?cost1 [*])
                                    :in $ [?tb-name ?id]
                                    :where
                                    [?order :order/eid ?id]
                                    [?task :task/ref.order ?order]
                                    [?process :process/ref.task ?task]
                                    [?cost1 :cost/ref.for-what ?task]
                                    [?cost2 :cost/ref.for-what ?process]]
                           :input [:order/eid id]}]

   [:d/datalog
    :order.all {:query '[:find (pull ?order [*]) 
                         :where
                         [?order :order/eid]
                         [?order :order/ref.order_number  ?e ]
                         [?e :order/ref.order_company ?com]
                         [?e :order/ref.customer ?custom]
                         ]}]

   ;;
   ])

(defn init!  [id]
  (map (fn [[k v m]]
         (dispatch [k  m])) (interface id)))

(init! 200)


