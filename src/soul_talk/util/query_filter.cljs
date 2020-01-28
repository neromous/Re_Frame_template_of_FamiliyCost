(ns soul-talk.util.query-filter)

(defn  is-part-of-query? [target query]
  (= (select-keys  target (keys query))  query))

(defn  not-part-of-query? [target query]
  (not= (select-keys  target (keys query))  query))

(defn value-in-list? [target column-key value-vecs]
  (contains? (set value-vecs)  (get column-key target)))

(defn has-kv?  [target k v]
  (= (get target k)  v))



