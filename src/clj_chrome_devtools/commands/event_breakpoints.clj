(ns clj-chrome-devtools.commands.event-breakpoints
  "EventBreakpoints permits setting JavaScript breakpoints on operations and events\noccurring in native code invoked from JavaScript. Once breakpoint is hit, it is\nreported through Debugger domain, similarly to regular breakpoints being hit."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(defn
 set-instrumentation-breakpoint
 "Sets breakpoint on particular native event.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :event-name | Instrumentation name to stop on."
 ([]
  (set-instrumentation-breakpoint
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [event-name]}]
  (set-instrumentation-breakpoint
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [event-name]}]
  (cmd/command
   connection
   "EventBreakpoints"
   "setInstrumentationBreakpoint"
   params
   {:event-name "eventName"})))

(s/fdef
 set-instrumentation-breakpoint
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::event-name]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::event-name])))
 :ret
 (s/keys))

(defn
 remove-instrumentation-breakpoint
 "Removes breakpoint on particular native event.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :event-name | Instrumentation name to stop on."
 ([]
  (remove-instrumentation-breakpoint
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [event-name]}]
  (remove-instrumentation-breakpoint
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [event-name]}]
  (cmd/command
   connection
   "EventBreakpoints"
   "removeInstrumentationBreakpoint"
   params
   {:event-name "eventName"})))

(s/fdef
 remove-instrumentation-breakpoint
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::event-name]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::event-name])))
 :ret
 (s/keys))

(defn
 disable
 "Removes all breakpoints"
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
   "EventBreakpoints"
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
