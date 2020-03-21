(ns soul-talk.sub.datomic
  (:require [re-frame.core :refer [subscribe reg-event-db
                                   dispatch reg-sub reg-event-fx]]
            [reagent.core :as r]
            [re-posh.core :as rd]
            [datascript.core :as d]
            [soul-talk.db :refer  [db]]
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
 (fn [_ [_ query  & args]]
   (if (nil? args)
     (d/q  query  (deref  db))
     (apply d/q  query  (deref  db)  args))))

(reg-sub
 :pull
 (fn [_ [_ id]]
   (d/pull (deref db)  '[*]  id)))

(reg-sub
 :pull-many
 (fn [_ [_ ids]]
   (d/pull-many (deref db)  '[*]  ids)))

(reg-sub
 :select
 (fn [_ [_ tb-name id]]
   (if (nil? id)
     (d/q  '[:find  [(pull ?e [*]) ...]
             :in $ ?tb-name
             :where
             [?e ?tb-name]]    (deref  db)  tb-name)
     (d/q  '[:find  (pull ?e [*]) .
             :in $ ?tb-name ?id
             :where
             [?e ?tb-name ?id]]    (deref  db)  tb-name id))))


(reg-sub
 :select-many
 (fn [_ [_ tb-name ids]]
     (d/q  '[:find  [(pull ?e [*]) ...] 
             :in $ ?tb-name [?id ...]
             :where
             [?e ?tb-name ?id]]    (deref  db)  tb-name ids)))






