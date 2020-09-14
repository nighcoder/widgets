(defproject clojupyter-plugin/widgets "0.1.0"
  :description "Clojupyter plugin"
  :url "http://example.com/FIXME"
  :license {:name "MIT"}

  :dependencies [[clojupyter "0.4.0"]]

  :profiles {:dev           {:plugins [[lein-metajar "0.1.2"]]}
             :metajar       {:libdir "../lib"}}

  :target-path "target/plugins")
