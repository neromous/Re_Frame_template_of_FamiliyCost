(ns soul-talk.model.account
  (:require [soul-talk.model.base :as base]))

(defmulti account (fn [x _] x))

(def Account
  {:key :md/account
   :url "http://localhost:8000/api/v1/Account/"})

(base/model-init account {} [:md/account])
(base/map->method  account Account)

(defmulti record (fn [x _] x))

(def Record
  {:key :md/record
   :datasets {}
   :url "http://localhost:8000/api/v1/CostRecord/"})
(base/model-init record {} [:md/record])
(base/map->method  record Record)


