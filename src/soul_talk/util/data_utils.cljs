(ns soul-talk.util.data-utils)

(defn to-2-float
  [f]
  (/ (.round js/Math (* 100 f)) 100))


(defn to-float
  [f]
  (js/parseFloat f))


(def sql_type->type
  {"varchar"  str
   "text"  str
   "int"   int
   "int4"  int
   })

(def trans-map
  [[:plan_time  int]
   [:id  int]])

(defn dto
  [item trans-map]
  (reduce (fn [x [k func]]  (assoc x k (-> x (get k)   func)))
          item trans-map))





