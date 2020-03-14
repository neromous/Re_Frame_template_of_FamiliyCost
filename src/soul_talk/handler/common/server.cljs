(ns soul-talk.handler.common.server
  (:require [ajax.core :refer [POST
                               GET
                               DELETE
                               PUT
                               ajax-request
                               url-request-format
                               json-request-format
                               json-response-format]]))

(defn server-pull!
  [url-prefix]
  (let []
    (fn [_ [_ model-key query]]
      {:http {:method    method
              :url     url-prefix
              :ajax-map       {:params query
                               :keywords? true
                               :format (json-request-format)
                               :response-format :json}
              :success-event [:model/mdw.replace model-key]}}
      ;;
      )))


