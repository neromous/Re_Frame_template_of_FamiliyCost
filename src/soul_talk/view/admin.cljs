(ns soul-talk.view.admin
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.page.admin :as admin]
   [soul-talk.view.pages :refer [pages]]))

(defmethod pages :admin-page [state & _]  [admin/home-page state])

(defmethod pages :admin-detail-page [state _]
  (fn []
    [:p "这里是明细页面"
     [:p (str state)]]))

(defn admin-model-table [model-key]
  ;;(r/with-let (subscribe [:resource/all model-key])

    ;;
;;  )
  )

(defn admin-main-page []
  (r/with-let [view-state  (subscribe [:views])]
    (fn []
      (let [active-model  (get @view-state  :admin-active-model)
            id (get-in @view-state [active-model :id])]
        (r/with-let [])

        [:div
         [admin-model-table]]


        ;;
        ))))
