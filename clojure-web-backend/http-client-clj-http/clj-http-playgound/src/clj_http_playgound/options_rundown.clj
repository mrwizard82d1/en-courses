(ns clj-http-playgound.options-rundown
  (:require [clj-http.client :as http]))

;; Working through options one may wish to set / change
;;

;; Time outs
(http/get "http/lispcast.com/"
          {;; Wait for 1 sec (1000ms
           :conn-timeout 1000
           :socket-timeout 1000})
