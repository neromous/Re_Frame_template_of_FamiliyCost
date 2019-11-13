(ns soul-talk.components.itemlist
  (:require [soul-talk.routes :refer [navigate!]]
            [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [re-frame.core :as rf]))

(defn sample-list
  ""
  [model show-key]
  (fn []
    (r/with-let [data (subscribe [(:db-key model)])]
      [:> js/antd.List
       {:itemLayout "vertical"
        :dataSource (if (= @data nil)
                      []
                      (-> @data vals))
        :renderItem (fn  [items]
                      (let [item  (js->clj items :keywordize-keys true)]
                        (r/as-element
                         [:> js/antd.List.Item
                          [:a {:href (str "#/" (:model-str-name model) "/" (:id item))}
                           (show-key item)]])))}])))

(defn model-list [model-key show-key]
  (r/with-let [view-key (subscribe [:active-page])
               data (subscribe [:model/all model-key])]
    (fn []
      [:> js/antd.List
       {:itemLayout "vertical"
        :dataSource (if (= @data nil)
                      []
                      (-> @data))
        :renderItem       (fn  [items]
                            (let [item  (js->clj items :keywordize-keys true)]
                              (r/as-element
                               [:> js/antd.List.Item
                                [:a {:href (str "#" @(subscribe [:model/api-get model-key]) "/" (:id item))}
                                 (show-key item)]])))}])))
