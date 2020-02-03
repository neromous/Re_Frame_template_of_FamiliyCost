(ns soul-talk.util.subs
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]))

(def sub>  (comp deref re-frame.core/subscribe))
