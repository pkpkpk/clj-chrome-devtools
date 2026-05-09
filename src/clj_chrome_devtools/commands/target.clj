(ns clj-chrome-devtools.commands.target
  "Supports additional targets discovery and allows to attach to them."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::target-id
 string?)

(s/def
 ::session-id
 string?)

(s/def
 ::target-info
 (s/keys
  :req-un
  [::target-id
   ::type
   ::title
   ::url
   ::attached
   ::can-access-opener]
  :opt-un
  [::parent-id
   ::opener-id
   ::opener-frame-id
   ::parent-frame-id
   ::browser-context-id
   ::subtype]))

(s/def
 ::filter-entry
 (s/keys
  :opt-un
  [::exclude
   ::type]))

(s/def
 ::target-filter
 (s/coll-of any?))

(s/def
 ::remote-location
 (s/keys
  :req-un
  [::host
   ::port]))

(s/def
 ::window-state
 #{"normal" "fullscreen" "maximized" "minimized"})
(defn
 activate-target
 "Activates (focuses) the target.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :target-id | null"
 ([]
  (activate-target
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [target-id]}]
  (activate-target
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [target-id]}]
  (cmd/command
   connection
   "Target"
   "activateTarget"
   params
   {:target-id "targetId"})))

(s/fdef
 activate-target
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::target-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::target-id])))
 :ret
 (s/keys))

(defn
 attach-to-target
 "Attaches to the target with given id.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :target-id | null\n  :flatten   | Enables \"flat\" access to the session via specifying sessionId attribute in the commands.\nWe plan to make this the default, deprecate non-flattened mode,\nand eventually retire it. See crbug.com/991325. (optional)\n\nReturn map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :session-id | Id assigned to the session."
 ([]
  (attach-to-target
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [target-id flatten]}]
  (attach-to-target
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [target-id flatten]}]
  (cmd/command
   connection
   "Target"
   "attachToTarget"
   params
   {:target-id "targetId", :flatten "flatten"})))

(s/fdef
 attach-to-target
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::target-id]
    :opt-un
    [::flatten]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::target-id]
    :opt-un
    [::flatten])))
 :ret
 (s/keys
  :req-un
  [::session-id]))

(defn
 attach-to-browser-target
 "Attaches to the browser target, only uses flat sessionId mode.\n\nReturn map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :session-id | Id assigned to the session."
 ([]
  (attach-to-browser-target
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (attach-to-browser-target
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Target"
   "attachToBrowserTarget"
   params
   {})))

(s/fdef
 attach-to-browser-target
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
 (s/keys
  :req-un
  [::session-id]))

(defn
 close-target
 "Closes the target. If the target is a page that gets closed too.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :target-id | null\n\nReturn map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :success | Always set to true. If an error occurs, the response indicates protocol error."
 ([]
  (close-target
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [target-id]}]
  (close-target
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [target-id]}]
  (cmd/command
   connection
   "Target"
   "closeTarget"
   params
   {:target-id "targetId"})))

(s/fdef
 close-target
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::target-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::target-id])))
 :ret
 (s/keys
  :req-un
  [::success]))

(defn
 expose-dev-tools-protocol
 "Inject object to the target's main frame that provides a communication\nchannel with browser target.\n\nInjected object will be available as `window[bindingName]`.\n\nThe object has the following API:\n- `binding.send(json)` - a method to send messages over the remote debugging protocol\n- `binding.onmessage = json => handleMessage(json)` - a callback that will be called for the protocol notifications and command responses.\n\nParameters map keys:\n\n\n  Key                  | Description \n  ---------------------|------------ \n  :target-id           | null\n  :binding-name        | Binding name, 'cdp' if not specified. (optional)\n  :inherit-permissions | If true, inherits the current root session's permissions (default: false). (optional)"
 ([]
  (expose-dev-tools-protocol
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [target-id binding-name inherit-permissions]}]
  (expose-dev-tools-protocol
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [target-id binding-name inherit-permissions]}]
  (cmd/command
   connection
   "Target"
   "exposeDevToolsProtocol"
   params
   {:target-id "targetId",
    :binding-name "bindingName",
    :inherit-permissions "inheritPermissions"})))

(s/fdef
 expose-dev-tools-protocol
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::target-id]
    :opt-un
    [::binding-name
     ::inherit-permissions]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::target-id]
    :opt-un
    [::binding-name
     ::inherit-permissions])))
 :ret
 (s/keys))

(defn
 create-browser-context
 "Creates a new empty BrowserContext. Similar to an incognito profile but you can have more than\none.\n\nParameters map keys:\n\n\n  Key                                    | Description \n  ---------------------------------------|------------ \n  :dispose-on-detach                     | If specified, disposes this context when debugging session disconnects. (optional)\n  :proxy-server                          | Proxy server, similar to the one passed to --proxy-server (optional)\n  :proxy-bypass-list                     | Proxy bypass list, similar to the one passed to --proxy-bypass-list (optional)\n  :origins-with-universal-network-access | An optional list of origins to grant unlimited cross-origin access to.\nParts of the URL other than those constituting origin are ignored. (optional)\n\nReturn map keys:\n\n\n  Key                 | Description \n  --------------------|------------ \n  :browser-context-id | The id of the context created."
 ([]
  (create-browser-context
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [dispose-on-detach
     proxy-server
     proxy-bypass-list
     origins-with-universal-network-access]}]
  (create-browser-context
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [dispose-on-detach
     proxy-server
     proxy-bypass-list
     origins-with-universal-network-access]}]
  (cmd/command
   connection
   "Target"
   "createBrowserContext"
   params
   {:dispose-on-detach "disposeOnDetach",
    :proxy-server "proxyServer",
    :proxy-bypass-list "proxyBypassList",
    :origins-with-universal-network-access
    "originsWithUniversalNetworkAccess"})))

(s/fdef
 create-browser-context
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::dispose-on-detach
     ::proxy-server
     ::proxy-bypass-list
     ::origins-with-universal-network-access]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::dispose-on-detach
     ::proxy-server
     ::proxy-bypass-list
     ::origins-with-universal-network-access])))
 :ret
 (s/keys
  :req-un
  [::browser-context-id]))

(defn
 get-browser-contexts
 "Returns all browser contexts created with `Target.createBrowserContext` method.\n\nReturn map keys:\n\n\n  Key                         | Description \n  ----------------------------|------------ \n  :browser-context-ids        | An array of browser context ids.\n  :default-browser-context-id | The id of the default browser context if available. (optional)"
 ([]
  (get-browser-contexts
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (get-browser-contexts
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Target"
   "getBrowserContexts"
   params
   {})))

(s/fdef
 get-browser-contexts
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
 (s/keys
  :req-un
  [::browser-context-ids]
  :opt-un
  [::default-browser-context-id]))

(defn
 create-target
 "Creates a new page.\n\nParameters map keys:\n\n\n  Key                         | Description \n  ----------------------------|------------ \n  :url                        | The initial URL the page will be navigated to. An empty string indicates about:blank.\n  :left                       | Frame left origin in DIP (requires newWindow to be true or headless shell). (optional)\n  :top                        | Frame top origin in DIP (requires newWindow to be true or headless shell). (optional)\n  :width                      | Frame width in DIP (requires newWindow to be true or headless shell). (optional)\n  :height                     | Frame height in DIP (requires newWindow to be true or headless shell). (optional)\n  :window-state               | Frame window state (requires newWindow to be true or headless shell).\nDefault is normal. (optional)\n  :browser-context-id         | The browser context to create the page in. (optional)\n  :enable-begin-frame-control | Whether BeginFrames for this target will be controlled via DevTools (headless shell only,\nnot supported on MacOS yet, false by default). (optional)\n  :new-window                 | Whether to create a new Window or Tab (false by default, not supported by headless shell). (optional)\n  :background                 | Whether to create the target in background or foreground (false by default, not supported\nby headless shell). (optional)\n  :for-tab                    | Whether to create the target of type \"tab\". (optional)\n  :hidden                     | Whether to create a hidden target. The hidden target is observable via protocol, but not\npresent in the tab UI strip. Cannot be created with `forTab: true`, `newWindow: true` or\n`background: false`. The life-time of the tab is limited to the life-time of the session. (optional)\n  :focus                      | If specified, the option is used to determine if the new target should\nbe focused or not. By default, the focus behavior depends on the\nvalue of the background field. For example, background=false and focus=false\nwill result in the target tab being opened but the browser window remain\nunchanged (if it was in the background, it will remain in the background)\nand background=false with focus=undefined will result in the window being focused.\nUsing background: true and focus: true is not supported and will result in an error. (optional)\n\nReturn map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :target-id | The id of the page opened."
 ([]
  (create-target
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [url
     left
     top
     width
     height
     window-state
     browser-context-id
     enable-begin-frame-control
     new-window
     background
     for-tab
     hidden
     focus]}]
  (create-target
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [url
     left
     top
     width
     height
     window-state
     browser-context-id
     enable-begin-frame-control
     new-window
     background
     for-tab
     hidden
     focus]}]
  (cmd/command
   connection
   "Target"
   "createTarget"
   params
   {:enable-begin-frame-control "enableBeginFrameControl",
    :top "top",
    :for-tab "forTab",
    :window-state "windowState",
    :width "width",
    :background "background",
    :new-window "newWindow",
    :hidden "hidden",
    :browser-context-id "browserContextId",
    :url "url",
    :focus "focus",
    :height "height",
    :left "left"})))

(s/fdef
 create-target
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::url]
    :opt-un
    [::left
     ::top
     ::width
     ::height
     ::window-state
     ::browser-context-id
     ::enable-begin-frame-control
     ::new-window
     ::background
     ::for-tab
     ::hidden
     ::focus]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::url]
    :opt-un
    [::left
     ::top
     ::width
     ::height
     ::window-state
     ::browser-context-id
     ::enable-begin-frame-control
     ::new-window
     ::background
     ::for-tab
     ::hidden
     ::focus])))
 :ret
 (s/keys
  :req-un
  [::target-id]))

(defn
 detach-from-target
 "Detaches session with given id.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :session-id | Session to detach. (optional)\n  :target-id  | Deprecated. (optional)"
 ([]
  (detach-from-target
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [session-id target-id]}]
  (detach-from-target
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [session-id target-id]}]
  (cmd/command
   connection
   "Target"
   "detachFromTarget"
   params
   {:session-id "sessionId", :target-id "targetId"})))

(s/fdef
 detach-from-target
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::session-id
     ::target-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::session-id
     ::target-id])))
 :ret
 (s/keys))

(defn
 dispose-browser-context
 "Deletes a BrowserContext. All the belonging pages will be closed without calling their\nbeforeunload hooks.\n\nParameters map keys:\n\n\n  Key                 | Description \n  --------------------|------------ \n  :browser-context-id | null"
 ([]
  (dispose-browser-context
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [browser-context-id]}]
  (dispose-browser-context
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [browser-context-id]}]
  (cmd/command
   connection
   "Target"
   "disposeBrowserContext"
   params
   {:browser-context-id "browserContextId"})))

(s/fdef
 dispose-browser-context
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::browser-context-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::browser-context-id])))
 :ret
 (s/keys))

(defn
 get-target-info
 "Returns information about a target.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :target-id | null (optional)\n\nReturn map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :target-info | null"
 ([]
  (get-target-info
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [target-id]}]
  (get-target-info
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [target-id]}]
  (cmd/command
   connection
   "Target"
   "getTargetInfo"
   params
   {:target-id "targetId"})))

(s/fdef
 get-target-info
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::target-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::target-id])))
 :ret
 (s/keys
  :req-un
  [::target-info]))

(defn
 get-targets
 "Retrieves a list of available targets.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :filter | Only targets matching filter will be reported. If filter is not specified\nand target discovery is currently enabled, a filter used for target discovery\nis used for consistency. (optional)\n\nReturn map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :target-infos | The list of targets."
 ([]
  (get-targets
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [filter]}]
  (get-targets
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [filter]}]
  (cmd/command
   connection
   "Target"
   "getTargets"
   params
   {:filter "filter"})))

(s/fdef
 get-targets
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::filter]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::filter])))
 :ret
 (s/keys
  :req-un
  [::target-infos]))

(defn
 send-message-to-target
 "Sends protocol message over session with given id.\nConsider using flat mode instead; see commands attachToTarget, setAutoAttach,\nand crbug.com/991325.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :message    | null\n  :session-id | Identifier of the session. (optional)\n  :target-id  | Deprecated. (optional)"
 ([]
  (send-message-to-target
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [message session-id target-id]}]
  (send-message-to-target
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [message session-id target-id]}]
  (cmd/command
   connection
   "Target"
   "sendMessageToTarget"
   params
   {:message "message",
    :session-id "sessionId",
    :target-id "targetId"})))

(s/fdef
 send-message-to-target
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::message]
    :opt-un
    [::session-id
     ::target-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::message]
    :opt-un
    [::session-id
     ::target-id])))
 :ret
 (s/keys))

(defn
 set-auto-attach
 "Controls whether to automatically attach to new targets which are considered\nto be directly related to this one (for example, iframes or workers).\nWhen turned on, attaches to all existing related targets as well. When turned off,\nautomatically detaches from all currently attached targets.\nThis also clears all targets added by `autoAttachRelated` from the list of targets to watch\nfor creation of related targets.\nYou might want to call this recursively for auto-attached targets to attach\nto all available targets.\n\nParameters map keys:\n\n\n  Key                         | Description \n  ----------------------------|------------ \n  :auto-attach                | Whether to auto-attach to related targets.\n  :wait-for-debugger-on-start | Whether to pause new targets when attaching to them. Use `Runtime.runIfWaitingForDebugger`\nto run paused targets.\n  :flatten                    | Enables \"flat\" access to the session via specifying sessionId attribute in the commands.\nWe plan to make this the default, deprecate non-flattened mode,\nand eventually retire it. See crbug.com/991325. (optional)\n  :filter                     | Only targets matching filter will be attached. (optional)"
 ([]
  (set-auto-attach
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys [auto-attach wait-for-debugger-on-start flatten filter]}]
  (set-auto-attach
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys [auto-attach wait-for-debugger-on-start flatten filter]}]
  (cmd/command
   connection
   "Target"
   "setAutoAttach"
   params
   {:auto-attach "autoAttach",
    :wait-for-debugger-on-start "waitForDebuggerOnStart",
    :flatten "flatten",
    :filter "filter"})))

(s/fdef
 set-auto-attach
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::auto-attach
     ::wait-for-debugger-on-start]
    :opt-un
    [::flatten
     ::filter]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::auto-attach
     ::wait-for-debugger-on-start]
    :opt-un
    [::flatten
     ::filter])))
 :ret
 (s/keys))

(defn
 auto-attach-related
 "Adds the specified target to the list of targets that will be monitored for any related target\ncreation (such as child frames, child workers and new versions of service worker) and reported\nthrough `attachedToTarget`. The specified target is also auto-attached.\nThis cancels the effect of any previous `setAutoAttach` and is also cancelled by subsequent\n`setAutoAttach`. Only available at the Browser target.\n\nParameters map keys:\n\n\n  Key                         | Description \n  ----------------------------|------------ \n  :target-id                  | null\n  :wait-for-debugger-on-start | Whether to pause new targets when attaching to them. Use `Runtime.runIfWaitingForDebugger`\nto run paused targets.\n  :filter                     | Only targets matching filter will be attached. (optional)"
 ([]
  (auto-attach-related
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [target-id wait-for-debugger-on-start filter]}]
  (auto-attach-related
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [target-id wait-for-debugger-on-start filter]}]
  (cmd/command
   connection
   "Target"
   "autoAttachRelated"
   params
   {:target-id "targetId",
    :wait-for-debugger-on-start "waitForDebuggerOnStart",
    :filter "filter"})))

(s/fdef
 auto-attach-related
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::target-id
     ::wait-for-debugger-on-start]
    :opt-un
    [::filter]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::target-id
     ::wait-for-debugger-on-start]
    :opt-un
    [::filter])))
 :ret
 (s/keys))

(defn
 set-discover-targets
 "Controls whether to discover available targets and notify via\n`targetCreated/targetInfoChanged/targetDestroyed` events.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :discover | Whether to discover available targets.\n  :filter   | Only targets matching filter will be attached. If `discover` is false,\n`filter` must be omitted or empty. (optional)"
 ([]
  (set-discover-targets
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [discover filter]}]
  (set-discover-targets
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [discover filter]}]
  (cmd/command
   connection
   "Target"
   "setDiscoverTargets"
   params
   {:discover "discover", :filter "filter"})))

(s/fdef
 set-discover-targets
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::discover]
    :opt-un
    [::filter]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::discover]
    :opt-un
    [::filter])))
 :ret
 (s/keys))

(defn
 set-remote-locations
 "Enables target discovery for the specified locations, when `setDiscoverTargets` was set to\n`true`.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :locations | List of remote locations."
 ([]
  (set-remote-locations
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [locations]}]
  (set-remote-locations
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [locations]}]
  (cmd/command
   connection
   "Target"
   "setRemoteLocations"
   params
   {:locations "locations"})))

(s/fdef
 set-remote-locations
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::locations]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::locations])))
 :ret
 (s/keys))

(defn
 get-dev-tools-target
 "Gets the targetId of the DevTools page target opened for the given target\n(if any).\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :target-id | Page or tab target ID.\n\nReturn map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :target-id | The targetId of DevTools page target if exists. (optional)"
 ([]
  (get-dev-tools-target
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [target-id]}]
  (get-dev-tools-target
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [target-id]}]
  (cmd/command
   connection
   "Target"
   "getDevToolsTarget"
   params
   {:target-id "targetId"})))

(s/fdef
 get-dev-tools-target
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::target-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::target-id])))
 :ret
 (s/keys
  :opt-un
  [::target-id]))

(defn
 open-dev-tools
 "Opens a DevTools window for the target.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :target-id | This can be the page or tab target ID.\n  :panel-id  | The id of the panel we want DevTools to open initially. Currently\nsupported panels are elements, console, network, sources, resources\nand performance. (optional)\n\nReturn map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :target-id | The targetId of DevTools page target."
 ([]
  (open-dev-tools
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [target-id panel-id]}]
  (open-dev-tools
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [target-id panel-id]}]
  (cmd/command
   connection
   "Target"
   "openDevTools"
   params
   {:target-id "targetId", :panel-id "panelId"})))

(s/fdef
 open-dev-tools
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::target-id]
    :opt-un
    [::panel-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::target-id]
    :opt-un
    [::panel-id])))
 :ret
 (s/keys
  :req-un
  [::target-id]))
