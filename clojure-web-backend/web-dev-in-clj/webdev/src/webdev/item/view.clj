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

(defn new-item
  "Create a new item"
  []
  ;; Although it is not strictly necessary to wrap this code in the `html`
  ;; macro, it does make the code more efficient to do so.
  (html
   [:form.form-horizontal
    {:method "POST" :action "/items"}
    [:div.form-group
     [:label.control-label.col-sm-2 {:for :name-input}
      "Name"]
     [:div.col-sm-10
      [:input#name-input.form-control
       {:name :name
        :placeholder "Name"}]]]
    [:div.form-group
     [:label.control-label.col-sm-2 {:for :desc-input}
      "Description"]
     [:div.col-sm-10
      [:input#desc-input.form-control
       {:name :description
        :placeholder "Description"}]]]
    [:div.form-group
     [:div-col-sm-offset-2.col-sm-10
      [:input.btn.btn-primary
       {:type :submit
        :value "New item"}]]]]))

(defn delete-item-form
  "An HTTP 'form' used to delete an item.

  This technique is used because our application is completely server-side."
  [id]
  (html
   [:form
    {:method "POST" :action (str "/items/" id)}
    [:input {:type :hidden
             :name "_method"
             :value "DELETE"}]
    [:div.btn-group
     [:input.btn.btn-danger.btn-xs
      {:type :submit
       :value "Delete"}]]]))

(defn update-item-form
  "An HTTP 'form' used to update an item.

  This technique is used because our application is completely server-side."
  [id checked]
  (html
   [:form
    {:method "POST"
     :action (str "/items/" id)}
    [:input {:type :hidden
             :name "_method"
             :value "PUT"}]
    [:input {:type :hidden
             :name "checked"
             :value (if checked "false" "true")}]
    [:div.btn-group
     [:button.btn.btn-primary.btn-xs
      {:type :submit}
      (if checked "DONE" "TODO")]]]))

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
          [:div.container
           ;; Render a heading
           [:h1 "My items"]
           ;; Render a grid row (if we have items)
           [:div.row
            (if (seq items)
              ;; Add a (striped) table
              [:table.table.table-striped
               [:thead
                ;; Add the table header as a table row
                [:tr
                 [:th.col-sm-2]
                 [:th.col-sm-2]
                 [:th "Name"]
                 [:th "Description"]]]
               [:tbody
                ;; Iterate over each item in `items`
                (for [i items]
                  [:tr
                   ;; Remember to escape the `name` and `description`
                   ;; data if they are from user input
                   ;;
                   ;; Present a "form" (only showing a delete button)
                   [:td (delete-item-form (:id i))]
                   ;; Present a "form" (only showing an update button). The
                   ;; text of the button is the current status.
                   [:td (update-item-form (:id i) (:checked i))]
                   ;; Present the name of the item
                   [:td (h (:name i))]
                   ;; And the description
                   [:td (h (:description i))]])]]
              [:div.col-sm-offset-1 "There are not items."])]
           [:div.col-sm-6
            [:h2 "Create a new item"]
            (new-item)]]
          ;; A script tag referring to `jquery`
          [:script {:src "https://code.jquery.com/jquery-3.7.1.min.js"
                    :integrity "sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
                    ;; The following allows crossorigin requests only as
                    ;; an anonymous user.
                    :crossorigin "anonymous"}]
          ;; JavaScript to support bootstrap
          [:script {:src "/bootstrap/js/bootstrap.min.js"}]]))
