(ns playground.db
  (:require [io.pedestal.http :as http])
  (:import java.util.UUID))

(def local-db  (atom {"94e8f98c-bb63-48bf-a5bd-58ca5c539d27" {:merchant "Extra Supermercados" :cnpj "02.309.554/0001-09"}
                      "f28fb376-ea37-4aed-9f4a-41256b70f813" {:merchant "Drogasil" :cnpj "06.657.654/0001-79"}
                      "10487a24-527d-425e-9578-9f67c2b88be0" {:merchant "Kopenhagen" :cnpj "06.657.654/0001-79"}}))

(defn get-uuid-str []
  (str (UUID/randomUUID)))

(defn list-all-merchants [request]
  (prn "Listing all merchants")
  (http/json-response @local-db))

(defn list-merchant-by-id [request]
  (prn "Listing merchant by id")
  (let [id (get-in request [:path-params :id])
        data (get @local-db id {})]
    (http/json-response data)))

(defn create-new-merchant [request]
  (prn "Creating new merchant")
  (let [value (:json-params request)
        key (get-uuid-str)
        data {key value}]
    (swap! local-db assoc key value)
    (-> data
        http/json-response
        (assoc :status 201))))

(defn update-merchant [request]
  (prn "Updating merchant")
  (let [id (get-in request [:path-params :id])
        data (:json-params request)]
    (swap! local-db assoc id data)
    (-> {}
        http/json-response
        (assoc :status 204))))

(defn delete-merchant [request]
  (prn "Deleting merchant")
  (let [id (get-in request [:path-params :id])]
    (swap! local-db dissoc id)
    (-> {}
        http/json-response
        (assoc :status 202))))
