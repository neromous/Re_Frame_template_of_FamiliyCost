(ns soul-talk.db
  (:require  [reagent.core :as r]))

(def model-relation
  (into
   (sorted-map)
   {2 []
    1 []
    3 []}))

(def value-register
  {:test {:url ""
          :data-path [:datas :values :test]}})

(def model-register
  {:material-raw {:url "http://0.0.0.0:3000/cost/material-raw"
                  :model-name "material-raw"
                  :model-key :material-raw
                  :data-path [:datas :models :material-raw]
                  :view-path [:views :models :material-raw]
                  :cache-path [:caches :material-raw]
                  :fields-key  :material-raw
                  :event/server-pull [:resource/server.query :material-raw]
                  :sub/all [:resource/all :material-raw]
                  :query-sub/filter [:resource/filter  :material-raw]
                  :query-sub/find-by [:resource/find-by :material-raw]}

   :material-craft {:url "http://0.0.0.0:3000/cost/material-craft"
                    :model-name "material-craft"
                    :model-key :material-craft
                    :data-path [:datas :models :material-craft]
                    :view-path [:views :models :material-craft]
                    :cache-path [:caches :material-craft]
                    :fields-key  :material-craft
                    :event/server-pull [:resource/server.query :material-craft]
                    :sub/all [:resource/all :material-craft]
                    :query-sub/filter [:resource/filter  :material-craft]
                    :query-sub/find-by [:resource/find-by :material-craft]}

   :human-resource {:url "http://0.0.0.0:3000/cost/human"
                    :model-name "human-resource"
                    :model-key :human-resource
                    :data-path [:datas :models :human-resource]
                    :view-path [:views :models :human-resource]
                    :cache-path [:caches :human-resource]
                    :fields-key  :human-resource
                    :event/server-pull [:resource/server.query :human-resource]
                    :sub/all [:resource/all :human-resource]
                    :query-sub/filter [:resource/filter  :human-resource]
                    :query-sub/find-by [:resource/find-by :human-resource]}

   :energy-oa {:url "http://0.0.0.0:3000/cost/energy/oa_report"
               :model-name "energy-oa"
               :model-key :energy-oa
               :data-path [:datas :models :energy-oa]
               :view-path [:views :models :energy-oa]
               :cache-path [:caches :energy-oa]
               :fields-key  :energy-oa
               :event/server-pull [:resource/server.query :energy-oa]
               :sub/all [:resource/all :energy-oa]
               :query-sub/filter [:resource/filter  :energy-oa]
               :query-sub/find-by [:resource/find-by :energy-oa]}

   :product-output {:url "http://0.0.0.0:3000/cost/product-output"
                    :model-name "product-output"
                    :model-key :product-output
                    :data-path [:datas :models :product-output]
                    :view-path [:views :models :product-output]
                    :cache-path [:caches :product-output]
                    :fields-key  :product-output
                    :event/server-pull [:resource/server.query :product-output]
                    :sub/all [:resource/all :product-output]
                    :query-sub/filter [:resource/filter  :product-output]
                    :query-sub/find-by [:resource/find-by :product-output]}

   :order-track {:url "http://0.0.0.0:3000/cost/order-track"
                 :model-name "order-track"
                 :model-key :order-track
                 :data-path [:datas :models :order-track]
                 :view-path [:views :models :order-track]
                 :cache-path [:caches :order-track]
                 :fields-key  :order-track
                 :event/server-pull [:resource/server.query :order-track]
                 :sub/all [:resource/all :order-track]
                 :query-sub/filter [:resource/filter  :order-track]
                 :query-sub/find-by [:resource/find-by :order-track]}

   :machine-resource {:url "http://0.0.0.0:3000/cost/machine"
                      :model-name "machine-resource"
                      :model-key :machine-resource
                      :data-path [:datas :models :machine-resource]
                      :view-path [:views :models :machine-resource]
                      :cache-path [:caches :machine-resource]
                      :fields-key  :machine-resource
                      :event/server-pull [:resource/server.query :machine-resource]
                      :sub/all [:resource/all :machine-resource]
                      :query-sub/filter [:resource/filter  :machine-resource]
                      :query-sub/find-by [:resource/find-by :machine-resource]}

   :sys_org {:url "http://0.0.0.0:3000/api/v2/query/sys_org"
             :model-name "sys_org"
             :table_name "sys_org"
             :model-key :sys_org
             :data-path [:datas :models  :sys_org]
             :view-path [:views :models :sys_org]
             :cache-path [:caches :sys_org]
             :fields-key  :sys_org
             :event/server-pull [:resource/server.query :sys_org]
             :sub/all [:resource/all :sys_org]
             :query-sub/filter [:resource/filter  :sys_org]
             :query-sub/find-by [:resource/find-by :sys_org]}

   :erp_goods {:url "http://0.0.0.0:3000/api/v2/query/erp_goods"
               :model-name "erp_goods"
               :table_name "erp_goods"
               :model-key :erp_goods
               :data-path [:datas :models :erp_goods]
               :view-path [:views :models :erp_goods]
               :cache-path [:caches :erp_goods]
               :fields-key  :erp_goods
               :event/server-pull [:resource/server.query :erp_goods]
               :sub/all [:resource/all :erp_goods]
               :query-sub/filter [:resource/filter  :erp_goods]
               :query-sub/find-by [:resource/find-by :erp_goods]}

   :erp_provider {:url "http://0.0.0.0:3000/api/v2/query/erp_provider"
                  :model-name "erp_provider"
                  :table_name "erp_provider"
                  :model-key :erp_provider
                  :data-path [:datas :models :erp_provider]
                  :view-path [:views :models :erp_provider]
                  :cache-path [:caches :erp_provider]
                  :fields-key  :erp_provider
                  :event/server-pull [:resource/server.query :erp_provider]
                  :sub/all [:resource/all :erp_provider]
                  :query-sub/filter [:resource/filter  :erp_provider]
                  :query-sub/find-by [:resource/find-by :erp_provider]}

   :erp_customer {:url "http://0.0.0.0:3000/api/v2/query/erp_customer"
                  :model-name "erp_customer"
                  :table_name "erp_customer"
                  :model-key :erp_customer
                  :data-path [:datas :models :erp_customer]
                  :view-path [:views :models :erp_customer]
                  :cache-path [:caches :erp_customer]
                  :fields-key  :erp_customer
                  :event/server-pull [:resource/server.query :erp_customer]
                  :sub/all [:resource/all :erp_customer]
                  :query-sub/filter [:resource/filter  :erp_customer]
                  :query-sub/find-by [:resource/find-by :erp_customer]}

   :todos {:url "http://localhost:3000/api/todos"
           :model-name "todos"
           :table_name "todos"
           :model-key :todos
           :data-path [:datas :models :todos]
           :view-path [:views :models :todos]
           :cache-path [:caches :todos]
           :fields-key  :todos
           :event/server-pull [:resource/server.query :todos]
           :sub/all [:resource/all :todos]
           :query-sub/filter [:resource/filter  :todos]
           :query-sub/find-by [:resource/find-by :todos]}
   ;;
   })

(defonce model-state (r/atom  model-register))

(def default-db
  {:active {}
   :breadcrumb ["Home"]
   :login-events []})

(defonce unique-work (r/atom 0))

(defn unique-id []
  (swap! unique-work inc))

(goog-define api-uri "http://localhost:3000/api/v1")
