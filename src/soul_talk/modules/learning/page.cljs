(ns soul-talk.modules.learning.page
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch dispatch-sync subscribe]]
   [soul-talk.components.home-page :as hpc]
   [soul-talk.components.modal :as modal]
   [soul-talk.components.common :as c]
   [soul-talk.util.date-utils :as du]
   [soul-talk.util.data-formatter :as formatter]
   [soul-talk.components.antd-dsl
    :refer [>Input  >InputNumber >Col  >Row >List >Card
            list-item  >AutoComplete  >Modal  >Table
            >Content   >Form  >Description  descrip-item
            >Cascader  >Button  >DatePicker >Divider  >Header
            >Select  >Footer  >Switch >Sider
            >Checkbox  >Checkbox_group  >Form  form-item
            >Layout >Content]]
   [soul-talk.utils :as utils]))

(defn home-page [state & _]
  (r/with-let [active-page (:active-page state)
               page (:page-state state)
               page-state (r/atom {})]

    [>Layout
     [hpc/head state
      [hpc/nav state]]
     [>Layout {:style {:padding "24px"}}

      [>Content {:style {:background "#fff"
                         :padding 24
                         :margin 0
                         :minHeight 280}}

       [>Row {:gutter 16}
        [>Col   {:span 5}
         [>Divider]]

        [>Col {:span 19}
         [>Row {:gutter 24}
          [>Col {:span 16}]

          [>Col {:span 8}]]

         [:p]
         [>Row]


         ;;
         ]]]]

     [hpc/foot state]]))



