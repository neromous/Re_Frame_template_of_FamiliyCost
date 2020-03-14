(ns soul-talk.util.serializer
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [soul-talk.util.date-utils :as du]
             [reagent.core :as r]))

(defmulti serializer (fn [x & _] x))

(defmethod serializer :int [_ v]  (js/parseInt v))

(defmethod serializer :float [_ v]  (js/parseFloat v))

(defmethod serializer :text [_ v]  (str v))

(defmethod serializer :keyword [_ v]  (keyword v))

(defmethod serializer :date [_ v] (du/str->date v))

(defmethod serializer :datetime [_ v] (du/str->date-time v))

(defmethod serializer :moment [_ v] (du/to-moment v))

(defmethod serializer :default [_ v]  v)



