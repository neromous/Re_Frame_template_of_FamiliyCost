(ns soul-talk.models
  (:require soul-talk.model.account
            [soul-talk.model.account :refer [account record category gears]]
            [soul-talk.route.utils :refer [run-events run-events-admin logged-in? navigate!]]
            [re-frame.core :refer [inject-cofx dispatch dispatch-sync reg-event-db reg-event-fx subscribe reg-sub]]
            [soul-talk.components.modal :as md]
            [soul-talk.db :refer [Env]]))

(defn preform-modals []
  (doall
   (for  [[k item] @Env]
     ^{:key  (str "input-" k)}
     [:div   [md/input-modal item]
      [md/edit-modal item]
      [md/show-modal item]])))


;; (defn preform-modals []
;;   (doall
;;    (concat
;;     (for  [[k item] @Env]
;;       ^{:key  (str "input-" k)}
;;       [md/input-modal item])
;;     (for  [[k item] @Env]
;;       ^{:key  (str "edit-" k)}
;;       [md/edit-modal item])
;;     (for  [[k item] @Env]
;;       ^{:key  (str "show-" k)}
;;       [md/show-modal item]))))



