(ns clj-chrome-devtools.commands.device-access
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::request-id
 string?)

(s/def
 ::device-id
 string?)

(s/def
 ::prompt-device
 (s/keys
  :req-un
  [::id
   ::name]))
(defn
 enable
 "Enable events in this domain."
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
   "DeviceAccess"
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

(defn
 disable
 "Disable events in this domain."
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
   "DeviceAccess"
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
 select-prompt
 "Select a device in response to a DeviceAccess.deviceRequestPrompted event.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :id        | null\n  :device-id | null"
 ([]
  (select-prompt
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [id device-id]}]
  (select-prompt
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [id device-id]}]
  (cmd/command
   connection
   "DeviceAccess"
   "selectPrompt"
   params
   {:id "id", :device-id "deviceId"})))

(s/fdef
 select-prompt
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::id
     ::device-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::id
     ::device-id])))
 :ret
 (s/keys))

(defn
 cancel-prompt
 "Cancel a prompt in response to a DeviceAccess.deviceRequestPrompted event.\n\nParameters map keys:\n\n\n  Key | Description \n  ----|------------ \n  :id | null"
 ([]
  (cancel-prompt
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [id]}]
  (cancel-prompt
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [id]}]
  (cmd/command
   connection
   "DeviceAccess"
   "cancelPrompt"
   params
   {:id "id"})))

(s/fdef
 cancel-prompt
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::id])))
 :ret
 (s/keys))
