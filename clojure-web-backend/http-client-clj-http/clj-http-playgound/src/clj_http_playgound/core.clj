(ns clj-http-playgound.core
  (:require [clj-http.client :as http]))

;; Making a request (and capturing the response)
(def response (http/get "http://lispcast.com"))

;; Query the status (200 expected and received)
(:status response)

;; Query the headers in the responseonse
(:headers response)

;; Although the map returned by the previous expression is a map of
;; string to string, the `clj-http.client` library provides a very
;; nice "piece of syntatic sugar" that allows us to query the values
;; of this map using keywords. For example,
(:server (:headers response))

;; Let's look at the body of the `response`
;;
;; The body is an HTML document which means its pretty difficult to
;; "grok" just by reading it even if formatted nicely.
(:body response)

;; The type of the `body` is `java.lang.String`.
(type (:body response))

;; One can actually **debug** the HTTP request by suppling the option,
;; `{:debug true}`.
;;
;; This technique is **very useful** when one wants to
(http/get "http://lispcast.com"
          {:debug true})

;; Although the site, "http://lispcast.com", does not require (or respond
;; to) any query parameters, one can supply them anyway.
;;
;; For example, this request supplies a query parameter of "name" with the
;; value of "Eric".
(http/get "http://lispcast.com"
          {:debug true
           :query-params {:name "Larry"}})

;; The `clj-http` package transforms the Clojure request, a map, into an
;; HTTP request using middleware - similar to Ring middleware. The
;; `clj-http` package was inspired by Ring and used a similar approach.
;; In fact, one code use `clj-http` to build a proxy that uses Ring.

;; In addition to HTTP GET requests, `clj-http` supports HTTP POST
;; requests.
(http/post "http://lispcast.com"
           {:form-params {:name "Larry"}})

;; Although Eric received a 404 error when he executed this HTTP POST
;; request, I see an HTTP response code of 308 (permanent redirect).
;; I suspect this difference prevents denial of service attacks.
