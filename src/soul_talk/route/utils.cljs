(ns soul-talk.route.utils
  (:require   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
               [accountant.core :as accountant]))


(defn logged-in? []
  @(subscribe [:user]))

(defn context-url [url]
  (str url))

(defn href [url]
  {:href (str url)})

(defn navigate! [url]
  (accountant/navigate! (context-url url)))

;; 加载多个事件
(defn run-events
  [events]
  (doseq [event events]
    (dispatch event)))

(defn run-events-admin
  [events]
  (doseq [event events]
    (if (logged-in?)
      (dispatch event)
      (dispatch [:add-login-event event]))))

(defn home-page-events [& events]
  (.scrollTo js/window 0 0)
  (run-events (into
               [[:load-categories]
                [:load-tags]
                [:set-active-page :home]]
               events)))


(defn admin [page]
  (r/with-let [user (subscribe [:user])]
    (if @user
      [page]
      (navigate! "#/admin"))))

