(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

(defn greet [req]
  {:status 200
   :body "Hello, Ring World (no not that Ringworld)!"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye, Cruel Ring World (still not that Ringworld!)"
   :headers {}})

(defn about [req]
  {:status 200
   :body "Written by mrwizard82d1. Learning about Clojure web development."
   :headers {}})

(defn yo-name [req]
  (let [name (get-in req [:route-params :name])]
    {:status 200
     :body (str "Yo! " name "!")
     :headers {}}))

(defn string->op [op-string]
  (let [converter {"+" +
                   "-" -
                   "*" *
                   ":" /}]
    (get converter op-string)))

(defn calc [req]
  (let [left (parse-long (get-in req [:route-params :left]))
        op (string->op (get-in req [:route-params :op]))
        right (parse-long (get-in req [:route-params :right]))]
    {:status 200
     :body (str (apply op [left right]))
     :headers {}}))

(defroutes app
  ;; When a user requests the root, supply a friendly greeting
  (GET "/" [] greet)
  ;; When a user requests goodbye, supply a sad so-long
  (GET "/goodbye" [] goodbye)
  ;; Describe this application
  (GET "/about" [] about)
  ;; Echo the request
  (GET "/request" [] handle-dump)
  ;; A "friendly" greeting
  (GET "/yo/:name" [] yo-name)
  ;; An inline arithmetic calculator
  (GET "/calc/:left/:op/:right" [] calc)
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
