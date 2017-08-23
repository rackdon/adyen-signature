(ns adyen-signature.core
  (:require [clojure.data.codec.base64 :as b64]
            [clojure.string :as string])
  (:import [javax.crypto.spec SecretKeySpec]
           [javax.crypto Mac]
           [com.google.common.io BaseEncoding]))

(defn to-camel-case [s]
  (as-> (name s) $
        (string/lower-case $)
        (string/split $ #"-")
        (map-indexed (fn [idx itm] (if (= idx 0) itm (string/capitalize itm))) $)
        (string/join $)))

(defn replace-values [s]
  (if (nil? s)
    ""
    (string/replace s #"\\|:" {"\\" "\\\\" ":" "\\:"})))

(defn to-byte-array [s]
  "Convert an string to byte array"
  (into-array Byte/TYPE (.getBytes s "UTF-8")))

(defn decode-hex [s]
  "Convert hex string into Byte Array"
  (.decode (BaseEncoding/base16) s))

(defn encode-b64-string [ba]
  "Convert a byte array to a base64 String"
  (String. (b64/encode ba) "UTF-8"))

(defn sign [secret data]
  (let [sha256-mac (Mac/getInstance "HmacSHA256")
        signing-key (SecretKeySpec. (decode-hex secret) "HmacSHA256")
        byte-data (->> (map (fn [[k v]] [(to-camel-case k) (replace-values v)]) (dissoc data :merchant-sig))
                       sort
                       (into {})
                       ((fn [coll] (concat (keys coll) (vals coll))))
                       (string/join ":")
                       to-byte-array)]

    (.init sha256-mac signing-key)
    (encode-b64-string (.doFinal sha256-mac byte-data))))