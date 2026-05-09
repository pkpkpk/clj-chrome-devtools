(ns clj-chrome-devtools.commands.file-system
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::file
 (s/keys
  :req-un
  [::name
   ::last-modified
   ::size
   ::type]))

(s/def
 ::directory
 (s/keys
  :req-un
  [::name
   ::nested-directories
   ::nested-files]))

(s/def
 ::bucket-file-system-locator
 (s/keys
  :req-un
  [::storage-key
   ::path-components]
  :opt-un
  [::bucket-name]))
(defn
 get-directory
 "\n\nParameters map keys:\n\n\n  Key                         | Description \n  ----------------------------|------------ \n  :bucket-file-system-locator | null\n\nReturn map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :directory | Returns the directory object at the path."
 ([]
  (get-directory
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [bucket-file-system-locator]}]
  (get-directory
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [bucket-file-system-locator]}]
  (cmd/command
   connection
   "FileSystem"
   "getDirectory"
   params
   {:bucket-file-system-locator "bucketFileSystemLocator"})))

(s/fdef
 get-directory
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::bucket-file-system-locator]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::bucket-file-system-locator])))
 :ret
 (s/keys
  :req-un
  [::directory]))
