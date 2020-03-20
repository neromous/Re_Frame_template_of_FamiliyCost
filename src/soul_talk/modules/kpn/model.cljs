(ns soul-talk.modules.kpn.model
  (:require  [re-frame.core :refer [subscribe reg-event-db
                                    dispatch reg-sub reg-event-fx]]
             [reagent.core :as r]
             [soul-talk.handler.common :refer  [model-register]]))

;; (model-register
;;  :order_number
;;  {:url "http://localhost:3000/api/kpn-metadata/order_number"
;;   :root-path [:datas :items :order_number]})

;; (model-register
;;  :sell-info
;;  {:url "http://localhost:3000/api/product-info/all"
;;   :root-path [:datas :items :product-track]})

;; (model-register
;;  :craft-material-info
;;  {:url "http://localhost:3000/api/craft-material-info/flow_id"
;;   :root-path [:datas :items :craft-material-info]})

;; (model-register
;;  :sell-order
;;  {:url "http://localhost:3000/api/datomic-api/query"
;;   :root-path [:datomic  :sell-order]
;;   :query {:query '[:find (pull ?e [*])
;;                    :where
;;                    [?e :sell-order/order_number]]}})

;; (model-register
;;  :sell-detail
;;  {:url "http://localhost:3000/api/datomic-api/query"
;;   :root-path [:datomic :sell-detail]
;;   :query {:query '[:find  (pull ?e [*])
;;                    :where
;;                    [?e :sell-detail/detail_id]]}})

;; (model-register
;;  :datomic/schema
;;  {:url "http://localhost:3000/api/datomic-api/schema"
;;   :root-path [:datomic :schema]})

;; (model-register
;;  :product-task
;;  {:url "http://localhost:3000/api/datomic-api/query"
;;   :root-path [:datomic :product-task]
;;   :query {:query '[:find  (pull ?e [*])
;;                    :where
;;                    [?e :product-task/flow_id]]}})

;; (model-register
;;  :flow.cost
;;  {:url "http://localhost:3000/api/datomic-api/query"
;;   :root-path [:datomic :flow.cost]
;;   :query {:query '[:find  (pull ?ee [*])
;;                    :in $ ?id
;;                    :where
;;                    [?e :product-task/flow_id ?id]
;;                    [?ee :cost/ref.product-task ?e]
;;                    [?e :product-task/ref.sell-detail ?eee]
;;                    [?material  :cost/ref.sell-detail ?eee]]
;;           :input 1}})

;; (model-register
;;  :datomic/update
;;  {:url "http://localhost:3000/api/datomic-api/update"
;;   :root-path [:datomic :datomic.save]
;;   })








