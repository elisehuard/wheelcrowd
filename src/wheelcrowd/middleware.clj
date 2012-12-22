(ns wheelcrowd.middleware
  (:use [clojure.tools.logging] 
        [slingshot.slingshot :only [try+]]))

(defn wrap-request-logging
  [handler]
  (fn [{:keys [request-method uri query-string headers] :as req}]
    (let [start  (System/currentTimeMillis)
          resp   (handler req)
          finish (System/currentTimeMillis)
          processing-time  (- finish start)
          user-agent (get headers "user-agent")]
      (info (format "%s %s?%s (%dms) \"%s\"" request-method uri (or query-string "") processing-time user-agent))
      resp)))

(defn wrap-exception-logging
  [handler]
  (fn [req]
    (try
      (handler req)
      (catch Exception e
        (error e)
        (throw e)))))
