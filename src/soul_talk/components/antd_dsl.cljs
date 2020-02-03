(ns soul-talk.components.antd-dsl)
;; (defn input-value [in]
;;   (-> in .-target .-value))


(defn >Input [& args]
  (vec  (concat [:> js/antd.Input]  args)))

(defn >InputNumber [& args]
  (vec  (concat [:> js/antd.InputNumber]  args)))

(defn >Col [& args]
  (vec (concat [:> js/antd.Col]  args)))

(defn >Row [& args]
  (vec (concat [:> js/antd.Row]  args)))

(defn >List [& args]
  (vec (concat [:> js/antd.List]  args)))

(defn >List_Item [& args]
  (vec  (concat [:> js/antd.List.Item]  args)))

(defn >Table [& args]
  (vec  (concat [:> js/antd.Table]  args)))

(defn >Description [& args]
  (vec (concat [:> js/antd.Descriptions]  args)))

(defn >Description_Item [& args]
  (vec (concat [:> js/antd.Descriptions.Item]  args)))

(defn >Cascader [& args]
  (vec (concat [:> js/antd.Cascader]  args)))

(defn >Button [& args]
  (vec (concat [:> js/antd.Button]  args)))

(defn >Content [& args]
  (vec (concat [:> js/antd.Layout.Content] args)))

(defn >Header [& args]
  (vec (concat [:> js/antd.Layout.Header] args)))
(defn >Footer [& args]
  (vec (concat [:> js/antd.Layout.Footer] args)))

(defn >Sider [& args]
  (vec (concat [:> js/antd.Layout.Sider] args)))

(defn >Layout [& args]
  (vec (concat [:> js/antd.Layout] args)))


(defn >Divine [& args]
  (vec (concat [:> js/antd.Divider] args)))

(defn >AutoComplete [& args]
  (vec (concat [:> js/antd.AutoComplete] args)))


















