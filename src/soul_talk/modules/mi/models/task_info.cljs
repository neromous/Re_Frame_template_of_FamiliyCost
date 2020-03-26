(ns soul-talk.modules.mi.models.task-info
  (:require
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.util.reframe-helper :refer
    [sub> act>  to-columns fix-key fix-value]]))

