(ns soul-talk.models)

(def model-register
  {:material-raw {:url "http://0.0.0.0:3000/cost/material-raw"
                  :model-name "material-raw"
                  :model-key :material-raw
                  :data-path [:datas  :material-raw]
                  :view-path [:views  :material-raw]
                  :cache-path [:caches :material-raw]
                  :fields-key  :material-raw
                  :event/server-pull [:resource/server.query :material-raw]
                  :sub/all [:resource/all :material-raw]
                  :query-sub/filter [:resource/filter  :material-raw]
                  :query-sub/find-by [:resource/find-by :material-raw]}

   :material-craft {:url "http://0.0.0.0:3000/cost/material-craft"
                    :model-name "material-craft"
                    :model-key :material-craft
                    :data-path [:datas  :material-craft]
                    :view-path [:views  :material-craft]
                    :cache-path [:caches :material-craft]
                    :fields-key  :material-craft
                    :event/server-pull [:resource/server.query :material-craft]
                    :sub/all [:resource/all :material-craft]
                    :query-sub/filter [:resource/filter  :material-craft]
                    :query-sub/find-by [:resource/find-by :material-craft]}

   :human-resource {:url "http://0.0.0.0:3000/cost/human"
                    :model-name "human-resource"
                    :model-key :human-resource
                    :data-path [:datas  :human-resource]
                    :view-path [:views  :human-resource]
                    :cache-path [:caches :human-resource]
                    :fields-key  :human-resource
                    :event/server-pull [:resource/server.query :human-resource]
                    :sub/all [:resource/all :human-resource]
                    :query-sub/filter [:resource/filter  :human-resource]
                    :query-sub/find-by [:resource/find-by :human-resource]}

   :energy-oa {:url "http://0.0.0.0:3000/cost/energy/oa_report"
               :model-name "energy-oa"
               :model-key :energy-oa
               :data-path [:datas  :energy-oa]
               :view-path [:views  :energy-oa]
               :cache-path [:caches :energy-oa]
               :fields-key  :energy-oa
               :event/server-pull [:resource/server.query :energy-oa]
               :sub/all [:resource/all :energy-oa]
               :query-sub/filter [:resource/filter  :energy-oa]
               :query-sub/find-by [:resource/find-by :energy-oa]}

   :product-output {:url "http://0.0.0.0:3000/cost/product-output"
                    :model-name "product-output"
                    :model-key :product-output
                    :data-path [:datas  :product-output]
                    :view-path [:views  :product-output]
                    :cache-path [:caches :product-output]
                    :fields-key  :product-output
                    :event/server-pull [:resource/server.query :product-output]
                    :sub/all [:resource/all :product-output]
                    :query-sub/filter [:resource/filter  :product-output]
                    :query-sub/find-by [:resource/find-by :product-output]}

   :order-track {:url "http://0.0.0.0:3000/cost/order-track"
                 :model-name "order-track"
                 :model-key :order-track
                 :data-path [:datas  :order-track]
                 :view-path [:views  :order-track]
                 :cache-path [:caches :order-track]
                 :fields-key  :order-track
                 :event/server-pull [:resource/server.query :order-track]
                 :sub/all [:resource/all :order-track]
                 :query-sub/filter [:resource/filter  :order-track]
                 :query-sub/find-by [:resource/find-by :order-track]}

   :machine-resource {:url "http://0.0.0.0:3000/cost/machine"
                      :model-name "machine-resource"
                      :model-key :machine-resource
                      :data-path [:datas  :machine-resource]
                      :view-path [:views  :machine-resource]
                      :cache-path [:caches :machine-resource]
                      :fields-key  :machine-resource
                      :event/server-pull [:resource/server.query :machine-resource]
                      :sub/all [:resource/all :machine-resource]
                      :query-sub/filter [:resource/filter  :machine-resource]
                      :query-sub/find-by [:resource/find-by :machine-resource]}



   ;;
   })
