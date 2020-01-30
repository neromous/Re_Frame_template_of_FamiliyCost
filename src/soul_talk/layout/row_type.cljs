(ns soul-talk.layout.row-type
  (:require
   [soul-talk.util.route-utils :refer [run-events run-events-admin logged-in? navigate!]]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.utils :as utils]
   [reagent.core :as r]
   [re-frame.core :as rf]))


(defn input?>
  [col-1  label-form
   col-2  input-form]
  [:> js/antd.Row
   [:> js/antd.Col  col-1
    label-form]
   [:> js/antd.Col  col-2
    input-form]]
  )

(defn row2>
  [col-1  column1
   col-2  column2]
  [:> js/antd.Row
   [:> js/antd.Col col-1
    column1]
   [:> js/antd.Col col-2
    column2]])

(defn row3>
  [col-1   column1
   col-2   column2
   col-3   column3]
  [:> js/antd.Row
   [:> js/antd.Col col-1
    [column1]]
   [:> js/antd.Col col-2
    [column2]]
   [:> js/antd.Col col-3
    [column3]]])

(defn row4>
  [col-1   column1
   col-2   column2
   col-3   column3
   col-4   column4]
  [:> js/antd.Row
   [:> js/antd.Col col-1
    [column1]]
   [:> js/antd.Col col-2
    [column2]]
   [:> js/antd.Col col-3
    [column2]]
   [:> js/antd.Col
    [column3]]
   [:> js/antd.Col col-4
    [column4]]])


