(ns clj-chrome-devtools.commands.fed-cm
  "This domain allows interacting with the FedCM dialog."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::login-state
 #{"SignIn" "SignUp"})

(s/def
 ::dialog-type
 #{"AutoReauthn" "AccountChooser" "ConfirmIdpLogin" "Error"})

(s/def
 ::dialog-button
 #{"ErrorGotIt" "ErrorMoreDetails" "ConfirmIdpLoginContinue"})

(s/def
 ::account-url-type
 #{"TermsOfService" "PrivacyPolicy"})

(s/def
 ::account
 (s/keys
  :req-un
  [::account-id
   ::email
   ::name
   ::given-name
   ::picture-url
   ::idp-config-url
   ::idp-login-url
   ::login-state]
  :opt-un
  [::terms-of-service-url
   ::privacy-policy-url]))
(defn
 enable
 "\n\nParameters map keys:\n\n\n  Key                      | Description \n  -------------------------|------------ \n  :disable-rejection-delay | Allows callers to disable the promise rejection delay that would\nnormally happen, if this is unimportant to what's being tested.\n(step 4 of https://fedidcg.github.io/FedCM/#browser-api-rp-sign-in) (optional)"
 ([]
  (enable
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [disable-rejection-delay]}]
  (enable
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [disable-rejection-delay]}]
  (cmd/command
   connection
   "FedCm"
   "enable"
   params
   {:disable-rejection-delay "disableRejectionDelay"})))

(s/fdef
 enable
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::disable-rejection-delay]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::disable-rejection-delay])))
 :ret
 (s/keys))

(defn
 disable
 ""
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
   "FedCm"
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
 select-account
 "\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :dialog-id     | null\n  :account-index | null"
 ([]
  (select-account
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [dialog-id account-index]}]
  (select-account
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [dialog-id account-index]}]
  (cmd/command
   connection
   "FedCm"
   "selectAccount"
   params
   {:dialog-id "dialogId", :account-index "accountIndex"})))

(s/fdef
 select-account
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::dialog-id
     ::account-index]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::dialog-id
     ::account-index])))
 :ret
 (s/keys))

(defn
 click-dialog-button
 "\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :dialog-id     | null\n  :dialog-button | null"
 ([]
  (click-dialog-button
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [dialog-id dialog-button]}]
  (click-dialog-button
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [dialog-id dialog-button]}]
  (cmd/command
   connection
   "FedCm"
   "clickDialogButton"
   params
   {:dialog-id "dialogId", :dialog-button "dialogButton"})))

(s/fdef
 click-dialog-button
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::dialog-id
     ::dialog-button]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::dialog-id
     ::dialog-button])))
 :ret
 (s/keys))

(defn
 open-url
 "\n\nParameters map keys:\n\n\n  Key               | Description \n  ------------------|------------ \n  :dialog-id        | null\n  :account-index    | null\n  :account-url-type | null"
 ([]
  (open-url
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [dialog-id account-index account-url-type]}]
  (open-url
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [dialog-id account-index account-url-type]}]
  (cmd/command
   connection
   "FedCm"
   "openUrl"
   params
   {:dialog-id "dialogId",
    :account-index "accountIndex",
    :account-url-type "accountUrlType"})))

(s/fdef
 open-url
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::dialog-id
     ::account-index
     ::account-url-type]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::dialog-id
     ::account-index
     ::account-url-type])))
 :ret
 (s/keys))

(defn
 dismiss-dialog
 "\n\nParameters map keys:\n\n\n  Key               | Description \n  ------------------|------------ \n  :dialog-id        | null\n  :trigger-cooldown | null (optional)"
 ([]
  (dismiss-dialog
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [dialog-id trigger-cooldown]}]
  (dismiss-dialog
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [dialog-id trigger-cooldown]}]
  (cmd/command
   connection
   "FedCm"
   "dismissDialog"
   params
   {:dialog-id "dialogId", :trigger-cooldown "triggerCooldown"})))

(s/fdef
 dismiss-dialog
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::dialog-id]
    :opt-un
    [::trigger-cooldown]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::dialog-id]
    :opt-un
    [::trigger-cooldown])))
 :ret
 (s/keys))

(defn
 reset-cooldown
 "Resets the cooldown time, if any, to allow the next FedCM call to show\na dialog even if one was recently dismissed by the user."
 ([]
  (reset-cooldown
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (reset-cooldown
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "FedCm"
   "resetCooldown"
   params
   {})))

(s/fdef
 reset-cooldown
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
