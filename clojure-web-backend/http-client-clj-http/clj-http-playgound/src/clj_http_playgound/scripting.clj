(ns clj-http-playgound.scripting
  (:require [clj-http.client :as http]
            [clj-http.cookies :as cookies]
            [cemerick.uri :refer [uri uri-encode]])
  (:import [org.jsoup Jsoup]))

;; Use the `clj-http` library to "scrape" / script web sites
;;

;; Some experiments
(def secret-key "MKBA5NUV2USYQA3EA4CAD5XR3")
(def base-url "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/")
(def location "29.74284,-95.63790")
(def location-forecast-uri (-> (uri base-url location)
                               (assoc :query {:key secret-key})
                               str))
location-forecast-uri

(http/get location-forecast-uri)

;; A browser manages cookies on behalf of a user (typically without any
;; user action); however, when scraping / scripting, our client code
;; must manage cookies. Specifically, we must create and manage a
;; "cookie store". The `clj-http` package provides services to help
;; in this effort.

(def login-url "https://www.visualcrossing.com/account/#")
(let [cookie-store (cookies/cookie-store)
      login-page (http/get login-url
                           {:cookie-store cookie-store})
      login-doc (-> login-page
                    :body
                    Jsoup/parse)
      auth-slot (-> login-doc
                    (.select ".auth-slot"))
      #_sign-in-btn #_(-> auth-slot
                      (.select "a.#"))
      #_login-form #_(-> login-doc
                     (.select "input#exampleInputEmail1.form-control")
                     first)]
  auth-slot)
