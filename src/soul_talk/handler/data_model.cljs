(ns soul-talk.handler.data-model
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.db :refer [api-uri]]
             [soul-talk.utils :refer [mapset2map]]
             [soul-talk.model.account :refer [account]]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]))

(reg-event-fx
 :mdw/django-response-parser
 (fn [db [_ model response]]
   (do
     (dispatch (model :data.init  (-> response :results mapset2map)))
     (dispatch (model :state.change :pagination (dissoc response :results))))))

(reg-event-fx
 :server/dataset-find-by
 (fn [_ [_  model args]]
   {:http {:method        GET
           :url           (model :url)
           :ajax-map      {:params args
                           :keywords? true
                           :response-format :json}
           :success-event [:mdw/django-response-parser model]}}))



(reg-event-fx
 :server/delete
 (fn [_ [_ model id]]
   {:http {:method   DELETE
           :url      (str (model :url)  (name id))
           :ajax-map       {;:params params
                            :keywords? true
                            :response-format :json}

           :success-event (model :data.delete id)}}))

(reg-event-fx
 :mdw/django-response-add-parser
 (fn [db [_ model response]]
   (println response)
   (let [id (-> response :id str keyword)]
     (dispatch [:db/assoc-in [model :datasets id] response]))))

(reg-event-fx
 :server/new
 (fn [_ [_ model item]]
   {:http {:method        POST

           :url           (model :url)
           :ajax-map       {:params item
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}

           :success-event [:mdw/django-response-add-parser model]}}))

(reg-event-fx
 :server/update
 (fn [_ [_ model  id item]]
   {:http {:method        PUT
           :url      (str (model :url) (name id) "/")
           :ajax-map       {:params (dissoc item :id)
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}

           :success-event [:mdw/django-response-add-parser model]}}))








