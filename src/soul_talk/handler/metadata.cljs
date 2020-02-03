(ns soul-talk.handler.metadata
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.util.route-utils :refer [run-events run-events-admin logged-in? navigate!]]
             [soul-talk.util.query-filter :as query-filter]
             [soul-talk.models :refer [model-register]]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]))


(reg-event-db
 :metadata/dto
 (fn [db [_ response]]
   (let [dataset  (get-in response [:result])
         state  (get response :query)]

     (assoc-in db [:metadata]   (set dataset)))))

(reg-event-fx
 :metadata/server.query
 (fn [_ [_]]
   {:http {:method    GET
           :url   "http://0.0.0.0:3000/table-fields"
           :ajax-map       {;;:params query
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:metadata/dto]}}))

(reg-event-db
 :metadata/update
 (fn [db [_ query update-form]]
   (let [data-path [:metadata]
         all-data (get-in db data-path)]
     (update-in
      db
      data-path
      (fn [all-data]
        (map (fn [item]
               (if (query-filter/is-part-of-query? item query)
                 (merge item update-form)
                 item)) all-data))))))

(reg-event-db
 :metadata/new
 (fn [db [_ item]]
   (let [data-path [:metadata]]
     (update-in db data-path conj item))))

(reg-event-db
 :metadata/delete
 (fn [db [_ query]]
   (let [data-path [:metadata]
         all-data (get-in db data-path)]
     (assoc-in  db data-path
                (filter #(query-filter/not-part-of-query?  % query)   all-data)))))



