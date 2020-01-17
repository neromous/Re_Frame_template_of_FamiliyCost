(ns soul-talk.page.test-page
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.base-layout :refer [content header nav footer siderbar]]
            [soul-talk.components.fields :as fields]
            [soul-talk.components.columns :as columns]
            [soul-talk.date-utils :as du]
            [soul-talk.route.utils :refer [logged-in?
                                           context-url
                                           href
                                           navigate!
                                           run-events
                                           run-events-admin]]))

(defonce org-store (r/atom {:visible false
                            :cache {:name ""
                                    :independent false
                                    :activate false
                                    :income 0.0
                                    :outcome 0.0
                                    :cash 0.0}
                            :store {:name ""
                                    :independent false
                                    :activate false
                                    :income 0.0
                                    :outcome 0.0
                                    :cash 0.0}}))

(defonce site-store (r/atom {:visible false
                             :cache {:name ""
                                     :lon 0.0
                                     :lat 0.0
                                     :type nil
                                     :in-capacity 0.0
                                     :out-capacity 0.0}}))

(defn create-modal []
  (r/with-let []
    [:> js/antd.Modal
     {:title   (str "查看: ")
      :visible  (get @org-store :visible)
      :onOk     #(swap! org-store assoc :visible false)
      :onCancel #(swap! org-store assoc :visible false)}
     [:> js/antd.Card
      [:div "数据输入模型"
       [:> js/antd.Input
        {:on-change #(swap! org-store assoc-in [:cache :name]  (->  % .-target .-value))
         :defaultValue (->  @org-store (get-in [:cache :name])  str clj->js)}]

       [:div
        [:> js/antd.Select {:value "选择上级组织"}
         [:> js/antd.Select.Option  {:value ""} "测试"]]

        [:> js/antd.Select {:value "选择归属组织"}
         [:> js/antd.Select.Option  {:value ""} "测试"]]

        [:> js/antd.Select {:value "选择组织类型"}
         [:> js/antd.Select.Option  {:value ""} "测试"]]]

       [:div
        [:> js/antd.Switch
         {:defaultValue  #(get-in @org-store [:cache :activate])
          :on-change #(swap! org-store update-in [:cache :activate]   not)
          :checkedChildren "活跃"
          :unCheckedChildren "暂停"
          :size "big"}]

        [:> js/antd.Switch
         {:defaultValue  #(get-in @org-store [:cache :independent])
          :on-change #(swap! org-store update-in [:cache :independent]   not)
          :checkedChildren "独立"
          :unCheckedChildren "附庸"
          :size "big"}]]

       [:div
        [:> js/antd.InputNumber
         {:on-change #(swap! org-store assoc-in [:cache :income]  (->  %))
          :defaultValue (->  @org-store (get-in [:cache :income]))}]

        [:> js/antd.InputNumber
         {:on-change #(swap! org-store assoc-in [:cache :outcome]  (->  %))
          :defaultValue (->  @org-store (get-in [:cache :outcome]))}]

        [:> js/antd.InputNumber
         {:on-change #(swap! org-store assoc-in [:cache :cash]  (->  %))
          :defaultValue (->  @org-store (get-in [:cache :outcome]))}]]
       ;;
       ]]
     ;;
     ]))

(defmethod header
  [:test :test]
  [db nav]

  (r/with-let [active-page (subscribe [:active])]

    [:> js/antd.Layout.Header
     [:> js/antd.Row
      [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8
                       :on-click #(navigate! "#/")}

       [:h1 "测试管理系统"]]
      [:> js/antd.Col {:xs 24 :sm 24 :md 16 :lg 16
                       :style {:text-align "right"}}
       nav]]]))

(defmethod nav
  [:test :test]
  [db]
  (r/with-let [active-page (subscribe [:active])]

    [:> js/antd.Menu {:className         "home-nav"
                      :mode              "horizontal"
                      :theme "dark"
                      :defaultSelectKeys ["home"]
                      :selectedKeys      [(key->js active-page)]}
     [:> js/antd.Menu.Item {:key      "home"
                            :on-click #(navigate! "#/")}
      "首页"]]))

(defmethod footer
  [:test :test]
  [db]
  (r/with-let [active-page (subscribe [:active])]

    [:> js/antd.Layout.Footer {:style {:text-align "center"
                                       :background "#3e3e3e"}}
     [:> js/antd.Row
      [:h4 {:style {:color "#FFF"}}
       "Made with By "
       [:a
        {:type   "link"
         :href   "https://ant.design"
         :target "_blank"}
        "Ant Design"]
       " and JIESOUL "]]]))

(defmethod siderbar
  [:test :test]
  [db]
  (r/with-let [active-page (subscribe [:active])]

    [:p]))

(def text-store (r/cursor org-store [:store :text]))
(def text-cache (r/cursor org-store [:cache :text]))
(def num-store (r/cursor org-store [:store :num]))
(def num-cache (r/cursor org-store [:cache :num]))

(def date-store (r/cursor org-store [:store :date]))
(def date-cache (r/cursor org-store [:cache :date]))
(def bool-store (r/cursor org-store [:store :bool]))
(def bool-cache (r/cursor org-store [:cache :bool]))

(def category-cache (r/cursor org-store [:cache :category]))

(reset! category-cache 1)

(def order-track-columns
  (let [origin ["生产任务通知id"
                "客户名称"
                "客户色号"
                "本场色号"
                "下单时间"
                "计划完成时间"
                "客户需求重量"
                "单价"
                "总价"
                "生产订单下单时间"
                "生产任务重量"
                "生产任务创建时间"
                "流转卡创建时间"
                "入库单创建时间"
                "生产任务计划投料"
                "生产任务实际产出"
                "生产任务入库净重"
                "生产任务入库重量"]]
    (map (fn [x] {:title x :dataIndex x :key x}) origin)))

(defmethod  content
  [:test :test]
  [db]

  (let [data (subscribe [:db/get-in [:documents "order_track"]])
        column-config [{:key "_id" :title "id" :dataIndex "_id"}
                       {:key "name" :title "name" :dataIndex "name"}]]

    [:div
     [:> js/antd.Table
      (-> {:dataSource
           (->>
            @data
            vals)
           :columns order-track-columns}
          ;; 增加筛选器

          (columns/add-filter
           [:key "客户名称"]
           (fn [value record]
             (let [record-value (-> record js->clj (get "客户名称"))]
               (=  record-value value)))
           (->> @data
                vals
                (map (fn [x] {:text (-> x (get "客户名称") str)
                              :value (-> x (get "客户名称") str)}))
                distinct)
           {})

          ;;
          )
      ;;
      ]]))

;;(def test-work @(subscribe [:db/get-in [:documents "order_track"]]))



