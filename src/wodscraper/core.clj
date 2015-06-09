(ns wodscraper.core
  (:require [pl.danieljanus.tagsoup :refer [parse]]
            [clojure.core.async
             :refer [>! <! >!! <!! put! take! go go-loop chan buffer close! thread
                     alts! alts!! timeout onto-chan pipeline]]
            [monger.core :as mg]
            [monger.collection :as mc])
  (:import [com.mongodb MongoOptions ServerAddress]))

(def base-url "http://www.crossfit.com/mt-archive2/00")
(def html-suffix ".html")
(def start 2668)
(def end 9609)

(defn build-url [num]
  (str base-url num html-suffix))

;; ((((((((((p 3) 2) 3) 2) 2) 2) 2) 2) 3) 3) for blogbody

(defn get-body-from-crossfit [url]
  (let [page (parse url)
        blogbody ((((((((((page 3) 2) 3) 2) 2) 2) 2) 2) 3) 3)]
    blogbody))

(defn get-body-for-page [num]
  (let [url (build-url num)
        ;body (get-body-from-crossfit url)
        body "test"
        ]
    body))

(defn download-pages []
  (let [pages (map #(get-body-for-page %) (range start end))]
    (prn (str "done with: " (count pages)))
    nil))

;; localhost, default port
(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")]
  ;; with a generated document id, returns the complete
  ;; inserted document
  (mc/insert-and-return db "documents" {:name "John" :age 30}))

;;