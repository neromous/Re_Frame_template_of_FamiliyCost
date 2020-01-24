(ns soul-talk.handlers
  (:require
   [re-frame.core :refer [inject-cofx dispatch dispatch-sync reg-event-db reg-event-fx subscribe reg-sub]]
   [soul-talk.db :refer [default-db]]
   [soul-talk.local-storage :as storage]
   [soul-talk.utils :refer [url->id]]
   soul-talk.ajax
   soul-talk.handler.admin
   soul-talk.handler.auth
   soul-talk.handler.errors
   soul-talk.handler.files
   soul-talk.handler.server
   soul-talk.handler.users
   soul-talk.handler.table-fields
   soul-talk.handler.order-track
   soul-talk.handler.org
   soul-talk.handler.cost
   soul-talk.handler.customer
   ))

;; 初始化


(reg-event-fx
 :initialize-db
 [(inject-cofx :local-store storage/login-user-key)
  (inject-cofx :local-store storage/auth-token-key)]
 (fn [cofx _]
   (let [user (get-in cofx [:local-store storage/login-user-key])
         auth-token (get-in cofx [:local-store storage/auth-token-key])
         db (:db cofx)]
     {:db (merge db (assoc default-db :user (js->clj user :keywordize-keys true)
                           :auth-token auth-token))})))

;; 设置当前页
(reg-event-db
 :set-active
 (fn [db [_  x]]
   (assoc db :active x)))

(reg-event-db
 :set-breadcrumb
 (fn [db [_ breadcrumb]]
   (assoc db :breadcrumb breadcrumb)))

(reg-event-fx
 :navigate-to
 (fn [_ [_ url]]
   {:navigate url}))

(reg-event-db
 :set-success
 (fn [db [_ message]]
   (assoc db :success message)))

(reg-event-db
 :clean-success
 (fn [db _]
   (dissoc db :success)))

(reg-event-db
 :update-value
 (fn [db [_ keys val]]
   (assoc-in db keys val)))

;; 取消加载
(reg-event-db
 :unset-loading
 (fn [db _]
   (dissoc db :loading? :error :should-be-loading?)))

;; 设置加载为 true
(reg-event-db
 :set-loading-for-real-this-time
 (fn [{:keys [should-be-loading?] :as db} _]
   (if should-be-loading?
     (assoc db :loading? true)
     db)))

;; 设置加载
(reg-event-fx
 :set-loading
 (fn [{db :db} _]
   {:dispatch-later [{:ms 100 :dispatch [:set-loading-for-real-this-time]}]
    :db             (-> db
                        (assoc :should-be-loading? true)
                        (dissoc :error))}))

;; 通用db深度操作方法
(reg-sub
 :db/get-in
 (fn [db [_  ks]]
   (get-in db ks)))

(reg-sub
 :db/group-by
 (fn [db [_ ks group-func]]
   (group-by  group-func  (get-in db ks))))

(reg-sub
 :db/select-keys
 (fn [db [_  ks selector]]
   (select-keys (get-in db ks)  selector)))

(reg-sub
 :db/find-by
 (fn [db [_ ks query]]
   (filter #(=  query (select-keys % (keys query)))
           ;; (-> db (get-in ks) vals))))
           (-> db (get-in ks) vals))))

(reg-event-db
 :db/dissoc-in
 (fn [db [_ ks]]
   (update-in db (drop-last ks) dissoc (last ks))))

(reg-event-db
 :db/assoc-in
 (fn [db [_ ks v]]
   (assoc-in db ks v)))

(reg-event-db
 :db/merge-in
 (fn [db [_ ks v]]
   (update-in db ks merge v)))

(reg-event-db
 :db/update-in
 (fn [db [_ ks func v]]
   (update-in db ks func v)))

(reg-event-db
 :paths/copy
 (fn [db [_ pk-target pk-origin]]
   (let [origin pk-origin
         target pk-target]
     (update-in db (drop-last target) assoc (last target)  (get-in db origin)))))

(reg-event-db
 :paths/move
 (fn [db [_ pk-target pk-origin]]
   (let [origin pk-origin
         target pk-target]
     (update-in db (drop-last target) assoc (last target)  (get-in db origin))
     (update-in db (drop-last origin) dissoc (last origin)))))

(reg-event-db
 :paths/merge
 (fn [db [_ pk-target pk-origin]]
   (let [origin pk-origin
         target pk-target]
     (update-in db target merge  (get-in db origin)))))

(reg-sub
 :item/one2one
 (fn [db [_ origin-path target-set-path relate-field]]
   ;; origin-path :目标item位置
   ;; taget-set-path 需要查询的集合位置
   ;; relate-field  关联的字段
   (let [item (get-in db origin-path)
         dataset (get-in db  target-set-path)
         item-url (get item relate-field)]
     (get dataset  item-url))))

(reg-sub
 :item/relate-value
 (fn [db [_ origin-path target-set-path relate-field  target-field]]
   ;; origin-path :目标item位置
   ;; taget-set-path 需要查询的集合位置
   ;; relate-field  关联的字段
   (let [item (get-in db origin-path)
         dataset (get-in db  target-set-path)
         item-url (get item relate-field)]
     (->  dataset
          (get   item-url)
          (get   target-field)))))

(reg-sub
 :item/one2many
 ;; origin-path :目标item位置
 ;; taget-set-path 需要查询的集合位置
 ;; relate-field  关联的字段
 (fn [db [_ origin-path target-set-path relate-field]]
   (let [item (get-in db origin-path)
         dataset (get-in db  target-set-path)
         item-urls (get   item relate-field)]
     (select-keys dataset  item-urls))))

(reg-sub
 :set/map
 (fn [db [_ target-path map-fn]]
   (map map-fn  (get-in db target-path))))

(reg-sub
 :db/get
 (fn [db [_ k]]
   (get db k)))

(reg-event-db
 :db/assoc
 (fn [db [_ k v]]
   (assoc db k v)))

(reg-event-db
 :db/dissoc
 (fn [db [_ k]]
   (dissoc db k)))


