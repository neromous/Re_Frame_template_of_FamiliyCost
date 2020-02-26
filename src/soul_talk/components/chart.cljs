(ns soul-talk.components.chart
  (:require [reagent.core :as r]
           ;; [soul-talk.layouts.basic-layout :refer [basic-layout]]
            [cljsjs.chartjs]
            [cljsjs.d3]
            [soul-talk.components.common :as c]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Model

(defn render-data [node data]
  (js/Chart.
   node
   (clj->js
    {:type    "bar"
     :data    {:labels   (map :title data)
               :datasets [{:label "votes"
                           :data  (map :score data)}]}
     :options {:scales {:xAxes [{:display false}]}
               :scaleGridLineColor "rgba(0,0,0,.05)"

               }})))





(defonce app-state
  (r/atom
   {:width 300
    :data  [{:x 5}
            {:x 2}
            {:x 3}]}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Util Fns

(defn get-width [ratom]
  (:width @ratom))

(defn get-height [ratom]
  (let [width (get-width ratom)]
    (* 0.8 width)))

(defn get-data [ratom]
  (:data @ratom))

(defn randomize-data [ratom]
  (let [points-n (max 2 (rand-int 8))
        points   (range points-n)
        create-x (fn [] (max 1 (rand-int 5)))]
    (swap! ratom update :data
           (fn []
             (mapv #(hash-map :x (create-x))
                   points)))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Components


(defn btn-toggle-width [ratom]
  [:div
   [:button
    {:on-click #(swap! ratom update
                       :width (fn [width]
                                (if (= 300 width) 500 300)))}
    "Toggle width"]])

(defn btn-randomize-data [ratom]
  [:div
   [:button
    {:on-click #(randomize-data ratom)}
    "Randomize data"]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Viz

;; Container

(defn container-enter [ratom]
  (-> (js/d3.select "#barchart svg")
      (.append "g")
      (.attr "class" "container")))

(defn container-did-mount [ratom]
  (container-enter ratom))


;; Bars


(defn bars-enter [ratom]
  (let [data (get-data ratom)]
    (-> (js/d3.select "#barchart svg .container .bars")
        (.selectAll "rect")
        (.data (clj->js data))
        .enter
        (.append "rect"))))

(defn bars-update [ratom]
  (let [width   (get-width ratom)
        height  (get-height ratom)
        data    (get-data ratom)
        data-n  (count data)
        rect-height (/ height data-n)
        x-scale (-> js/d3
                    .scaleLinear
                    (.domain #js [0 5])
                    (.range #js [0 width]))]
    (-> (js/d3.select "#barchart svg .container .bars")
        (.selectAll "rect")
        (.data (clj->js data))
        (.attr "fill" "green")
        (.attr "x" (x-scale 0))
        (.attr "y" (fn [_ i]
                     (* i rect-height)))
        (.attr "height" (- rect-height 1))
        (.attr "width" (fn [d]
                         (x-scale (aget d "x")))))))

(defn bars-exit [ratom]
  (let [data (get-data ratom)]
    (-> (js/d3.select "#barchart svg .container .bars")
        (.selectAll "rect")
        (.data (clj->js data))
        .exit
        .remove)))

(defn bars-did-update [ratom]
  (bars-enter ratom)
  (bars-update ratom)
  (bars-exit ratom))

(defn bars-did-mount [ratom]
  (-> (js/d3.select "#barchart svg .container")
      (.append "g")
      (.attr "class" "bars"))
  (bars-did-update ratom))


;; Main


(defn viz-render [ratom]
  (let [width  (get-width ratom)
        height (get-height ratom)]
    [:div
     {:id "barchart"}

     [:svg
      {:width  width
       :height height}]]))

(defn viz-did-mount [ratom]
  (container-did-mount ratom)
  (bars-did-mount ratom))

(defn viz-did-update [ratom]
  (bars-did-update ratom))

(defn viz [ratom]
  (r/create-class
   {:r-render      #(viz-render ratom)
    :component-did-mount #(viz-did-mount ratom)
    :component-did-update #(viz-did-update ratom)}))




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page


(defn home []
  [:div
   [:h1 "Barchart"]
   [btn-toggle-width app-state]
   [btn-randomize-data app-state]
   [viz app-state]])



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

;; (defn show-revenue-chart
;;   []
;;   (let [context (.getContext (js/document.getElementById "rev-chartjs") "2d")
;;         chart-data {:type "bar"
;;                     :data {:labels ["2012" "2013" "2014" "2015" "2016"]
;;                            :datasets [{:data [5 10 15 20 25]
;;                                        :label "Rev in MM"
;;                                        :backgroundColor "#90EE90"}
;;                                       {:data [3 6 9 12 15]
;;                                        :label "Cost in MM"
;;                                        :backgroundColor "#F08080"}]}}]
;;     (js/chartjs. context (clj->js chart-data))))

;; (defn canvas-component
;;   []
;;   (r/create-class
;;    {:component-did-mount #(show-revenue-chart)
;;     :display-name        "chartjs-component"
;;     :r-render      (fn []
;;                            [:canvas {:id "rev-chartjs"
;;                                      :width "200"
;;                                      :height "150"}])}))

;; (defn complex-component [a b c]
;;   (let [state (r/atom {})]
;;     (r/create-class
;;      {:component-did-mount (fn [comp]
;;                              (js/console.log comp))

;;       :display-name "complex-component"
;;       :r-render
;;       (fn [a b c]
;;         [:div {:class c}
;;          [:i a] " " b])})))

;; (defn dash-page []
;;   (fn []

;;     [:div
;;      [:h1.h2 "Dashboard"]
;;      [:div.btn-toolbar.mb-2.mb-md-0
;;       [:div.btn-group.mr-2
;;        [:button.btn.btn-sm.btn-outline-secondary "Share"]
;;        [:button.btn.btn-sm.btn-outline-secondary "Export"]
;;        [canvas-component]]]]))

;; (def ^:private revenue-by-industry (js/anychart.data.set))
;; (def ^:private revenue-by-sales (js/anychart.data.set))
;; (def ^:private revenue-by-product (js/anychart.data.set))
;; (def ^:private revenue-by-quarter (atom nil))

;; (defn create
;;   "Setup all charts"
;;   []
;;   (js/anychart.theme (clj->js {:defaultTooltip {:title {:enabled false}}}))

;;   (doto (anychart.bar revenue-by-industry)
;;     (.container "rev-by-industry")
;;     (.title "Revenue by industry")
;;     (.draw))

;;   (doto (anychart.column revenue-by-sales)
;;     (.container "rev-by-sales")
;;     (.title "Revenue by sales rep")
;;     (.draw))

;;   (doto (anychart.pie revenue-by-product)
;;     (.container "rev-by-product")
;;     (.title "Revenue by product")
;;     (.draw))

;;   (reset! revenue-by-quarter (anychart.line))
;;   (doto @revenue-by-quarter
;;     (.container "rev-by-quarter")
;;     (.title "Revenue by quarter")
;;     (.draw)))

;; (defn update-charts
;;   "Replace charts data"
;;   [data]
;;   (.data revenue-by-industry (clj->js (data :revenue-by-industry)))
;;   (.data revenue-by-sales (clj->js (data :revenue-by-sales-reps)))
;;   (.data revenue-by-product (clj->js (data :revenue-by-product)))
;;   (.removeAllSeries @revenue-by-quarter)
;;   (.apply (.-addSeries @revenue-by-quarter)
;;           @revenue-by-quarter
;;           (anychart.data.mapAsTable (clj->js (data :revenue-by-quarter)))))
