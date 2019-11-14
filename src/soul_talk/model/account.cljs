(ns soul-talk.model.account
  (:require [soul-talk.model.base :as base]))

(defn dto-add [origin]
  (-> origin
      ;; 以下是为了生成columns的table做的
      (assoc :dataIndex (name (:inner-key origin)))
      (assoc :key (name (:inner-key origin)))
      (assoc :title (:name origin))))

(def Account
  {:name :account
   :title "账户"
   :root-path [:md/account]
   :url "http://localhost:8000/api/v1/Account/"
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

(def Record
  {:name :record
   :title "账单记录"
   :root-path [:md/record]
   :url "http://localhost:8000/api/v1/CostRecord/"
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
    :billTime (dto-add {:name "账单时间"
                        :inner-key :billTime
                        :dtype :date
                        :vtype :new})}
   ;;
   })

(defmulti record (fn [x _] x))
(base/model-init record Record)

(def Category
  {:name :category
   :root-path [:md/category]
   :url "http://localhost:8000/api/v1/Category/"
   :template
   {:id (dto-add {:name "账户id"
                  :inner-key :id
                  :dtype :text
                  :vtype :read})

    :name (dto-add {:name "上级分类"
                    :inner-key :topclass
                    :dtype :text
                    :vtype :new})

    :costValue (dto-add {:name "分类名称"
                         :inner-key :name
                         :dtype :text
                         :vtype :new})}})

(defmulti category (fn [x _] x))
(base/model-init category Category)
