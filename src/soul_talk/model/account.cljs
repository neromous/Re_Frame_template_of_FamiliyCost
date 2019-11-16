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
     :fake-dataset {:1 {:id 1
                        :name "测试账户"
                        :quota  10000
                        :accountType 0}}

     :template
     {:id (dto-add {:name "账户id"
                    :inner-key :id
                    :dtype :text
                    :vtype :read})

      :name (dto-add {:name "账户名称"
                      :inner-key :name
                      :dtype :text
                      :vtype :new})

      :quota (dto-add {:name "账单额度"
                       :inner-key :quota
                       :dtype :text
                       :vtype :new})

      :accountType (dto-add {:name "账户类型"
                             :inner-key :accountType
                             :dtype :text
                             :vtype :new})}
     ;;
     })
  (defmulti account (fn [x _] x))
  (base/model-init account Account)
  (swap! Env assoc :account account))

(do
  (def Category
    {:name :category
     :title "类别"
     :root-path [:md/category]
     :url "http://localhost:8000/api/v1/Category/"
     :fake-dataset {:1 {:id 1
                        :topclass "测试账户"
                        :name "再次测试账户"}}

     :template
     {:id (dto-add {:name "账户id"
                    :inner-key :id
                    :dtype :text
                    :vtype :read})

      :topclass (dto-add {:name "上级分类"
                          :inner-key :topclass
                          :dtype :text
                          :vtype :new})

      :name (dto-add {:name "分类名称"
                      :inner-key :name
                      :dtype :text
                      :vtype :new})}})

  (defmulti category (fn [x _] x))
  (base/model-init category Category)
  (swap! Env assoc :category category)
  ;;
  )
(defmulti gears_type (fn [x _] x))

(do
  (def Gears
    {:name :gears
     :root-path [:md/gears]
     :url "http://localhost:8000/api/v1/Gears/"
     :fake-dataset {:1 {:id 1
                        :name "洗衣机"
                        :uuid "98899dfasddu767ads"
                        :price 9000}}
     :template
     {:id (dto-add {:name "商品id"
                    :inner-key :id
                    :dtype :text
                    :vtype :read})

      :name (dto-add {:name "商品名称"
                      :inner-key :name
                      :dtype :text
                      :vtype :read})

      :uuid (dto-add {:name "商品实体id"
                      :inner-key :uuid
                      :dtype :text
                      :vtype :read})

      :price (dto-add {:name "商品价格"
                       :inner-key :price
                       :dtype :text
                       :vtype :read})}})

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
     :fake-dataset  {:1 {:id 1
                         :name "测试记录"
                         :costValue 9000
                         :category "http://localhost:8000/api/v1/Category/1/"
                         :gears  "http://localhost:8000/api/v1/Gears/1/"
                         :billTime "2019-05-03"}}
     :template
     {:id (dto-add {:name "账户id"
                    :inner-key :id
                    :dtype :text
                    :vtype :read})

      :name (dto-add {:name "账户名称"
                      :inner-key :name
                      :dtype :text
                      :vtype :new})

      :costValue (dto-add {:name "交易额"
                           :inner-key :costValue
                           :dtype :text
                           :vtype :new})
      :category (dto-add {:name "交易类型"
                          :inner-key :category
                          :dtype :select
                          :vtype :new
                          :relation  category})
      :gears (dto-add {:name "关联商品"
                       :inner-key :gears
                       :dtype :select
                       :vtype :read
                       :relation gears})

      :billTime (dto-add {:name "账单时间"
                          :inner-key :billTime
                          :dtype :date
                          :vtype :new})}
   ;;
     })

  (defmulti record (fn [x _] x))
  (base/model-init record Record)
  (swap! Env assoc :record record)
  ;;
  )

