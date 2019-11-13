(ns soul-talk.model.handler
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [soul-talk.models.init :refer [model-path]]
             [soul-talk.db :refer [api-uri]]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]))

(reg-event-fx
 :parser/response-parser-replace
 (fn [db [_  view-key model-key response]]
   (dispatch [:model/replace-all-by-list model-key (:results response)])
   (dispatch [:view/model-set-pagination view-key model-key (dissoc  response  :results)])))

(reg-event-fx
 :parser/response-parser-update
 (fn [db [_  view-key model-key response]]
   (dispatch [:model/update-all-by-list model-key (:results response)])))

(reg-event-fx
 :server/sync-pull-replace
 (fn [_ [_  view-key model-key args]]
   {:http {:method        GET
           :url           (str api-uri "/Contact/")
           :ajax-map      {:params args
                           :keywords? true
                           :response-format :json}
           :success-event [:parser/response-parser-replace view-key model-key]}}))

(reg-event-fx
 :server/sync-pull-update
 (fn [_ [_  view-key model-key args]]
   {:http {:method        GET
           :url           (str api-uri "/Contact/")
           :ajax-map      {:params args
                           :keywords? true
                           :response-format :json}
           :success-event [:parser/response-parser-update view-key model-key]}}))



;; (dispatch [:server/sync-pull-replace :homepage :test {:offset 0
;;                                               :limit 10}])

;; (dispatch [:server/sync-pull-update :homepage :test {:offset 10
;;                                                      :limit 10}])
;; (subscribe [:view/model-pagination :homepage :test])
;; (subscribe [:model/all :test])
