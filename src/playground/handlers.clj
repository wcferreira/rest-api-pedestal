(ns playground.handlers
  (:require [io.pedestal.http :as http]
            [playground.db :as db])
  (:import java.util.UUID))

(defn get-uuid-str []
  (str (UUID/randomUUID)))

(defn list-all-merchants [request]
  (prn "Listing all merchants")
  (http/json-response (db/reads)))

(defn list-merchant-by-id [request]
  (prn "Listing merchant by id")
  (let [id (get-in request [:path-params :id])
        data (get (db/reads) id {})]
    (http/json-response data)))

(defn create-new-merchant [request]
  (prn "Creating new merchant")
  (let [value (:json-params request)
        key (get-uuid-str)
        data {key value}]
    (db/create key value)
    (-> data
        http/json-response
        (assoc :status 201))))

(defn update-merchant [request]
  (prn "Updating merchant")
  (let [id (get-in request [:path-params :id])
        data (:json-params request)]
    (db/updates id data)
    (http/json-response data)))

(defn delete-merchant [request]
  (prn "Deleting merchant")
  (let [id (get-in request [:path-params :id])]
    (db/delete id)
    (-> {}
        http/json-response
        (assoc :status 202))))
