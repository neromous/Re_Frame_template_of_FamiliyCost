(ns soul-talk.db
  (:require  [reagent.core :as r]))

(def model-relation
  (into
   (sorted-map)
   {2 []
    1 []
    3 []}))

(def item-register
  {:todo {:url "http://localhost:3000/api/todos"
          :data-path [:datas :items :todo]}
   :relation {:url "http://localhost:3000/api/relations"
              :data-path [:datas :items :relation]}

   :sell-order {:url "http://localhost:3000/api/prodcut-track/sell-order"
                :data-path [:datas :items :sell-order]}

   :product-track {:url "http://localhost:3000/api/prodcut-track/order-track-big"
                   :data-path [:datas :items :product-track]}

   :price-index {:url ""
                 :data-path [:datas :items :price-index]}

   ;;
   })

(def model-register
  {:todos {:url "http://localhost:3000/api/todos"
           :model-name "todos"
           :table_name "todos"
           :model-key :todos
           :data-path [:datas :models :todos]
           :view-path [:views :models :todos]
           :metadata-path [:metadata :todos]
           :cache-path [:caches :todos]
           :fields-key  :todos
           :type-list [[:id int]]}

   :tags {:url "http://localhost:3000/api/tags"
          :model-name "tags"
          :table_name "tags"
          :model-key :tags
          :data-path [:datas :models :tags]
          :view-path [:views :models :tags]
          :metadata-path [:metadata :tags]
          :cache-path [:caches :tags]
          :fields-key  :tags
          :type-list [[:id int]
                      [:tag_name str]]}
   :tag_type {:url "http://localhost:3000/api/tag_types"
              :model-name "tag_type"
              :table_name "tag_type"
              :model-key :tag_type
              :data-path [:datas :models :tag_type]
              :view-path [:views :models :tag_type]
              :metadata-path [:metadata :tag_type]
              :cache-path [:caches :tag_type]
              :fields-key  :tag_type
              :type-list [[:id int]
                          [:type_name str]]}
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
