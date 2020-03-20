(ns soul-talk.modules.mi.model.org
  (:require
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.util.reframe-helper :refer
    [sub> act> dsub dget dset to-columns fix-key fix-value]]))

(defn interface [id]
  [[:datalog  :workshop.orders
    {:query '[:find (pull ?order [*])
              :in $ [?id]
              :where
              [?workshop :org/eid ?id]
              [?task :task/ref.workshop ?workshop]
              [?task :task/ref.order ?order]
              [?order :order/ref.order_number  ?num]
              [?order :order/eid]]
     :input [id]}]

   [:datalog  :company.orders
    {:query '[:find (pull ?order [*])
              :in $ [?id]
              :where
              [?com :org/eid ?id]
              [?num :order/ref.order_company ?com]
              [?order :order/ref.order_number ?num]
              [?order :order/eid]]
     :input [id]}]

   [:datalog  :company.product
    {:query '[:find (pull ?order [*])
              :in $ [?id]
              :where
              [?com :org/eid ?id]
              [?task :task/ref.product-company ?com]
              [?task :task/ref.order ?order]
              [?order :order/eid]]
     :input [id]}]

     ;;
   ])

(dispatch [:datalog  :org.select
  {:query '[:find (pull ?e [*])
            :in $ [?id]
            :where
            [?e :org/eid ?id]]
   :input [3]}])

(defn init!  [id]
  (map (fn [[k v m]]
         (dispatch [k v m])) (interface id)))

(init! 3)
