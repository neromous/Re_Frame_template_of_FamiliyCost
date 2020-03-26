(ns soul-talk.handler.datomic
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

;; (rd/reg-event-ds
;;  :d/add-field
;;  (fn [ds [_ id path value]]
;;    [[:db/add id path value]]))

;; (rd/reg-event-ds
;;  :d/add-entity
;;  (fn [ds [_ form]]
;;    (let [form (dissoc form :db/id)]
;;      [form])))

;; (rd/reg-event-ds
;;  :d/update-entity
;;  (fn [ds [_ id form]]
;;    (let [form (assoc form :db/id id)]
;;      [form])))

(reg-event-fx
 :d/query.mdw
 (fn [_ [_ resp]]
   (let [dataset (get resp :dataset)
         dataset (if (map? (first dataset))
                   dataset
                   (reduce into [] dataset))
         dataset  (into [] (comp remove-db_id) dataset)]
     {:datomic/transact   dataset})))


(reg-event-fx
 :d/calc.mdw
 (fn [_ [_ resp]]
   (let [dataset (get resp :dataset)
         dataset (if (map? (first dataset))
                   dataset
                   (reduce into [] dataset))
         dataset  (into [] (comp remove-db_id) dataset)]
     {:datomic/transact   dataset})))


(reg-event-fx
 :d/datalog
 (fn [_ [_  form]]
   (let [url "http://localhost:3000/api/datomic-api/query"]
     {:http {:method  POST
             :url url
             :ajax-map       {:params form
                              :format (edn-request-format)
                              :Accept "application/edn"
                              :response-format (edn-response-format)}
             :success-event [:d/query.mdw]}})))

(reg-event-fx
 :d/calc
 (fn [_ [_  form]]
   (let [url "http://localhost:3000/api/datomic-api/query"]
     {:http {:method  POST
             :url url
             :ajax-map       {:params form
                              :format (edn-request-format)
                              :Accept "application/edn"
                              :response-format (edn-response-format)}
             :success-event [:d/calc.mdw]}})))





;; (def path-prefix  [:storage])

;; (defn path [sub-path]
;;   (if (vector? sub-path)
;;     (concat path-prefix sub-path)
;;     (conj path-prefix sub-path)))



;; (reg-event-db
;;  :mdw/datomic
;;  (fn [db [_ sub-path resp]]
;;    (let [data-path (path sub-path)
;;          dataset (get resp :dataset)
;;          dataset (apply concat dataset)]
;;      (assoc-in  db data-path dataset))))

;; (reg-event-fx
;;  :d/transact
;;  (fn [_ [_ sub-path form]]
;;    (let [url "http://localhost:3000/api/datomic-api/query"]
;;      {:http {:method    PUT
;;              :url url
;;              :ajax-map       {:params form
;;                               :format (edn-request-format)
;;                               :Accept "application/edn"
;;                               :response-format (edn-response-format)}
;;              :success-event [:mdw/datomic sub-path]}})))

;; (reg-event-fx
;;  :d/datalog
;;  (fn [_ [_ sub-path form]]
;;    (let [url "http://localhost:3000/api/datomic-api/query"]
;;      {:http {:method    POST
;;              :url url
;;              :ajax-map       {:params form
;;                               :format (edn-request-format)
;;                               :Accept "application/edn"
;;                               :response-format (edn-response-format)}
;;              :success-event [:mdw/datomic sub-path]}})))


;; (reg-sub
;;  :d/get
;;  (fn [db [_ sub-path]]
;;    (let [data-path  (path sub-path)]
;;      (get-in db data-path))))

