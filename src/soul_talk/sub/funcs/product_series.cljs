(ns soul-talk.sub.funcs.product-series
  (:require   [re-frame.core :refer [inject-cofx
                                     dispatch
                                     dispatch-sync
                                     reg-event-db
                                     reg-event-fx
                                     subscribe reg-sub]]
              [soul-talk.util.data-formatter :as formatter]))

;;================================
(defn find-by
  ([all-data  k v]
   (find-by all-data {k v}))
  ([all-data  query]
   (filter #(= (select-keys % (keys query)) query) all-data)))

(defn find-kv [all-data  k v]
  (filter #(= (get  % k)  v) all-data))

(defn find-one [all-data query]
  (first (filter #(= (select-keys % (keys query)) query) all-data)))

(defn series->sum-by-key [all-data sum-key]
  (-> all-data
      (->> (map #(get % sum-key))
           (filter #(-> % nil? not))
           (filter #(not= % ""))
           (map js/parseFloat)
           (apply +))))

(defn series->count [all-data [_]]
  (count all-data))

(def series-filter
  {"重染" #(find-by %  :is_re_dye 2)
   "未重染" #(find-by % :is_re_dye 1)
   "下单公司:泰安" {:order_company_id 3}
   "下单公司:新泰" {:order_company_id 35}
   "生产公司:泰安"  {:product_company_id 3}
   "生产公司:新泰"  {:product_company_id 35}
   ;;
   })

(def named-result
  {})

;; ================================================
;; 前处理过程
;; ================================================

(defn sell-order [all-data]
  (-> (map #(select-keys %  [:order_id
                             :contract_number
                             :order_number
                             :order_seller
                             :order_is_delete
                             :order_detail_id
                             :order_company_id
                             :order_saler_id
                             :goods_id
                             :goods_name
                             :color_number
                             :customer_color
                             :tax_price
                             :tax_money
                             :customer_name
                             :customer_id
                             :order_detail_weight
                             ;;:company_id
                             :order_time
                             :finish_time])
           all-data)
      set))

(defn product-flow [all-data]
  (-> (map #(select-keys %  [:order_id
                             :order_detail_id
                             :job_order_id
                             :flow_id
                             :sheet_id
                             :order_number
                             :dyelot_number
                             :order_company_id
                             :order_saler_id
                             :goods_id
                             :goods_name
                             :color_number
                             :customer_color
                             :customer_name
                             :customer_id
                             :order_detail_weight
                             :job_order_weight
                             :flow_plan_release
                             :flow_final_weight
                              ;; 地点信息
                             :company_id
                             :factory_id
                             :workshop_id])
           all-data)
      set))

(defn product-order [all-data]
  (-> (map #(select-keys %  [:order_id
                             :order_detail_id
                             :order_company_id
                             :product_company_id
                             :product_factory_id
                             :product_workshop_id
                             :job_order_id
                             :order_number
                             :goods_id
                             :goods_name
                             :order_company_id
                             :order_saler_id
                             :color_number
                             :customer_color
                             :tax_price
                             :tax_money
                             :customer_name
                             :customer_id
                             :order_detail_weight
                             :job_order_weight])
           all-data)
      set))

;; ============================================================
;;  序列过滤器
;; =============================================================

(defn is_re_dye [all-data]
  (find-by all-data :is_re_dye 2))

(defn not_re_dye [all-data]
  (find-by all-data :is_re_dye 1))

(defn order-company->TaiAn
  ([all-data]
   (find-by  all-data :order_company_id 3)))

(defn order-company->XinTai
  ([all-data]
   (find-by  all-data :order_company_id 35)))

(defn product-company->TaiAn [all-data]
  (find-by all-data :product_company_id 3))

(defn product-company->XinTai [all-data]
  (find-by all-data :product_company_id 35))

;; ===========================
;;
;; ===========================

(defn flow_id [all-data id]
  (find-by all-data :flow_id id))

(defn order_id [all-data id]
  (find-by all-data :order_id id))

(defn order_detail_id [all-data id]
  (find-by all-data :order_detail_id id))

(defn job_order_id [all-data id]
  (find-by all-data :job_order_id id))

;; =================================
;;  计算
;; ==================================

(defn count-order_id [all-data]
  (-> (map :order_id  all-data)
      set
      count))

(defn count-order_detail_id [all-data]
  (-> (map :order_detail_id  all-data)
      set
      count))

(defn count-flow_id [all-data]
  (-> (map :flow_id  all-data)
      set
      count))

(defn count-job_order_id [all-data]
  (-> (map :job_order_id  all-data)
      set
      count))

;;=======================
;;
;;========================


(defn tax_money
  ([all-data]
   (-> (series->sum-by-key all-data :tax_money)
       formatter/round-number)))

(defn flow_plan_release
  ([all-data]
   (-> (series->sum-by-key all-data :flow_plan_release)
       formatter/round-number)))

(defn flow_final_weight
  ([all-data]
   (-> (series->sum-by-key all-data :flow_final_weight)
       formatter/round-number)))

(defn order_detail_weight
  ([all-data]
   (-> (series->sum-by-key all-data :order_detail_weight)
       formatter/round-number)))

(defn job_order_weight
  ([all-data]
   (-> (series->sum-by-key all-data :job_order_weight)
       formatter/round-number)))
