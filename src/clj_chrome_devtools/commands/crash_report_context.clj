(ns clj-chrome-devtools.commands.crash-report-context
  "This domain exposes the current state of the CrashReportContext API."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::crash-report-context-entry
 (s/keys
  :req-un
  [::key
   ::value
   ::frame-id]))
(defn
 get-entries
 "Returns all entries in the CrashReportContext across all frames in the page.\n\nReturn map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :entries | null"
 ([]
  (get-entries
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (get-entries
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "CrashReportContext"
   "getEntries"
   params
   {})))

(s/fdef
 get-entries
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
  [::entries]))
