(ns soul-talk.util.model-utils
  (:require
   [soul-talk.db :refer [model-register]]))

(defn metadata->dto [item]
  (let [ks  {:table_name ""
             :column_name ""
             :column_type ""
             :data_type ""
             :column_comment ""
             :column_default ""}]
    (merge (zipmap (keys ks)  (vals ks))
           (select-keys item  (keys ks)))))
