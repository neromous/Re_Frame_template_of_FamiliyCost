(ns soul-talk.model.base
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]))

(defn item->id [item]
  (-> item :id str keyword))

(defn map->method [object prototype-map]
  (doseq [[k v] prototype-map]
    (defmethod object k [_ & x] v)))

(defn model-init [object   model-map  prefix]
  (let [model-name (:key model-map)
        prefix prefix]
    (do
      (defmethod object :default [_ & x] (concat prefix x))
      (defmethod object :prototype [_] model-map)
      ;; 自省  查看自己有多少方法和属性 并且调用那些 基本上不推荐使用
      (defmethod object :attrbs [_] (methods object))
      (defmethod object :getattrb [_ attrb-key]  (get attrb-key (methods object)))
      ;; 路径管理 主要用于管理针对整体db的映射
      (defmethod object :db [_ & x] (concat prefix x))
      (defmethod object :db.datasets [_ & x] (concat prefix [:datasets] x))
      (defmethod object :db.url [_ & x] (concat prefix [:url] x))
      (defmethod object :db.states [_ & x] (concat prefix [:states] x))
      (defmethod object :db.modals [_ & x] (concat prefix [:modals] x))
      (defmethod object :db.cache [_ & x] (concat prefix [:cache] x))
      ;; 数据操作
      ;;;; 主要用于数据层面的操作

      (defmethod object :data.init
        [_ new-map]
        [:db/assoc-in  (object :db.datasets) new-map])

      (defmethod object :data.delete
        [_ id]
        [:db/dissoc-in  (object :db.datasets id)])

      (defmethod object :data.new
        [_ item]
        [:db/assoc-in  (object :db.datasets (item->id item)) item])

      (defmethod object :data.update
        [_ id item]
        [:db/merge-in  (object :db.datasets  id) item])

      (defmethod object :data.all
        [_]
        [:db/get-in (object :db.datasets)])

      ;; 状态操作
      (defmethod object :state.change
        [_  & kv]
        (let [prefix-path (object :db.states)
              kv-path (drop-last kv)
              full-path (concat prefix-path kv-path)]
          [:db/assoc-in full-path (last kv)]))

      (defmethod object :state.get
        [_  & ks]
        (let [prefix-path (object :db.states)
              full-path (concat prefix-path ks)]
          [:db/get-in full-path]))



      (defmethod object :state.delete
        [_ & ks]
        [:db/dissoc-in  (apply  object (cons :db.states ks))])

      (defmethod object :states.copy
        [_ pk-target pk-origin]
        (let [target (object :db.states pk-target)
              origin (object :db.states pk-origin)]
          [:paths/copy target origin]))

      (defmethod object :states.move
        [_ subpk-target subpk-origin]
        (let [target (concat (object :db.states) subpk-target)
              origin (concat (object :db.states) subpk-origin)]
          [:paths/move target origin]))
      ;;
      )))



;; (account :attrbs)
;; (account :states.copy [:2] [:3])

;; (dispatch (account :state.change :2 "dfasdfasd"))

;; (dispatch (account :states.copy :3 :2 ))


;; (defmulti path (fn [x _] x))

;; (defmethod path :default [root & x] (cons root x))

;; (defn path-register [model-key]
;;   (let [model-name (name model-key)
;;         mk (keyword (str "md" "/" model-name))
;;         prefix (fn [method] (keyword (str model-name "/" method)))]
;;     (do
;;       (defmethod path model-key [_ & x] (cons mk x))
;;       (defmethod path (prefix "datasets") [_] [mk :datasets])
;;       (defmethod path (prefix "datasets-subkey") [_ id] [mk :datasets id])
;;       (defmethod path (prefix "url") [_] [mk :url])
;;       (defmethod path (prefix "states") [_] [mk :states])
;;       (defmethod path (prefix "modals") [_] [mk :modals])
;;       (defmethod path (prefix "cache") [_] [mk :cache])
;;       (defmethod path (prefix "states-subkey") [_ subkey] [mk :states subkey])
;;       (defmethod path (prefix "pagination") [_] [mk :pagination]))
;;     ;;
;;     ))

;; (path-register :account)
;; (path-register :record)
;; (path-register :category)

