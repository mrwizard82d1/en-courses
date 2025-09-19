(ns clj-http-playgound.send-recv-data
  (:require [clj-http.client :as http]))

;; JSON is **built-into** `clj-http`.

;; The demos use the Dark Sky API which is no longer available.
;;
;; I will try using the free, open-meteo api, from
;; https://api.open-meteo.com/v1
;;
;; Here is a more-detailed request from the web page
;; ```
;; https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current=temperature_2m,wind_speed_10m&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m
;; ```
;; We will see.

;; I encounter an (expected) error using the URL from the video.
(def response
  (try (http/get "https://api.forecast.io/forecast/406ec3fa3b29f291894f42b3fa48d29f/29.74284,-95.63790")
       (catch Exception ex
         (println (.getMessage ex)))))

;; This request **failed**. After some investigation, the service that l
;; chose to use, `https://api.open-meteo.com`, provides its data in JSON
;; format "out-of-the-box". As a result,
(def location-text "latitude=29.74284&longitude=-95.63790")
(def requested-data "current=temperature_2m,wind_speed_10m&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m")
(def weather-uri (str "https://api.open-meteo.com/v1/forecast"
                 "?"
                 location-text
                 "&"
                 "current=temperature_2m,wind_speed_10m&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m"))

(def response (http/get weather-uri))

(:status response)
(:headers response)
;; Notice that the body of the response is actually in JSON. I believe this
;; behavior results from the actual API proviider.
(:body response)

;; We can issue tha same request requesting that we want a response
;; body as JSON format. This option seems to actually return a
;; Clojure map with keyword keys converted from the JSON response
;; returned previously.
(def response (http/get weather-uri {:as :json}))

(:status response)
(:headers response)
(:body response)

(println (str (:body response)))

;; Request the information translated into clojure strangely fails
(try
  (def response (http/get weather-uri {:as :clojure}))
  (catch Exception ex
    (.getMessage ex)))

;; I have much to learn about the `clj-http` library. Sigh...
