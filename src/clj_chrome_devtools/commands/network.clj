(ns clj-chrome-devtools.commands.network
  "Network domain allows tracking network activities of the page. It exposes information about http,\nfile, data and other requests and responses, their headers, bodies, timing, etc."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::resource-type
 #{"Script" "FedCM" "Media" "WebSocket" "Prefetch" "Preflight"
   "Manifest" "SignedExchange" "XHR" "Fetch" "Font" "TextTrack" "Ping"
   "Stylesheet" "Image" "Document" "EventSource" "CSPViolationReport"
   "Other"})

(s/def
 ::loader-id
 string?)

(s/def
 ::request-id
 string?)

(s/def
 ::interception-id
 string?)

(s/def
 ::error-reason
 #{"ConnectionFailed" "Failed" "AccessDenied" "BlockedByClient"
   "ConnectionAborted" "AddressUnreachable" "TimedOut"
   "BlockedByResponse" "NameNotResolved" "ConnectionReset" "Aborted"
   "ConnectionRefused" "ConnectionClosed" "InternetDisconnected"})

(s/def
 ::time-since-epoch
 number?)

(s/def
 ::monotonic-time
 number?)

(s/def
 ::headers
 (s/keys))

(s/def
 ::connection-type
 #{"none" "wimax" "wifi" "cellular3g" "other" "ethernet" "cellular2g"
   "cellular4g" "bluetooth"})

(s/def
 ::cookie-same-site
 #{"None" "Lax" "Strict"})

(s/def
 ::cookie-priority
 #{"Medium" "High" "Low"})

(s/def
 ::cookie-source-scheme
 #{"NonSecure" "Secure" "Unset"})

(s/def
 ::resource-timing
 (s/keys
  :req-un
  [::request-time
   ::proxy-start
   ::proxy-end
   ::dns-start
   ::dns-end
   ::connect-start
   ::connect-end
   ::ssl-start
   ::ssl-end
   ::worker-start
   ::worker-ready
   ::worker-fetch-start
   ::worker-respond-with-settled
   ::send-start
   ::send-end
   ::push-start
   ::push-end
   ::receive-headers-start
   ::receive-headers-end]
  :opt-un
  [::worker-router-evaluation-start
   ::worker-cache-lookup-start]))

(s/def
 ::resource-priority
 #{"Medium" "High" "VeryHigh" "Low" "VeryLow"})

(s/def
 ::render-blocking-behavior
 #{"NonBlockingDynamic" "Blocking" "NonBlocking" "PotentiallyBlocking"
   "InBodyParserBlocking"})

(s/def
 ::post-data-entry
 (s/keys
  :opt-un
  [::bytes]))

(s/def
 ::request
 (s/keys
  :req-un
  [::url
   ::method
   ::headers
   ::initial-priority
   ::referrer-policy]
  :opt-un
  [::url-fragment
   ::post-data
   ::has-post-data
   ::post-data-entries
   ::mixed-content-type
   ::is-link-preload
   ::trust-token-params
   ::is-same-site
   ::is-ad-related]))

(s/def
 ::signed-certificate-timestamp
 (s/keys
  :req-un
  [::status
   ::origin
   ::log-description
   ::log-id
   ::timestamp
   ::hash-algorithm
   ::signature-algorithm
   ::signature-data]))

(s/def
 ::security-details
 (s/keys
  :req-un
  [::protocol
   ::key-exchange
   ::cipher
   ::certificate-id
   ::subject-name
   ::san-list
   ::issuer
   ::valid-from
   ::valid-to
   ::signed-certificate-timestamp-list
   ::certificate-transparency-compliance
   ::encrypted-client-hello]
  :opt-un
  [::key-exchange-group
   ::mac
   ::server-signature-algorithm]))

(s/def
 ::certificate-transparency-compliance
 #{"not-compliant" "unknown" "compliant"})

(s/def
 ::blocked-reason
 #{"csp" "sri-message-signature-mismatch" "origin" "content-type"
   "corp-not-same-site"
   "coop-sandboxed-iframe-cannot-navigate-to-coop-page" "integrity"
   "corp-not-same-origin-after-defaulted-to-same-origin-by-dip"
   "corp-not-same-origin" "subresource-filter" "mixed-content"
   "corp-not-same-origin-after-defaulted-to-same-origin-by-coep"
   "other"
   "corp-not-same-origin-after-defaulted-to-same-origin-by-coep-and-dip"
   "inspector" "coep-frame-resource-needs-coep-header"})

(s/def
 ::cors-error
 #{"PreflightInvalidStatus" "PreflightMultipleAllowOriginValues"
   "DisallowedByMode" "InsecureLocalNetwork"
   "PreflightAllowOriginMismatch" "InvalidAllowCredentials"
   "PreflightInvalidAllowCredentials" "NoCorsRedirectModeNotFollow"
   "InvalidAllowOriginValue" "LocalNetworkAccessPermissionDenied"
   "InvalidAllowHeadersPreflightResponse" "InvalidResponse"
   "AllowOriginMismatch" "MethodDisallowedByPreflightResponse"
   "PreflightInvalidAllowExternal" "InvalidLocalNetworkAccess"
   "PreflightDisallowedRedirect" "MultipleAllowOriginValues"
   "PreflightMissingAllowExternal" "PreflightInvalidAllowOriginValue"
   "MissingAllowOriginHeader" "RedirectContainsCredentials"
   "WildcardOriginNotAllowed" "HeaderDisallowedByPreflightResponse"
   "PreflightMissingAllowOriginHeader"
   "PreflightWildcardOriginNotAllowed" "CorsDisabledScheme"
   "InvalidAllowMethodsPreflightResponse"})

(s/def
 ::cors-error-status
 (s/keys
  :req-un
  [::cors-error
   ::failed-parameter]))

(s/def
 ::service-worker-response-source
 #{"cache-storage" "fallback-code" "network" "http-cache"})

(s/def
 ::trust-token-params
 (s/keys
  :req-un
  [::operation
   ::refresh-policy]
  :opt-un
  [::issuers]))

(s/def
 ::trust-token-operation-type
 #{"Signing" "Redemption" "Issuance"})

(s/def
 ::alternate-protocol-usage
 #{"alternativeJobWonWithoutRace" "alternativeJobWonRace"
   "unspecifiedReason" "dnsAlpnH3JobWonWithoutRace" "broken"
   "mainJobWonRace" "mappingMissing" "dnsAlpnH3JobWonRace"})

(s/def
 ::service-worker-router-source
 #{"fetch-event" "race-network-and-fetch-handler" "cache" "network"
   "race-network-and-cache"})

(s/def
 ::service-worker-router-info
 (s/keys
  :opt-un
  [::rule-id-matched
   ::matched-source-type
   ::actual-source-type]))

(s/def
 ::response
 (s/keys
  :req-un
  [::url
   ::status
   ::status-text
   ::headers
   ::mime-type
   ::charset
   ::connection-reused
   ::connection-id
   ::encoded-data-length
   ::security-state]
  :opt-un
  [::headers-text
   ::request-headers
   ::request-headers-text
   ::remote-ip-address
   ::remote-port
   ::from-disk-cache
   ::from-service-worker
   ::from-prefetch-cache
   ::from-early-hints
   ::service-worker-router-info
   ::timing
   ::service-worker-response-source
   ::response-time
   ::cache-storage-cache-name
   ::protocol
   ::alternate-protocol-usage
   ::security-details]))

(s/def
 ::web-socket-request
 (s/keys
  :req-un
  [::headers]))

(s/def
 ::web-socket-response
 (s/keys
  :req-un
  [::status
   ::status-text
   ::headers]
  :opt-un
  [::headers-text
   ::request-headers
   ::request-headers-text]))

(s/def
 :user/web-socket-frame
 (s/keys
  :req-un
  [:user/opcode :user/mask :user/payload-data]))

(s/def
 :user/cached-resource
 (s/keys
  :req-un
  [:user/url :user/type :user/body-size]
  :opt-un
  [:user/response]))

(s/def
 :user/initiator
 (s/keys
  :req-un
  [:user/type]
  :opt-un
  [:user/stack
   :user/url
   :user/line-number
   :user/column-number
   :user/request-id]))

(s/def
 :user/cookie-partition-key
 (s/keys
  :req-un
  [:user/top-level-site :user/has-cross-site-ancestor]))

(s/def
 :user/cookie
 (s/keys
  :req-un
  [:user/name
   :user/value
   :user/domain
   :user/path
   :user/expires
   :user/size
   :user/http-only
   :user/secure
   :user/session
   :user/priority
   :user/source-scheme
   :user/source-port]
  :opt-un
  [:user/same-site :user/partition-key :user/partition-key-opaque]))

(s/def
 :user/set-cookie-blocked-reason
 #{"NoCookieContent" "DisallowedCharacter" "UnknownError"
   "SchemeNotSupported" "SameSiteNoneInsecure" "SecureOnly"
   "NameValuePairExceedsMaxSize" "SyntaxError" "InvalidDomain"
   "OverwriteSecure" "SchemefulSameSiteUnspecifiedTreatedAsLax"
   "SameSiteUnspecifiedTreatedAsLax" "ThirdPartyPhaseout"
   "ThirdPartyBlockedInFirstPartySet" "UserPreferences" "InvalidPrefix"
   "SameSiteLax" "SchemefulSameSiteLax" "SameSiteStrict"
   "SchemefulSameSiteStrict"})

(s/def
 :user/cookie-blocked-reason
 #{"SchemeMismatch" "UnknownError" "SameSiteNoneInsecure" "SecureOnly"
   "NameValuePairExceedsMaxSize" "PortMismatch" "NotOnPath"
   "SchemefulSameSiteUnspecifiedTreatedAsLax"
   "SameSiteUnspecifiedTreatedAsLax" "AnonymousContext"
   "ThirdPartyPhaseout" "DomainMismatch"
   "ThirdPartyBlockedInFirstPartySet" "UserPreferences" "SameSiteLax"
   "SchemefulSameSiteLax" "SameSiteStrict" "SchemefulSameSiteStrict"})

(s/def
 :user/cookie-exemption-reason
 #{"UserSetting" "TPCDHeuristics" "EnterprisePolicy"
   "TopLevelTPCDDeprecationTrial" "TPCDDeprecationTrial" "None"
   "Scheme" "StorageAccess" "TopLevelStorageAccess" "TPCDMetadata"
   "SameSiteNoneCookiesInSandbox"})

(s/def
 :user/blocked-set-cookie-with-reason
 (s/keys
  :req-un
  [:user/blocked-reasons :user/cookie-line]
  :opt-un
  [:user/cookie]))

(s/def
 :user/exempted-set-cookie-with-reason
 (s/keys
  :req-un
  [:user/exemption-reason :user/cookie-line :user/cookie]))

(s/def
 :user/associated-cookie
 (s/keys
  :req-un
  [:user/cookie :user/blocked-reasons]
  :opt-un
  [:user/exemption-reason]))

(s/def
 :user/cookie-param
 (s/keys
  :req-un
  [:user/name :user/value]
  :opt-un
  [:user/url
   :user/domain
   :user/path
   :user/secure
   :user/http-only
   :user/same-site
   :user/expires
   :user/priority
   :user/source-scheme
   :user/source-port
   :user/partition-key]))

(s/def
 :user/auth-challenge
 (s/keys
  :req-un
  [:user/origin :user/scheme :user/realm]
  :opt-un
  [:user/source]))

(s/def
 :user/auth-challenge-response
 (s/keys
  :req-un
  [:user/response]
  :opt-un
  [:user/username :user/password]))

(s/def
 :user/interception-stage
 #{"Request" "HeadersReceived"})

(s/def
 :user/request-pattern
 (s/keys
  :opt-un
  [:user/url-pattern :user/resource-type :user/interception-stage]))

(s/def
 :user/signed-exchange-signature
 (s/keys
  :req-un
  [:user/label
   :user/signature
   :user/integrity
   :user/validity-url
   :user/date
   :user/expires]
  :opt-un
  [:user/cert-url :user/cert-sha256 :user/certificates]))

(s/def
 :user/signed-exchange-header
 (s/keys
  :req-un
  [:user/request-url
   :user/response-code
   :user/response-headers
   :user/signatures
   :user/header-integrity]))

(s/def
 :user/signed-exchange-error-field
 #{"signatureCertUrl" "signatureSig" "signatureCertSha256"
   "signatureValidityUrl" "signatureTimestamps" "signatureIntegrity"})

(s/def
 :user/signed-exchange-error
 (s/keys
  :req-un
  [:user/message]
  :opt-un
  [:user/signature-index :user/error-field]))

(s/def
 :user/signed-exchange-info
 (s/keys
  :req-un
  [:user/outer-response :user/has-extra-info]
  :opt-un
  [:user/header :user/security-details :user/errors]))

(s/def
 :user/content-encoding
 #{"br" "gzip" "zstd" "deflate"})

(s/def
 :user/network-conditions
 (s/keys
  :req-un
  [:user/url-pattern
   :user/latency
   :user/download-throughput
   :user/upload-throughput]
  :opt-un
  [:user/connection-type
   :user/packet-loss
   :user/packet-queue-length
   :user/packet-reordering
   :user/offline]))

(s/def
 :user/block-pattern
 (s/keys :req-un [:user/url-pattern :user/block]))

(s/def
 :user/direct-socket-dns-query-type
 #{"ipv4" "ipv6"})

(s/def
 :user/direct-tcp-socket-options
 (s/keys
  :req-un
  [:user/no-delay]
  :opt-un
  [:user/keep-alive-delay
   :user/send-buffer-size
   :user/receive-buffer-size
   :user/dns-query-type]))

(s/def
 :user/direct-udp-socket-options
 (s/keys
  :opt-un
  [:user/remote-addr
   :user/remote-port
   :user/local-addr
   :user/local-port
   :user/dns-query-type
   :user/send-buffer-size
   :user/receive-buffer-size
   :user/multicast-loopback
   :user/multicast-time-to-live
   :user/multicast-allow-address-sharing]))

(s/def
 :user/direct-udp-message
 (s/keys
  :req-un
  [:user/data]
  :opt-un
  [:user/remote-addr :user/remote-port]))

(s/def
 :user/local-network-access-request-policy
 #{"PermissionBlock" "Allow" "WarnFromInsecureToMorePrivate"
   "BlockFromInsecureToMorePrivate" "PermissionWarn"})

(s/def
 :user/ip-address-space
 #{"Public" "Loopback" "Unknown" "Local"})

(s/def
 :user/connect-timing
 (s/keys :req-un [:user/request-time]))

(s/def
 :user/client-security-state
 (s/keys
  :req-un
  [:user/initiator-is-secure-context
   :user/initiator-ip-address-space
   :user/local-network-access-request-policy]))

(s/def
 :user/ad-script-identifier
 (s/keys
  :req-un
  [:user/script-id :user/debugger-id :user/name]))

(s/def
 :user/ad-ancestry
 (s/keys
  :req-un
  [:user/ancestry-chain]
  :opt-un
  [:user/root-script-filterlist-rule]))

(s/def
 :user/ad-provenance
 (s/keys
  :opt-un
  [:user/filterlist-rule :user/ad-script-ancestry]))

(s/def
 :user/cross-origin-opener-policy-value
 #{"SameOriginPlusCoep" "SameOriginAllowPopups" "NoopenerAllowPopups"
   "RestrictProperties" "RestrictPropertiesPlusCoep" "SameOrigin"
   "UnsafeNone"})

(s/def
 :user/cross-origin-opener-policy-status
 (s/keys
  :req-un
  [:user/value :user/report-only-value]
  :opt-un
  [:user/reporting-endpoint :user/report-only-reporting-endpoint]))

(s/def
 :user/cross-origin-embedder-policy-value
 #{"None" "Credentialless" "RequireCorp"})

(s/def
 :user/cross-origin-embedder-policy-status
 (s/keys
  :req-un
  [:user/value :user/report-only-value]
  :opt-un
  [:user/reporting-endpoint :user/report-only-reporting-endpoint]))

(s/def
 :user/content-security-policy-source
 #{"Meta" "HTTP"})

(s/def
 :user/content-security-policy-status
 (s/keys
  :req-un
  [:user/effective-directives :user/is-enforced :user/source]))

(s/def
 :user/security-isolation-status
 (s/keys :opt-un [:user/coop :user/coep :user/csp]))

(s/def
 :user/report-status
 #{"Success" "MarkedForRemoval" "Pending" "Queued"})

(s/def :user/report-id string?)

(s/def
 :user/reporting-api-report
 (s/keys
  :req-un
  [:user/id
   :user/initiator-url
   :user/destination
   :user/type
   :user/timestamp
   :user/depth
   :user/completed-attempts
   :user/body
   :user/status]))

(s/def
 :user/reporting-api-endpoint
 (s/keys :req-un [:user/url :user/group-name]))

(s/def
 :user/device-bound-session-key
 (s/keys :req-un [:user/site :user/id]))

(s/def
 :user/device-bound-session-with-usage
 (s/keys :req-un [:user/session-key :user/usage]))

(s/def
 :user/device-bound-session-cookie-craving
 (s/keys
  :req-un
  [:user/name :user/domain :user/path :user/secure :user/http-only]
  :opt-un
  [:user/same-site]))

(s/def
 :user/device-bound-session-url-rule
 (s/keys
  :req-un
  [:user/rule-type :user/host-pattern :user/path-prefix]))

(s/def
 :user/device-bound-session-inclusion-rules
 (s/keys
  :req-un
  [:user/origin :user/include-site :user/url-rules]))

(s/def
 :user/device-bound-session
 (s/keys
  :req-un
  [:user/key
   :user/refresh-url
   :user/inclusion-rules
   :user/cookie-cravings
   :user/expiry-date
   :user/allowed-refresh-initiators]
  :opt-un
  [:user/cached-challenge]))

(s/def
 :user/device-bound-session-event-id
 string?)

(s/def
 :user/device-bound-session-fetch-result
 #{"InvalidCredentialsCookieInvalidDomain"
   "RelyingPartyWellKnownUnavailable"
   "ScopeRuleSiteScopedHostPatternMismatch"
   "MissingScopeSpecificationType" "NetError"
   "SessionDeletedDuringRefresh" "InvalidCredentialsCookieCreationTime"
   "InvalidCredentialsCookieUnpermittedAttribute"
   "FederatedKeyThumbprintMismatch" "InvalidCredentialsCookie"
   "BoundCookieSetForbidden"
   "InvalidFederatedSessionProviderFailedToRestoreKey"
   "FailedToUnwrapKey" "InvalidScopeIncludeSite" "InvalidConfigJson"
   "PersistentHttpError" "TransientHttpError"
   "RegistrationAttemptedChallenge" "FederatedNotAuthorizedByProvider"
   "NoCredentials" "SessionProviderWellKnownUnavailable"
   "InvalidChallenge" "InvalidFederatedSessionWrongProviderOrigin"
   "ProxyError" "InvalidScopeRuleHostPattern"
   "ScopeRuleOriginScopedHostPatternMismatch" "InvalidScopeRulePath"
   "InvalidScopeSpecificationType" "RelyingPartyWellKnownMalformed"
   "InvalidFederatedKey" "TooManyChallenges" "SigningError"
   "RefreshInitiatorNotString" "ServerRequestedTermination"
   "RefreshInitiatorInvalidHostPattern" "EmptySessionConfig"
   "InvalidCredentialsCookieName" "MismatchedSessionId" "Success"
   "RelyingPartyWellKnownHasRelyingOrigins"
   "SubdomainRegistrationWellKnownUnavailable"
   "TooManyRelyingOriginLabels" "InvalidCredentialsCookiePrefix"
   "InvalidCredentialsConfig" "TransientSigningError"
   "EmptyScopeSpecificationDomain" "SigningQuotaExceeded"
   "InvalidFederatedSessionUrl" "SessionProviderWellKnownMalformed"
   "ScopeOriginSameSiteMismatch" "KeyError"
   "InvalidCredentialsCookieParsing" "InvalidRefreshUrl"
   "InvalidCredentialsType" "MissingScopeIncludeSite"
   "ScopeOriginContainsPath" "EmptyScopeSpecificationPath"
   "InvalidSessionId" "RefreshUrlSameSiteMismatch" "InvalidScopeOrigin"
   "SubdomainRegistrationUnauthorized" "MissingScope"
   "InvalidFetcherUrl" "InvalidCredentialsEmptyName"
   "FederatedNotAuthorizedByRelyingParty"
   "SubdomainRegistrationWellKnownMalformed"
   "InvalidScopeSpecification"
   "SessionProviderWellKnownHasProviderOrigin"
   "InvalidFederatedSessionProviderSessionMissing"})

(s/def
 :user/device-bound-session-failed-request
 (s/keys
  :req-un
  [:user/request-url]
  :opt-un
  [:user/net-error :user/response-error :user/response-error-body]))

(s/def
 :user/creation-event-details
 (s/keys
  :req-un
  [:user/fetch-result]
  :opt-un
  [:user/new-session :user/failed-request]))

(s/def
 :user/refresh-event-details
 (s/keys
  :req-un
  [:user/refresh-result :user/was-fully-proactive-refresh]
  :opt-un
  [:user/fetch-result :user/new-session :user/failed-request]))

(s/def
 :user/termination-event-details
 (s/keys :req-un [:user/deletion-reason]))

(s/def
 :user/challenge-event-details
 (s/keys
  :req-un
  [:user/challenge-result :user/challenge]))

(s/def
 :user/load-network-resource-page-result
 (s/keys
  :req-un
  [:user/success]
  :opt-un
  [:user/net-error
   :user/net-error-name
   :user/http-status-code
   :user/stream
   :user/headers]))

(s/def
 :user/load-network-resource-options
 (s/keys
  :req-un
  [:user/disable-cache :user/include-credentials]))
(defn
 set-accepted-encodings
 "Sets a list of content encodings that will be accepted. Empty list means no encoding is accepted.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :encodings | List of accepted content encodings."
 ([]
  (set-accepted-encodings
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [encodings]}]
  (set-accepted-encodings
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [encodings]}]
  (cmd/command
   connection
   "Network"
   "setAcceptedEncodings"
   params
   {:encodings "encodings"})))

(s/fdef
 set-accepted-encodings
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::encodings]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::encodings])))
 :ret
 (s/keys))

(defn
 clear-accepted-encodings-override
 "Clears accepted encodings set by setAcceptedEncodings"
 ([]
  (clear-accepted-encodings-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (clear-accepted-encodings-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Network"
   "clearAcceptedEncodingsOverride"
   params
   {})))

(s/fdef
 clear-accepted-encodings-override
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
 can-clear-browser-cache
 "Tells whether clearing browser cache is supported.\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :result | True if browser cache can be cleared."
 ([]
  (can-clear-browser-cache
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (can-clear-browser-cache
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Network"
   "canClearBrowserCache"
   params
   {})))

(s/fdef
 can-clear-browser-cache
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
  [::result]))

(defn
 can-clear-browser-cookies
 "Tells whether clearing browser cookies is supported.\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :result | True if browser cookies can be cleared."
 ([]
  (can-clear-browser-cookies
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (can-clear-browser-cookies
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Network"
   "canClearBrowserCookies"
   params
   {})))

(s/fdef
 can-clear-browser-cookies
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
  [::result]))

(defn
 can-emulate-network-conditions
 "Tells whether emulation of network conditions is supported.\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :result | True if emulation of network conditions is supported."
 ([]
  (can-emulate-network-conditions
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (can-emulate-network-conditions
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Network"
   "canEmulateNetworkConditions"
   params
   {})))

(s/fdef
 can-emulate-network-conditions
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
  [::result]))

(defn
 clear-browser-cache
 "Clears browser cache."
 ([]
  (clear-browser-cache
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (clear-browser-cache
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Network"
   "clearBrowserCache"
   params
   {})))

(s/fdef
 clear-browser-cache
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
 clear-browser-cookies
 "Clears browser cookies."
 ([]
  (clear-browser-cookies
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (clear-browser-cookies
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Network"
   "clearBrowserCookies"
   params
   {})))

(s/fdef
 clear-browser-cookies
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
 continue-intercepted-request
 "Response to Network.requestIntercepted which either modifies the request to continue with any\nmodifications, or blocks it, or completes it with the provided response bytes. If a network\nfetch occurs as a result which encounters a redirect an additional Network.requestIntercepted\nevent will be sent with the same InterceptionId.\nDeprecated, use Fetch.continueRequest, Fetch.fulfillRequest and Fetch.failRequest instead.\n\nParameters map keys:\n\n\n  Key                      | Description \n  -------------------------|------------ \n  :interception-id         | null\n  :error-reason            | If set this causes the request to fail with the given reason. Passing `Aborted` for requests\nmarked with `isNavigationRequest` also cancels the navigation. Must not be set in response\nto an authChallenge. (optional)\n  :raw-response            | If set the requests completes using with the provided base64 encoded raw response, including\nHTTP status line and headers etc... Must not be set in response to an authChallenge. (Encoded as a base64 string when passed over JSON) (optional)\n  :url                     | If set the request url will be modified in a way that's not observable by page. Must not be\nset in response to an authChallenge. (optional)\n  :method                  | If set this allows the request method to be overridden. Must not be set in response to an\nauthChallenge. (optional)\n  :post-data               | If set this allows postData to be set. Must not be set in response to an authChallenge. (optional)\n  :headers                 | If set this allows the request headers to be changed. Must not be set in response to an\nauthChallenge. (optional)\n  :auth-challenge-response | Response to a requestIntercepted with an authChallenge. Must not be set otherwise. (optional)"
 ([]
  (continue-intercepted-request
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [interception-id
     error-reason
     raw-response
     url
     method
     post-data
     headers
     auth-challenge-response]}]
  (continue-intercepted-request
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [interception-id
     error-reason
     raw-response
     url
     method
     post-data
     headers
     auth-challenge-response]}]
  (cmd/command
   connection
   "Network"
   "continueInterceptedRequest"
   params
   {:interception-id "interceptionId",
    :error-reason "errorReason",
    :raw-response "rawResponse",
    :url "url",
    :method "method",
    :post-data "postData",
    :headers "headers",
    :auth-challenge-response "authChallengeResponse"})))

(s/fdef
 continue-intercepted-request
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::interception-id]
    :opt-un
    [::error-reason
     ::raw-response
     ::url
     ::method
     ::post-data
     ::headers
     ::auth-challenge-response]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::interception-id]
    :opt-un
    [::error-reason
     ::raw-response
     ::url
     ::method
     ::post-data
     ::headers
     ::auth-challenge-response])))
 :ret
 (s/keys))

(defn
 delete-cookies
 "Deletes browser cookies with matching name and url or domain/path/partitionKey pair.\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :name          | Name of the cookies to remove.\n  :url           | If specified, deletes all the cookies with the given name where domain and path match\nprovided URL. (optional)\n  :domain        | If specified, deletes only cookies with the exact domain. (optional)\n  :path          | If specified, deletes only cookies with the exact path. (optional)\n  :partition-key | If specified, deletes only cookies with the the given name and partitionKey where\nall partition key attributes match the cookie partition key attribute. (optional)"
 ([]
  (delete-cookies
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [name url domain path partition-key]}]
  (delete-cookies
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [name url domain path partition-key]}]
  (cmd/command
   connection
   "Network"
   "deleteCookies"
   params
   {:name "name",
    :url "url",
    :domain "domain",
    :path "path",
    :partition-key "partitionKey"})))

(s/fdef
 delete-cookies
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::name]
    :opt-un
    [::url
     ::domain
     ::path
     ::partition-key]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::name]
    :opt-un
    [::url
     ::domain
     ::path
     ::partition-key])))
 :ret
 (s/keys))

(defn
 disable
 "Disables network tracking, prevents network events from being sent to the client."
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
   "Network"
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
 emulate-network-conditions
 "Activates emulation of network conditions. This command is deprecated in favor of the emulateNetworkConditionsByRule\nand overrideNetworkState commands, which can be used together to the same effect.\n\nParameters map keys:\n\n\n  Key                  | Description \n  ---------------------|------------ \n  :offline             | True to emulate internet disconnection.\n  :latency             | Minimum latency from request sent to response headers received (ms).\n  :download-throughput | Maximal aggregated download throughput (bytes/sec). -1 disables download throttling.\n  :upload-throughput   | Maximal aggregated upload throughput (bytes/sec).  -1 disables upload throttling.\n  :connection-type     | Connection type if known. (optional)\n  :packet-loss         | WebRTC packet loss (percent, 0-100). 0 disables packet loss emulation, 100 drops all the packets. (optional)\n  :packet-queue-length | WebRTC packet queue length (packet). 0 removes any queue length limitations. (optional)\n  :packet-reordering   | WebRTC packetReordering feature. (optional)"
 ([]
  (emulate-network-conditions
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [offline
     latency
     download-throughput
     upload-throughput
     connection-type
     packet-loss
     packet-queue-length
     packet-reordering]}]
  (emulate-network-conditions
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [offline
     latency
     download-throughput
     upload-throughput
     connection-type
     packet-loss
     packet-queue-length
     packet-reordering]}]
  (cmd/command
   connection
   "Network"
   "emulateNetworkConditions"
   params
   {:offline "offline",
    :latency "latency",
    :download-throughput "downloadThroughput",
    :upload-throughput "uploadThroughput",
    :connection-type "connectionType",
    :packet-loss "packetLoss",
    :packet-queue-length "packetQueueLength",
    :packet-reordering "packetReordering"})))

(s/fdef
 emulate-network-conditions
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::offline
     ::latency
     ::download-throughput
     ::upload-throughput]
    :opt-un
    [::connection-type
     ::packet-loss
     ::packet-queue-length
     ::packet-reordering]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::offline
     ::latency
     ::download-throughput
     ::upload-throughput]
    :opt-un
    [::connection-type
     ::packet-loss
     ::packet-queue-length
     ::packet-reordering])))
 :ret
 (s/keys))

(defn
 emulate-network-conditions-by-rule
 "Activates emulation of network conditions for individual requests using URL match patterns. Unlike the deprecated\nNetwork.emulateNetworkConditions this method does not affect `navigator` state. Use Network.overrideNetworkState to\nexplicitly modify `navigator` behavior.\n\nParameters map keys:\n\n\n  Key                             | Description \n  --------------------------------|------------ \n  :offline                        | True to emulate internet disconnection. Deprecated, use the offline property in matchedNetworkConditions\nor emulateOfflineServiceWorker instead. (optional)\n  :emulate-offline-service-worker | True to emulate offline service worker. (optional)\n  :matched-network-conditions     | Configure conditions for matching requests. If multiple entries match a request, the first entry wins.  Global\nconditions can be configured by leaving the urlPattern for the conditions empty. These global conditions are\nalso applied for throttling of p2p connections.\n\nReturn map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :rule-ids | An id for each entry in matchedNetworkConditions. The id will be included in the requestWillBeSentExtraInfo for\nrequests affected by a rule."
 ([]
  (emulate-network-conditions-by-rule
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [offline
     emulate-offline-service-worker
     matched-network-conditions]}]
  (emulate-network-conditions-by-rule
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [offline
     emulate-offline-service-worker
     matched-network-conditions]}]
  (cmd/command
   connection
   "Network"
   "emulateNetworkConditionsByRule"
   params
   {:offline "offline",
    :emulate-offline-service-worker "emulateOfflineServiceWorker",
    :matched-network-conditions "matchedNetworkConditions"})))

(s/fdef
 emulate-network-conditions-by-rule
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::matched-network-conditions]
    :opt-un
    [::offline
     ::emulate-offline-service-worker]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::matched-network-conditions]
    :opt-un
    [::offline
     ::emulate-offline-service-worker])))
 :ret
 (s/keys
  :req-un
  [::rule-ids]))

(defn
 override-network-state
 "Override the state of navigator.onLine and navigator.connection.\n\nParameters map keys:\n\n\n  Key                  | Description \n  ---------------------|------------ \n  :offline             | True to emulate internet disconnection.\n  :latency             | Minimum latency from request sent to response headers received (ms).\n  :download-throughput | Maximal aggregated download throughput (bytes/sec). -1 disables download throttling.\n  :upload-throughput   | Maximal aggregated upload throughput (bytes/sec).  -1 disables upload throttling.\n  :connection-type     | Connection type if known. (optional)"
 ([]
  (override-network-state
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [offline
     latency
     download-throughput
     upload-throughput
     connection-type]}]
  (override-network-state
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [offline
     latency
     download-throughput
     upload-throughput
     connection-type]}]
  (cmd/command
   connection
   "Network"
   "overrideNetworkState"
   params
   {:offline "offline",
    :latency "latency",
    :download-throughput "downloadThroughput",
    :upload-throughput "uploadThroughput",
    :connection-type "connectionType"})))

(s/fdef
 override-network-state
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::offline
     ::latency
     ::download-throughput
     ::upload-throughput]
    :opt-un
    [::connection-type]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::offline
     ::latency
     ::download-throughput
     ::upload-throughput]
    :opt-un
    [::connection-type])))
 :ret
 (s/keys))

(defn
 enable
 "Enables network tracking, network events will now be delivered to the client.\n\nParameters map keys:\n\n\n  Key                           | Description \n  ------------------------------|------------ \n  :max-total-buffer-size        | Buffer size in bytes to use when preserving network payloads (XHRs, etc).\nThis is the maximum number of bytes that will be collected by this\nDevTools session. (optional)\n  :max-resource-buffer-size     | Per-resource buffer size in bytes to use when preserving network payloads (XHRs, etc). (optional)\n  :max-post-data-size           | Longest post body size (in bytes) that would be included in requestWillBeSent notification (optional)\n  :report-direct-socket-traffic | Whether DirectSocket chunk send/receive events should be reported. (optional)\n  :enable-durable-messages      | Enable storing response bodies outside of renderer, so that these survive\na cross-process navigation. Requires maxTotalBufferSize to be set.\nCurrently defaults to false. This field is being deprecated in favor of the dedicated\nconfigureDurableMessages command, due to the possibility of deadlocks when awaiting\nNetwork.enable before issuing Runtime.runIfWaitingForDebugger. (optional)"
 ([]
  (enable
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [max-total-buffer-size
     max-resource-buffer-size
     max-post-data-size
     report-direct-socket-traffic
     enable-durable-messages]}]
  (enable
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [max-total-buffer-size
     max-resource-buffer-size
     max-post-data-size
     report-direct-socket-traffic
     enable-durable-messages]}]
  (cmd/command
   connection
   "Network"
   "enable"
   params
   {:max-total-buffer-size "maxTotalBufferSize",
    :max-resource-buffer-size "maxResourceBufferSize",
    :max-post-data-size "maxPostDataSize",
    :report-direct-socket-traffic "reportDirectSocketTraffic",
    :enable-durable-messages "enableDurableMessages"})))

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
    [::max-total-buffer-size
     ::max-resource-buffer-size
     ::max-post-data-size
     ::report-direct-socket-traffic
     ::enable-durable-messages]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::max-total-buffer-size
     ::max-resource-buffer-size
     ::max-post-data-size
     ::report-direct-socket-traffic
     ::enable-durable-messages])))
 :ret
 (s/keys))

(defn
 configure-durable-messages
 "Configures storing response bodies outside of renderer, so that these survive\na cross-process navigation.\nIf maxTotalBufferSize is not set, durable messages are disabled.\n\nParameters map keys:\n\n\n  Key                       | Description \n  --------------------------|------------ \n  :max-total-buffer-size    | Buffer size in bytes to use when preserving network payloads (XHRs, etc). (optional)\n  :max-resource-buffer-size | Per-resource buffer size in bytes to use when preserving network payloads (XHRs, etc). (optional)"
 ([]
  (configure-durable-messages
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys [max-total-buffer-size max-resource-buffer-size]}]
  (configure-durable-messages
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys [max-total-buffer-size max-resource-buffer-size]}]
  (cmd/command
   connection
   "Network"
   "configureDurableMessages"
   params
   {:max-total-buffer-size "maxTotalBufferSize",
    :max-resource-buffer-size "maxResourceBufferSize"})))

(s/fdef
 configure-durable-messages
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::max-total-buffer-size
     ::max-resource-buffer-size]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::max-total-buffer-size
     ::max-resource-buffer-size])))
 :ret
 (s/keys))

(defn
 get-all-cookies
 "Returns all browser cookies. Depending on the backend support, will return detailed cookie\ninformation in the `cookies` field.\nDeprecated. Use Storage.getCookies instead.\n\nReturn map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :cookies | Array of cookie objects."
 ([]
  (get-all-cookies
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (get-all-cookies
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Network"
   "getAllCookies"
   params
   {})))

(s/fdef
 get-all-cookies
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
  [::cookies]))

(defn
 get-certificate
 "Returns the DER-encoded certificate.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :origin | Origin to get certificate for.\n\nReturn map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :table-names | null"
 ([]
  (get-certificate
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [origin]}]
  (get-certificate
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [origin]}]
  (cmd/command
   connection
   "Network"
   "getCertificate"
   params
   {:origin "origin"})))

(s/fdef
 get-certificate
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
  [::table-names]))

(defn
 get-cookies
 "Returns all browser cookies for the current URL. Depending on the backend support, will return\ndetailed cookie information in the `cookies` field.\n\nParameters map keys:\n\n\n  Key   | Description \n  ------|------------ \n  :urls | The list of URLs for which applicable cookies will be fetched.\nIf not specified, it's assumed to be set to the list containing\nthe URLs of the page and all of its subframes. (optional)\n\nReturn map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :cookies | Array of cookie objects."
 ([]
  (get-cookies
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [urls]}]
  (get-cookies
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [urls]}]
  (cmd/command
   connection
   "Network"
   "getCookies"
   params
   {:urls "urls"})))

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
    [::urls]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::urls])))
 :ret
 (s/keys
  :req-un
  [::cookies]))

(defn
 get-response-body
 "Returns content served for the given request.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | Identifier of the network request to get content for.\n\nReturn map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :body           | Response body.\n  :base64-encoded | True, if content was sent as base64."
 ([]
  (get-response-body
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id]}]
  (get-response-body
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id]}]
  (cmd/command
   connection
   "Network"
   "getResponseBody"
   params
   {:request-id "requestId"})))

(s/fdef
 get-response-body
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
 (s/keys
  :req-un
  [::body
   ::base64-encoded]))

(defn
 get-request-post-data
 "Returns post data sent with the request. Returns an error when no data was sent with the request.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | Identifier of the network request to get content for.\n\nReturn map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :post-data      | Request body string, omitting files from multipart requests\n  :base64-encoded | True, if content was sent as base64."
 ([]
  (get-request-post-data
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id]}]
  (get-request-post-data
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id]}]
  (cmd/command
   connection
   "Network"
   "getRequestPostData"
   params
   {:request-id "requestId"})))

(s/fdef
 get-request-post-data
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
 (s/keys
  :req-un
  [::post-data
   ::base64-encoded]))

(defn
 get-response-body-for-interception
 "Returns content served for the given currently intercepted request.\n\nParameters map keys:\n\n\n  Key              | Description \n  -----------------|------------ \n  :interception-id | Identifier for the intercepted request to get body for.\n\nReturn map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :body           | Response body.\n  :base64-encoded | True, if content was sent as base64."
 ([]
  (get-response-body-for-interception
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [interception-id]}]
  (get-response-body-for-interception
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [interception-id]}]
  (cmd/command
   connection
   "Network"
   "getResponseBodyForInterception"
   params
   {:interception-id "interceptionId"})))

(s/fdef
 get-response-body-for-interception
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::interception-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::interception-id])))
 :ret
 (s/keys
  :req-un
  [::body
   ::base64-encoded]))

(defn
 take-response-body-for-interception-as-stream
 "Returns a handle to the stream representing the response body. Note that after this command,\nthe intercepted request can't be continued as is -- you either need to cancel it or to provide\nthe response body. The stream only supports sequential read, IO.read will fail if the position\nis specified.\n\nParameters map keys:\n\n\n  Key              | Description \n  -----------------|------------ \n  :interception-id | null\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :stream | null"
 ([]
  (take-response-body-for-interception-as-stream
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [interception-id]}]
  (take-response-body-for-interception-as-stream
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [interception-id]}]
  (cmd/command
   connection
   "Network"
   "takeResponseBodyForInterceptionAsStream"
   params
   {:interception-id "interceptionId"})))

(s/fdef
 take-response-body-for-interception-as-stream
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::interception-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::interception-id])))
 :ret
 (s/keys
  :req-un
  [::stream]))

(defn
 replay-xhr
 "This method sends a new XMLHttpRequest which is identical to the original one. The following\nparameters should be identical: method, url, async, request body, extra headers, withCredentials\nattribute, user, password.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | Identifier of XHR to replay."
 ([]
  (replay-xhr
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id]}]
  (replay-xhr
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id]}]
  (cmd/command
   connection
   "Network"
   "replayXHR"
   params
   {:request-id "requestId"})))

(s/fdef
 replay-xhr
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
 search-in-response-body
 "Searches for given string in response content.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :request-id     | Identifier of the network response to search.\n  :query          | String to search for.\n  :case-sensitive | If true, search is case sensitive. (optional)\n  :is-regex       | If true, treats string parameter as regex. (optional)\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :result | List of search matches."
 ([]
  (search-in-response-body
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id query case-sensitive is-regex]}]
  (search-in-response-body
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [request-id query case-sensitive is-regex]}]
  (cmd/command
   connection
   "Network"
   "searchInResponseBody"
   params
   {:request-id "requestId",
    :query "query",
    :case-sensitive "caseSensitive",
    :is-regex "isRegex"})))

(s/fdef
 search-in-response-body
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
     ::query]
    :opt-un
    [::case-sensitive
     ::is-regex]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id
     ::query]
    :opt-un
    [::case-sensitive
     ::is-regex])))
 :ret
 (s/keys
  :req-un
  [::result]))

(defn
 set-blocked-ur-ls
 "Blocks URLs from loading.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :url-patterns | Patterns to match in the order in which they are given. These patterns\nalso take precedence over any wildcard patterns defined in `urls`. (optional)\n  :urls         | URL patterns to block. Wildcards ('*') are allowed. (optional)"
 ([]
  (set-blocked-ur-ls
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [url-patterns urls]}]
  (set-blocked-ur-ls
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [url-patterns urls]}]
  (cmd/command
   connection
   "Network"
   "setBlockedURLs"
   params
   {:url-patterns "urlPatterns", :urls "urls"})))

(s/fdef
 set-blocked-ur-ls
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::url-patterns
     ::urls]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::url-patterns
     ::urls])))
 :ret
 (s/keys))

(defn
 set-bypass-service-worker
 "Toggles ignoring of service worker for each request.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :bypass | Bypass service worker and load from network."
 ([]
  (set-bypass-service-worker
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [bypass]}]
  (set-bypass-service-worker
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [bypass]}]
  (cmd/command
   connection
   "Network"
   "setBypassServiceWorker"
   params
   {:bypass "bypass"})))

(s/fdef
 set-bypass-service-worker
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::bypass]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::bypass])))
 :ret
 (s/keys))

(defn
 set-cache-disabled
 "Toggles ignoring cache for each request. If `true`, cache will not be used.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :cache-disabled | Cache disabled state."
 ([]
  (set-cache-disabled
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [cache-disabled]}]
  (set-cache-disabled
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [cache-disabled]}]
  (cmd/command
   connection
   "Network"
   "setCacheDisabled"
   params
   {:cache-disabled "cacheDisabled"})))

(s/fdef
 set-cache-disabled
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::cache-disabled]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::cache-disabled])))
 :ret
 (s/keys))

(defn
 set-cookie
 "Sets a cookie with the given cookie data; may overwrite equivalent cookies if they exist.\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :name          | Cookie name.\n  :value         | Cookie value.\n  :url           | The request-URI to associate with the setting of the cookie. This value can affect the\ndefault domain, path, source port, and source scheme values of the created cookie. (optional)\n  :domain        | Cookie domain. (optional)\n  :path          | Cookie path. (optional)\n  :secure        | True if cookie is secure. (optional)\n  :http-only     | True if cookie is http-only. (optional)\n  :same-site     | Cookie SameSite type. (optional)\n  :expires       | Cookie expiration date, session cookie if not set (optional)\n  :priority      | Cookie Priority type. (optional)\n  :source-scheme | Cookie source scheme type. (optional)\n  :source-port   | Cookie source port. Valid values are {-1, [1, 65535]}, -1 indicates an unspecified port.\nAn unspecified port value allows protocol clients to emulate legacy cookie scope for the port.\nThis is a temporary ability and it will be removed in the future. (optional)\n  :partition-key | Cookie partition key. If not set, the cookie will be set as not partitioned. (optional)\n\nReturn map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :success | Always set to true. If an error occurs, the response indicates protocol error."
 ([]
  (set-cookie
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [name
     value
     url
     domain
     path
     secure
     http-only
     same-site
     expires
     priority
     source-scheme
     source-port
     partition-key]}]
  (set-cookie
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [name
     value
     url
     domain
     path
     secure
     http-only
     same-site
     expires
     priority
     source-scheme
     source-port
     partition-key]}]
  (cmd/command
   connection
   "Network"
   "setCookie"
   params
   {:path "path",
    :same-site "sameSite",
    :partition-key "partitionKey",
    :name "name",
    :value "value",
    :source-scheme "sourceScheme",
    :expires "expires",
    :priority "priority",
    :source-port "sourcePort",
    :url "url",
    :domain "domain",
    :secure "secure",
    :http-only "httpOnly"})))

(s/fdef
 set-cookie
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::name
     ::value]
    :opt-un
    [::url
     ::domain
     ::path
     ::secure
     ::http-only
     ::same-site
     ::expires
     ::priority
     ::source-scheme
     ::source-port
     ::partition-key]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::name
     ::value]
    :opt-un
    [::url
     ::domain
     ::path
     ::secure
     ::http-only
     ::same-site
     ::expires
     ::priority
     ::source-scheme
     ::source-port
     ::partition-key])))
 :ret
 (s/keys
  :req-un
  [::success]))

(defn
 set-cookies
 "Sets given cookies.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :cookies | Cookies to be set."
 ([]
  (set-cookies
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [cookies]}]
  (set-cookies
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [cookies]}]
  (cmd/command
   connection
   "Network"
   "setCookies"
   params
   {:cookies "cookies"})))

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
    [::cookies]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::cookies])))
 :ret
 (s/keys))

(defn
 set-extra-http-headers
 "Specifies whether to always send extra HTTP headers with the requests from this page.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :headers | Map with extra HTTP headers."
 ([]
  (set-extra-http-headers
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [headers]}]
  (set-extra-http-headers
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [headers]}]
  (cmd/command
   connection
   "Network"
   "setExtraHTTPHeaders"
   params
   {:headers "headers"})))

(s/fdef
 set-extra-http-headers
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::headers]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::headers])))
 :ret
 (s/keys))

(defn
 set-attach-debug-stack
 "Specifies whether to attach a page script stack id in requests\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :enabled | Whether to attach a page script stack for debugging purpose."
 ([]
  (set-attach-debug-stack
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enabled]}]
  (set-attach-debug-stack
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enabled]}]
  (cmd/command
   connection
   "Network"
   "setAttachDebugStack"
   params
   {:enabled "enabled"})))

(s/fdef
 set-attach-debug-stack
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::enabled]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::enabled])))
 :ret
 (s/keys))

(defn
 set-request-interception
 "Sets the requests to intercept that match the provided patterns and optionally resource types.\nDeprecated, please use Fetch.enable instead.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :patterns | Requests matching any of these patterns will be forwarded and wait for the corresponding\ncontinueInterceptedRequest call."
 ([]
  (set-request-interception
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [patterns]}]
  (set-request-interception
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [patterns]}]
  (cmd/command
   connection
   "Network"
   "setRequestInterception"
   params
   {:patterns "patterns"})))

(s/fdef
 set-request-interception
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::patterns]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::patterns])))
 :ret
 (s/keys))

(defn
 set-user-agent-override
 "Allows overriding user agent with the given string.\n\nParameters map keys:\n\n\n  Key                  | Description \n  ---------------------|------------ \n  :user-agent          | User agent to use.\n  :accept-language     | Browser language to emulate. (optional)\n  :platform            | The platform navigator.platform should return. (optional)\n  :user-agent-metadata | To be sent in Sec-CH-UA-* headers and returned in navigator.userAgentData (optional)"
 ([]
  (set-user-agent-override
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys [user-agent accept-language platform user-agent-metadata]}]
  (set-user-agent-override
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys [user-agent accept-language platform user-agent-metadata]}]
  (cmd/command
   connection
   "Network"
   "setUserAgentOverride"
   params
   {:user-agent "userAgent",
    :accept-language "acceptLanguage",
    :platform "platform",
    :user-agent-metadata "userAgentMetadata"})))

(s/fdef
 set-user-agent-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [:user/user-agent]
    :opt-un
    [:user/accept-language :user/platform :user/user-agent-metadata]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/user-agent]
    :opt-un
    [:user/accept-language :user/platform :user/user-agent-metadata])))
 :ret
 (s/keys))

(defn
 stream-resource-content
 "Enables streaming of the response for the given requestId.\nIf enabled, the dataReceived event contains the data that was received during streaming.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | Identifier of the request to stream.\n\nReturn map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :buffered-data | Data that has been buffered until streaming is enabled. (Encoded as a base64 string when passed over JSON)"
 ([]
  (stream-resource-content
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id]}]
  (stream-resource-content
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [request-id]}]
  (cmd/command
   connection
   "Network"
   "streamResourceContent"
   params
   {:request-id "requestId"})))

(s/fdef
 stream-resource-content
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/request-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/request-id])))
 :ret
 (s/keys :req-un [:user/buffered-data]))

(defn
 get-security-isolation-status
 "Returns information about the COEP/COOP isolation status.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :frame-id | If no frameId is provided, the status of the target is provided. (optional)\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :status | null"
 ([]
  (get-security-isolation-status
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [frame-id]}]
  (get-security-isolation-status
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [frame-id]}]
  (cmd/command
   connection
   "Network"
   "getSecurityIsolationStatus"
   params
   {:frame-id "frameId"})))

(s/fdef
 get-security-isolation-status
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :opt-un [:user/frame-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :opt-un [:user/frame-id])))
 :ret
 (s/keys :req-un [:user/status]))

(defn
 enable-reporting-api
 "Enables tracking for the Reporting API, events generated by the Reporting API will now be delivered to the client.\nEnabling triggers 'reportingApiReportAdded' for all existing reports.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :enable | Whether to enable or disable events for the Reporting API"
 ([]
  (enable-reporting-api
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enable]}]
  (enable-reporting-api
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enable]}]
  (cmd/command
   connection
   "Network"
   "enableReportingApi"
   params
   {:enable "enable"})))

(s/fdef
 enable-reporting-api
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/enable]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/enable])))
 :ret
 (s/keys))

(defn
 enable-device-bound-sessions
 "Sets up tracking device bound sessions and fetching of initial set of sessions.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :enable | Whether to enable or disable events."
 ([]
  (enable-device-bound-sessions
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enable]}]
  (enable-device-bound-sessions
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enable]}]
  (cmd/command
   connection
   "Network"
   "enableDeviceBoundSessions"
   params
   {:enable "enable"})))

(s/fdef
 enable-device-bound-sessions
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/enable]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/enable])))
 :ret
 (s/keys))

(defn
 delete-device-bound-session
 "Deletes a device bound session.\n\nParameters map keys:\n\n\n  Key  | Description \n  -----|------------ \n  :key | null"
 ([]
  (delete-device-bound-session
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [key]}]
  (delete-device-bound-session
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [key]}]
  (cmd/command
   connection
   "Network"
   "deleteDeviceBoundSession"
   params
   {:key "key"})))

(s/fdef
 delete-device-bound-session
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/key]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/key])))
 :ret
 (s/keys))

(defn
 fetch-schemeful-site
 "Fetches the schemeful site for a specific origin.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :origin | The URL origin.\n\nReturn map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :schemeful-site | The corresponding schemeful site."
 ([]
  (fetch-schemeful-site
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [origin]}]
  (fetch-schemeful-site
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [origin]}]
  (cmd/command
   connection
   "Network"
   "fetchSchemefulSite"
   params
   {:origin "origin"})))

(s/fdef
 fetch-schemeful-site
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/origin]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/origin])))
 :ret
 (s/keys :req-un [:user/schemeful-site]))

(defn
 load-network-resource
 "Fetches the resource and returns the content.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :frame-id | Frame id to get the resource for. Mandatory for frame targets, and\nshould be omitted for worker targets. (optional)\n  :url      | URL of the resource to get content for.\n  :options  | Options for the request.\n\nReturn map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :resource | null"
 ([]
  (load-network-resource
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [frame-id url options]}]
  (load-network-resource
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [frame-id url options]}]
  (cmd/command
   connection
   "Network"
   "loadNetworkResource"
   params
   {:frame-id "frameId", :url "url", :options "options"})))

(s/fdef
 load-network-resource
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [:user/url :user/options]
    :opt-un
    [:user/frame-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/url :user/options]
    :opt-un
    [:user/frame-id])))
 :ret
 (s/keys :req-un [:user/resource]))

(defn
 set-cookie-controls
 "Sets Controls for third-party cookie access\nPage reload is required before the new cookie behavior will be observed\n\nParameters map keys:\n\n\n  Key                                    | Description \n  ---------------------------------------|------------ \n  :enable-third-party-cookie-restriction | Whether 3pc restriction is enabled."
 ([]
  (set-cookie-controls
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enable-third-party-cookie-restriction]}]
  (set-cookie-controls
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [enable-third-party-cookie-restriction]}]
  (cmd/command
   connection
   "Network"
   "setCookieControls"
   params
   {:enable-third-party-cookie-restriction
    "enableThirdPartyCookieRestriction"})))

(s/fdef
 set-cookie-controls
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [:user/enable-third-party-cookie-restriction]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/enable-third-party-cookie-restriction])))
 :ret
 (s/keys))
