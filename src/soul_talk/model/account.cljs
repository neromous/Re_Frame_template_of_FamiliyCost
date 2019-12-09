(ns soul-talk.model.account
  (:require [soul-talk.model.base :as base]
            [soul-talk.db :refer [Env]]))

(defn dto-add [origin]
  (-> origin
      ;; 以下是为了生成columns的table做的
      (assoc :dataIndex (name (:inner-key origin)))
      (assoc :key (name (:inner-key origin)))
      (assoc :title (:name origin))))

(do
  (def Account
    {:name :account
     :title "账户"
     :root-path [:md/account]
     :url "http://localhost:8000/api/v1/Account/"
     :fake-dataset
     {"http://localhost:8000/api/v1/Account/"
      {:id 1
       :name "测试账户"
       :quota  10000
       :url "http://localhost:8000/api/v1/Account/"
       :accountType 0}}

     :template  {:id  {:name "账户id"
                       :dtype :text}

                 :name  {:name "账户名称"
                         :dtype :text}

                 :quota  {:name "账单额度"
                          :dtype :text}

                 :accountType {:name "账户类型"
                               :dtype :text}}
     ;;
     })
  ;; 注册多重方法
  (defmulti account (fn [x _] x))
  ;; 批量注册子方法
  (base/model-init account Account)
  ;; 注册模型属性
  (swap! Env assoc :account account))

(do
  (def Category
    {:name :category
     :title "类别"
     :root-path [:md/category]
     :url "http://localhost:8000/api/v1/Category/"
     :fake-dataset
     {"http://localhost:8000/api/v1/Category/1/"
      {:id 1
       :topclass "测试账户"
       :url "http://localhost:8000/api/v1/Category/1/"
       :name "再次测试账户"}}

     :template {:id {:name "账户id"
                     :dtype :text}

                :topclass {:name "上级分类"
                           :dtype :text}

                :name {:name "分类名称"
                       :dtype :text}}})

  (defmulti category (fn [x _] x))
  (base/model-init category Category)
  (swap! Env assoc :category category)
  ;;
  )

(do
  (def Gears-type
    {:name :gears
     :root-path [:md/gears]
     :url "http://localhost:8000/api/v1/Gears/"
     :fake-dataset
     {"http://localhost:8000/api/v1/Gears/"
      {:id 1
       :name "洗衣机"
       :uuid "98899dfasddu767ads"
       :url "http://localhost:8000/api/v1/Gears/"
       :price 9000}}
     :template
     {:id  {:name "商品id"
            :dtype :text}

      :name  {:name "商品名称"
              :dtype :text}

      :uuid  {:name "商品实体id"
              :dtype :text}

      :price  {:name "商品价格"
               :dtype :text}}})

  (defmulti gears-type (fn [x _] x))
  (base/model-init gears-type Gears-type)
  (swap! Env assoc :gears-type gears-type)
  ;;
  )

(do
  (def Gears
    {:name :gears
     :root-path [:md/gears]
     :url "http://localhost:8000/api/v1/Gears/"
     :fake-dataset
     {:1
      {:id 1
       :name "洗衣机"
       :uuid "98899dfasddu767ads"
       :url "http://localhost:8000/api/v1/Gears/"
       :price 9000}}
     :template
     {:id  {:name "商品id"
            :dtype :text}

      :name  {:name "商品名称"
              :dtype :text}

      :uuid  {:name "商品实体id"
              :dtype :text}

      :price  {:name "商品价格"
               :dtype :text}}})

  (defmulti gears (fn [x _] x))
  (base/model-init gears Gears)
  (swap! Env assoc :gears gears)
  ;;
  )

(do
  (def Record
    {:name :record
     :title "账单记录"
     :root-path [:md/record]
     :url "http://localhost:8000/api/v1/CostRecord/"
     :fake-dataset
     {"http://localhost:8000/api/v1/CostRecord/1/"
      {:id 1
       :name "测试记录"
       :costValue 9000
       :url "http://localhost:8000/api/v1/CostRecord/1/"
       :category "http://localhost:8000/api/v1/Category/1/"
       :gears  "http://localhost:8000/api/v1/Gears/1/"
       :billTime "2019-05-03"}}
     :template
     {:id {:name "账户id"
           :dtype :text}

      :name  {:name "账户名称"
              :dtype :text}

      :costValue  {:name "交易额"
                   :dtype :text}
      :category {:name "交易类型"
                 :dtype :select
                 :relation  category}
      :gears {:name "关联商品"
              :dtype :select
              :relation gears}

      :billTime {:name "账单时间"
                 :dtype :date}}
   ;;
     })

  (defmulti record (fn [x _] x))
  (base/model-init record Record)
  (swap! Env assoc :record record)
  ;;
  )

(do
  (def Todo
    {:name :todo
     :title "账单记录"
     :root-path [:md/todo]
     :url "http://localhost:8000/api/v1/CostTodo/"
     :fake-dataset
     {"http://localhost:8000/api/v1/CostTodo/1/"
      {:是否完成 false
       :名称 "空"
       :优先级 0
       :紧急程度 0
       :实际发生日期 "1900-01-01"
       :计划发生日期 "1900-01-01"
       :循环类型 false
       :实际花费 0
       :计划花费 0
       :url "http://localhost:8000/api/v1/CostTodo/1/"
       :实际结束日期 "1900-01-01"}}

     :template

     {:是否完成 {:title "是否完成"
             :dtype :bool}
      :名称 {:title "名称"
           :dtype :text}
      :优先级 {:title "优先级"
            :dtype :int}
      :紧急程度 {:title "紧急程度"
             :dtype :int}
      :实际发生日期 {:title "实际发生日期"
               :dtype :date}
      :计划发生日期 {:title "计划发生日期"
               :dtype :date}
      :循环类型 {:title "循环类型"
             :dtype :text}
      :实际花费 {:title "实际花费"
             :dtype :float}
      :计划花费 {:title "计划花费"
             :dtype :float}
      :实际结束日期 {:title "实际结束日期"
               :dtype :date}
      :url {:title "url"
            :dtype :text}}
   ;;
     })

  (defmulti todo (fn [x _] x))
  (base/model-init todo Todo)
  (swap! Env assoc :todo todo)
  ;;
  )



