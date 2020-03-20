(ns soul-talk.views
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.modules.index.page :as index]
   [soul-talk.modules.kpn.prices :as prices]
   [soul-talk.modules.kpn.single-order :as single-order]
   [soul-talk.modules.kpn.global-order :as global-order]
   [soul-talk.modules.relations.page :as relations]
   [soul-talk.modules.mi.pages.company :as company ]
   ;;[soul-talk.page.state-capital  :as state-capital]
   [soul-talk.modules.scp.page :as scp]
   ))

(defn default-page []
  [:p "没有这一页"])

(defn pages [page-name page-state]
  (case page-name
    :home-page [index/home-page page-state]
    :product-track [global-order/home-page page-state]
    :product-detail [single-order/home-page page-state]
    :price-index [prices/home-page page-state]
    :state-capital-index [scp/home-page page-state]
    :relations [relations/home-page page-state]
    :kpn.company [company/index-page page-state]
    [default-page]))

(defn main-page []
  (r/with-let [ready? (subscribe [:initialised?])
               page-name (subscribe [:active-page])
               page-state (subscribe [:get-view])]
    (when @ready?
      (fn []
        [:div
         [pages @page-name  @page-state]]))))


