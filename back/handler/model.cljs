(ns soul-talk.handler.model

  (:require [re-frame.core :refer [reg-event-fx reg-event-db dispatch dispatch-sync subscribe]]
            [re-frame.core :refer [reg-sub]]
            [soul-talk.subs :as subs]
            [soul-talk.utils :as utils]
            [ajax.core :refer [POST
                               GET
                               DELETE
                               PUT
                               ajax-request
                               url-request-format
                               json-request-format
                               json-response-format]]
            [soul-talk.db :refer [api-uri]]))

(defonce envs (atom (hash-map)))

(defn model-register
  "model注册机制 采用闭包, "
  [function model-key env & args]
  (swap! env assoc model-key (into {} [(model-key @env) (function  model-key env args)])))

(defn model-init
  [model-key env & args]

  {:model-str-name (name model-key)
   :db-key (->
            (str  "model/" (name model-key) ".data")
            keyword)
   :state-key  (->
                (str "model/"  (name model-key) ".state")
                keyword)
   :json-data-key :results})

(defn model-reg-db-sub
  [model-key env & args]
  (let [{:keys [model-str-name db-key state-key json-data-key]}  (model-key @env)
        model (model-key @env)]
    {:name-str model-str-name
     :name-key  (utils/reg-sub-fix db-key subs/query)
     :state-key (utils/reg-sub-fix state-key subs/query)
            ;; 本地数据融合远程获取的

     :db/merge (->
                (keyword (str "model/" model-str-name ".db.merge"))
                (utils/reg-event-db-fix
                 (fn [db [_ item]]
                          ;; 保存基础数据
                   (merge-with into db {db-key  (:results item) }))))

     ;; 本地数据清除后更新远程获取的
     :db/update (->
                 (keyword (str "model/" model-str-name ".db.update"))
                 (utils/reg-event-db-fix
                  (fn [db [_ item]]
                    ;; 保存基础数据
                    (-> db
                        (assoc  db-key (:results item))
                        ;; 更新分页信息
                        (assoc-in  [state-key :pagination] (dissoc item :results))))))

     ;; 本地数据删除某个
     :db/delete (->
                 (keyword (str "model/" model-str-name ".db.delete"))
                 (utils/reg-event-db-fix
                  (fn [db [_ id]]
                           ;; 删除某个数据
                    (assoc db db-key (-> db db-key (dissoc id))))))

     :db/new-item (->
                   (keyword (str "model/" model-str-name ".db.newItem"))
                   (utils/reg-event-db-fix
                    (fn [db [_ item]]
                             ;; 删除某个数据
                      (assoc-in db [db-key (-> item :id str keyword)] item))))

     :db/update-item (->
                      (keyword (str "model/" model-str-name ".db.updateItem"))
                      (utils/reg-event-db-fix
                       (fn [db [_ item]]
                      ;; 删除某个数据
                         (println item)
                         (assoc-in db [db-key (-> item :id str keyword)] item))))}))

(defn model-reg-server
  [model-key env  [args]]
  (let [{:keys [model-str-name db-key state-key json-data-key]}  (model-key @env)
        {:keys [url config]} args
        model (model-key @env)]
    {:server/url-pre (str "#/" model-str-name)
     :server/api-path (str api-uri url)

     :server/all (->
                  (keyword (str "model/" model-str-name ".server.all"))
                  (utils/reg-event-fx-fix
                   (fn [_ [_ params]]
                     {:http {:method        GET
                             :url           (str api-uri url)
                             :ajax-map      (into {:params params
                                                   :keywords? true
                                                   :response-format :json}  config)
                             :success-event [(:db/update model)]}})))

     :server/find-by  (->
                       (keyword (str "model/" model-str-name ".server.find-by"))
                       (utils/reg-event-fx-fix
                        (fn [_ [_ params]]
                          {:http {:method        GET
                                  :url           (str api-uri url)
                                  :ajax-map      (into {:params params
                                                        :keywords? true
                                                        :response-format :json}  config)

                                  :success-event [(:db/update model)]}})))

     :server/delete-one (->
                         (keyword (str "model/" model-str-name ".server.delete"))
                         (utils/reg-event-fx-fix
                          (fn [_ [_ id]]
                            {:http {:method        DELETE
                                    :url           (str api-uri url id)
                                    :ajax-map      (into {;:params params
                                                          :keywords? true
                                                          :response-format :json}  config)

                                    :success-event [(:db/delete model) (-> id  str keyword)]}})))

     :server/new (->
                  (keyword (str "model/" model-str-name ".server.new"))
                  (utils/reg-event-fx-fix
                   (fn [_ [_ item]]
                     {:http {:method        POST
                             :url           (str api-uri url)
                             :ajax-map      (into {:params item
                                                   :keywords? true
                                                   :format (json-request-format)
                                                   :response-format :json}  config)

                             :success-event [(:db/new-item model)]}})))
     :server/update (->
                     (keyword (str "model/" model-str-name ".server.update"))
                     (utils/reg-event-fx-fix
                      (fn [_ [_ item]]
                        {:http {:method        PUT
                                :url           (str api-uri url)
                                :ajax-map      (into {:params item
                                                      :keywords? true
                                                      :format (json-request-format)
                                                      :response-format :json}  config)

                                :success-event [(:db/new-item model)]}})))}))

(defn model-register-multi
  [model-name env url config]
  (do
    (model-register model-init model-name env)
    (model-register model-reg-db-sub  model-name env)
    (model-register model-reg-server  model-name env  {:url url

                                                       :config config})))
(model-register-multi :contact envs "/Contact/" {})

(model-register-multi :area envs "/Area/" {})
(model-register-multi :storage envs "/Storage/" {})
(model-register-multi :storageExchange envs "/StorageExchange/" {})
(model-register-multi :measureUnit envs "/CMeasureUnit/" {})


