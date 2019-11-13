(ns soul-talk.utils
  (:require
                 [re-frame.core :refer [reg-sub
                                     dispatch
                                     reg-event-fx
                                     reg-event-db
                                     subscribe]]))
;; route 的工具函数



(defn mapset2map [mapset] (into {} (for [x mapset] (hash-map  (-> x :id str keyword) x))))

(defn query_filter [model-map query]
  (filter #(= query (select-keys % (keys query))) (vals model-map)))

(defn columns-make-filter [key-name text-name model data]
  {:title text-name
   :dataIndex (name key-name)
   :key (name key-name)
   :filters (for [item (set (map key-name @data))]
              {:text item
               :value item})
   :onFilter (fn [value record]
               (= (-> record (js->clj :keywordize-keys true) key-name) value))
   :align "center"})

(defn columns-fake-maker [sample-data]
  (let [{:keys [formatter data]}  sample-data
        columns-detail formatter
        ]
    (fn []
      (for [column  (:columns columns-detail)]
        (if (contains? (set (:filters columns-detail)) (name column))
          {:title (name column)
           :dataIndex (name column)
           :key (name column)
           :filters (for [item (map #((keyword column) %) data)]     {:text item
                                                                      :value item})
           :onFilter (fn [value record]
                       (= (-> record (js->clj :keywordize-keys true) ((keyword column))) value))}
          {:title (name column)
           :dataIndex (name column)
           :key (name column)})))))




