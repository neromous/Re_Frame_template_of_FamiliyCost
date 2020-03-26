(ns soul-talk.sub.datomic
  (:require [re-frame.core :refer [subscribe reg-event-db
                                   dispatch reg-sub reg-event-fx]]
            [reagent.core :as r]
            [datascript.core :as d]
            [soul-talk.util.reframe-helper :refer [remove-db_id]]
            [ajax.core :refer [POST
                               GET
                               DELETE
                               PUT
                               ajax-request
                               url-request-format
                               json-request-format
                               json-response-format]]
            [ajax.edn :refer [edn-request-format
                              edn-response-format]]))

(reg-sub
 :q
 (fn [db [_ query  & args]]
   (let [conn (get db :conn)]
     (if (nil? args)
       (d/q  query  (deref  conn))
       (apply d/q  query  (deref  conn)  args)))))

(reg-sub
 :pull
 (fn [db [_ id]]

   (let [conn (get db :conn)]
     (d/pull (deref conn)  '[*]  id))))

(reg-sub
 :pull-many
 (fn [db [_ ids]]

   (let [conn (get db :conn)]
     (d/pull-many (deref conn)  '[*]  ids))))

(reg-sub
 :select
 (fn [db [_ tb-name id]]

   (let [conn (get db :conn)]
     (if (nil? id)
       (d/q  '[:find  [(pull ?e [*]) ...]
               :in $ ?tb-name
               :where
               [?e ?tb-name]]    (deref  conn)  tb-name)
       (d/q  '[:find  (pull ?e [*]) .
               :in $ ?tb-name ?id
               :where
               [?e ?tb-name ?id]]    (deref  conn)  tb-name id)))))

(reg-sub
 :select-many
 (fn [db [_ tb-name ids]]

   (let [conn (get db :conn)]
     (d/q  '[:find  [(pull ?e [*]) ...]
             :in $ ?tb-name [?id ...]
             :where
             [?e ?tb-name ?id]]    (deref  conn)  tb-name ids))))
