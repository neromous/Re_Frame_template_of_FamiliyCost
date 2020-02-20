(ns soul-talk.page.mother-index
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.antd-dsl
    :refer [>Input  >InputNumber >Col  >Row >List >Card
            list-item  >AutoComplete  >Modal  >Table
            >Content   >Form  >Description  descrip-item
            >Cascader  >Button  >DatePicker >Divider  >Header
            >Select  >Footer  >Switch >Sider
            >Checkbox  >Checkbox_group  >Form  form-item
            >Layout >Content]]
   [soul-talk.util.route-utils :refer [logged-in?
                                       context-url
                                       href
                                       navigate!
                                       run-events
                                       run-events-admin]]

   [soul-talk.utils :as utils]))

(defn head [state  nav]

  [>Header
   [>Row
    [>Col {:xs 24 :sm 24 :md 2 :lg 4
           :on-click #(navigate! "#/")}
     [:h1 "亲子信息网"]]
    [>Col {:xs 24 :sm 24 :md 16 :lg 16
           :style {:text-align "left"}}
     nav]]])

(defn nav [state]
  [:> js/antd.Menu {:className         "home-nav"
                    :mode              "horizontal"
                    :theme "dark"
                    :defaultSelectKeys ["home"]
                    :selectedKeys      []}

   [:> js/antd.Menu.Item {:key      "index"
                          :on-click #(navigate! "#/product-track")}
    "首页"]
   ])





(defn content [state  page-state & _]
  (r/with-let []
    [:p]))

(defn home-page [state & _]
  (r/with-let [active-page (:active-page state)
               page-state (:page-state state)
               page (r/atom {})]

    [>Layout
     [head state
      [nav state]]
     [>Layout {:style {:padding "24px"}}
      [hpc/side-bar state]

      [>Content {:style {:background "#fff"
                         :padding 24
                         :margin 0
                         :minHeight 280}}
       [content state]]]
     [hpc/foot state]]))



