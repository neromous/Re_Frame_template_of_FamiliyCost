(ns todomvc.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))




(reg-sub
 :showing          ;; usage:   (subscribe [:showing])
 (fn [db _]        ;; db is the (map) value stored in the app-db atom
   (:showing db))) ;; extract a value from the application state


(defn sorted-todos
  [db _]
  (:todos db))

(reg-sub :sorted-todos sorted-todos)    ;; usage: (subscribe [:sorted-todos])

(reg-sub
 :todos        ;; usage:   (subscribe [:todos])
 (fn [query-v _]
   (subscribe [:sorted-todos]))    ;; returns a single input signal
 (fn [sorted-todos query-v _]
   (vals sorted-todos)))

(subscribe [:todos])

;; (reg-sub
;;  :visible-todos

;;  (fn [query-v _]
;;    [(subscribe [:todos])
;;     (subscribe [:showing])])

;;  (fn [[todos showing] _]   ;; that 1st parameter is a 2-vector of values
;;    (let [filter-fn (case showing
;;                      :active (complement :done)
;;                      :done   :done
;;                      :all    identity)]
;;      (filter filter-fn todos))))

;; (subscribe [:visible-todos])


(reg-sub
 :visible-todos
 :<- [:todos]
 :<- [:showing]
 (fn [[todos showing] [arg1 arg2]  ]
   (let [filter-fn (case showing
                     :active (complement :done)
                     :done   :done
                     :all    identity)]
     (filter filter-fn todos))))

(subscribe [:visible-todos :dddi :dddd :ddd] )


;; (reg-sub
;;  :all-complete?
;;  :<- [:todos]
;;  (fn [todos _]
;;    (every? :done todos)))

;; (reg-sub
;;  :completed-count
;;  :<- [:todos]
;;  (fn [todos _]
;;    (count (filter :done todos))))

;; (reg-sub
;;  :footer-counts
;;  :<- [:todos]
;;  :<- [:completed-count]
;;  (fn [[todos completed] _]
;;    [(- (count todos) completed) completed]))
