(ns soul-talk.model-schema)

(def schema
  {:cost/id.machine #:db{:unique :db.unique/identity}
   :task/ref.costs #:db{:valueType :db.type/ref}
   :order/ref.workshop #:db{:valueType :db.type/ref  :isComponent true}
   :cost/ref.for-what #:db{:valueType :db.type/ref}
   :org/id.provider #:db{:unique :db.unique/identity}
   :geo/code #:db{:unique :db.unique/identity}
   :geo/ref.parent-geo #:db{:valueType :db.type/ref}
   :order/ref.order_number #:db{:valueType :db.type/ref :isComponent true}
   :order/ref.order_company #:db{:valueType :db.type/ref :isComponent true}
   :cost/id.workload #:db{:unique :db.unique/identity}
   :human/user_id #:db{:unique :db.unique/identity}
   :task/ref.workshop #:db{:valueType :db.type/ref :isComponent true}
   :order/order_number #:db{:unique :db.unique/identity}
   :task/ref.material-info #:db{:valueType :db.type/ref :isComponent true}
   :unit/name #:db{:unique :db.unique/identity}
   :human/eid #:db{:unique :db.unique/identity}
   :order/ref.customer #:db{:valueType :db.type/ref :isComponent true }
   :cost/ref.org #:db{:valueType :db.type/ref}
   :cost/id.material #:db{:unique :db.unique/identity}
   :process/eid #:db{:unique :db.unique/identity}
   :order/eid #:db{:unique :db.unique/identity}
   :task/ref.machine-info #:db{:valueType :db.type/ref}
   :task/ref.human-info #:db{:valueType :db.type/ref}
   :task/eid #:db{:unique :db.unique/identity}
   :task/ref.material #:db{:valueType :db.type/ref}
   :org/eid #:db{:unique :db.unique/identity}
   :machine/eid #:db{:unique :db.unique/identity}
   :task/ref.energy-info #:db{:valueType :db.type/ref}
   :material/eid #:db{:unique :db.unique/identity}
   :order/ref.material #:db{:valueType :db.type/ref :isComponent true}
   :task/ref.product-company #:db{:valueType :db.type/ref  :isComponent true}
   :task/ref.order #:db{:valueType :db.type/ref}
   :org/id.customer #:db{:unique :db.unique/identity}
   :process/ref.task #:db{:valueType :db.type/ref}
   :cost/ref.material #:db{:valueType :db.type/ref}
   :task/id.dyelot_number #:db{:unique :db.unique/identity}
   :cost/id.craft #:db{:unique :db.unique/identity}
   :org/ref.belong-to #:db{:valueType :db.type/ref}
   :material/code #:db{:unique :db.unique/identity}})



