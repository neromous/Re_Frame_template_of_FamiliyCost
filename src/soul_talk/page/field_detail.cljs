(ns soul-talk.page.field-detail
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]
            [soul-talk.components.common :as c]
            [soul-talk.components.fields :as fields]
            [soul-talk.components.tables :as tables]
            [soul-talk.components.columns :as columns]
            [soul-talk.layouts.home-layout :as home-layout]
            [soul-talk.date-utils :as du]
            [soul-talk.utils :as utils]
            [soul-talk.route.utils :refer [logged-in?
                                           context-url
                                           href
                                           navigate!
                                           run-events
                                           run-events-admin]]))

(defn  form-field-attrib-add []
  (r/with-let  [form (subscribe [:table/table.fields])
                selected (subscribe [:table/selected-field])]
    (fn []
      (let [attrib-form  (-> {:attrib-name ""
                              :attrib-value ""}
                             r/atom)
            attrib-name (r/cursor  attrib-form [:attrib-name])
            attrib-value (r/cursor  attrib-form [:attrib-value])
            item (last @selected)
            table-name (-> item :table_name keyword)
            field-name (-> item :column_name keyword)]

        [:div
         [:> js/antd.Input {:on-change
                            #(reset! attrib-name (-> % .-target .-value
                                                     (or "nil") keyword))}]
         [:> js/antd.Input {:on-change
                            #(reset! attrib-value (-> % .-target .-value
                                                      (or "nil")))}]
         [:> js/antd.Button  {:on-click
                              #(dispatch
                                [:table/table.fields table-name field-name   {@attrib-name  @attrib-value

                                                                              :type "primary"}])}  "输入属性"]]
        ;;
        ))))

(defn form-relation-input []
  (r/with-let [relation (subscribe [:table/table-relation]) ]
    (fn []
      (let [edit-relation (-> [] )   ]
        

        ;;
        ))))

(defn field-content []
  (r/with-let [selected (subscribe [:table/selected-field])]

    (fn []
      (let [item (->> @selected last  )
            table-name (-> @selected last :table_name keyword)
            field-name (-> @selected last :column_name keyword)]
        [:div
         [:> js/antd.Row
          [:> js/antd.Col {:span 4}
           [:> js/antd.Breadcrumb {:style {:margin "16px 0"}}
            [:> js/antd.Breadcrumb.Item table-name]
            [:> js/antd.Breadcrumb.Item field-name]]]]
         ;;

         [:> js/antd.Row
          [:> js/antd.Col {:span 8}

           [form-field-attrib-add]
           [:p]

           (for [[k v]   item]
             ^{:key k}
             [:div
              [:> js/antd.Row
               [:> js/antd.Col {:span 12}
                [:label k]]
               [:> js/antd.Col {:span 10}
                [:label v]]]

              [:> js/antd.Divider]])
           ;;
           ]

          [:> js/antd.Col {:span 6}


           ;;
           ]]

         [:> js/antd.Divider]
         [:> js/antd.Row
          [:> js/antd.Col  {:span 12}]

          [:> js/antd.Col  {:span 12}]]
         ;; [:> js/antd.Row

         ;;  [:> js/antd.Col {:span 6}
         ;;   [:label "字段名"]]
         ;;  [:> js/antd.Col {:span 6}
         ;;   [:label field-name]]]

         ;; [:p]
         ;; ;;
         ;; [:> js/antd.Row
         ;;  [:> js/antd.Col {:span 6}
         ;;   [:label ""]]
         ;;  [:> js/antd.Col {:span 6}
         ;;   [:label field-name]]]
         ;;
         ]))))

(defn home-page
  []
  (r/with-let  []
    (let []
      [home-layout/table-field-detail-layout
       home-layout/table-top-head
       home-layout/table-top-nav
       home-layout/default-table-side-bar
       field-content
       ;;home-layout/table-content
       home-layout/table-foot])))



