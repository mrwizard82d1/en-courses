(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]))

(defn -main [port]
  ;; The function, `jetty/run-jetty` creates a Jetty adapter for Ring. Ring
  ;; is the code that handles the request / response cycle but expects
  ;; requests to be in the form that Ring understands (using Clojure maps
  ;; and so on).
  (jetty/run-jetty
   ;; The following function is the (Ring) handler. This handler function
   ;; accepts a (single) Ring request and translates it to a (single) Ring
   ;; response. The (Jetty) adapter translates the HTTP reqjuest into Ring
   ;; request **and** eventually translates a Ring response into an HTTP
   ;; response.
   (fn [req]
     ;; The Ring response has three parts:
     ;; - `:status` The HTTP status code
     ;; - `:body` The body of the HTTP response. The body can be any of:
     ;;    - A string
     ;;    - A file
     ;;    - An input stream (Should this be an **output** stream?)
     ;; - `:headers` The map of response headers. This value is a map of
     ;;    - Strings to strings
     ;;    - Strings to `seqs` of strings (if you have multiple values
     ;;      for the **same** headre)
     ;;
     ;; Our handler is very simple. It
     ;; - Returns a status code of 200 (everthing A-ok) to all requests
     ;; - Returns the same (text) budy to all requests
     ;; - Returns **no** headers.
     {:status 200
      :body "Hello, Ring World (no not that \"Ringwordl\")"
      :headers {}})
   ;; Our HTTP server listens for requests sent to `port`.
   {:port (Integer. port)}))
