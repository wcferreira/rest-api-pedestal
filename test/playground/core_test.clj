(ns playground.core-test
  (:require [clojure.test :refer [deftest is testing are run-tests]]
            [clojure.test.check.generators :as gen]
            [io.pedestal.http :as http]
            [io.pedestal.test :as http-test]
            [matcher-combinators.test]
            [cheshire.core :as json]
            [playground.core :as core]
            [playground.db :as db]))

(defn make-request! [verb path & args]
  (let [service-fn (::http/service-fn (core/create-server))
        response (apply http-test/response-for service-fn verb path args)]
    (update response :body json/decode true)))

(deftest merchants-listing-test
  (testing "Listing merchants"
    (reset! db/local-db {})
    (is (= {}                                               ;; expected
           (:body (make-request! :get "/merchants"))))))

(deftest merchants-listing-using-match-combinators-test
  (testing "Listing merchants using match combinators"
    (reset! db/local-db {})
    (is (match? {:body {} :status 200}
                (make-request! :get "/merchants")))))

#_(deftest merchant-create-test
  (reset! db/local-db {})
  (is (match? {:body {:merchant "Galeria dos Pães" :cnpj "07.354.853/0001-22"} :status 201}
              (make-request! :post "/merchants"
                             :headers {"Content-Type" "application/json"}
                             :body (json/encode {:merchant "Galeria dos Pães" :cnpj "07.354.853/0001-22"})))))

(deftest merchant-create-test
  (reset! db/local-db {})
  (let [new-merchant {:merchant "Galeria dos Pães" :cnpj "07.354.853/0001-22"}
        response (make-request! :post "/merchants"
                            :headers {"Content-Type" "application/json"}
                            :body (json/encode {:merchant "Galeria dos Pães" :cnpj "07.354.853/0001-22"}))
        body (first (vals (:body response)))
        new-body-without-id (assoc response :body body)]
    (is (match? {:body new-merchant :status 201} new-body-without-id))))


(deftest merchant-update-test
  (reset! db/local-db {})
  (let [merchant {:merchant "Americanas" :cnpj "05.334.987/0001-35"}
        body (:body (make-request! :post "/merchants"
                                   :headers {"Content-Type" "application/json"}
                                   :body (json/encode merchant)))
        id (first (keys body))
        response (make-request!  :put (str "/merchants/" id)
                                 :headers {"Content-Type" "application/json"}
                                 :body (json/encode {:merchant "Restaurante Dona Flor" :cnpj "05.334.987/0001-35"}))]
    (is (match? {:body {:merchant "Restaurante Dona Flor" :cnpj "05.334.987/0001-35"} :status 200}
                response))))


(comment
  (json/decode "{\"name\": \"Obama\"}" true)

  (update(http-test/response-for
           (::http/service-fn (core/create-server))
           :get
           "/merchants")
         :body json/decode true)
  (make-request! :get "/merchants")

  (let [data {:merchant "Fooo" :cnpj "07.354.853/0001-22"}]
    (make-request! :post "/merchants"
                   :header {"Content-Type" "application/json"}
                   :body (json/encode data)))
  (gen/generate (gen/map gen/keyword gen/nat) 1)

  (def response {:status 201,
                 :body {:44e4d6c5-8a58-4669-b588-1deb94a48863
                        {:merchant "Galeria dos Pães", :cnpj "07.354.853/0001-22"}}})
  (first (vals (:body response)))
  (assoc response :body (first (vals (:body response))))
  )