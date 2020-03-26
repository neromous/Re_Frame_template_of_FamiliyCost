(ns soul-talk.modules.mi.models.material
  (:require
   [re-frame.core :refer [dispatch
                          dispatch-sync
                          subscribe
                          reg-sub]]
   [soul-talk.util.reframe-helper :as rh]
   [datascript.core :as d]))

(reg-sub
 :material/all
 (fn [db [_]]

   (let [conn (get db :conn)
         query '[:find  [(pull ?e [* {:org/ref.belong-to [*]}]) ...]

                 :where
                 [?e :material/eid]]]

     (d/q  query  (deref  conn)))))

(reg-sub
 :material/eid.orders
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?order [*]) ...]
                 :in $ ?id
                 :where
                 [?e :material/eid ?id]
                 [?order :order/ref.material ?e]]]
     (d/q  query  (deref  conn)  id))))

(reg-sub
 :material/eid.tasks
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?task [*]) ...]
                 :in $ ?id
                 :where
                 [?e :material/eid ?id]
                 [?task :task/ref.material ?e]]]
     (d/q  query  (deref  conn)  id))))
