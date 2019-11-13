(ns soul-talk.model.view
  (:require  [re-frame.core :refer [subscribe reg-event-db dispatch reg-sub reg-event-fx]]
             [soul-talk.models.init :refer [model-path]]
             [ajax.core :refer [POST GET DELETE PUT]]))

; 定义路径解析的多重方法
(defmethod model-path ::view [m] [:views (:view-key m)])
(defmethod model-path ::view-model [m] [:views
                                        (:view-key m)
                                        :models
                                        (:model-key m)])

(defmethod model-path ::view-model-show-ids [m] [:views
                                                 (:view-key m)
                                                 :models
                                                 (:model-key m)
                                                 :show-ids])

(defmethod model-path ::view-model-cache [m] [:views
                                              (:view-key m)
                                              :models
                                              (:model-key m)
                                              :cache])

(defmethod model-path ::view-model-pagination [m] [:views
                                                   (:view-key m)
                                                   :models
                                                   (:model-key m)
                                                   :pagination])

(defmethod model-path ::view-model-columns [m] [:views
                                                (:view-key m)
                                                :models
                                                (:model-key m)
                                                :columns])
;; view 层操作

(reg-event-db
 :view/init
 (fn [db [_ view-key]]
   (-> db
       (assoc-in (model-path {:method ::view
                              :view-key view-key})  {}))))

(reg-event-db
 ;; 在原有的基础上更新
 :view/model-update
 (fn [db [_  view-key model-key new-model-state]]
   (-> db
       (update-in (model-path {:method ::view-model
                               :model-key model-key
                               :view-key view-key})  merge new-model-state))))

(reg-event-db
 ;; 模型初始化
 :view/model-init
 (fn [db [_  view-key model-key new-model-state]]
   (-> db
       (assoc-in (model-path {:method ::view-model
                              :model-key model-key
                              :view-key view-key})   new-model-state))))

(reg-event-db
 :view/model-set-show-ids
 (fn [db [_ view-key model-key key-ids]]
   (-> db
       (assoc-in (model-path {:method ::view-model-show-ids
                              :model-key model-key
                              :view-key view-key})  key-ids))))

(reg-event-db
 :view/model-set-cache
 (fn [db [_ view-key model-key new-cache-state]]
   (-> db
       (assoc-in (model-path {:method ::view-model-cache
                              :model-key model-key
                              :view-key view-key})  new-cache-state))))

(reg-event-db
 :view/model-set-pagination
 (fn [db [_ view-key model-key new-pagination]]
   (-> db
       (assoc-in (model-path {:method ::view-model-pagination
                              :model-key model-key
                              :view-key view-key})  new-pagination))))

(reg-event-db
 :view/model-set-columns
 (fn [db [_ view-key model-key new-columns]]
   (-> db
       (assoc-in (model-path {:method ::view-model-pagination
                              :model-key model-key
                              :view-key view-key})  new-columns))))
;
;; view 查询层
(reg-sub
 :view/get-states
 (fn [db [_ view-key]]
   (get-in db (model-path {:method ::view
                           :view-key view-key}))))

(reg-sub
 :view/get-model
 (fn [db [_ view-key  model-key]]
   (get-in db (model-path {:method ::views-model
                           :model-key model-key
                           :view-key view-key}))))

(reg-sub
 :view/get-model-cache
 (fn [db [_ view-key model-key]]
   (get-in db (model-path {:method ::view-model-cache
                           :model-key model-key
                           :view-key view-key}))))

(reg-sub
 :view/get-model-pagination
 (fn [db [_ view-key  model-key]]
   (get-in db (model-path {:method ::view-model-pagination
                           :model-key model-key
                           :view-key view-key}))))

(reg-sub
 :view/get-model-columns
 (fn [db [_ view-key  model-key]]
   (get-in db (model-path {:method ::view-model-columns
                           :model-key model-key
                           :view-key view-key}))))


;; (defn test-work []
;;   (dispatch [:view/init :homepage])
;;   (println @(subscribe [:view/states :homepage]))
;;   (dispatch [:view/model-init :homepage :test {}])
;;   (println @(subscribe [:view/states :homepage]))
;;   ;;(dispatch [:view/model-update :homepage  :test {:ddd "aaaa" :id "1"}])
;;   )

