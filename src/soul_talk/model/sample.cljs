(ns soul-talk.model.sample)

(def meta-data
  {:organization {:columns {:id          [:id :primary-key nil nil  "id"]
                            :name        [:name :text nil nil "组织名"]
                            :independent [:independent :bool false nil "独立性"]
                            :parent-id   [:parent-id :object nil :organization  "上级组织id"]
                            :master-id   [:master-id :object nil :organization  "主管组织id"]
                            :activate    [:activate :bool nil nil "活跃性"]
                            :type        [:type :object nil :organization-type "组织类型"]
                            :income      [:income :float nil nil "收入"]
                            :outputcome  [:outputcome :float nil nil "支出"]
                            :cash        [:cash :float nil nil "现金"]}
                  :interface {:title "组织名称"
                              :root-path [:organization]
                              :url-prefix "organization"}}

   :organization-type {:columns {:id     [:id :primary-key nil nil  "id"]
                                 :name   [:name :text nil nil "组织名"]}

                       :interface {}}
   :site  {:columns {:id           [:id :primary-key nil nil "场所id"]
                     :name         [:name :text nil nil "场所名称"]
                     :parent-id    [:parent-site-id :object nil :site "上级场所"]
                     :org-id       [:org-id :object nil :organization  "所属组织id"]
                     :lon          [:lon :float nil nil "经度"]
                     :lat          [:lat :float nil nil "纬度"]
                     :type         [:type :key nil nil "场所类型"]
                     :in-capacity  [:in-capacity :float nil nil "输入容量(吨每小时)"]
                     :out-capacity [:out-capacity :float nil nil "输出容量(吨每小时)"]}

           :interface {:title "站点"
                       :root-path [:site]
                       :url-prefix "site"}}
   :package {:columns {:id  [:id :primary-key nil nil "包裹id"]
                       :name [:name :text nil nil "包裹名称"]
                       :org-id [:org-id :object nil :org  "所属组织"]
                       :master-id [:material-id :object nil :material  "材料id"]
                       :quantity [:quantity :float nil nil "数量"]
                       :create-time [:create-time :date nil nil "包裹创建时间"]}
             :interface {}}

   :material {:columns {:id [:id :primary-key nil nil "材料id"]
                        :name [:name :text nil nil "材料名称"]
                        :unit [:unit :text nil nil "单位"]
                        :category [:category :text nil nil "材料类别"]}
              :interface {}}
   :formula-tips {:columns {:id          [:id :primary-key nil nil "配方明细id"]
                            :formula-id  [:formula-id :object nil :formula  "配方id"]
                          ;;输入 输出  催化剂
                            :output-type [:output-type :text nil nil "输出类型"]
                            :type        [:type :text nil nil "材料类型"]
                          ;;包括 材料 催化剂 设备时间
                            :material-id [:material :object nil :material   "材料id"]
                            :amout       [:amout :float nil nil "材料数量"]}
                  :interface {}}

   :formula {:columns {:id [:id :primary-key nil nil "配方id"]
                       :name [:name :text nil nil "配方名称"]}
             :interface {}}

   :route   {:columns {:id              [:id :primary-key nil nil "路径id"]
                       :name            [:name :text nil nil "路径名称"]
                       :start-site-id   [:start-site-id :object nil :site "起始路径id"]
                       :target-site-id  [:target-site-id :object nil :site "目标路径id"]}
             :interface {}}

   :cango {:columns {:id          [:id :primary-key nil nil "集装箱id"]
                     :name        [:name :text nil nil "集装箱名称"]
                     :package-ids [:package-ids :object-sets nil :package  "包括名称序列"]
                     :route-id    [:route-id :object nil :route  "路径"]
                     :create-time [:create-time :datetime nil nil "集装箱创建时间"]
                     :attach-time [:attach-time :datetime nil nil "集装箱到达目的地时间"]}
           :interface {}}

   :storage {:columns {:site-id     [:site-id :object nil :site  "所处地点id"]
                       :material-id [:material-id :object nil :material  "材料id"]
                       :amout       [:amount :float 0 nil "存储量"]
                       :org-id      [:belongto :object :organization   "归属哪个组织"]}
             :interface {}}

   :equipment {:columns {:id               [:id :primary-key nil nil "设备id"]
                         :name             [:name :text nil nil "设备名称"]
                         :site-id          [:site-id :object nil :site  "地点id"]
                         :maintenance      [:maintenance :float nil nil "维护费"]
                         :acquisition      [:acquisition :float nil nil  "购置费"]
                         :acquisition-time [:acquisition-time :date nil nil "购置时间"]
                       ;; 这个机器可以被作为那些资源调动 机械基本单位为小时
                         :ability          [:ability :object-sets nil :material  "设备能力列表"]}
               :interface {}}

   :person {:columns {:id         [:id :primary-key nil nil "人员id"]
                      :name       [:name :text nil nil "人员名称"]
                      :org-id     [:org-id :object nil :organization "所属组织id"]
                      :site-id    [:site-id :object nil :site "归属地点id"]
                      :work-time  [:work-time :float nil nil "当前回合可用工时"]
                      :salary     [:salary :float nil nil "日薪"]
                      :on-duty-time  [:on-duty-time :date nil nil "当前回合消耗工时"]
                    ;; 这个人可以被作为某种资源调动的范围
                    ;; 人的基本单位为小时
                      :ability    [:ability :object-sets nil :material  "可用清单"]}
            :interface {}}
;;
   })
