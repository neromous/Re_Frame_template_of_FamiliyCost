(ns soul-talk.page.drop-test
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

   [cljsjs.react-beautiful-dnd]
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
                                       run-events-admin]]))

(defn sample-1 []
  (r/as-element
   [:> js/ReactBeautifulDnd.DragDropContext
    {:onDragEnd #(println "ddd")}
    [:p "这里可以拖拽"]]))
