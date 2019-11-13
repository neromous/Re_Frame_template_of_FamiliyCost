(ns soul-talk.components.post-layout
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            ))

(defn post-errors [post]
  (->
   (b/validate
    post
    :title [[v/required :message "标题不能为空\n"]]
    :category [[v/required :message "请选择一个分类\n"]]
    :content [[v/required :message "内容不能为空\n"]])
   first
   (vals)))

