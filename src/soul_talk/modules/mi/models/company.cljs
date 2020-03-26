(ns soul-talk.modules.mi.models.company
  (:require
   [re-frame.core :refer [dispatch
                          dispatch-sync
                          subscribe
                          reg-sub]]
   [soul-talk.util.reframe-helper :as rh]
   [datascript.core :as d]))

(def code-xin-tai 36)
(def code-tai-an 3)
(def code-headquarters 26)

(reg-sub
 :org/all
 (fn [db [_ id]]

   (let [conn (get db :conn)
         query '[:find  [(pull ?e [* {:org/ref.belong-to [*]}]) ...]
                 :in $ ?id
                 :where
                 [?e :org/eid]]]

     (d/q  query  (deref  conn)  id))))

(reg-sub
 :company/all
 (fn [db [_ id]]

   (let [conn (get db :conn)
         query '[:find  [(pull ?e [* {:org/ref.belong-to [*]}]) ...]
                 :in $ ?id
                 :where
                 [?e :org/eid]
                 [?e :org/type "company"]]]
     (d/q  query  (deref  conn)  id))))

(reg-sub
 :company/subs
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?e [*]) ...]
                 :in $ ?id
                 :where
                 [?e :org/eid]
                 [?e :org/ref.belong-to ?ee]
                 [?ee :org/eid ?id]]]

     (d/q  query  (deref  conn)  id))))

(reg-sub
 :headquarters/subs
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?e [*]) ...]
                 :where
                 [?e :org/eid]
                 [?e :org/ref.belong-to ?ee]
                 [?ee :org/eid 26]]]

     (d/q  query  (deref  conn)))))

(reg-sub
 :com.tai-an/subs
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?e [*]) ...]
                 :where
                 [?e :org/eid]
                 [?e :org/ref.belong-to ?ee]
                 [?ee :org/eid 3]]]
     (d/q  query  (deref  conn)))))

(reg-sub
 :com.xin-tai/subs
 (fn [db [_ id]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?e [*]) ...]
                 :where
                 [?e :org/eid]
                 [?e :org/ref.belong-to ?ee]
                 [?ee :org/eid 36]]]

     (d/q  query  (deref  conn)))))

(reg-sub
 :com.tai-an/orders
 (fn [db [_]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?e [*]) ...]
                 :where
                 [?eee :org/eid 3]
                 [?ee :order/ref.order_company ?eee]
                 [?e :order/ref.order_number ?ee]]]
     (d/q  query  (deref  conn)))))

(reg-sub
 :com.tai-an/tasks
 (fn [db [_]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?e [* {:task/ref.workshop [*]
                                      :task/ref.product-company [*]
                                      :task/ref.material [*]}]) ...]
                 :where
                 [?ee :org/eid 3]
                 [?e :task/ref.product-company ?ee]
                 [?e :task/eid]]]

     (d/q  query  (deref  conn)))))

(reg-sub
 :com.xin-tai/orders
 (fn [db [_]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?e [*]) ...]
                 :where
                 [?eee :org/eid 35]
                 [?ee :order/ref.order_company ?eee]
                 [?e :order/ref.order_number ?ee]]]
     (d/q  query  (deref  conn)))))

(reg-sub
 :com.xin-tai/tasks
 (fn [db [_]]
   (let [conn (get db :conn)
         query '[:find  [(pull ?e [* {:task/ref.workshop [*]
                                      :task/ref.product-company [*]
                                      :task/ref.material [*]}]) ...]
                 :where
                 [?ee :org/eid 35]
                 [?e :task/ref.product-company ?ee]
                 [?e :task/eid]]]
     (d/q  query  (deref  conn)))))

(first @(rh/sub> :com.xin-tai/tasks))

(rh/sub> :select :org/eid 35)

