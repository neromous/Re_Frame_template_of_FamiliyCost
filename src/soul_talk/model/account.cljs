(ns soul-talk.model.account
  (:require [soul-talk.model.base :as base]))

(def Account
  {:name :account
   :title "账户"
   :root-path [:md/account]
   :url "http://localhost:8000/api/v1/Account/"
   :template.fields [{:title "id" :dataIndex "id" :key "id"}
                     {:title "账户名称" :dataIndex "name" :key "name"}
                     {:title "额度" :dataIndex "quota" :key "quota"}
                     {:title "账户类型" :dataIndex "accountType" :key "accountType"}]
   :template.item {:id "账户id"
                   :name "账户"
                   :quota "额度"
                   :accountType "账户类别"}})

(defmulti account (fn [x _] x))
(base/model-init account Account)

(defmulti record (fn [x _] x))

(def Record
  {:name :record
   :root-path [:md/record]
   :url "http://localhost:8000/api/v1/CostRecord/"
   :template.fields   [{:title "id" :dataIndex "id" :key "id"}
                       {:title "账户名称" :dataIndex "name" :key "name"}
                       {:title "交易额" :dataIndex "costValue" :key "costValue"}
                       {:title "账单录入日期" :dataIndex "billTime" :key "billTime"}
                       {:title "操作" :dataIndex "actions" :key "actions"}]
   :template.item {:id "记录id"
                   :name "记录名称"
                   :costValue "记录金额"
                   :billTime "记录时间"}})

(defmulti record (fn [x _] x))
(base/model-init record Record)

(def Category
  {:name :category
   :root-path [:md/category]
   :url "http://localhost:8000/api/v1/Category/"})

(defmulti category (fn [x _] x))
(base/model-init category Category)



