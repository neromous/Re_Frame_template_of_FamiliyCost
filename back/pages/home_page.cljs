(ns soul-talk.pages.home-page
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.pages.skeleton :as skeleton]
            [soul-talk.components.global-header :as header]
            [soul-talk.components.nav :as nav]
            [soul-talk.components.siderbar :as side]
            [soul-talk.components.itemlist :as itemlist]
            [soul-talk.components.table :as table]
            [soul-talk.components.global-footer :as footer]
            [re-frame.core :refer [subscribe]]
            ;; 路由定义协助
            [secretary.core :as secretary :refer-macros [defroute]]
            [soul-talk.routes :refer [
                                     logged-in?
                                     context-url
                                     href
                                     navigate!
                                     run-events
                                     run-events-admin
                                     ]]
            ))



(def home-page
  (skeleton/layout-hcfs-left
   header/header nav/nav-home
   (table/sample-model-table :hoempage :contact (fn [] [{:title "name"
                                                         :dataIndex "name"
                                                         :key "name"}
                                                        {:title "id"
                                                         :dataIndex "id"
                                                         :key "id"}]))
   footer/footer
   side/siderbar))
