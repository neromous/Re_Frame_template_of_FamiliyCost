(ns soul-talk.db
  (:require  [reagent.core :as r]
             [soul-talk.util.date-utils :as du]))

(def item-register
  {:metadata {:url "http://localhost:3000/api/kpn-metadata/all"
              :data-path [:datas :items :metadata]
              :validator {:yarn_weight [:int 0]}
              ;; === 如果是 dataset ==========
              :dataset? false
              :serialize? false
              :to-map? false
              :query {}}

   :order_number {:url "http://localhost:3000/api/kpn-metadata/order_number"
                  :data-path [:datas :items :order_number]}

   :sell-info {:url "http://localhost:3000/api/product-info/all"
               :data-path [:datas :items :product-track]}

   :craft-material-info {:url "http://localhost:3000/api/craft-material-info/flow_id"
                         :data-path [:datas :items :craft-material-info]}

   :human-info {:url "http://localhost:3000/api/human-info/flow_id"
                :data-path [:datas :items :human-info]}

   :machine-info {:url "http://localhost:3000/api/machine-info/flow_id"
                  :data-path [:datas :items :machine-info]}

   ;;
   })

(def model-register
  {:tb_relation {:url "http://localhost:3000/api/tb_relation"
                 :model-name "tb_relation"
                 :table_name "tb_relation"
                 :model-key :tb_relation
                 :data-path [:datas :models :tb_relation]
                 :view-path [:views :models :tb_relation]
                 :metadata-path [:metadata :tb_relation]
                 :cache-path [:caches :tb_relation]
                 :fields-key  :tb_relation
                 :type-list []}
     ;;
   })

(defonce model-state (r/atom  model-register))

(def default-db
  (->   {:active {}
         :breadcrumb ["Home"]
         :login-events []}
        ))

(defonce unique-work (r/atom 0))

(defn unique-id []
  (swap! unique-work inc))

(goog-define api-uri "http://localhost:3000/api/v1")
