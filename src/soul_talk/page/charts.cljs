(ns soul-talk.page.charts
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.util.route-utils :refer [run-events run-events-admin logged-in? navigate!]]
            [cljsjs.chartjs]
            [soul-talk.util.date-utils :as du]
            [cljsjs.react-beautiful-dnd]

            ))

(defn render-data [node data]
  (js/Chart.
   node
   (clj->js
    {:type    "bar"
     :data    {:labels   ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"]
               :datasets [{:label "# of Votes"
                           :data [12 19 3 5 2 3]
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
                                         "rgba(255, 159, 64, 1)"]}]}

     :options {:scales {:xAxes [{:display false :ticks {:suggestedMax 8
                                                        :suggestedMin 1}}]
                        :yAxex [{:ticks {:suggestedMax 8
                                         :suggestedMin 1}}]}}})))

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

(def data-temp
  (->
   [{:v 10 :c :Red} {:v 20 :c "Yello"}  {:v 30 :c "Blue"}]
   r/atom))

