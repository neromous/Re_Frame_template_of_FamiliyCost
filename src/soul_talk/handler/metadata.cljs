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
         data dataset
         dataset (group-by :table_name dataset)
         x (keys dataset)
         y (vals dataset)

         dataset (zipmap x  (map
                             (fn [z]
                               (zipmap (map :column_name z)
                                       z))
                             y))
         state  (get response :query)]

     (-> db
         (assoc-in  [:metadata]   dataset)
         (assoc-in  [:metadata.unique :data_type]
                    (-> (map :data_type data)
                        set))
         (assoc-in  [:metadata.unique :view_type]
                    (-> (map :view_type data)
                        set))
         (assoc-in  [:metadata.unique :entity_type]
                    (-> (map :entity_type data)
                        set))

         ;;
         ))))

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
 :metadata/table.new
 (fn [db [_ table-name]]
   (let [data-path [:metadata]]
     (update-in db (conj data-path table-name) merge {})
     ;;
     )))

(reg-event-db
 :metadata/column.new
 (fn [db [_ table-name column-name form]]
   (let [data-path [:metadata]]
     (assoc-in db (concat data-path [table-name column-name]) form)
     ;;
     )))

(reg-event-db
 :metadata/column.update
 (fn [db [_ table-name column-name form]]
   (let [data-path [:metadata]]
     (update-in db (concat data-path [table-name column-name])  merge form)
     ;;
     )))

(reg-event-db
 :metadata/column.delete
 (fn [db [_ query]]
   (let [data-path [:metadata]
         all-data (get-in db data-path)]
     (assoc-in  db data-path
                (filter #(query-filter/not-part-of-query?  % query)   all-data)))))

(reg-event-db
 :metadata/relation.new
 (fn [db [_ form]]
   (let [data-path [:metadata.relation]
         id (:id form)]
     (assoc-in db (conj data-path id)  form))))

;; (reg-sub
;;  :metadata/relation.delete
;;  (fn [db [_ id]]
;;    (let [data-path [:metadata.relation]
;;          ]
;;      (update-in db  data-path  dissoc id ))))



