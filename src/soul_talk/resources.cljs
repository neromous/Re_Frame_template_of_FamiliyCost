(ns soul-talk.resources
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.util.route-utils :refer [run-events run-events-admin logged-in? navigate!]]
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
 :resource/dto
 (fn [db [_ model-key response]]
   (let [model (get model-register model-key)
         data-path (get model :data-path)
         view-path (get model :view-path)
         data-state-path (concat view-path [:data-state])
         dataset  (get-in response [:result])
         state  (get response :query)]
     (-> db
         (assoc-in  data-path   dataset)
         (assoc-in  data-state-path state)))))

(reg-event-fx
 :resource/server.query
 (fn [_ [_ model-key query]]
   {:http {:method    POST
           :url   (get-in model-register [model-key :url])
           :ajax-map       {:params query
                            :keywords? true
                            :format (json-request-format)
                            :response-format :json}
           :success-event [:resource/dto model-key]}}))

