(ns soul-talk.model.init)
;; 定义数据查找路由
(defmulti model-path :method)
;; 定义模型解析函数

;; view 路径
(def sample-db-template
  ;; 说明文档
  {:model
   {:input/model-key
    {:data {:1  {:id (keyword 1)}}}
    :state {:input/view-key {:show-id []
                             :pagination {}
                             :group-keys []}
            :api "input/prefix"}}

   :view {:input/view-key {:model {:inputs/model1 {:show-ids []
                                                   :pagination {}
                                                   :cache {}}

                                   :inputs/model2 {:show-ids []
                                                   :pagination {}
                                                   :cache {}}}}}})

;; 路径优化函数


