(ns soul-talk.util.date-utils
  (:require [cljs-time.format :as cf :refer [parse
                                             unparse
                                             formatter
                                             formatters
                                             show-formatters]]
            [soul-talk.utils :as utils]
            [cljs-time.coerce :as tc]))

(defn to-moment
  [date]
  (cond
    (nil? date)  nil
    (= date "") nil
    :default    (->>  date
                      js/moment.)))

(defn moment->datetime
  [date]
  (cond
    (nil? date) nil
    (= date "") nil
    :default (-> date
                 (.format  "YYYY-MM-DDTHH:mm:ssZ"))))

(defn moment->date
  [date]
  (cond
    (nil? date) nil
    (= date "") nil
    :default (-> date
                 (.format  "YYYY-MM-DD"))))

(defn str->date
  [date ]
  (cond
    (nil? date)  nil
    (= date "") ""
    :default    (->> date
                     tc/from-string
                     (unparse (cf/formatters :year-month-day)))))

(defn str->date-time
  [date]
  (cond
    (nil? date)  nil
    (= date "") ""
    :default    (->> date
                     tc/from-string
                     (unparse (cf/formatters :mysql)))))




