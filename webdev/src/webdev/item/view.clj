(ns webdev.item.view
  (:require
   ;; This makes an HTML5 compatible page
   [hiccup.page :refer [html5]]
   ;; We use `html` to get the code that converts our code (expressions
   ;; like `:div`) to HTML tags.
   ;;
   ;; Use the function, `h`, to prevent cross-site scripting attacks by
   ;; escaping HTML characters.
   [hiccup.core :refer [html h]]))

(defn items-page
  "Create a basic HTML page to start."
  [items]
  (html5 {:lang :en}
         ;; The document header
         [:head
          ;; Page title
          [:title "Listronica"]
          ;; Page meta-data
          [:meta {:name :viewport
                  :content "width=device-width, initial-scale=1.0"}]
          ;; Link to the bootstrap CSS
          [:link {:href "/bootstrap/css/bootstrap.min.css"
                  :rel :stylesheet}]]
         ;; The document body
         [:body
          ;; A container `div`
          [:div.container]
          ;; A script tag referring to `jquery`
          [:script {:src "https://code.jquery.com/jquery-3.7.1.min.js"
                    :integrity "sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
                    ;; The following allows crossorigin requests only as
                    ;; an anonymous user.
                    :crossorigin "anonymous"}]
          ;; JavaScript to support bootstrap
          [:script {:src "/bootstrap/js/bootstrap.min.js"}]]))
