(defproject ambient-weather-clj "0.1.0"
  :description "Ambient Weather API"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [cheshire "5.10.0"]
                 [clj-http "3.10.1"]
                 [org.clojars.rockchalkjay/clj-socketio-client "0.1.1"]
                 [org.json/json "20200518"]]
  :repositories [["clojars" "https://clojars.org/repo"]]
  :repl-options {:init-ns ambient-weather-clj.core})