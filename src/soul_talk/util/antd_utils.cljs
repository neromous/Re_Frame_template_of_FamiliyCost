(ns soul-talk.util.antd-utils)

(defn input-value [item]
  (-> item .-target .-value))

