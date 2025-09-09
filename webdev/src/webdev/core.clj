(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]))

(defn -main [port]
  (jetty/run-jetty
   (fn [req]
     {:status 200
      :body "Hello, Ring World (no not that \"Ringwordl\")"
      :headers {}})
   {:port (Integer. port)}))
