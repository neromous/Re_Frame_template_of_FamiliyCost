(ns soul-talk.model-schema)

(def schame
  {:org/eid   {:db/unique :db.unique/identity},
   :order/ref.customer {:db/valueType :db.type/ref}
   :order/ref.order_number {:db/valueType :db.type/ref}
   :org/id.customer   #:db{:unique :db.unique/identity},
   :org/id.provider   #:db{:unique :db.unique/identity}
   
   

   })



