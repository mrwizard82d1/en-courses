(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]))

(defn greet [req]
  (cond
    (= (:uri req) "/")
    {:status 200
     :body "Hello, Ring World (no not that \"Ringworld\")!"
     :headers {}}
    (= (:uri req) "/goodbye")
    {:status 200
     :body "Goobye, Cruel World!"
     :headers {}}
    :else
    {:status 404
     :body "Page not found"
     :headers{}}))

(defn -main [port]
  ;; The function, `jetty/run-jetty` creates a Jetty adapter for Ring. Ring
  ;; is the code that handles the request / response cycle but expects
  ;; requests to be in the form that Ring understands (using Clojure maps
  ;; and so on).
  (jetty/run-jetty greet {:port (Integer. port)}))

(defn -dev-main
  "This function is **only** called in development.

  The major difference between `-main` and `-dev-main`` is that `-dev-main`
  wraps our key function, `greet`, in middleware that supports reloading
  thereby reducing the number of times one must restart the server"
  [port]
  (jetty/run-jetty (wrap-reload #'greet) {:port (Integer. port)}))
