(ns clj-chrome-devtools.commands.web-mcp
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::annotation
 (s/keys
  :opt-un
  [::read-only
   ::untrusted-content
   ::autosubmit]))

(s/def
 ::invocation-status
 #{"Canceled" "Error" "Completed"})

(s/def
 ::tool
 (s/keys
  :req-un
  [::name
   ::description
   ::frame-id]
  :opt-un
  [::input-schema
   ::annotations
   ::backend-node-id
   ::stack-trace]))

(s/def
 ::removed-tool
 (s/keys
  :req-un
  [::name
   ::frame-id]))
(defn
 enable
 "Enables the WebMCP domain, allowing events to be sent. Enabling the domain will trigger a toolsAdded event for\nall currently registered tools."
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
   "WebMCP"
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
 "Disables the WebMCP domain."
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
   "WebMCP"
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
 invoke-tool
 "Invokes a registered tool.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :frame-id  | Frame in which to invoke the tool.\n  :tool-name | Name of the tool to invoke.\n  :input     | Input parameters for the tool, matching the tool's inputSchema.\n\nReturn map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :invocation-id | Unique identifier for this invocation. Response is sent before tool events."
 ([]
  (invoke-tool
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [frame-id tool-name input]}]
  (invoke-tool
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [frame-id tool-name input]}]
  (cmd/command
   connection
   "WebMCP"
   "invokeTool"
   params
   {:frame-id "frameId", :tool-name "toolName", :input "input"})))

(s/fdef
 invoke-tool
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::frame-id
     ::tool-name
     ::input]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::frame-id
     ::tool-name
     ::input])))
 :ret
 (s/keys
  :req-un
  [::invocation-id]))

(defn
 cancel-invocation
 "Cancels a pending tool invocation.\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :invocation-id | Invocation identifier to cancel."
 ([]
  (cancel-invocation
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [invocation-id]}]
  (cancel-invocation
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [invocation-id]}]
  (cmd/command
   connection
   "WebMCP"
   "cancelInvocation"
   params
   {:invocation-id "invocationId"})))

(s/fdef
 cancel-invocation
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::invocation-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::invocation-id])))
 :ret
 (s/keys))
