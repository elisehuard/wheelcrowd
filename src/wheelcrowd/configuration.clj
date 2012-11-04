(ns wheelcrowd.configuration)

(def env 
    (let [env (get (System/getenv) "APP_ENV")]
          (or (keyword env) :development)))

(def production?
    (= :production env))

(def development?
    (= :development env))
