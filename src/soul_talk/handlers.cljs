(ns soul-talk.handlers
  (:require
   [re-frame.core :refer [inject-cofx dispatch dispatch-sync reg-event-db reg-event-fx subscribe reg-sub]]
   [soul-talk.db :refer [default-db]]
   [soul-talk.local-storage :as storage]
   soul-talk.ajax
   soul-talk.effects
   soul-talk.handler.page-state
   soul-talk.handler.errors
   soul-talk.handler.item-server
   soul-talk.handler.model
   soul-talk.handler.auth
   soul-talk.handler.model-server
   soul-talk.handler.users
   soul-talk.handler.files))

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




;; (reg-event-db
;;  :set-breadcrumb
;;  (fn [db [_ breadcrumb]]
;;    (assoc db :breadcrumb breadcrumb)))

;; (reg-event-fx
;;  :navigate-to
;;  (fn [_ [_ url]]
;;    {:navigate url}))

;; (reg-event-db
;;  :set-success
;;  (fn [db [_ message]]
;;    (assoc db :success message)))

;; (reg-event-db
;;  :clean-success
;;  (fn [db _]
;;    (dissoc db :success)))

;; (reg-event-db
;;  :update-value
;;  (fn [db [_ keys val]]
;;    (assoc-in db keys val)))

