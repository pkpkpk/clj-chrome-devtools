(ns clj-chrome-devtools.commands.extensions
  "Defines commands and events for browser extensions."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::storage-area
 #{"managed" "sync" "local" "session"})

(s/def
 ::extension-info
 (s/keys
  :req-un
  [::id
   ::name
   ::version
   ::path
   ::enabled]))
(defn
 trigger-action
 "Runs an extension default action.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :id        | Extension id.\n  :target-id | A tab target ID to trigger the default extension action on."
 ([]
  (trigger-action
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [id target-id]}]
  (trigger-action
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [id target-id]}]
  (cmd/command
   connection
   "Extensions"
   "triggerAction"
   params
   {:id "id", :target-id "targetId"})))

(s/fdef
 trigger-action
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
     ::target-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::id
     ::target-id])))
 :ret
 (s/keys))

(defn
 load-unpacked
 "Installs an unpacked extension from the filesystem similar to\n--load-extension CLI flags. Returns extension ID once the extension\nhas been installed.\n\nParameters map keys:\n\n\n  Key                  | Description \n  ---------------------|------------ \n  :path                | Absolute file path.\n  :enable-in-incognito | Enable the extension in incognito (optional)\n\nReturn map keys:\n\n\n  Key | Description \n  ----|------------ \n  :id | Extension id."
 ([]
  (load-unpacked
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [path enable-in-incognito]}]
  (load-unpacked
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [path enable-in-incognito]}]
  (cmd/command
   connection
   "Extensions"
   "loadUnpacked"
   params
   {:path "path", :enable-in-incognito "enableInIncognito"})))

(s/fdef
 load-unpacked
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::path]
    :opt-un
    [::enable-in-incognito]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::path]
    :opt-un
    [::enable-in-incognito])))
 :ret
 (s/keys
  :req-un
  [::id]))

(defn
 get-extensions
 "Gets a list of all unpacked extensions.\n\nReturn map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :extensions | null"
 ([]
  (get-extensions
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (get-extensions
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Extensions"
   "getExtensions"
   params
   {})))

(s/fdef
 get-extensions
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
  [::extensions]))

(defn
 uninstall
 "Uninstalls an unpacked extension (others not supported) from the profile.\n\nParameters map keys:\n\n\n  Key | Description \n  ----|------------ \n  :id | Extension id."
 ([]
  (uninstall
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [id]}]
  (uninstall
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [id]}]
  (cmd/command
   connection
   "Extensions"
   "uninstall"
   params
   {:id "id"})))

(s/fdef
 uninstall
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

(defn
 get-storage-items
 "Gets data from extension storage in the given `storageArea`. If `keys` is\nspecified, these are used to filter the result.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :id           | ID of extension.\n  :storage-area | StorageArea to retrieve data from.\n  :keys         | Keys to retrieve. (optional)\n\nReturn map keys:\n\n\n  Key   | Description \n  ------|------------ \n  :data | null"
 ([]
  (get-storage-items
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [id storage-area keys]}]
  (get-storage-items
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [id storage-area keys]}]
  (cmd/command
   connection
   "Extensions"
   "getStorageItems"
   params
   {:id "id", :storage-area "storageArea", :keys "keys"})))

(s/fdef
 get-storage-items
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
     ::storage-area]
    :opt-un
    [::keys]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::id
     ::storage-area]
    :opt-un
    [::keys])))
 :ret
 (s/keys
  :req-un
  [::data]))

(defn
 remove-storage-items
 "Removes `keys` from extension storage in the given `storageArea`.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :id           | ID of extension.\n  :storage-area | StorageArea to remove data from.\n  :keys         | Keys to remove."
 ([]
  (remove-storage-items
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [id storage-area keys]}]
  (remove-storage-items
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [id storage-area keys]}]
  (cmd/command
   connection
   "Extensions"
   "removeStorageItems"
   params
   {:id "id", :storage-area "storageArea", :keys "keys"})))

(s/fdef
 remove-storage-items
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
     ::storage-area
     ::keys]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::id
     ::storage-area
     ::keys])))
 :ret
 (s/keys))

(defn
 clear-storage-items
 "Clears extension storage in the given `storageArea`.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :id           | ID of extension.\n  :storage-area | StorageArea to remove data from."
 ([]
  (clear-storage-items
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [id storage-area]}]
  (clear-storage-items
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [id storage-area]}]
  (cmd/command
   connection
   "Extensions"
   "clearStorageItems"
   params
   {:id "id", :storage-area "storageArea"})))

(s/fdef
 clear-storage-items
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
     ::storage-area]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::id
     ::storage-area])))
 :ret
 (s/keys))

(defn
 set-storage-items
 "Sets `values` in extension storage in the given `storageArea`. The provided `values`\nwill be merged with existing values in the storage area.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :id           | ID of extension.\n  :storage-area | StorageArea to set data in.\n  :values       | Values to set."
 ([]
  (set-storage-items
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [id storage-area values]}]
  (set-storage-items
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [id storage-area values]}]
  (cmd/command
   connection
   "Extensions"
   "setStorageItems"
   params
   {:id "id", :storage-area "storageArea", :values "values"})))

(s/fdef
 set-storage-items
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
     ::storage-area
     ::values]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::id
     ::storage-area
     ::values])))
 :ret
 (s/keys))
