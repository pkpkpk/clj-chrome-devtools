(ns clj-chrome-devtools.commands.smart-card-emulation
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::result-code
 #{"success" "no-memory" "unknown-reader" "sharing-violation"
   "reset-card" "comm-error" "invalid-parameter" "no-smartcard"
   "reader-unavailable" "no-readers-available" "no-service"
   "invalid-handle" "server-too-busy" "internal-error" "unpowered-card"
   "shutdown" "cancelled" "invalid-value" "unexpected" "timeout"
   "service-stopped" "unsupported-card" "not-transacted" "unknown"
   "not-ready" "unknown-card" "unsupported-feature" "unresponsive-card"
   "system-cancelled" "removed-card" "proto-mismatch"
   "insufficient-buffer"})

(s/def
 ::share-mode
 #{"shared" "exclusive" "direct"})

(s/def
 ::disposition
 #{"reset-card" "leave-card" "eject-card" "unpower-card"})

(s/def
 ::connection-state
 #{"specific" "powered" "negotiable" "swallowed" "present" "absent"})

(s/def
 ::reader-state-flags
 (s/keys
  :opt-un
  [::unaware
   ::ignore
   ::changed
   ::unknown
   ::unavailable
   ::empty
   ::present
   ::exclusive
   ::inuse
   ::mute
   ::unpowered]))

(s/def
 ::protocol-set
 (s/keys
  :opt-un
  [::t0
   ::t1
   ::raw]))

(s/def
 ::protocol
 #{"t1" "raw" "t0"})

(s/def
 ::reader-state-in
 (s/keys
  :req-un
  [::reader
   ::current-state
   ::current-insertion-count]))

(s/def
 ::reader-state-out
 (s/keys
  :req-un
  [::reader
   ::event-state
   ::event-count
   ::atr]))
(defn
 enable
 "Enables the |SmartCardEmulation| domain."
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
   "SmartCardEmulation"
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
 "Disables the |SmartCardEmulation| domain."
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
   "SmartCardEmulation"
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
 report-establish-context-result
 "Reports the successful result of a |SCardEstablishContext| call.\n\nThis maps to:\nPC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#gaa1b8970169fd4883a6dc4a8f43f19b67\nMicrosoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardestablishcontext\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | null\n  :context-id | null"
 ([]
  (report-establish-context-result
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id context-id]}]
  (report-establish-context-result
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id context-id]}]
  (cmd/command
   connection
   "SmartCardEmulation"
   "reportEstablishContextResult"
   params
   {:request-id "requestId", :context-id "contextId"})))

(s/fdef
 report-establish-context-result
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::request-id
     ::context-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id
     ::context-id])))
 :ret
 (s/keys))

(defn
 report-release-context-result
 "Reports the successful result of a |SCardReleaseContext| call.\n\nThis maps to:\nPC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#ga6aabcba7744c5c9419fdd6404f73a934\nMicrosoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardreleasecontext\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | null"
 ([]
  (report-release-context-result
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id]}]
  (report-release-context-result
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id]}]
  (cmd/command
   connection
   "SmartCardEmulation"
   "reportReleaseContextResult"
   params
   {:request-id "requestId"})))

(s/fdef
 report-release-context-result
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::request-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id])))
 :ret
 (s/keys))

(defn
 report-list-readers-result
 "Reports the successful result of a |SCardListReaders| call.\n\nThis maps to:\nPC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#ga93b07815789b3cf2629d439ecf20f0d9\nMicrosoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardlistreadersa\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | null\n  :readers    | null"
 ([]
  (report-list-readers-result
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id readers]}]
  (report-list-readers-result
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id readers]}]
  (cmd/command
   connection
   "SmartCardEmulation"
   "reportListReadersResult"
   params
   {:request-id "requestId", :readers "readers"})))

(s/fdef
 report-list-readers-result
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::request-id
     ::readers]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id
     ::readers])))
 :ret
 (s/keys))

(defn
 report-get-status-change-result
 "Reports the successful result of a |SCardGetStatusChange| call.\n\nThis maps to:\nPC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#ga33247d5d1257d59e55647c3bb717db24\nMicrosoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardgetstatuschangea\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :request-id    | null\n  :reader-states | null"
 ([]
  (report-get-status-change-result
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id reader-states]}]
  (report-get-status-change-result
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id reader-states]}]
  (cmd/command
   connection
   "SmartCardEmulation"
   "reportGetStatusChangeResult"
   params
   {:request-id "requestId", :reader-states "readerStates"})))

(s/fdef
 report-get-status-change-result
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::request-id
     ::reader-states]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id
     ::reader-states])))
 :ret
 (s/keys))

(defn
 report-begin-transaction-result
 "Reports the result of a |SCardBeginTransaction| call.\nOn success, this creates a new transaction object.\n\nThis maps to:\nPC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#gaddb835dce01a0da1d6ca02d33ee7d861\nMicrosoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardbegintransaction\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | null\n  :handle     | null"
 ([]
  (report-begin-transaction-result
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id handle]}]
  (report-begin-transaction-result
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id handle]}]
  (cmd/command
   connection
   "SmartCardEmulation"
   "reportBeginTransactionResult"
   params
   {:request-id "requestId", :handle "handle"})))

(s/fdef
 report-begin-transaction-result
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::request-id
     ::handle]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id
     ::handle])))
 :ret
 (s/keys))

(defn
 report-plain-result
 "Reports the successful result of a call that returns only a result code.\nUsed for: |SCardCancel|, |SCardDisconnect|, |SCardSetAttrib|, |SCardEndTransaction|.\n\nThis maps to:\n1. SCardCancel\n   PC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#gaacbbc0c6d6c0cbbeb4f4debf6fbeeee6\n   Microsoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardcancel\n\n2. SCardDisconnect\n   PC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#ga4be198045c73ec0deb79e66c0ca1738a\n   Microsoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scarddisconnect\n\n3. SCardSetAttrib\n   PC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#ga060f0038a4ddfd5dd2b8fadf3c3a2e4f\n   Microsoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardsetattrib\n\n4. SCardEndTransaction\n   PC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#gae8742473b404363e5c587f570d7e2f3b\n   Microsoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardendtransaction\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | null"
 ([]
  (report-plain-result
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id]}]
  (report-plain-result
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id]}]
  (cmd/command
   connection
   "SmartCardEmulation"
   "reportPlainResult"
   params
   {:request-id "requestId"})))

(s/fdef
 report-plain-result
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::request-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id])))
 :ret
 (s/keys))

(defn
 report-connect-result
 "Reports the successful result of a |SCardConnect| call.\n\nThis maps to:\nPC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#ga4e515829752e0a8dbc4d630696a8d6a5\nMicrosoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardconnecta\n\nParameters map keys:\n\n\n  Key              | Description \n  -----------------|------------ \n  :request-id      | null\n  :handle          | null\n  :active-protocol | null (optional)"
 ([]
  (report-connect-result
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id handle active-protocol]}]
  (report-connect-result
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id handle active-protocol]}]
  (cmd/command
   connection
   "SmartCardEmulation"
   "reportConnectResult"
   params
   {:request-id "requestId",
    :handle "handle",
    :active-protocol "activeProtocol"})))

(s/fdef
 report-connect-result
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::request-id
     ::handle]
    :opt-un
    [::active-protocol]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id
     ::handle]
    :opt-un
    [::active-protocol])))
 :ret
 (s/keys))

(defn
 report-data-result
 "Reports the successful result of a call that sends back data on success.\nUsed for |SCardTransmit|, |SCardControl|, and |SCardGetAttrib|.\n\nThis maps to:\n1. SCardTransmit\n   PC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#ga9a2d77242a271310269065e64633ab99\n   Microsoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardtransmit\n\n2. SCardControl\n   PC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#gac3454d4657110fd7f753b2d3d8f4e32f\n   Microsoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardcontrol\n\n3. SCardGetAttrib\n   PC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#gaacfec51917255b7a25b94c5104961602\n   Microsoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardgetattrib\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | null\n  :data       | null"
 ([]
  (report-data-result
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id data]}]
  (report-data-result
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id data]}]
  (cmd/command
   connection
   "SmartCardEmulation"
   "reportDataResult"
   params
   {:request-id "requestId", :data "data"})))

(s/fdef
 report-data-result
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::request-id
     ::data]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id
     ::data])))
 :ret
 (s/keys))

(defn
 report-status-result
 "Reports the successful result of a |SCardStatus| call.\n\nThis maps to:\nPC/SC Lite: https://pcsclite.apdu.fr/api/group__API.html#gae49c3c894ad7ac12a5b896bde70d0382\nMicrosoft: https://learn.microsoft.com/en-us/windows/win32/api/winscard/nf-winscard-scardstatusa\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :request-id  | null\n  :reader-name | null\n  :state       | null\n  :atr         | null\n  :protocol    | null (optional)"
 ([]
  (report-status-result
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id reader-name state atr protocol]}]
  (report-status-result
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [request-id reader-name state atr protocol]}]
  (cmd/command
   connection
   "SmartCardEmulation"
   "reportStatusResult"
   params
   {:request-id "requestId",
    :reader-name "readerName",
    :state "state",
    :atr "atr",
    :protocol "protocol"})))

(s/fdef
 report-status-result
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::request-id
     ::reader-name
     ::state
     ::atr]
    :opt-un
    [::protocol]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id
     ::reader-name
     ::state
     ::atr]
    :opt-un
    [::protocol])))
 :ret
 (s/keys))

(defn
 report-error
 "Reports an error result for the given request.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :request-id  | null\n  :result-code | null"
 ([]
  (report-error
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id result-code]}]
  (report-error
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id result-code]}]
  (cmd/command
   connection
   "SmartCardEmulation"
   "reportError"
   params
   {:request-id "requestId", :result-code "resultCode"})))

(s/fdef
 report-error
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::request-id
     ::result-code]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id
     ::result-code])))
 :ret
 (s/keys))
