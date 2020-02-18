(ns soul-talk.components.antd-dsl)

(defn >Card [& args]
  (vec  (concat [:> js/antd.Card]  args)))

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

(def list-item js/antd.List.Item)

(defn >Table [& args]
  (vec  (concat [:> js/antd.Table]  args)))

(defn >Description [& args]
  (vec (concat [:> js/antd.Descriptions]  args)))

(def descrip-item js/antd.Descriptions.Item)

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

(defn >Divider [& args]
  (vec (concat [:> js/antd.Divider] args)))

(defn >AutoComplete [& args]
  (vec (concat [:> js/antd.AutoComplete] args)))

(defn >Switch [& args]
  (vec (concat [:> js/antd.Switch] args)))

(defn >Checkbox [& args]
  (vec (concat [:> js/antd.Checkbox] args)))

(defn >Checkbox_group [& args]
  (vec (concat [:> js/antd.Checkbox.Group] args)))

(defn >Select [& args]
  (vec (concat [:> js/antd.Select] args)))

(def select-option  js/antd.Select.Option)

(defn >Form [& args]
  (vec (concat [:> js/antd.Form] args)))

(def form-item js/antd.Form.Item)

(defn >Modal [& args]
  (vec (concat [:> js/antd.Modal] args)))

(defn >DatePicker [& args]
  (vec (concat [:> js/antd.DatePicker] args)))






















