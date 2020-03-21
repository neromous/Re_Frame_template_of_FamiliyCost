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
    [sub> act>  to-columns fix-key fix-value] :as rh]
   [soul-talk.util.query-filter :as query-filter]))


;; (first (rh/dsub> {:query '[:find (pull ?e [*])
;;                            :where
;;                            [?e :task/eid]]}))

;; (first @(sub> :task.all))

;; (first @(sub> :order.columns))


(sub> :order.eid.task 1)
(sub> :select :process/eid 1)
(sub> :order.eid.process 1)

;; (first @(sub> :select-many :order/eid   ))
;; (sub>  :q '[:find (pull ?e [*]) .
;;             :in $ ?id  ?id1
;;             :where
;;             [?e :order/eid ?id]]   1 2)

;; (sub>  :pull-many [1 2])

;; (act> :d/datalog   {:query '[:find (pull ?e [*])
;;                              :where
;;                              [?e :org/eid]]})

;; (act> :d/datalog   {:query '[:find (pull ?e [*])
;;                              :where
;;                              [?e :material/eid]]})

;; (act> :d/datalog   {:query '[:find (pull ?e [*])
;;                              :where
;;                              [?e :org/id.customer]]})

;; (act> :d/datalog   {:query '[:find (pull ?e [*])
;;                              :where
;;                              [?e :org/id.provider]]})

;; (act> :d/datalog   {:query '[:find (pull ?e [*])
;;                              :where
;;                              [?e :human/eid]]})

;; (act> :d/datalog   {:query '[:find (pull ?e [*])
;;                              :where
;;                              [?e :order/eid]]})

;; (act> :d/datalog   {:query '[:find (pull ?e [* {:task/ref.order [*]}])
;;                              :where
;;                              [?e :task/eid]]})

;; (act> :d/datalog   {:query '[:find (pull ?e [*  {:process/ref.task [*] }  ])
;;                              :where
;;                              [?e :process/eid]]})

;; (act> :d/datalog   {:query '[:find (pull ?e [*])
;;                              :where
;;                              [?e :process/eid]]})
