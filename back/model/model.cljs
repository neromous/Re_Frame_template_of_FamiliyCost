(ns soul-talk.model.model
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [soul-talk.models.init :refer [model-path]]
             [soul-talk.utils :as utils]
             [ajax.core :refer [POST GET DELETE PUT]]))

(defmethod model-path ::data [m] [:models (:model-key m) :data])
(defmethod model-path ::state [m] [:models (:model-key m) :state])
(defmethod model-path ::state-api [m] [:models (:model-key m) :state :api])

(defmethod model-path ::id-key [m] [:models (:model-key m) :data (-> m :id)])
(defmethod model-path ::id-raw [m] [:models (:model-key m) :data (-> m :id str keyword)])
(defmethod model-path ::id-item [m] [:models (:model-key m) :data (-> m :item :id keyword)])

;; entity层 操作层


(reg-event-db
 :model/global-init!
 (fn [db [_ _]]
   (-> db
       (assoc  :models {}))))

(reg-event-db
 :model/init
 (fn [db [_ model-key api]]
   (-> db
       (assoc-in
        (model-path {:method ::data :model-key model-key})  {})
       (assoc-in
        (model-path {:method ::state :model-key model-key}) {:api api}))))

(reg-event-db
 :model/api-set
 (fn [db [_ model-key api]]
   (update-in db (model-path {:method ::state-api
                              :model-key model-key})  api)))

(reg-event-db
 :model/update-one
 (fn [db [_ model-key item]]
   (update-in db (model-path {:method ::data
                              :model-key model-key}) into {(-> item :id str keyword) item})))

(reg-event-db
 :model/delete
 (fn [db [_ model-key key-id]]
   (update-in db (model-path {:method ::data
                              :model-key model-key}) dissoc  key-id)))

(reg-event-db
 :model/update-all
 (fn [db [_ model-key new-data-map]]
   (update-in db (model-path {:method ::data
                              :model-key model-key}) into new-data-map)))

(reg-event-db
 :model/update-all-by-list
 (fn [db [_ model-key new-data-map]]
   (update-in db (model-path {:method ::data
                              :model-key model-key}) merge (utils/mapset2map new-data-map))))

(reg-event-db
 :model/replace-all
 (fn [db [_ model-key new-data-map]]
   (assoc-in db (model-path {:method ::data
                             :model-key model-key})  new-data-map)))
(reg-event-db
 :model/replace-all-by-list
 (fn [db [_ model-key new-data-list]]
   (assoc-in db (model-path {:method ::data
                             :model-key model-key}) (utils/mapset2map new-data-list))))
;

;; db的查询层
(reg-sub
 :model/api-get
 (fn [db [_ model-key key-id]]
   (get-in db (model-path {:method ::state-api
                           :model-key  model-key
                           :id key-id}))))

(reg-sub
 :model/find-id
 (fn [db [_ model-key key-id]]
   (get-in db (model-path {:method ::id-key
                           :model-key  model-key
                           :id key-id}))))

(reg-sub
 :model/find-id-str
 (fn [db [_ model-key key-id]]
   (get-in db (model-path {:method ::id-raw
                           :model-key  model-key
                           :id key-id}))))

(reg-sub
 :model/find-ids-str
 (fn [db [_ model-key str-ids]]
   (-> db
       (get-in  (model-path {:method ::data :model-key model-key}))
       (select-keys (map #(keyword %) str-ids))
       vals)))

(reg-sub
 :model/find-ids
 (fn   [db [_ model-key key-ids]]
   (-> db
       (get-in  (model-path {:method ::data :model-key model-key}))
       (select-keys key-ids)
       vals)))

(reg-sub
 :model/find-all
 (fn [db [_ model-key query]]
   (filter #(=  query (select-keys % (keys query)))
           (-> db (get-in (model-path {:method ::data :model-key model-key})) vals))))

(reg-sub
 :model/filter-by
 (fn [db [_ model-key query]]
   (filter #(not=  query (select-keys % (keys query)))
           (-> db (get-in  (model-path {:method ::data :model-key model-key}))  vals))))
(reg-sub
 :model/all
 (fn [db [_ model-key]]
   (-> db (get-in  (model-path {:method ::data :model-key model-key})) vals)))

(defn test-work1 []
  (dispatch [:model/init :test "/Contact/"])
  (println @(subscribe [:model/all :test]))
  (dispatch [:model/update-one :test {:ddd "aaaa" :id "1"}])
  (println @(subscribe [:model/all :test]))
  (dispatch [:model/update-one :test {:ddd "ddd" :id "2"}])
  (println   @(subscribe [:model/find-all :test {}]))
  (println @(subscribe [:model/find-id :test :1]))
  ;;(println (dispatch [:model/replace-all :test {}]))
  )

;; (test-work1)

;; ;; (subscribe [:model/find-ids :test ["1"]])

;; ;; (subscribe [:db-state])

