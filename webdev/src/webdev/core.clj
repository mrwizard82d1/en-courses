(ns webdev.core
  (:require [webdev.item.model :as items])
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE ]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

(def db "jdbc:postgresql://localhost/webdev")

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
        op (get-in req [:route-params :op])
        right (parse-long (get-in req [:route-params :right]))
        f (string->op op)]
    (if f
      {:status 200
       :body (str (f left right))
       :headers {}}
      {:status 404
       :body (str "Unrecognized operator, `" op "`")
       :headers {}})))

(defroutes routes
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

(defn wrap-server-name [handler]
  (fn [req]
    (let [response (handler req)]
      (clojure.pprint/pprint (get response :headers))
      (assoc-in response [:headers :server] "bullwinkle")
      (clojure.pprint/pprint (get response :headers)))))

;; We must define our second piece of middleware
(defn wrap-db [handler]
  (fn [req]
    ;; All this wrapper does is associate the database (`db`) with the
    ;; request. (I find this technique an interesting way of passing
    ;; "arguments" from "main" to child functions.)
    (handler (assoc req :webdev/db db))))

;; Add a function, `app`, to contain our middleware.
;; The symbol, `app`, refers to routse directly becouse we have **no**
;; middleware.
(def app
  ;; Middleware to add the name of our server to our response
  (wrap-server-name
   ;; Middleware to add the database to our request map.
   (wrap-db
    ;; Middleware to add the query parameters to our request map.
    (wrap-params
     routes))))

(defn -main [port]
  ;; The function, `jetty/run-jetty` creates a Jetty adapter for Ring. Ring
  ;; is the code that handles the request / response cycle but expects
  ;; requests to be in the form that Ring understands (using Clojure maps
  ;; and so on).
  (items/create-table db)
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main
  "This function is **only** called in development.

  The major difference between `-main` and `-dev-main`` is that `-dev-main`
  wraps our key function, `greet`, in middleware that supports reloading
  thereby reducing the number of times one must restart the server"
  [port]
  (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
