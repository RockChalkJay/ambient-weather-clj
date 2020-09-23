(ns ambient-weather-clj.core
  (:require [clj-http.client :as http]
            [cheshire.core :refer [parse-string generate-string]]
            [clj-socketio-client.core :as sckio])
  (:import [org.json JSONObject]
           (io.socket.engineio.client.transports WebSocket)))

;;; Ambient Weather REST and Realtime API client
;;
;; Ambient Weather's documentation
;;
;; API documentation
;; https://ambientweather.docs.apiary.io
;;
;; Available device data
;; https://github.com/ambient-weather/api-docs/wiki/Device-Data-Specs

(def amb-weather-url "https://api.ambientweather.net")

(defn user-devices
  "Returns all the user's devices
   along with their most recent data"
  [app-key api-key]
  (let [url (str amb-weather-url
                 "/v1/devices?"
                 "applicationKey=" app-key
                 "&apiKey=" api-key)]
    (-> (http/get url)
        :body
        (parse-string true))))

(defn device-data
  "Returns historical device data.

  date - Data is stored in 5 or 30 min increments.
  This is the date of the most recent result to be
  returned by the query. The rest of the results
  are in descending order. date is milliseconds since epoch
  or a string formatted as https://momentjs.com/docs/#/parsing/string/
  in UTC.
  If nil/empty, the query will start from the most recent
  set of data.

  limit - max results returned. Defaults to 288"
  [{:keys [app-key api-key
           mac-addr date limit :as query]}]
  (let [url (str amb-weather-url
                 "/v1/devices/" mac-addr "?"
                 "applicationKey=" app-key
                 "&apiKey=" api-key
                 "&endDate=" date
                 "&limit=" limit)]
    (-> (http/get url)
        :body
        (parse-string true))))

;; Realtime

;; https://ambientweather.docs.apiary.io/#reference/ambient-realtime-api

(def default-opts
  {:transports (into-array [(WebSocket/NAME)])})

(defn socket
  "Creates and connects an IO socket. event-fns is a map of event name strs
  and fns to execute on receiving the event. The fn should take a variable
  list of args [& args]"
  [app-key event-fns]
  (let [url (str amb-weather-url "?api=1&applicationKey=" app-key)]
    (sckio/make-socket url default-opts event-fns)))

(defn disconnect! [socket] (sckio/disconnect! socket))

;; Websocket commands

(defn subscribe
  "Subscribe to data events for all users' devices. api-keys
  is an array of api-keys, each represent one user."
  [api-keys socket]
  (sckio/emit! socket :subscribe (-> {:apiKeys api-keys}
                                     (generate-string)
                                     (JSONObject.))))

(defn unsubscribe
  "Unsubscribe to data events for a user. api-keys
  is an array of api-keys, each represent one user."
  [api-keys socket]
  (sckio/emit! socket :unsubscribe (-> {:apiKeys api-keys}
                                       (generate-string)
                                       (JSONObject.))))

;; Debugging event fns

(defn print-event-fn [& args]
  (println (apply str args)))

(def print-events-map
  {:subscribed print-event-fn
   :data       print-event-fn})

