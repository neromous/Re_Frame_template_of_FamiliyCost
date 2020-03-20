(ns soul-talk.test
  (:require
   [reagent.core :as r]
   [goog.events :as events]
   [clojure.string :as str]
   [secretary.core :as secretary :refer-macros [defroute]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   soul-talk.subs
   soul-talk.handlers
   [accountant.core :as accountant]
   [soul-talk.util.route-utils :refer [run-events
                                       run-events-admin
                                       logged-in?
                                       navigate!]]
   [soul-talk.util.reframe-helper :refer
    [sub> act> dsub dget dset to-columns fix-key fix-value]]
   [soul-talk.util.query-filter :as query-filter]))

(act> :d/datalog   {:query '[:find (pull ?e [*])
                             :where
                             [?e :org/eid]]})

(act> :d/datalog   {:query '[:find (pull ?e [*])
                             :where
                             [?e :material/eid]]})

(act> :d/datalog   {:query '[:find (pull ?e [*])
                             :where
                             [?e :org/id.customer]]})

(act> :d/datalog   {:query '[:find (pull ?e [*])
                             :where
                             [?e :org/id.provider]]})

(act> :d/datalog   {:query '[:find (pull ?e [*])
                             :where
                             [?e :human/eid]]})

(act> :d/datalog   {:query '[:find (pull ?e [*])
                             :where
                             [?e :order/eid]]})

(act> :d/datalog   {:query '[:find (pull ?e [*])
                             :where
                             [?e :task/eid]]})

(act> :d/datalog   {:query '[:find (pull ?e [*])
                             :where
                             [?e :process/eid]]})

(act> :d/datalog   {:query '[:find (pull ?e [*])
                             :where
                             [?e :process/eid]]})







