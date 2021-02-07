(ns playground.core
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-param]
            [playground.db :as db]))

(def routes
  #{["/merchants"     :get    db/list-all-merchants  :route-name :get-merchants]
    ["/merchants/:id" :get    db/list-merchant-by-id :route-name :get-merchant-by-id]
    ["/merchants"     :post   db/create-new-merchant :route-name :post-merchants]
    ["/merchants/:id" :put    db/update-merchant     :route-name :put-merchant]
    ["/merchants/:id" :delete db/delete-merchant     :route-name :delete-merchant]})

(def service-map
  (-> {::http/routes routes
       ::http/port 8000
       ::http/type :jetty}
      http/default-interceptors
      (update ::http/interceptors conj (body-param/body-params))))

(defonce server (atom nil))

(defn go []
  (reset! server
          (http/start (http/create-server
                        (assoc service-map ::http/join? false))))
  (prn "Server started on localhost:8000")
  :started)

(defn halt []
  (http/stop @server))

