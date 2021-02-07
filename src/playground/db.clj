(ns playground.db)


(def local-db  (atom {"94e8f98c-bb63-48bf-a5bd-58ca5c539d27" {:merchant "Extra Supermercados" :cnpj "02.309.554/0001-09"}
                      "f28fb376-ea37-4aed-9f4a-41256b70f813" {:merchant "Drogasil" :cnpj "06.657.654/0001-79"}
                      "10487a24-527d-425e-9578-9f67c2b88be0" {:merchant "Kopenhagen" :cnpj "06.657.654/0001-79"}}))

(defn reads []
  @local-db)

(defn create [key value]
  (swap! local-db assoc key value))

(defn updates [id data]
  (swap! local-db assoc id data))

(defn delete [id]
  (swap! local-db dissoc id))

