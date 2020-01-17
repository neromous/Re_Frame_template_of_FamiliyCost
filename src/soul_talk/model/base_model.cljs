(ns soul-talk.model.base-model
  (:require
   [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
   [soul-talk.model.sample :refer [meta-data]]))

(defn item->id [item]
  (-> item (get :_id)))

(defn set-attrib [meta-data model-key  obj]
  (let [model-map   (get meta-data model-key)
        columns     (get model-map :columns)
        interface   (get model-map :interface)
        db-path     (concat [:models]  (get interface :root-path))
        state-path  (concat [:states] (get interface :root-path))]
    (-> obj
        (defmethod :default [_ & x] (concat prefix x))

        (defmethod :db-path [_  & x] (concat db-path x))

        (defmethod :state-path [_  & x] (concat state-path x))

        (defmethod :db.init
          [_ new-data]
          [:db/assoc-in (obj :db-path) new-data])

        (defmethod  :db.del
          [_  id]
          [:db/dissoc-in  (obj :db-path id)])

        (defmethod  :db.new
          [_  item]
          [:db/assoc-in  (obj :db-path (item->id item)) item])

        (defmethod  :db.update
          [_ id item]
          [:db/merge-in  (obj :db-path  id) item])

        (defmethod :db.find-by
          [_ query-map]
          [:db/find-by (obj :db-path) query-map])

        (defmethod :server.pull
          [_ pull-request]
          [:server/pull model-key  request-map])

        ;;
        )))


;; (defmulti uu-test (fn [x _] x))
;; (set-attrib meta-data :organization uu-test)

;; (uu-test :db.find-by {})



