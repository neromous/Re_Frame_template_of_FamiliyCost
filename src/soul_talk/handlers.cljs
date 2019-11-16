(ns soul-talk.handlers
  (:require
   [re-frame.core :refer [inject-cofx dispatch dispatch-sync reg-event-db reg-event-fx subscribe reg-sub]]
   [soul-talk.db :refer [default-db]]
   [soul-talk.local-storage :as storage]
   [soul-talk.utils :refer [url->id]]
   soul-talk.ajax
   soul-talk.effects
   soul-talk.handler.errors
   soul-talk.handler.auth
   soul-talk.handler.admin
   soul-talk.handler.users
   soul-talk.handler.category
   soul-talk.handler.files
   soul-talk.handler.fake-data
   soul-talk.handler.data-model))

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
 :db/find-by
 (fn [db [_ ks query]]
   (filter #(=  query (select-keys % (keys query)))
           (-> db (get-in ks) vals))))

(reg-event-db
 :db/dissoc-in
 (fn [db [_ ks]]
   (update-in db (drop-last ks) dissoc (last ks))))


;; (reg-event-db
;;  :db/set-in
;;  (fn [db [_  x]]
;;    (let [rest-path (drop-last x)
;;          value (last x)
;;          full-path rest-path]
;;      (assoc-in db  full-path value))))


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


;;  从一个位置的map 的外键找到另一个位置的列表中匹配其外键的map
;; (reg-sub
;;  :item/foreignkey
;;  (fn [db [_ value-path foreign-set-path  foreign-key]]
;;    (let [value (get-in db value-path)
;;          query {foreign-key value}
;;          foreign-set (get-in db foreign-set-path)]
;;      (filter #(=  query (select-keys % (keys query)))
;;              (vals foreign-set )))))


(reg-sub
 :item/foreignkey
 (fn [db [_ value-path foreign-set-path]]
   (let [value (get-in db value-path)
         foreign-set (get-in db foreign-set-path)]
     (get foreign-set  (-> value  url->id)))))

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




