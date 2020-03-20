(ns soul-talk.modules.learning.database.common)

;; 2020年03月14日

(def logic1
  [{:name "直言命题"
    :note/sub ["主项" "谓项" "联项" "量项"]
    :note/category [["全程肯定命题" "所有S都是P"     "SAP"  "A"]
                    ["全程否定命题" "所有S都不是P"   "SEP" "E"]
                    ["特称肯定命题" "有的S是P"       "SIP" "I"]
                    ["特称否定命题" "有的S不是P"     "SOP" "O"]
                    ["单程肯定命题" "a(或者某个S)是p"    "a"]
                    ["单称否定命题" "a(或者某个S)不是P"  "e"]
                    ;;
                    ]
    ;;
    }
   {:name "直言命题的对当关系"
    :note "主语和宾语之间的对应关系的研究, 关系间对应关系的说明, 主语和宾语的同时对应时"
    :note/ref.picture " 直言命题对当关系图"
    :note/sub.ref []}

   ;;
   ])

(def logic
  [{:db/id "假言命题"
    :name "假言命题"}
   {:name "直言命题"}

   {:name ""}])

;; 2020年03月15日
(def logic3
  [{:name  "全程肯定命题"
    :logic/say  "s->p"
    :logic/reverse "^p -> ^s"
    :logic/replace "[限直言命题]有p -> s"
    
    }
   {:name "全程否定命题"
    :logic/say "s-> ^p"
    :logic/reverse " p-> ^s"
    :logic/replace "[限直言命题]  有 ^p -> s"
    }
   {:name "特称肯定命题"
    :logic/say "有s-> p"
    :logic/reverse false
    :logic/replace "有p->s"
    }

   {:name "特称否定命题"
    :logic/say "有s -> ^p"
    :logic/reverse false
    :logic/replace "有 ^p -> s"
    }

   {:name "三段论"
    :note "直言命题之间的推理,  三段论有共同的词项  中项- 就是结论里面没有的东西就是中项,  三顿论有且只有三个词项, 每个词出现且出现两次
           2. 特称命题放句守首, 结论做拆分  钟祥做桥梁  "
    :logic/sample  ["所有M都是P, 有的S都是M, 因此,有的S是P"]

    }


   ]

  )
