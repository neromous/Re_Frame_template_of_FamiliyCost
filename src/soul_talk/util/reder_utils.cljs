(ns soul-talk.util.render-utils
  (:require [cljs-time.format :as cf :refer [parse
                                             unparse
                                             formatter
                                             formatters
                                             show-formatters]]
            [reagent.core :as r]
            [cljs-time.coerce :as tc]))

(defn utc->date  [data _]
  (r/as-element  (-> data js/moment. (.format "YYYY-MM-DD"))))

(defn utc->date-time  [data _]
  (r/as-element  (-> data js/moment. (.format "YYYY-MM-DD HH:mm:ss"))))

