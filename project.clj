(defproject wodscraper "0.1.0-SNAPSHOT"
            :description "FIXME: write description"
            :url "http://example.com/FIXME"
            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.6.0"]
                           [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                           [clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]] ; tagsoup is old, clojure 1.2
                           [com.novemberain/monger "2.0.0"]
                           ])
