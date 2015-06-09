(ns wodscraper.core

  (:require [pl.danieljanus.tagsoup :refer [parse]]
            [clojure.core.async
             :refer [>! <! >!! <!! put! take! go go-loop chan buffer close! thread
                     alts! alts!! timeout onto-chan pipeline]]
            [monger.core :as mg]
            [monger.collection :as mc])
  (:import [com.mongodb MongoOptions ServerAddress]
           [java.util.concurrent Executors]))

(def base-url "http://www.crossfit.com/mt-archive2/00")
(def html-suffix ".html")
(def start-1 2668)
;(def end-1 2658)
(def end-1 1754)
;(def end 9609)
;(def end 2678)

(defn build-url [num]
  (str base-url num html-suffix))

;; ((((((((((p 3) 2) 3) 2) 2) 2) 2) 2) 3) 3) for blogbody

(defn get-page [url]
  (try
    (parse url)
    (catch Exception e
      (prn "caught" e)
      "error")))

(defn get-body-from-crossfit [url]
  (let [page (get-page url)]
    (if (= "error" page)
      "error"
      ((((((((((page 3) 2) 3) 2) 2) 2) 2) 2) 3) 3))))

(defn get-body-for-page [num]
  (let [url (build-url num)
        ;body (get-body-from-crossfit url)
        body "test"
        ]
    body))

(defn store-pages [pages]
  (let [conn (mg/connect)
        db (mg/get-db conn "wodscraper")]
    (map #(mc/insert-and-return db "body" {:body %}) pages)))

(defn download-pages [num-a num-b]
  (let [start (min num-a num-b)
        end (max num-a num-b)
        pages (map #(get-body-for-page %) (range start end))]
    (do
      (prn (str "done with: " (count pages)))
      (store-pages pages))))

(defn get-page-batch [url start end]
  (let [pool (Executors/newFixedThreadPool 10)
        tasks (map (fn [n] (prn (str "hi" n))) (range start end))]
    (doseq [future (.invokeAll pool tasks)]
      (.get future))
    (.shutdown pool)))

;;