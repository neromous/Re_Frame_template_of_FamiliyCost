(ns soul-talk.handler.cost-human
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]
             [soul-talk.utils :as utils]))


(reg-sub
 :cost.human/all
 (fn [db [_]]
   (get-in  db [:cost.human :datasets])))

