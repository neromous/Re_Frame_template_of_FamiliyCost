(ns soul-talk.page.state-capital
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.antd-dsl
    :refer [>Input  >InputNumber >Col  >Row >List >Card
            list-item  >AutoComplete  >Modal  >Table
            >Content   >Form  >Description  descrip-item
            >Cascader  >Button  >DatePicker >Divider  >Header
            >Select  >Footer  >Switch >Sider
            >Checkbox  >Checkbox_group  >Form  form-item
            >Layout >Content]]

   [cljsjs.chartjs]
   [soul-talk.components.chart :as chart]
   [soul-talk.components.maps :as react-map]
   [soul-talk.utils :as utils]
   [soul-talk.util.date-utils :as du]
   [soul-talk.util.route-utils :refer [logged-in?
                                       context-url
                                       href
                                       navigate!
                                       run-events
                                       run-events-admin]]
   [soul-talk.page.drop-test :as dp-test]
   ))




(defn head [state  nav]

  [>Header
   [>Row
    [>Col {:xs 24 :sm 24 :md 2 :lg 4
           :on-click #(navigate! "#/")}
     [:h1 "国资监管云平台"]]
    [>Col {:xs 24 :sm 24 :md 16 :lg 16
           :style {:text-align "left"}}
     nav]
    [>Col  {:xs 24 :sm 24 :md 2 :lg 2}
     "您好, 用户"
     ]

    [>Col  {:xs 24 :sm 24 :md 2 :lg 2}
     [>Select {:style {:width 120}
               :placeholder "账号管理"
               }
      [:> js/antd.Select.Option {:value "1"}  "登录" ]
      [:> js/antd.Select.Option {:value "2"}  "设置" ]
      [:> js/antd.Select.Option {:value "3"}  "登出" ]
      
      ]
     ]


    ]])

(defn nav [state]
  [:> js/antd.Menu {:className         "home-nav"
                    :mode              "horizontal"
                    :theme "dark"
                    :defaultSelectKeys ["home"]
                    :selectedKeys      []}

   [:> js/antd.Menu.Item {:key "index"} "首页" ]
   [:> js/antd.Menu.Item {:key "todo"}  "待办列表" ]
   [:> js/antd.Menu.Item {:key "organization-info"}  "企业信息模块" ]
   [:> js/antd.Menu.Item  {:key "capital-info"}  "资本运营分析模块" ]
   [:> js/antd.Menu.Item {:key "organization-relation"}  "企业关联信息" ]

   ])

(defn side-bar [state]
  [>Sider {:className "sidebar"}
   [:> js/antd.Menu {:mode                "inline"
                     :className            "sidebar"
                     :theme               "light"
                     :default-select-keys ["user"]
                     :default-open-keys   ["account" "user"]
                     :selected-keys       ["sales-area"]}

    [:> js/antd.Menu.SubMenu
     {:key   "corporate-reform"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "企业改革"]])}
     [:> js/antd.Menu.Item {:key      "market-manager"
                            :on-click #(navigate! "#/market-anager")}
      "企业上市管理"]
     [:> js/antd.Menu.Item {:key      "project-approval"
                            :on-click #(navigate! "#/project-approval")}
      "项目审批"]

     [:> js/antd.Menu.Item {:key      "Land-co-ordination"
                            :on-click #(navigate! "#/Land-co-ordination")}
      "土地统筹"]]

    [:> js/antd.Menu.SubMenu
     {:key   "corporate-restructuring"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "企业改组"]])}

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "低效企业盘活"]

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业资产清理"]
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业清算注销"]

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业破产管理"]]
    [:> js/antd.Menu.SubMenu
     {:key   "menu-1000"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "投资管理"]])}

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "投资计划总览"]
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "投资计划审批"]

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "投资后评价"]]

    [:> js/antd.Menu.SubMenu
     {:key   "menu-3"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "企业工资管理"]])}
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业工资统筹管理"]

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业负责人薪酬管理"]]
    [:> js/antd.Menu.SubMenu
     {:key   "menu-4"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "企业绩效评定"]])}

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业绩效评定"]

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业负责人绩效评定"]]

    [:> js/antd.Menu.SubMenu
     {:key   "menu-500"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "上市企业管理"]])}
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业上市报告"]
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业上市申请"]]

    [:> js/antd.Menu.SubMenu
     {:key   "menu-5"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "企业规划管理"]])}
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业规划审批"]]

    [:> js/antd.Menu.SubMenu
     {:key   "menu-6"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "企业预决算管理"]])}

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业预算审批"]
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "企业结算审批"]]

    [:> js/antd.Menu.SubMenu
     {:key   "menu-7"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "人事管理"]])}

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "人事档案管理"]
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "人员出国情况统计"]

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "人事变动报告"]]

    [:> js/antd.Menu.SubMenu
     {:key   "menu-8"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "通用报表管理"]])}
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "报表定义"]

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "报表下发"]
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "报表统计"]

     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "报表导出"]]

    [:> js/antd.Menu.SubMenu
     {:key   "menu-9"
      :title (r/as-element [:span
                            [:> js/antd.Icon {:type "form"}]
                            [:span "信息简报"]])}
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "收件箱"]
     [:> js/antd.Menu.Item {:key      "index"
                            :on-click #(navigate! "#/state_capital_index")}
      "发件箱"]]

    [:> js/antd.Menu.Item {:key      "index"
                           :on-click #(navigate! "#/state_capital_index")}
     "法律法规"]

    [:> js/antd.Menu.Item {:key      "index"
                           :on-click #(navigate! "#/state_capital_index")}
     "信访管理"]

    [:> js/antd.Menu.Item {:key      "index"
                           :on-click #(navigate! "#/state_capital_index")}
     "党建管理"]

    [:> js/antd.Menu.Item {:key      "index"
                           :on-click #(navigate! "#/state_capital_index")}
     "资产管理"]
    ;;
    ]])

(defn static-part [state page-state & _]
  (r/with-let []

    [:div
     [>Row
      [>Col  {:span 4}]
      [>Col  {:span 4}]
      [>Col  {:span 4}]
      [>Col  {:span 4}]
      [>Col  {:span 4}]
      [>Col  {:span 4}]
      [>Col  {:span 4}]
      [>Col  {:span 4}]]]))


;; 用于绘图的部分


(defn render-data [node data]
  (js/Chart.
   node
   (clj->js
    {:type    "bar"
     :data    {:labels   ["交通" "建筑" "水力" "城建" "电力" "通信"]
               :datasets [{:label ["行业分布"]
                           :data  [2900 1920 3500 5320 7000 3200]
                           :backgroundColor ["rgba(255, 99, 132, 0.2)"
                                             "rgba(54, 162, 235, 0.2)"
                                             "rgba(255, 206, 86, 0.2)"
                                             "rgba(75, 192, 192, 0.2)"
                                             "rgba(153, 102, 255, 0.2)"
                                             "rgba(255, 159, 64, 0.2)"]
                           :borderColor ["rgba(255, 99, 132, 1)"
                                         "rgba(54, 162, 235, 1)"
                                         "rgba(255, 206, 86, 1)"
                                         "rgba(75, 192, 192, 1)"
                                         "rgba(153, 102, 255, 1)"
                                         "rgba(255, 159, 64, 1)"]
                           :fill false

                           :beginAtZero true}]}

     :options {:scales {:xAxes [{:ticks {:suggestedMax 8
                                         :suggestedMin 1
                                         :beginAtZero true}}]

                        :yAxes [{:type "linear"

                                 :ticks {:suggestedMax 8

                                         :beginAtZero true
                                         :suggestedMin 1}}
                                ;;}
                                ]}}})))

(def data-temp
  (-> [2 7 3 5 7 3]
      r/atom))

(def data-temp-v2
  (-> [3 3 4 5 6 3]
      r/atom))


;; (def data-temp
;;   (->
;;    [{:v 40 :c :Red} {:v 20 :c "Yello"}  {:v 30 :c "Blue"}]
;;    r/atom))

;; (def data-temp-v2
;;   (->
;;    [{:v 40 :c :Red} {:v 20 :c "Yello"}  {:v 30 :c "Blue"}]
;;    r/atom))


(defn destroy-chart [chart]
  (when @chart
    (.destroy @chart)
    (reset! chart nil)))

(defn render-chart [chart data]
  (fn [component]
    (when-not (empty? @data)
      (let [node (r/dom-node component)]
        (destroy-chart chart)
        (reset! chart (render-data node @data))))))

(defn chart-posts-by-votes [data]
  (let [chart (r/atom nil)]
    (r/create-class
     {:component-did-mount  (render-chart chart data)
      :component-did-update (render-chart chart data)
      :component-will-unmount (fn [_] (destroy-chart chart))
      :render               (fn [] (when @data
                                     [:canvas]))})))

(defn content [state  page-state & _]
  (r/with-let []
    [:div
     [>Row {:gutter 24}
      [>Col {:span 18}
       [>Row {:gutter 24}
        [>Col {:span 6}
         [>Card

          [:> js/antd.Statistic
           {:title "企业数量"
            :value 42
            :prefix
            (r/as-element
             [:> js/antd.Icon {:type "bank"}])}]]]

        [>Col {:span 6}
         [>Card
          [:> js/antd.Statistic
           {:title "年产值(万元)"
            :value 10500
            :prefix
            (r/as-element
             [:> js/antd.Icon {:type "pay-circle"}])}]]]

        [>Col {:span 6}
         [>Card
          [:> js/antd.Statistic
           {:title "投资收益率"
            :value "12%"
            :prefix
            (r/as-element
             [:> js/antd.Icon {:type "like"}])}]]]

        [>Col {:span 6}
         [>Card
          [:> js/antd.Statistic
           {:title "亩均效益(万元/年)"
            :value 47
            :prefix
            (r/as-element
             [:> js/antd.Icon {:type "money-collect"}])}]]]]

       [:p]

       [>Row {:gutter 18}
        [>Col {:span 14}
         [>Card
          [>Description
           {:title "业务概况" :colmns 2}
           [:> js/antd.Descriptions.Item {:label "员工人数"}  17320]
           [:> js/antd.Descriptions.Item {:label "工资总额"}  478250]
           [:> js/antd.Descriptions.Item {:label "人均产值"} 72080]
           [:> js/antd.Descriptions.Item {:label "上交利税(万元)"}  2700]
           [:> js/antd.Descriptions.Item  {:label "投资总额(万元)"} 4700]
           [:> js/antd.Descriptions.Item  {:label "上市公司数"} 3]
           [:> js/antd.Descriptions.Item  {:label "年度预算(万元)"} 3700]
           [:> js/antd.Descriptions.Item]
           [:> js/antd.Descriptions.Item]]]]

        [>Col {:span 10}

         [chart-posts-by-votes data-temp]
         [:div]]]

       [>Row {:gutter 24}
        [>Col {:span 14}
         [:div {:style   {:width 700  :height 400}}
          [:h2 "本市国资企业分布"]
          [:> (.-Map js/reactMap)    {:amapkey "14496d2c07234db4cb989b9c2549fe08"}

           [:> (.-Marker js/reactMap)
            {:position {:longitude 117.34558 :latitude 34.91145}
             :title "测试1"}]
           [:> (.-Marker js/reactMap)
            {:position {:longitude 117.65558 :latitude 34.81145}}]
           [:> (.-Marker js/reactMap)
            {:position {:longitude 117.54658 :latitude 34.79145}}]
           [:> (.-Marker js/reactMap)
            {:position {:longitude 117.44758 :latitude 34.721145}}]]]]

        [>Col {:span 10}

         [>List
          {:itemLayout "horizontal"
           :header (r/as-element [:b "本年度信息采集状态"])
           :size "small"
           :dataSource [{:title "财务申报" :process 30}
                        {:title "工资申报" :process 60}
                        {:title "股权组成申报" :process 70 :status "exception"}
                        {:title "人事信息完善"  :process 40}
                        {:title "企业资产信息" :process 80}
                        {:title "企业信息" :process 35}
                        {:title "企业预算" :process 70}
                        {:title "企业决算" :process 60}
                        {:title "企业上市进度" :process 60}
                        {:title "上市企业决算" :process 60}]

           :renderItem    #(let [n   (js->clj  % :keywordize-keys true)]
                             (r/as-element
                              [:> js/antd.List.Item
                               [:span  (:title n)]
                               [>Divider {:type "vertical"}]

                               [:> js/antd.Progress
                                {:size "small"
                                 :percent (:process n)
                                 :style {:width 380}
                                 :status (:status n)}]]))}]]]]

      [>Col {:span 6}

       [:> js/antd.Calendar
        {:fullscreen false
         :dateFullCellRender
         (fn [t]
           (let [x (-> t .date)]
             (r/as-element
              [>Row
               [>Col {:span 9}]
               [>Col {:span 7}
                [:> js/antd.Badge
                 {:status  (cond
                             (contains? #{1 2 20 15} x) "success"
                             (contains? #{6 9} x) "error"
                             (contains? #{10 16} x) "warning")

                  :offset [2 -2]}
                 x]]])))}]

       [:p]
       [dp-test/sample-1]
       [>Card  {:title "信息快报"}
        [>List
         [:> js/antd.List.Item

          [:b "企业上市"]
          [>Divider {:type "vertical"}]
          [:span "2020-02-01"]
          [>Divider {:type "vertical"}]
          "枣庄胜龙煤焦化厂上市申报"]
         [:> js/antd.List.Item

          [:b "年度财务申报"]
          [>Divider {:type "vertical"}]
          [:span "2020-02-02"]
          [>Divider {:type "vertical"}]
          "薛城焦化厂年度财务"]
         [:> js/antd.List.Item

          [:b "年度财务申报"]
          [>Divider {:type "vertical"}]
          [:span "2020-02-06"]
          [>Divider {:type "vertical"}]
          "薛城焦化厂年度预算"]
         [:> js/antd.List.Item

          [:b "工资调整"]
          [>Divider {:type "vertical"}]
          [:span "2020-02-09"]
          [>Divider {:type "vertical"}]

          "薛城焦化厂工资调整"]

         [:> js/antd.List.Item

          [:b "人事变动"]
          [>Divider {:type "vertical"}]
          [:span "2020-02-10"]
          [>Divider {:type "vertical"}]
          "薛城城建人事任命"]

         [:> js/antd.List.Item
          [:b "人事变动"]
          [>Divider {:type "vertical"}]
          [:span "2020-02-15"]
          [>Divider {:type "vertical"}]
          "薛城水务人事变动"]

         ;;
         ]]]]]))

(defn home-page [state & _]
  (r/with-let [active-page (:active-page state)
               page-state (:page-state state)
               page (r/atom {})]

    [>Layout
     [head state
      [nav state]]
     [>Layout {:style {:padding "24px"}}
      [side-bar state]

      [>Content {:style {:background "#fff"
                         :padding 24
                         :margin 0
                         :minHeight 280}}
       [content state]]]
     [hpc/foot state]]))


