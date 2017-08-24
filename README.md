# adyen-signature
A clojure library designed to sign adyen requests

## Latest version

[![Clojars Project](https://img.shields.io/clojars/v/adyen-signature.svg)](https://clojars.org/adyen-signature)

## Example

```clojure
(require '[adyen-signature.core :refer :all])
 
(def my-secret "517083CAD0F9E6286ABA4B0FBE911840FE45953E2813525C60E3BDCB858311B7")
 
(def payment-data
  {:payment-amount 500
   :merchant-account "My-account"
   :merchant-reference "My-reference"
   :currency-code "EUR"
   :skin-code "sH9qpMyS"
   :session-validity "2017-12-25T10%3A31%3A06Z"
   :brand-code "MC"})
   
(let [signature (sign "my-adyen-secret" payment-data)]
  (->> (assoc payment-data :merchant-sig signature)
      (map (fn [[k v]] [(to-camel-case k) v]))))

```
```clojure
=> {"paymentAmount" 500,
    "merchantAccount" "My-account",
    "merchantReference" "My-reference",
    "currencyCode" "EUR",
    "skinCode" "sH9qpMyS",
    "sessionValidity" "2017-12-25T10%3A31%3A06Z",
    "brandCode" "MC",
    "merchantSig" "mUV+LEp+M5FQHSGzHX0i7Cj/ziMwiWoflDFgAIsBrTE="}
```

## License

Distributed under the Eclipse Public License, the same as Clojure.
