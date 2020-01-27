(ns soul-talk.views
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [subscribe]]
   [soul-talk.view.pages :refer [pages]]
   soul-talk.view.index
   soul-talk.view.admin
   ))

(defn main-page []
  (r/with-let [ready? (subscribe [:initialised?])
               view-state (subscribe [:views])]
    (when @ready?
      (fn []
        [:div
         [pages  @view-state nil]]))))


