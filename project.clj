(defproject clojupyter-plugin/widgets "0.1.0"
  :description "Clojupyter plugin"
  :url "http://example.com/FIXME"
  :license {:name "MIT"}

  :dependencies [[clojupyter "0.4.0"]
                 [camel-snake-kebab "0.4.1"]
                 [cheshire "5.10.0"]
                 [io.simplect/compose "0.7.27"]]


  :profiles {:dev           {:plugins [[lein-metajar "0.1.2"]]}
             :metajar       {:libdir "../lib"
                             :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}

  :target-path "target/plugins")
