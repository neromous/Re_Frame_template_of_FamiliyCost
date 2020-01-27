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
                  :query-sub/find-by [:resource/find-by :material-raw]

                  }

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







   ;;
   })
