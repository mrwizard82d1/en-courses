(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]))

(defn greet [req]
  {:status 200
    :body "Hello, Ring World (no not that \"Ringworld\")!"
    :headers {}})

(defroutes app
  ;; When a user requests the root, supply a friendly greeting
  (GET "/" [] greet)
  ;; No matches! Respond with a 404 and a "Page not found" message
  (not-found "Page not found"))

(defn -main [port]
  ;; The function, `jetty/run-jetty` creates a Jetty adapter for Ring. Ring
  ;; is the code that handles the request / response cycle but expects
  ;; requests to be in the form that Ring understands (using Clojure maps
  ;; and so on).
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main
  "This function is **only** called in development.

  The major difference between `-main` and `-dev-main`` is that `-dev-main`
  wraps our key function, `greet`, in middleware that supports reloading
  thereby reducing the number of times one must restart the server"
  [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
