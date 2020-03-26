(ns soul-talk.modules.mi.models.sell-info
  (:require
   [re-frame.core :refer [dispatch
                          dispatch-sync
                          subscribe
                          reg-sub]]
   [soul-talk.util.reframe-helper :as rh]
   [datascript.core :as d]))

(reg-sub
 :order/all
 (fn [db [_ id]]

   (let [conn (get db :conn)
         query '[:find  [(pull ?e [*]) ...]
                 :in $ ?id
                 :where
                 [?e :order/eid]]]

     (d/q  query  (deref  conn)  id))))

(reg-sub
 :order/eid.task
 (fn [db [_ id]]

   (let [conn (get db :conn)
         query '[:find  [(pull ?task [* {:task/ref.workshop [*]
                                         :task/ref.material [*]}]) ...]
                 :in $ ?id
                 :where
                 [?e :order/eid  ?id]
                 [?task :task/ref.order ?e]]]

     (d/q  query  (deref  conn)  id))))

(reg-sub
 :order/eid.process
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?process [*]) ...]
                 :in $ ?id
                 :where
                 [?e :order/eid  ?id]
                 [?task :task/ref.order ?e]
                 [?process :process/ref.task ?task]]]

     (d/q  query  (deref  conn)  id))))

(reg-sub
 :order/eid.machine
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?cost [*]) ...]
                 :in $ ?id
                 :where
                 [?e :order/eid  ?id]
                 [?task :task/ref.order ?e]
                 [?process :process/ref.task ?task]
                 [?cost :cost/ref.for-what ?process]
                 [?cost :cost/id.machine]]]

     (d/q  query  (deref  conn)  id))))

(reg-sub
 :order/eid.human
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?cost [*]) ...]
                 :in $ ?id
                 :where
                 [?e :order/eid  ?id]
                 [?task :task/ref.order ?e]
                 [?process :process/ref.task ?task]
                 [?cost :cost/ref.for-what ?process]
                 [?cost :cost/id.workload]]]

     (d/q  query  (deref  conn)  id))))

(reg-sub
 :order/eid.material
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?cost [* {:cost/ref.for-what [*]}]) ...]
                 :in $ ?id
                 :where
                 [?e :order/eid  ?id]
                 [?task :task/ref.order ?e]
                 [?cost :cost/ref.for-what ?task]
                 [?cost :cost/id.material]]]
     (d/q  query  (deref  conn)  id))))

(reg-sub
 :order/eid.craft
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?cost [*]) ...]
                 :in $ ?id
                 :where
                 [?e :order/eid  ?id]
                 [?task :task/ref.order ?e]
                 [?cost :cost/ref.for-what ?task]
                 [?cost :cost/id.craft]]]
     (d/q  query  (deref  conn)  id))))

(def order->table_source
  (let [add-field
        (map (fn [x]
               (-> x
                   (assoc  :customer_name (get-in x [:order/ref.order_number
                                                     :order/ref.customer
                                                     :org/name]))
                   (assoc  :company_name (get-in x [:order/ref.order_number
                                                    :order/ref.order_company
                                                    :org/name]))

                   (assoc  :material_name (get-in x [:order/ref.material
                                                     :material/name])))))]
    (comp add-field
          (rh/field->float&2 :order/yarn_weight)
          (rh/field->float&2 :order/tax_price)
          (rh/field->float&2 :order/tax_money))))

(def task->table_source
  (let [func  (map (fn [x]
                     (-> x
                         (assoc  :workshop (get-in x [:task/ref.workshop
                                                      :org/name]))
                         (assoc  :company (get-in x [:task/ref.product-company
                                                     :org/name]))
                         (assoc  :material_name (get-in x [:task/ref.material
                                                           :material/name]))
                         ;;
                         )))]
    (comp  func
           (rh/field->float&2 :task/plan_weight))))




