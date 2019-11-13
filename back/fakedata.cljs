(ns soul-talk.fakedata
  (:require  [soul-talk.utils :as utils]
             [soul-talk.components.card :as card]
             [soul-talk.components.table :as table]))

(def sales-detail
  {:columns ["客户id"
             "品种"
             "地区编码"
             "客户名称"
             "单价"
             "销售收入"
             "运输方式"
             "销量"]
   :axis ["时间"]
   :filters ["地区编码"
             "客户id"
             "运输方式"]
   :group ["地区编码"
           "客户id"
           "运输方式"
           "品种"]
   :calc ["销量"
          "销售收入"
          "订单数量"]}
  )

(def sales-data
  [{:客户id "001"
    :品种  "r001"
    :地区编码 "501"
    :客户名称 "众富"
    :子订单编号 "2019002"
    :销售单价 7.2
    :销售收入 1400
    :运输方式 "火车"
    :销量 "2000"
    :时间 "2019-02-02"}
   {:客户id "002"
    :品种  "b003"
    :地区编码 "729"
    :客户名称 "众泰"
    :子订单编号 "2019002"
    :销售单价 7.2
    :销售收入 1400
    :运输方式 "汽车"
    :销量 "2000"
    :时间 "2019-02-02"}])

;; (def fake-table
;;   (table/fake-table sales-detail sales-data (utils/columns-fake-maker sales-detail sales-data)))

;; (def fake-table-calc
;;   (table/fake-table-sum "客户名称" sales-detail  sales-data))

