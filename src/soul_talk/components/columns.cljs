(ns soul-talk.components.columns
  (:require    [re-frame.core :refer [dispatch dispatch-sync subscribe]]))

(defn vec-apply-set
  "对columns数据进行局部修正的函数"
  [series query-vec  form]
  (let [target?  (fn [x] (-> x (get (first query-vec)) (= (second query-vec))))
        func     (fn [x] (if (target? x)
                           (merge x form)
                           x))]
    (println target?)
    (map func series)))

(defn add-filter
  "添加过滤器
  filter: 里面的是函数
  "
  [table-config  query-vec   on-filter  filters config]
  (let [form  {:filters filters
               :onFilter on-filter}

        series (get table-config :columns)]
    (assoc-in table-config [:columns]  (vec-apply-set series query-vec form))))

(defn add-sorter
  "添加排序工具"
  [table-config query-vec sorter-func config]
  (let [form  {:sorter sorter-func
               :sortDirections ["descend"  "ascend"]}
        series (get table-config :columns)]
    (assoc-in table-config [:columns]  (vec-apply-set series query-vec form))))

;; ;; 添加排序工具
;; (columns/add-sorter
;;  [:key "name"]
;;  (fn [a b]
;;    (let [a-value (-> a js->clj (get "name") str)
;;          b-value (-> b js->clj (get "name") str)]
;;      (- (count a-value) (count b-value))))
;;  {}
;;  )
;; ;; 添加过滤器
;; (columns/add-filter
;;  [:key "name"]
;;  ;; 过滤函数 value是选定值  record是data对象
;;  (fn [value record]
;;    (let [record-value (-> record js->clj (get "name"))]
;;      (=  record-value value)))
;;  ;; 过滤器的列表内容
;;  (->> data
;;       vals
;;       (map (fn [x] {:text (-> x (get "name") str)
;;                     :value (-> x (get "name") str)}))
;;       set)
;;  {}
;;  )
 
