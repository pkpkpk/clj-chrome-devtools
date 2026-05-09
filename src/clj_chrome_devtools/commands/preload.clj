(ns clj-chrome-devtools.commands.preload
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::rule-set-id
 string?)

(s/def
 ::rule-set
 (s/keys
  :req-un
  [::id
   ::loader-id
   ::source-text]
  :opt-un
  [::backend-node-id
   ::url
   ::request-id
   ::error-type
   ::error-message
   ::tag]))

(s/def
 ::rule-set-error-type
 #{"SourceIsNotJsonObject" "InvalidRulesetLevelTag"
   "InvalidRulesSkipped"})

(s/def
 ::speculation-action
 #{"Prerender" "Prefetch" "PrerenderUntilScript"})

(s/def
 ::speculation-target-hint
 #{"Blank" "Self"})

(s/def
 ::preloading-attempt-key
 (s/keys
  :req-un
  [::loader-id
   ::action
   ::url]
  :opt-un
  [::form-submission
   ::target-hint]))

(s/def
 ::preloading-attempt-source
 (s/keys
  :req-un
  [::key
   ::rule-set-ids
   ::node-ids]))

(s/def
 ::preload-pipeline-id
 string?)

(s/def
 ::prerender-final-status
 #{"LowEndDevice" "ActivationUrlHasEffectiveUrl"
   "SameSiteCrossOriginNavigationNotOptInInInitialNavigation"
   "BatterySaverEnabled" "MemoryPressureAfterTriggered"
   "ActivatedWithAuxiliaryBrowsingContexts" "ClientCertRequested"
   "V8OptimizerDisabled" "TabClosedWithoutUserGesture"
   "MaxNumOfRunningEmbedderPrerendersExceeded"
   "PrerenderFailedDuringPrefetch" "DataSaverEnabled" "BlockedByClient"
   "SpeculationRuleRemoved" "NavigationNotCommitted"
   "PrerenderHostReused" "ActivationNavigationParameterMismatch"
   "TriggerUrlHasEffectiveUrl"
   "SameSiteCrossOriginRedirectNotOptInInInitialNavigation"
   "ActivatedInBackground" "CrossSiteNavigationInMainFrameNavigation"
   "AudioOutputDeviceRequested" "MixedContent"
   "MaxNumOfRunningNonEagerPrerendersExceeded"
   "NavigationRequestNetworkError" "TriggerBackgrounded"
   "RedirectedPrerenderingUrlHasEffectiveUrl" "SlowNetwork"
   "DidFailLoad" "JavaScriptInterfaceAdded" "AllPrerenderingCanceled"
   "SameSiteCrossOriginRedirectNotOptInInMainFrameNavigation"
   "WindowClosed" "TriggerDestroyed"
   "ActivationFramePolicyNotCompatible"
   "PrimaryMainFrameRendererProcessKilled"
   "PrimaryMainFrameRendererProcessCrashed"
   "ActivatedDuringMainFrameNavigation" "MemoryLimitExceeded"
   "BrowsingDataRemoved" "CancelAllHostsForTesting"
   "OtherPrerenderedPageActivated" "Download" "InactivePageRestriction"
   "PreloadingUnsupportedByWebContents" "Destroyed"
   "ActivationNavigationDestroyedBeforeSuccess" "SslCertificateError"
   "RendererProcessCrashed" "NavigationBadHttpStatus"
   "EmbedderHostDisallowed" "TabClosedByUserGesture"
   "MaxNumOfRunningEagerPrerendersExceeded" "InvalidSchemeRedirect"
   "SameSiteCrossOriginNavigationNotOptInInMainFrameNavigation"
   "MemoryPressureOnTrigger" "RendererProcessKilled"
   "FormSubmitWhenPrerendering" "PreloadingDisabled" "MojoBinderPolicy"
   "JavaScriptInterfaceRemoved" "UaChangeRequiresReload"
   "CrossSiteRedirectInInitialNavigation" "InvalidSchemeNavigation"
   "PrerenderingDisabledByDevTools"
   "CrossSiteRedirectInMainFrameNavigation" "ActivatedBeforeStarted"
   "Activated" "StartFailed" "LoginAuthRequested"
   "PrerenderingUrlHasEffectiveUrl" "Stop"
   "CrossSiteNavigationInInitialNavigation" "TimeoutBackgrounded"
   "NavigationRequestBlockedByCsp"})

(s/def
 ::preloading-status
 #{"NotSupported" "Ready" "Success" "Failure" "Running" "Pending"})

(s/def
 ::prefetch-status
 #{"PrefetchFailedIneligibleRedirect"
   "PrefetchNotEligibleBrowserContextOffTheRecord" "PrefetchNotStarted"
   "PrefetchNotFinishedInTime" "PrefetchSuccessfulButNotUsed"
   "PrefetchFailedNetError" "PrefetchNotEligibleDataSaverEnabled"
   "PrefetchIsPrivacyDecoy" "PrefetchNotEligibleExistingProxy"
   "PrefetchFailedInvalidRedirect" "PrefetchIneligibleRetryAfter"
   "PrefetchEvictedAfterBrowsingDataRemoved"
   "PrefetchNotUsedProbeFailed"
   "PrefetchNotEligibleSameSiteCrossOriginPrefetchRequiredProxy"
   "PrefetchResponseUsed" "PrefetchFailedNon2XX"
   "PrefetchNotEligibleNonDefaultStoragePartition"
   "PrefetchNotEligibleUserHasCookies" "PrefetchHeldback"
   "PrefetchEvictedAfterCandidateRemoved"
   "PrefetchEvictedForNewerPrefetch" "PrefetchProxyNotAvailable"
   "PrefetchNotEligibleBatterySaverEnabled"
   "PrefetchNotEligibleUserHasServiceWorkerNoFetchHandler"
   "PrefetchAllowed" "PrefetchNotEligibleUserHasServiceWorker"
   "PrefetchNotEligibleRedirectToServiceWorker"
   "PrefetchNotEligibleHostIsNonUnique"
   "PrefetchNotEligibleSchemeIsNotHttps"
   "PrefetchNotEligibleRedirectFromServiceWorker"
   "PrefetchNotEligiblePreloadingDisabled"
   "PrefetchNotUsedCookiesChanged" "PrefetchFailedMIMENotSupported"
   "PrefetchIsStale"})

(s/def
 ::prerender-mismatched-headers
 (s/keys
  :req-un
  [::header-name]
  :opt-un
  [::initial-value
   ::activation-value]))
(defn
 enable
 ""
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
   "Preload"
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
 ""
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
   "Preload"
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
