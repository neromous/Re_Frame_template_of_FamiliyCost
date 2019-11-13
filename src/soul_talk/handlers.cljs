(ns soul-talk.handlers
  (:require [re-frame.core :refer [inject-cofx dispatch dispatch-sync reg-event-db reg-event-fx subscribe reg-sub]]
            [soul-talk.db :refer [default-db]]
            [soul-talk.local-storage :as storage]
            soul-talk.handler.errors
            soul-talk.handler.auth
            soul-talk.handler.admin
            soul-talk.handler.users
            soul-talk.handler.category
            soul-talk.handler.files
            soul-talk.handler.data-model
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

(reg-event-db
 :db/dissoc-in
 (fn [db [_ ks]]
   (update-in db (drop-last ks) dissoc (last ks))))

(reg-event-db
 :db/set-in
 (fn [db [_  x]]
   (let [rest-path (drop-last x)
         value (last x)
         full-path rest-path]
     (assoc-in db  full-path value))))

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







;; ;; 针对path的操作内容 保证path能够

;; (reg-sub
;;  :path/get
;;  (fn [db [_ pk]]
;;    (get-in db (path pk))))

;; (reg-sub
;;  :path/get-in
;;  (fn [db [_ pk subkey-vec]]
;;    (let [full-key (concat (path pk) subkey-vec)]
;;      (get-in db full-key))))

;; (reg-event-db
;;  :path/assoc
;;  (fn [db [_ pk value]]
;;    (assoc-in db (path pk) value)))

;; (reg-event-db
;;  :path/assoc-in
;;  (fn [db [_ pk subkey-vec value]]
;;    (let [full-key (concat (path pk) subkey-vec)]
;;      (assoc-in db (path pk) value))))

;; (reg-event-db
;;  :path/dissoc
;;  (fn [db [_ pk]]
;;    (let [full-path (path pk)]
;;      (update-in db (drop-last full-path) dissoc (last full-path)))))

;; (reg-event-db
;;  :path/dissoc-in
;;  (fn [db [_ pk  subkey-vec]]
;;    (let [full-path (concat (path pk) subkey-vec)]
;;      (update-in db (drop-last full-path) dissoc (last full-path)))))

;; (reg-event-db
;;  :path/update-in
;;  (fn [db [_ pk func value]]
;;    (update-in db (path pk)  func value)))

;; (reg-event-db
;;  :paths/merge
;;  (fn [db [_ pk-origin pk-target]]
;;    (let [origin (path pk-origin)
;;          target (path pk-target)]
;;      (update-in db target merge (get-in db origin)))))

;; (reg-event-db
;;  :paths/move
;;  (fn [db [_ pk-origin pk-target]]
;;    (let [origin (path pk-origin)
;;          target (path pk-target)]
;;      (update-in db target merge (get-in db origin))
;;      (update-in db (drop-last origin) dissoc (last origin)))))


;; ;; 建立在pathkey基础上的快捷方法


;; (reg-event-db
;;  :pk/get
;;  (fn [db [_ pk & rest-key]]
;;    (let [full-path (concat (path pk) rest-key)]
;;      (get-in db  full-path))))

;; (reg-event-db
;;  :pk/set
;;  (fn [db [_ pk & rest-kv]]
;;    (let [pk-path (path pk)
;;          rest-path (drop-last rest-kv)
;;          value (last rest-kv)
;;          full-path (concat pk-path rest-path)]
;;      (assoc-in db  full-path value))))

;; (reg-event-db
;;  :pk/delete
;;  (fn [db [_ pk & rest-kv]]
;;    (let [pk-path (path pk)
;;          rest-path (drop-last rest-kv)
;;          id (last rest-kv)
;;          full-path (concat pk-path rest-path)]
;;      (update-in db  full-path dissoc id))))


;; (reg-event-db
;;  :pk/update
;;  (fn [db [_ pk & rest-kfv]]
;;    (let [pk-path (path pk)
;;          rest-path (-> rest-kfv drop-last drop-last)
;;          func (-> rest-kfv drop-last last)
;;          value (last rest-kfv)
;;          full-path (concat pk-path rest-path)]
;;      (update-in db  full-path func value))))

;; (reg-event-db
;;  :pk/merge
;;  (fn [db [_ pk & rest-kfv]]
;;    (let [pk-path (path pk)
;;          rest-path (-> rest-kfv drop-last)
;;          value (last rest-kfv)
;;          full-path (concat pk-path rest-path)]
;;      (update-in db  full-path merge value))))











