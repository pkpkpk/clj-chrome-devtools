(ns clj-chrome-devtools.commands.autofill
  "Defines commands and events for Autofill."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::credit-card
 (s/keys
  :req-un
  [::number
   ::name
   ::expiry-month
   ::expiry-year
   ::cvc]))

(s/def
 ::address-field
 (s/keys
  :req-un
  [::name
   ::value]))

(s/def
 ::address-fields
 (s/keys
  :req-un
  [::fields]))

(s/def
 ::address
 (s/keys
  :req-un
  [::fields]))

(s/def
 ::address-ui
 (s/keys
  :req-un
  [::address-fields]))

(s/def
 ::filling-strategy
 #{"autocompleteAttribute" "autofillInferred"})

(s/def
 ::filled-field
 (s/keys
  :req-un
  [::html-type
   ::id
   ::name
   ::value
   ::autofill-type
   ::filling-strategy
   ::frame-id
   ::field-id]))
(defn
 trigger
 "Trigger autofill on a form identified by the fieldId.\nIf the field and related form cannot be autofilled, returns an error.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :field-id | Identifies a field that serves as an anchor for autofill.\n  :frame-id | Identifies the frame that field belongs to. (optional)\n  :card     | Credit card information to fill out the form. Credit card data is not saved.  Mutually exclusive with `address`. (optional)\n  :address  | Address to fill out the form. Address data is not saved. Mutually exclusive with `card`. (optional)"
 ([]
  (trigger
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [field-id frame-id card address]}]
  (trigger
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [field-id frame-id card address]}]
  (cmd/command
   connection
   "Autofill"
   "trigger"
   params
   {:field-id "fieldId",
    :frame-id "frameId",
    :card "card",
    :address "address"})))

(s/fdef
 trigger
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::field-id]
    :opt-un
    [::frame-id
     ::card
     ::address]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::field-id]
    :opt-un
    [::frame-id
     ::card
     ::address])))
 :ret
 (s/keys))

(defn
 set-addresses
 "Set addresses so that developers can verify their forms implementation.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :addresses | null"
 ([]
  (set-addresses
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [addresses]}]
  (set-addresses
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [addresses]}]
  (cmd/command
   connection
   "Autofill"
   "setAddresses"
   params
   {:addresses "addresses"})))

(s/fdef
 set-addresses
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::addresses]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::addresses])))
 :ret
 (s/keys))

(defn
 disable
 "Disables autofill domain notifications."
 ([]
  (disable
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (disable
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Autofill"
   "disable"
   params
   {})))

(s/fdef
 disable
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat :params (s/keys))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys)))
 :ret
 (s/keys))

(defn
 enable
 "Enables autofill domain notifications."
 ([]
  (enable
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (enable
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Autofill"
   "enable"
   params
   {})))

(s/fdef
 enable
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat :params (s/keys))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys)))
 :ret
 (s/keys))
