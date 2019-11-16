(ns sample.scatterplot
  (:require [cljsjs.d3 :as d3]
            ))

 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; data

(def height 450)
(def width 540)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Lifecycle

(defn append-svg []
  (-> js/d3
      (.select "#slopegraph")
      (.append "svg")
      (.attr "height" height)
      (.attr "width" width))) 


(def data {2005 {:natural-gas 0.2008611514256557
                 :coal        0.48970650816857986
                 :nuclear     0.19367190804075465
                 :renewables  0.02374724819670379}
           2015 {:natural-gas 0.33808321253456974
                 :coal        0.3039492492908485
                 :nuclear     0.1976276775179704
                 :renewables  0.08379872568702211}})

(defn draw-column [svg data-col index custom-attrs]
  (-> svg
      (.selectAll (str "text.slopegraph-column-" index))
      (.data (into-array data-col))
      (.enter)
      (.append "text")))

(defn remove-svg []
  (-> js/d3
      (.selectAll "#slopegraph svg")
      (.remove)))

(defn draw-slopegraph [svg data]
  (let [data-2005 (get data 2005)
        data-2015 (get data 2015)]
    (draw-column svg data-2005 0 {"x" (* width 0.25)})
    (draw-column svg data-2015 0 {"x" (* width 0.75)})))

(draw-slopegraph (append-svg) data)





