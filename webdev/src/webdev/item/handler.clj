(ns webdev.item.handler
  (:require [webdev.item.model :refer [create-item
                                       read-items
                                       update-item
                                       delete-item]]
            [webdev.item.view :refer [items-page]]))

(defn handle-index-items
  "A handler to list existing items and a form to add new items."
  [req]
  (let [db (:webdev/db req)
        items (read-items db)]
    {:status 200
     :headers {}
     :body (items-page items)}))

(defn handle-create-item
  "Respond to a POST request to create a new item."
  [req]
  (let [name (get-in req [:params "name"])
        description (get-in req [:params "description"])
        db (:webdev/db req)
        item-id (create-item db name description)]:bp
    (println (str "Created item with id, '" item-id "'."))
    {:status 302
     :headers {"Location" "/items"}
     :body ""}))

(defn handle-delete-item
  "Respond to a request to delete a specific item."
  [req]
  (let [db (:webdev/db req)
        item-id (->> req
                     :route-params
                     :item-id
                     java.util.UUID/fromString)
        exists? (delete-item db item-id)]
    (if exists?
      {:status 302
       :headers {"Location" "/items"}
       :body ""}
      {:status 404
       :headers {}
       :body "List not found"})))

(defn handle-update-item
  "Respond to a request to update a specific item."
  [req]
  (let [db (:webdev/db req)
        item-id (->> req
                     :route-params
                     :item-id
                     java.util.UUID/fromString)
        checked (get-in req [:params "checked"])
        ;; Use string "true" because it is from the request and has not
        ;; been converted
        exists? (update-item db item-id (= "true" checked))]
    (if exists?
     {:status 302
      :headers {"Location" "/items"}
      :body ""}
     {:status 404
      :headers {}
      :body "Item not found"})))
