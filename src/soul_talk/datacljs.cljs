(ns soul-talk.datacljs
  (:require
   [reagent.core :as r]
   [goog.events :as events]
   [clojure.string :as str]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [datascript.core :as d]))


;; Implicit join, multi-valued attribute
(def conn (d/create-conn  @(subscribe [:db/schema])   ))

(println @conn)
;; Define datoms to transact
(def datoms [{:db/id -1 :product-task/flow_id 1 }
             {:db/id -2 :product-task/flow_id 2
              :process/ref.product-task {:product-task/flow_id 1}}])

(def ttt @(subscribe [:sell-detail.view/data]))

(d/transact! conn datoms)

(d/q '[:find (pull ?e [*])
       :where
       [?e :product-task/flow_id]]
     @conn)

(def q-young '[:find ?n
               :in $ ?min-age
               :where
               [?e :name ?n]
               [?e :age ?a]
               [(< ?a ?min-age)]])

(d/q q-young @conn 18)
