(ns soul-talk.util.page-path)

(def page-prefix [:views :pages])
(def value-prefix [:datas :values])
(def active-page  :active-page)

;; 页面所用的存储路径
(defn page->path [page-key]
  (conj page-prefix page-key))

(defn page->state [page-key state-key]
  (conj (page->path  page-key) state-key))
