(ns soul-talk.modules.mi.models.sell-info
  (:require
   [re-frame.core :refer [dispatch
                          dispatch-sync
                          subscribe
                          reg-sub]]
   [soul-talk.util.reframe-helper :as rh]
   [datascript.core :as d]
   [soul-talk.db :refer  [db]]))

(reg-sub
 :order.eid.task
 (fn [_ [_ id]]
   (let [query '[:find  [(pull ?task [*]) ...]
                 :in $ ?id
                 :where
                 [?e :order/eid  ?id]
                 [?task :task/ref.order ?e]]]

     (d/q  query  (deref  db)  id))))

(reg-sub
 :order.eid.process
 (fn [_ [_ id]]
   (let [query '[:find  [(pull ?process [*]) ...]
                 :in $ ?id
                 :where
                 [?e :order/eid  ?id]
                 [?task :task/ref.order ?e]
                 [?process :process/ref.task ?task]]]

     (d/q  query  (deref  db)  id))))

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





