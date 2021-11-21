(defproject org.clojars.nighcoder/widgets "0.1.1"
  :description "Interactive widgets for clojupyter."
  :url "https://github.com/nighcoder/widgets"
  :license {:name "MIT"}

  :dependencies [[clojupyter/clojupyter "0.4.0-alpha0"]
                 [camel-snake-kebab "0.4.1"]
                 [cheshire "5.10.0"]
                 [io.simplect/compose "0.7.27"]]


  :profiles {:dev           {:plugins [[org.clojars.nighcoder/lein-metajar "0.1.3"]]}
             :metajar       {:libdir "../lib"
                             :jvm-opts ["-Dclojure.compiler.direct-linking=true"]
                             :target-path "target/plugins"}
             :provided      {:dependencies [[clojupyter/clojupyter "0.4.0-alpha0"]]}}

  :target-path "target")
