(ns soul-talk.sub.cost-consume
  (:require [soul-talk.models :refer [model-register]]
            [re-frame.core :refer [inject-cofx
                                   dispatch
                                   dispatch-sync
                                   reg-event-db
                                   reg-event-fx
                                   subscribe reg-sub]]
            [soul-talk.utils :as utils]
            [soul-talk.util.query-filter :as query-filter]))

(reg-sub
 :human-resource/all
 :<- [:resource/all :human-resource]
 (fn [all-human [_]]
   all-human))

(reg-sub
 :machine-resource/all
 :<- [:resource/all :machine-resource]
 (fn [all-data [_]]
   all-data))




(reg-sub
 :material-raw/all
 :<- [:resource/all :material-raw]
 (fn [all-data [_]]
   all-data))

(reg-sub
 :material-craft/all
 :<- [:resource/all :material-craft]
 (fn [all-data [_]]
   all-data))

(reg-sub
 :energy-oa/all
 :<- [:resource/all :energy-oa]
 (fn [all-data [_]]
   (let [map-fn  (fn [x]
                   (-> x
                       (assoc  :report_time  (new js/moment  (:report_time x)))))]
     (map  map-fn all-data )
   ))
 )


(reg-sub
 :energy.oa_report/energy.values
 :<- [:energy-oa/all]
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

;;

(reg-sub
 :human-resource/view.index-page
 :<- [:human-resource/all]
 :<- [:active-page]
 :<- [:current-page-state]
 (fn [[all-data active-page current-page-state] [_]]
   (let [order_detail_id (get current-page-state :order_detail_id)]
     (filter  #(query-filter/has-kv? % :order_detail_id order_detail_id)  all-data)
     ;;
     )))

(reg-sub
 :material-raw/view.index-page
 :<- [:material-raw/all]
 :<- [:active-page]
 :<- [:current-page-state]
 (fn [[all-data active-page current-page-state] [_]]
   (let [order_detail_id (get current-page-state :order_detail_id)]
     (filter  #(query-filter/has-kv? % :order_detail_id order_detail_id) all-data)
     ;;
     )))

(reg-sub
 :material-craft/view.index-page
 :<- [:material-craft/all]
 :<- [:active-page]
 :<- [:current-page-state]
 (fn [[all-data active-page current-page-state] [_]]
   (let [order_detail_id (get current-page-state :order_detail_id)]
     (filter  #(query-filter/has-kv? % :order_detail_id order_detail_id) all-data)
     ;;
     )))

(reg-sub
 :machine-resource/view.index-page
 :<- [:machine-resource/all]
 :<- [:active-page]
 :<- [:current-page-state]
 (fn [[all-data active-page current-page-state] [_]]
   (let [order_detail_id (get current-page-state :order_detail_id)]
     (filter  #(query-filter/has-kv? % :order_detail_id order_detail_id) all-data)
     ;;
     )))







