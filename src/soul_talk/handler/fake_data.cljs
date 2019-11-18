(ns soul-talk.handler.fake-data
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.db :refer [api-uri]]
             [soul-talk.utils :refer [mapset2map]]
             [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]))

(defonce unique-work (r/atom 0))
(defn unique-id []
  (swap! unique-work inc))

(reg-event-fx
 :fake/sync-pull
 (fn [cofx [_ model args]]
   {:db (assoc-in (:db cofx) (model :db.datasets)   (model :fake-dataset))}))

(reg-event-fx
 :fake/new
 (fn [cofx [_  model item]]

   (let [id (unique-id)]
     {:db (assoc-in (:db cofx) (model :db.datasets  (-> id str keyword)  ) (assoc item :id id))})))

(reg-event-fx
 :fake/delete
 (fn [cofx [_  model id]]
   {:db (update-in (:db cofx) (model :db.datasets) dissoc  id)}))

(reg-event-fx
 :fake/update
 (fn [cofx [_  model id item]]
   {:db (update-in (:db cofx) (model :db.datasets id)  merge item)}))
