(ns soul-talk.macros)

  
;; (defmacro infix
;;   [x]
;;   `(do
;;      (println "===========")))




;; (defmacro page-register
;;   "用于进行页面注册的宏"
;;   [url-path  page-key  page-comp events defmethod_ defroute_]
;;   `(do
;;      (~defmethod_ pages ~page-key [_ _] [ ~page-comp ])
;;      (~defroute_ ~url-path [] (run-events ~events)  )

;;      )
;;   )


;; (defmacro i-am-macro1 []
;;   '(println "i-am-macro1"))
;; (defmacro i-am-macro2 []
;;   '(println "i-am-macro2"))

;; (defmacro add
;;   [a b]
;;   `(+ ~a ~b))
