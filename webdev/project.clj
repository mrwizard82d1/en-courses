(defproject webdev "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring "1.15.0-RC1"]
                 [ring/ring-codec "1.3.0"]
                 [compojure "1.7.1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.postgresql/postgresql "42.7.7"]]
  :repl-options {:init-ns webdev.core}
  :main webdev.core
  :profiles {:dev {:main webdev.core/-dev-main}})
