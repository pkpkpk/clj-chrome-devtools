(ns clj-chrome-devtools.commands.storage
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::serialized-storage-key
 string?)

(s/def
 ::storage-type
 #{"indexeddb" "file_systems" "service_workers" "shared_storage"
   "local_storage" "cache_storage" "storage_buckets" "cookies"
   "interest_groups" "other" "all" "websql" "shader_cache"})

(s/def
 ::usage-for-type
 (s/keys
  :req-un
  [::storage-type
   ::usage]))

(s/def
 ::trust-tokens
 (s/keys
  :req-un
  [::issuer-origin
   ::count]))

(s/def
 ::interest-group-auction-id
 string?)

(s/def
 ::interest-group-access-type
 #{"topLevelBid" "loaded" "update" "topLevelAdditionalBid" "win"
   "additionalBidWin" "leave" "additionalBid" "join" "bid" "clear"})

(s/def
 ::interest-group-auction-event-type
 #{"started" "configResolved"})

(s/def
 ::interest-group-auction-fetch-type
 #{"sellerTrustedSignals" "bidderTrustedSignals" "bidderJs" "sellerJs"
   "bidderWasm"})

(s/def
 ::shared-storage-access-scope
 #{"window" "protectedAudienceWorklet" "sharedStorageWorklet" "header"})

(s/def
 ::shared-storage-access-method
 #{"selectURL" "values" "keys" "delete" "batchUpdate" "run" "length"
   "createWorklet" "entries" "append" "set" "clear" "addModule" "get"
   "remainingBudget"})

(s/def
 ::shared-storage-entry
 (s/keys
  :req-un
  [::key
   ::value]))

(s/def
 ::shared-storage-metadata
 (s/keys
  :req-un
  [::creation-time
   ::length
   ::remaining-budget
   ::bytes-used]))

(s/def
 ::shared-storage-private-aggregation-config
 (s/keys
  :req-un
  [::filtering-id-max-bytes]
  :opt-un
  [::aggregation-coordinator-origin
   ::context-id
   ::max-contributions]))

(s/def
 ::shared-storage-reporting-metadata
 (s/keys
  :req-un
  [::event-type
   ::reporting-url]))

(s/def
 ::shared-storage-url-with-metadata
 (s/keys
  :req-un
  [::url
   ::reporting-metadata]))

(s/def
 ::shared-storage-access-params
 (s/keys
  :opt-un
  [::script-source-url
   ::data-origin
   ::operation-name
   ::operation-id
   ::keep-alive
   ::private-aggregation-config
   ::serialized-data
   ::urls-with-metadata
   ::urn-uuid
   ::key
   ::value
   ::ignore-if-present
   ::worklet-ordinal
   ::worklet-target-id
   ::with-lock
   ::batch-update-id
   ::batch-size]))

(s/def
 ::storage-buckets-durability
 #{"relaxed" "strict"})

(s/def
 ::storage-bucket
 (s/keys
  :req-un
  [::storage-key]
  :opt-un
  [::name]))

(s/def
 ::storage-bucket-info
 (s/keys
  :req-un
  [::bucket
   ::id
   ::expiration
   ::quota
   ::persistent
   ::durability]))

(s/def
 ::related-website-set
 (s/keys
  :req-un
  [::primary-sites
   ::associated-sites
   ::service-sites]))
(defn
 get-storage-key-for-frame
 "Returns a storage key given a frame id.\nDeprecated. Please use Storage.getStorageKey instead.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :frame-id | null\n\nReturn map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :storage-key | null"
 ([]
  (get-storage-key-for-frame
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [frame-id]}]
  (get-storage-key-for-frame
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [frame-id]}]
  (cmd/command
   connection
   "Storage"
   "getStorageKeyForFrame"
   params
   {:frame-id "frameId"})))

(s/fdef
 get-storage-key-for-frame
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::frame-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::frame-id])))
 :ret
 (s/keys
  :req-un
  [::storage-key]))

(defn
 get-storage-key
 "Returns storage key for the given frame. If no frame ID is provided,\nthe storage key of the target executing this command is returned.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :frame-id | null (optional)\n\nReturn map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :storage-key | null"
 ([]
  (get-storage-key
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [frame-id]}]
  (get-storage-key
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [frame-id]}]
  (cmd/command
   connection
   "Storage"
   "getStorageKey"
   params
   {:frame-id "frameId"})))

(s/fdef
 get-storage-key
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::frame-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::frame-id])))
 :ret
 (s/keys
  :req-un
  [::storage-key]))

(defn
 clear-data-for-origin
 "Clears storage for origin.\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :origin        | Security origin.\n  :storage-types | Comma separated list of StorageType to clear."
 ([]
  (clear-data-for-origin
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [origin storage-types]}]
  (clear-data-for-origin
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [origin storage-types]}]
  (cmd/command
   connection
   "Storage"
   "clearDataForOrigin"
   params
   {:origin "origin", :storage-types "storageTypes"})))

(s/fdef
 clear-data-for-origin
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::origin
     ::storage-types]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::origin
     ::storage-types])))
 :ret
 (s/keys))

(defn
 clear-data-for-storage-key
 "Clears storage for storage key.\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :storage-key   | Storage key.\n  :storage-types | Comma separated list of StorageType to clear."
 ([]
  (clear-data-for-storage-key
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [storage-key storage-types]}]
  (clear-data-for-storage-key
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [storage-key storage-types]}]
  (cmd/command
   connection
   "Storage"
   "clearDataForStorageKey"
   params
   {:storage-key "storageKey", :storage-types "storageTypes"})))

(s/fdef
 clear-data-for-storage-key
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::storage-key
     ::storage-types]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::storage-key
     ::storage-types])))
 :ret
 (s/keys))

(defn
 get-cookies
 "Returns all browser cookies.\n\nParameters map keys:\n\n\n  Key                 | Description \n  --------------------|------------ \n  :browser-context-id | Browser context to use when called on the browser endpoint. (optional)\n\nReturn map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :cookies | Array of cookie objects."
 ([]
  (get-cookies
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [browser-context-id]}]
  (get-cookies
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [browser-context-id]}]
  (cmd/command
   connection
   "Storage"
   "getCookies"
   params
   {:browser-context-id "browserContextId"})))

(s/fdef
 get-cookies
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::browser-context-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::browser-context-id])))
 :ret
 (s/keys
  :req-un
  [::cookies]))

(defn
 set-cookies
 "Sets given cookies.\n\nParameters map keys:\n\n\n  Key                 | Description \n  --------------------|------------ \n  :cookies            | Cookies to be set.\n  :browser-context-id | Browser context to use when called on the browser endpoint. (optional)"
 ([]
  (set-cookies
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [cookies browser-context-id]}]
  (set-cookies
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [cookies browser-context-id]}]
  (cmd/command
   connection
   "Storage"
   "setCookies"
   params
   {:cookies "cookies", :browser-context-id "browserContextId"})))

(s/fdef
 set-cookies
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::cookies]
    :opt-un
    [::browser-context-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::cookies]
    :opt-un
    [::browser-context-id])))
 :ret
 (s/keys))

(defn
 clear-cookies
 "Clears cookies.\n\nParameters map keys:\n\n\n  Key                 | Description \n  --------------------|------------ \n  :browser-context-id | Browser context to use when called on the browser endpoint. (optional)"
 ([]
  (clear-cookies
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [browser-context-id]}]
  (clear-cookies
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [browser-context-id]}]
  (cmd/command
   connection
   "Storage"
   "clearCookies"
   params
   {:browser-context-id "browserContextId"})))

(s/fdef
 clear-cookies
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::browser-context-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::browser-context-id])))
 :ret
 (s/keys))

(defn
 get-usage-and-quota
 "Returns usage and quota in bytes.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :origin | Security origin.\n\nReturn map keys:\n\n\n  Key              | Description \n  -----------------|------------ \n  :usage           | Storage usage (bytes).\n  :quota           | Storage quota (bytes).\n  :override-active | Whether or not the origin has an active storage quota override\n  :usage-breakdown | Storage usage per type (bytes)."
 ([]
  (get-usage-and-quota
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [origin]}]
  (get-usage-and-quota
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [origin]}]
  (cmd/command
   connection
   "Storage"
   "getUsageAndQuota"
   params
   {:origin "origin"})))

(s/fdef
 get-usage-and-quota
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::origin])))
 :ret
 (s/keys
  :req-un
  [::usage
   ::quota
   ::override-active
   ::usage-breakdown]))

(defn
 override-quota-for-origin
 "Override quota for the specified origin\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :origin     | Security origin.\n  :quota-size | The quota size (in bytes) to override the original quota with.\nIf this is called multiple times, the overridden quota will be equal to\nthe quotaSize provided in the final call. If this is called without\nspecifying a quotaSize, the quota will be reset to the default value for\nthe specified origin. If this is called multiple times with different\norigins, the override will be maintained for each origin until it is\ndisabled (called without a quotaSize). (optional)"
 ([]
  (override-quota-for-origin
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [origin quota-size]}]
  (override-quota-for-origin
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [origin quota-size]}]
  (cmd/command
   connection
   "Storage"
   "overrideQuotaForOrigin"
   params
   {:origin "origin", :quota-size "quotaSize"})))

(s/fdef
 override-quota-for-origin
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::origin]
    :opt-un
    [::quota-size]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::origin]
    :opt-un
    [::quota-size])))
 :ret
 (s/keys))

(defn
 track-cache-storage-for-origin
 "Registers origin to be notified when an update occurs to its cache storage list.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :origin | Security origin."
 ([]
  (track-cache-storage-for-origin
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [origin]}]
  (track-cache-storage-for-origin
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [origin]}]
  (cmd/command
   connection
   "Storage"
   "trackCacheStorageForOrigin"
   params
   {:origin "origin"})))

(s/fdef
 track-cache-storage-for-origin
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::origin])))
 :ret
 (s/keys))

(defn
 track-cache-storage-for-storage-key
 "Registers storage key to be notified when an update occurs to its cache storage list.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :storage-key | Storage key."
 ([]
  (track-cache-storage-for-storage-key
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [storage-key]}]
  (track-cache-storage-for-storage-key
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [storage-key]}]
  (cmd/command
   connection
   "Storage"
   "trackCacheStorageForStorageKey"
   params
   {:storage-key "storageKey"})))

(s/fdef
 track-cache-storage-for-storage-key
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::storage-key]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::storage-key])))
 :ret
 (s/keys))

(defn
 track-indexed-db-for-origin
 "Registers origin to be notified when an update occurs to its IndexedDB.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :origin | Security origin."
 ([]
  (track-indexed-db-for-origin
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [origin]}]
  (track-indexed-db-for-origin
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [origin]}]
  (cmd/command
   connection
   "Storage"
   "trackIndexedDBForOrigin"
   params
   {:origin "origin"})))

(s/fdef
 track-indexed-db-for-origin
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::origin])))
 :ret
 (s/keys))

(defn
 track-indexed-db-for-storage-key
 "Registers storage key to be notified when an update occurs to its IndexedDB.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :storage-key | Storage key."
 ([]
  (track-indexed-db-for-storage-key
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [storage-key]}]
  (track-indexed-db-for-storage-key
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [storage-key]}]
  (cmd/command
   connection
   "Storage"
   "trackIndexedDBForStorageKey"
   params
   {:storage-key "storageKey"})))

(s/fdef
 track-indexed-db-for-storage-key
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::storage-key]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::storage-key])))
 :ret
 (s/keys))

(defn
 untrack-cache-storage-for-origin
 "Unregisters origin from receiving notifications for cache storage.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :origin | Security origin."
 ([]
  (untrack-cache-storage-for-origin
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [origin]}]
  (untrack-cache-storage-for-origin
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [origin]}]
  (cmd/command
   connection
   "Storage"
   "untrackCacheStorageForOrigin"
   params
   {:origin "origin"})))

(s/fdef
 untrack-cache-storage-for-origin
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::origin])))
 :ret
 (s/keys))

(defn
 untrack-cache-storage-for-storage-key
 "Unregisters storage key from receiving notifications for cache storage.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :storage-key | Storage key."
 ([]
  (untrack-cache-storage-for-storage-key
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [storage-key]}]
  (untrack-cache-storage-for-storage-key
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [storage-key]}]
  (cmd/command
   connection
   "Storage"
   "untrackCacheStorageForStorageKey"
   params
   {:storage-key "storageKey"})))

(s/fdef
 untrack-cache-storage-for-storage-key
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::storage-key]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::storage-key])))
 :ret
 (s/keys))

(defn
 untrack-indexed-db-for-origin
 "Unregisters origin from receiving notifications for IndexedDB.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :origin | Security origin."
 ([]
  (untrack-indexed-db-for-origin
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [origin]}]
  (untrack-indexed-db-for-origin
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [origin]}]
  (cmd/command
   connection
   "Storage"
   "untrackIndexedDBForOrigin"
   params
   {:origin "origin"})))

(s/fdef
 untrack-indexed-db-for-origin
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::origin])))
 :ret
 (s/keys))

(defn
 untrack-indexed-db-for-storage-key
 "Unregisters storage key from receiving notifications for IndexedDB.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :storage-key | Storage key."
 ([]
  (untrack-indexed-db-for-storage-key
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [storage-key]}]
  (untrack-indexed-db-for-storage-key
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [storage-key]}]
  (cmd/command
   connection
   "Storage"
   "untrackIndexedDBForStorageKey"
   params
   {:storage-key "storageKey"})))

(s/fdef
 untrack-indexed-db-for-storage-key
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::storage-key]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::storage-key])))
 :ret
 (s/keys))

(defn
 get-trust-tokens
 "Returns the number of stored Trust Tokens per issuer for the\ncurrent browsing context.\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :tokens | null"
 ([]
  (get-trust-tokens
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (get-trust-tokens
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Storage"
   "getTrustTokens"
   params
   {})))

(s/fdef
 get-trust-tokens
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
  [::tokens]))

(defn
 clear-trust-tokens
 "Removes all Trust Tokens issued by the provided issuerOrigin.\nLeaves other stored data, including the issuer's Redemption Records, intact.\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :issuer-origin | null\n\nReturn map keys:\n\n\n  Key                | Description \n  -------------------|------------ \n  :did-delete-tokens | True if any tokens were deleted, false otherwise."
 ([]
  (clear-trust-tokens
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [issuer-origin]}]
  (clear-trust-tokens
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [issuer-origin]}]
  (cmd/command
   connection
   "Storage"
   "clearTrustTokens"
   params
   {:issuer-origin "issuerOrigin"})))

(s/fdef
 clear-trust-tokens
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::issuer-origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::issuer-origin])))
 :ret
 (s/keys
  :req-un
  [::did-delete-tokens]))

(defn
 get-interest-group-details
 "Gets details for a named interest group.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :owner-origin | null\n  :name         | null\n\nReturn map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :details | This largely corresponds to:\nhttps://wicg.github.io/turtledove/#dictdef-generatebidinterestgroup\nbut has absolute expirationTime instead of relative lifetimeMs and\nalso adds joiningOrigin."
 ([]
  (get-interest-group-details
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [owner-origin name]}]
  (get-interest-group-details
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [owner-origin name]}]
  (cmd/command
   connection
   "Storage"
   "getInterestGroupDetails"
   params
   {:owner-origin "ownerOrigin", :name "name"})))

(s/fdef
 get-interest-group-details
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::owner-origin
     ::name]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::owner-origin
     ::name])))
 :ret
 (s/keys
  :req-un
  [::details]))

(defn
 set-interest-group-tracking
 "Enables/Disables issuing of interestGroupAccessed events.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :enable | null"
 ([]
  (set-interest-group-tracking
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enable]}]
  (set-interest-group-tracking
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enable]}]
  (cmd/command
   connection
   "Storage"
   "setInterestGroupTracking"
   params
   {:enable "enable"})))

(s/fdef
 set-interest-group-tracking
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::enable]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::enable])))
 :ret
 (s/keys))

(defn
 set-interest-group-auction-tracking
 "Enables/Disables issuing of interestGroupAuctionEventOccurred and\ninterestGroupAuctionNetworkRequestCreated.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :enable | null"
 ([]
  (set-interest-group-auction-tracking
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enable]}]
  (set-interest-group-auction-tracking
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enable]}]
  (cmd/command
   connection
   "Storage"
   "setInterestGroupAuctionTracking"
   params
   {:enable "enable"})))

(s/fdef
 set-interest-group-auction-tracking
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::enable]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::enable])))
 :ret
 (s/keys))

(defn
 get-shared-storage-metadata
 "Gets metadata for an origin's shared storage.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :owner-origin | null\n\nReturn map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :metadata | null"
 ([]
  (get-shared-storage-metadata
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [owner-origin]}]
  (get-shared-storage-metadata
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [owner-origin]}]
  (cmd/command
   connection
   "Storage"
   "getSharedStorageMetadata"
   params
   {:owner-origin "ownerOrigin"})))

(s/fdef
 get-shared-storage-metadata
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::owner-origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::owner-origin])))
 :ret
 (s/keys
  :req-un
  [::metadata]))

(defn
 get-shared-storage-entries
 "Gets the entries in an given origin's shared storage.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :owner-origin | null\n\nReturn map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :entries | null"
 ([]
  (get-shared-storage-entries
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [owner-origin]}]
  (get-shared-storage-entries
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [owner-origin]}]
  (cmd/command
   connection
   "Storage"
   "getSharedStorageEntries"
   params
   {:owner-origin "ownerOrigin"})))

(s/fdef
 get-shared-storage-entries
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::owner-origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::owner-origin])))
 :ret
 (s/keys
  :req-un
  [::entries]))

(defn
 set-shared-storage-entry
 "Sets entry with `key` and `value` for a given origin's shared storage.\n\nParameters map keys:\n\n\n  Key                | Description \n  -------------------|------------ \n  :owner-origin      | null\n  :key               | null\n  :value             | null\n  :ignore-if-present | If `ignoreIfPresent` is included and true, then only sets the entry if\n`key` doesn't already exist. (optional)"
 ([]
  (set-shared-storage-entry
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [owner-origin key value ignore-if-present]}]
  (set-shared-storage-entry
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [owner-origin key value ignore-if-present]}]
  (cmd/command
   connection
   "Storage"
   "setSharedStorageEntry"
   params
   {:owner-origin "ownerOrigin",
    :key "key",
    :value "value",
    :ignore-if-present "ignoreIfPresent"})))

(s/fdef
 set-shared-storage-entry
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::owner-origin
     ::key
     ::value]
    :opt-un
    [::ignore-if-present]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::owner-origin
     ::key
     ::value]
    :opt-un
    [::ignore-if-present])))
 :ret
 (s/keys))

(defn
 delete-shared-storage-entry
 "Deletes entry for `key` (if it exists) for a given origin's shared storage.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :owner-origin | null\n  :key          | null"
 ([]
  (delete-shared-storage-entry
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [owner-origin key]}]
  (delete-shared-storage-entry
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [owner-origin key]}]
  (cmd/command
   connection
   "Storage"
   "deleteSharedStorageEntry"
   params
   {:owner-origin "ownerOrigin", :key "key"})))

(s/fdef
 delete-shared-storage-entry
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::owner-origin
     ::key]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::owner-origin
     ::key])))
 :ret
 (s/keys))

(defn
 clear-shared-storage-entries
 "Clears all entries for a given origin's shared storage.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :owner-origin | null"
 ([]
  (clear-shared-storage-entries
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [owner-origin]}]
  (clear-shared-storage-entries
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [owner-origin]}]
  (cmd/command
   connection
   "Storage"
   "clearSharedStorageEntries"
   params
   {:owner-origin "ownerOrigin"})))

(s/fdef
 clear-shared-storage-entries
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::owner-origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::owner-origin])))
 :ret
 (s/keys))

(defn
 reset-shared-storage-budget
 "Resets the budget for `ownerOrigin` by clearing all budget withdrawals.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :owner-origin | null"
 ([]
  (reset-shared-storage-budget
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [owner-origin]}]
  (reset-shared-storage-budget
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [owner-origin]}]
  (cmd/command
   connection
   "Storage"
   "resetSharedStorageBudget"
   params
   {:owner-origin "ownerOrigin"})))

(s/fdef
 reset-shared-storage-budget
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::owner-origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::owner-origin])))
 :ret
 (s/keys))

(defn
 set-shared-storage-tracking
 "Enables/disables issuing of sharedStorageAccessed events.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :enable | null"
 ([]
  (set-shared-storage-tracking
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enable]}]
  (set-shared-storage-tracking
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enable]}]
  (cmd/command
   connection
   "Storage"
   "setSharedStorageTracking"
   params
   {:enable "enable"})))

(s/fdef
 set-shared-storage-tracking
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::enable]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::enable])))
 :ret
 (s/keys))

(defn
 set-storage-bucket-tracking
 "Set tracking for a storage key's buckets.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :storage-key | null\n  :enable      | null"
 ([]
  (set-storage-bucket-tracking
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [storage-key enable]}]
  (set-storage-bucket-tracking
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [storage-key enable]}]
  (cmd/command
   connection
   "Storage"
   "setStorageBucketTracking"
   params
   {:storage-key "storageKey", :enable "enable"})))

(s/fdef
 set-storage-bucket-tracking
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::storage-key
     ::enable]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::storage-key
     ::enable])))
 :ret
 (s/keys))

(defn
 delete-storage-bucket
 "Deletes the Storage Bucket with the given storage key and bucket name.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :bucket | null"
 ([]
  (delete-storage-bucket
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [bucket]}]
  (delete-storage-bucket
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [bucket]}]
  (cmd/command
   connection
   "Storage"
   "deleteStorageBucket"
   params
   {:bucket "bucket"})))

(s/fdef
 delete-storage-bucket
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::bucket]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::bucket])))
 :ret
 (s/keys))

(defn
 run-bounce-tracking-mitigations
 "Deletes state for sites identified as potential bounce trackers, immediately.\n\nReturn map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :deleted-sites | null"
 ([]
  (run-bounce-tracking-mitigations
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (run-bounce-tracking-mitigations
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Storage"
   "runBounceTrackingMitigations"
   params
   {})))

(s/fdef
 run-bounce-tracking-mitigations
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
  [::deleted-sites]))

(defn
 get-related-website-sets
 "Returns the effective Related Website Sets in use by this profile for the browser\nsession. The effective Related Website Sets will not change during a browser session.\n\nReturn map keys:\n\n\n  Key   | Description \n  ------|------------ \n  :sets | null"
 ([]
  (get-related-website-sets
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (get-related-website-sets
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Storage"
   "getRelatedWebsiteSets"
   params
   {})))

(s/fdef
 get-related-website-sets
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
 (s/keys :req-un [:user/sets]))

(defn
 set-protected-audience-k-anonymity
 "\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :owner  | null\n  :name   | null\n  :hashes | null"
 ([]
  (set-protected-audience-k-anonymity
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [owner name hashes]}]
  (set-protected-audience-k-anonymity
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [owner name hashes]}]
  (cmd/command
   connection
   "Storage"
   "setProtectedAudienceKAnonymity"
   params
   {:owner "owner", :name "name", :hashes "hashes"})))

(s/fdef
 set-protected-audience-k-anonymity
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [:user/owner :user/name :user/hashes]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/owner :user/name :user/hashes])))
 :ret
 (s/keys))
