(defproject clojupyter-plugin/widgets "0.2.1-SNAPSHOT"
  :description "Interactive widgets for clojupyter."
  :url "https://github.com/nighcoder/widgets"
  :license {:name "MIT"}

  :dependencies [[org.clojars.nighcoder/clojupyter "0.4.1"]
                 [camel-snake-kebab "0.4.1"]
                 [cheshire "5.10.0"]
                 [io.simplect/compose "0.7.27"]]


  :profiles {:dev           {:plugins [[org.clojars.nighcoder/lein-metajar "0.1.2"]]}
             :metajar       {:libdir "../lib"
                             :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}

  :target-path "target/plugins")
