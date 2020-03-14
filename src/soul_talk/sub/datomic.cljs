(ns soul-talk.sub.datomic
  (:require  [re-frame.core :refer [inject-cofx
                                    dispatch
                                    dispatch-sync
                                    reg-event-db
                                    reg-event-fx
                                    subscribe reg-sub]]
             [soul-talk.util.db :as udb]))

(reg-sub
 :db/schema
 :<- [:datomic/schema.data]
 (fn [all-data [_]]
   (->>  (concat (->> (filter #(= (get % :db/unique) :db.unique/identity)
                              all-data)
                      (map #(select-keys % [:db/ident :db/unique])))
                 (->> (filter #(= (get % :db/cardinality) :db.cardinality/many)
                              all-data)
                      (map #(select-keys % [:db/ident :db/cardinality])))

                 (->> (filter #(= (get % :db/valueType) :db.type/ref)  all-data)
                      (map #(select-keys % [:db/ident :db/valueType]))))

         (group-by :db/ident)
         ((fn [x] (zipmap (keys x)
                          (->> (map #(apply merge %)  (vals x))
                               (map #(dissoc % :db/ident))
                           )

                          ))))))

