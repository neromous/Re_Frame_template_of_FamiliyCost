(ns soul-talk.handler.cost-energy
  (:require  [re-frame.core :refer [subscribe
                                    reg-event-db
                                    dispatch
                                    reg-sub
                                    reg-event-fx]]
             [ajax.core :refer [POST
                                GET
                                DELETE
                                PUT
                                ajax-request
                                url-request-format
                                json-request-format
                                json-response-format]]
             [soul-talk.utils :as utils]
             [soul-talk.date-utils :as du]
             ))

(reg-sub
 :energy.oa_report/all
 (fn [db [_]]
   (let [map-fn  (fn [x]
                   (-> x
                       (assoc  :report_time  (new js/moment  (:report_time x)))))]
     (->
      (get-in db  [:energy.oa_report :datasets])
      (->> (map  map-fn))))))

(reg-sub
 :energy.oa_report/filter
 :<- [:energy.oa_report/all]
 :<- [:cost.index/state]
 (fn [[all-data state] [_]]
   (let [date-min (get state  :time_begin)
         date-max (get state :time_end)
         ]
      (filter #(du/moment->is_time_between? (:report_time %) date-min date-max) all-data )
     ;;
     )))

(reg-sub
 :energy.oa_report/energy.values
 :<- [:energy.oa_report/filter]
 (fn [all-data [_]]
   (let [electric  (map :energy_electric all-data)
         water  (map :energy_water all-data)
         steam (map :energy_steam all-data)]
     {:energy_electric (->> (map utils/round-number electric)
                            (apply +))
      :energy_water (->> (map utils/round-number water)
                         (apply +))
      :energy_steam (->> (map utils/round-number steam)
                         (apply +))}

     ;;
     )))



