(ns soul-talk.models
  (:require soul-talk.model.account
            [soul-talk.model.account :refer [account record category]]
            [soul-talk.components.modal :as md]))

(def models
  {:account account
   :record record
   :category category})

(defn preform-modals []
  (doall
   (for  [[k item] models]
     [:div
      [md/input-modal item]
      [md/edit-modal item]
      [md/show-modal item]])))
