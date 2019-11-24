(ns soul-talk.config)

;;;;  注册全局数据库交流方法

;; (def source-pull  :server/dataset-find-by)
;; (def source-new  :server/new)
;; (def source-del  :server/delete)
;; (def source-update  :server/update)


;; 采用测试假数据的方法
(def source-pull  :fake/sync-pull)
(def source-new  :fake/new)
(def source-del  :fake/delete)
(def source-update  :fake/update)


;; 外键链接方案

(def  one2many  :item/one2many)
(def  one2one  :item/one2one)
