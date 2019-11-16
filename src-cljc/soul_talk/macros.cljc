(ns soul-talk.macros)

(defmacro  unless [condition & body]
  `(if (not ~condition)
     (do ~@body)))


(defmacro testw []
  `(do
     (def yyy# "0000")
       yyy#
  )
  )
