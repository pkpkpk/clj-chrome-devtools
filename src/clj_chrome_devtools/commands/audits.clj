(ns clj-chrome-devtools.commands.audits
  "Audits domain allows investigation of page violations and possible improvements."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::affected-cookie
 (s/keys
  :req-un
  [::name
   ::path
   ::domain]))

(s/def
 ::affected-request
 (s/keys
  :req-un
  [::url]
  :opt-un
  [::request-id]))

(s/def
 ::affected-frame
 (s/keys
  :req-un
  [::frame-id]))

(s/def
 ::cookie-exclusion-reason
 #{"ExcludeDomainNonASCII" "ExcludeSameSiteLax" "ExcludeSameSiteStrict"
   "ExcludeThirdPartyPhaseout" "ExcludeSameSiteNoneInsecure"
   "ExcludeSameSiteUnspecifiedTreatedAsLax"
   "ExcludeThirdPartyCookieBlockedInFirstPartySet"
   "ExcludeSchemeMismatch" "ExcludePortMismatch"})

(s/def
 ::cookie-warning-reason
 #{"WarnAttributeValueExceedsMaxSize" "WarnDomainNonASCII"
   "WarnSameSiteUnspecifiedCrossSiteContext"
   "WarnSameSiteStrictCrossDowngradeLax" "WarnDeprecationTrialMetadata"
   "WarnSameSiteLaxCrossDowngradeLax"
   "WarnSameSiteStrictLaxDowngradeStrict"
   "WarnThirdPartyCookieHeuristic"
   "WarnSameSiteStrictCrossDowngradeStrict"
   "WarnSameSiteLaxCrossDowngradeStrict" "WarnSameSiteNoneInsecure"
   "WarnSameSiteUnspecifiedLaxAllowUnsafe"
   "WarnCrossSiteRedirectDowngradeChangesInclusion"
   "WarnThirdPartyPhaseout"})

(s/def
 ::cookie-operation
 #{"ReadCookie" "SetCookie"})

(s/def
 ::insight-type
 #{"GracePeriod" "GitHubResource" "Heuristics"})

(s/def
 ::cookie-issue-insight
 (s/keys
  :req-un
  [::type]
  :opt-un
  [::table-entry-url]))

(s/def
 ::cookie-issue-details
 (s/keys
  :req-un
  [::cookie-warning-reasons
   ::cookie-exclusion-reasons
   ::operation]
  :opt-un
  [::cookie
   ::raw-cookie-line
   ::site-for-cookies
   ::cookie-url
   ::request
   ::insight]))

(s/def
 ::performance-issue-type
 #{"DocumentCookie"})

(s/def
 ::performance-issue-details
 (s/keys
  :req-un
  [::performance-issue-type]
  :opt-un
  [::source-code-location]))

(s/def
 ::mixed-content-resolution-status
 #{"MixedContentBlocked" "MixedContentAutomaticallyUpgraded"
   "MixedContentWarning"})

(s/def
 ::mixed-content-resource-type
 #{"XSLT" "Script" "XMLHttpRequest" "Video" "Audio" "Prefetch"
   "AttributionSrc" "Manifest" "ServiceWorker" "SpeculationRules"
   "PluginResource" "Form" "SharedWorker" "JSON" "Download" "Font"
   "Import" "Favicon" "Worker" "Track" "Beacon" "Frame" "CSPReport"
   "Ping" "Resource" "Stylesheet" "Image" "EventSource" "PluginData"})

(s/def
 ::mixed-content-issue-details
 (s/keys
  :req-un
  [::resolution-status
   ::insecure-url
   ::main-resource-url]
  :opt-un
  [::resource-type
   ::request
   ::frame]))

(s/def
 ::blocked-by-response-reason
 #{"CoepFrameResourceNeedsCoepHeader" "SRIMessageSignatureMismatch"
   "CorpNotSameSite"
   "CorpNotSameOriginAfterDefaultedToSameOriginByCoep"
   "CorpNotSameOriginAfterDefaultedToSameOriginByDip"
   "CoopSandboxedIFrameCannotNavigateToCoopPage" "CorpNotSameOrigin"
   "CorpNotSameOriginAfterDefaultedToSameOriginByCoepAndDip"})

(s/def
 ::blocked-by-response-issue-details
 (s/keys
  :req-un
  [::request
   ::reason]
  :opt-un
  [::parent-frame
   ::blocked-frame]))

(s/def
 ::heavy-ad-resolution-status
 #{"HeavyAdWarning" "HeavyAdBlocked"})

(s/def
 ::heavy-ad-reason
 #{"CpuTotalLimit" "CpuPeakLimit" "NetworkTotalLimit"})

(s/def
 ::heavy-ad-issue-details
 (s/keys
  :req-un
  [::resolution
   ::reason
   ::frame]))

(s/def
 ::content-security-policy-violation-type
 #{"kURLViolation" "kWasmEvalViolation" "kTrustedTypesPolicyViolation"
   "kSRIViolation" "kTrustedTypesSinkViolation" "kEvalViolation"
   "kInlineViolation"})

(s/def
 ::source-code-location
 (s/keys
  :req-un
  [::url
   ::line-number
   ::column-number]
  :opt-un
  [::script-id]))

(s/def
 ::content-security-policy-issue-details
 (s/keys
  :req-un
  [::violated-directive
   ::is-report-only
   ::content-security-policy-violation-type]
  :opt-un
  [::blocked-url
   ::frame-ancestor
   ::source-code-location
   ::violating-node-id]))

(s/def
 ::shared-array-buffer-issue-type
 #{"CreationIssue" "TransferIssue"})

(s/def
 ::shared-array-buffer-issue-details
 (s/keys
  :req-un
  [::source-code-location
   ::is-warning
   ::type]))

(s/def
 ::cors-issue-details
 (s/keys
  :req-un
  [::cors-error-status
   ::is-warning
   ::request]
  :opt-un
  [::location
   ::initiator-origin
   ::resource-ip-address-space
   ::client-security-state]))

(s/def
 ::attribution-reporting-issue-type
 #{"NoWebOrOsSupport" "NoRegisterSourceHeader"
   "InvalidRegisterOsSourceHeader" "NoRegisterOsTriggerHeader"
   "NavigationRegistrationUniqueScopeAlreadySet"
   "PermissionPolicyDisabled" "InvalidRegisterOsTriggerHeader"
   "TriggerIgnored" "OsSourceIgnored" "UntrustworthyReportingOrigin"
   "OsTriggerIgnored" "SourceAndTriggerHeaders" "InvalidInfoHeader"
   "NoRegisterOsSourceHeader" "SourceIgnored" "NoRegisterTriggerHeader"
   "InsecureContext" "InvalidHeader" "WebAndOsHeaders"
   "InvalidRegisterTriggerHeader"
   "NavigationRegistrationWithoutTransientUserActivation"})

(s/def
 ::shared-dictionary-error
 #{"WriteErrorNonStringMatchField" "WriteErrorDisallowedBySettings"
   "WriteErrorUnsupportedType" "WriteErrorNonTokenTypeField"
   "WriteErrorInsufficientResources" "UseErrorCrossOriginNoCorsRequest"
   "WriteErrorCossOriginNoCorsRequest" "WriteErrorInvalidTTLField"
   "WriteErrorNonStringIdField" "WriteErrorInvalidStructuredHeader"
   "WriteErrorNoMatchField" "WriteErrorRequestAborted"
   "WriteErrorShuttingDown" "WriteErrorExpiredResponse"
   "UseErrorMatchingDictionaryNotUsed"
   "WriteErrorNonListMatchDestField"
   "UseErrorUnexpectedContentDictionaryHeader"
   "WriteErrorNonIntegerTTLField" "WriteErrorInvalidMatchField"
   "WriteErrorNonStringInMatchDestList" "WriteErrorNavigationRequest"
   "UseErrorDictionaryLoadFailure" "WriteErrorNonSecureContext"
   "WriteErrorTooLongIdField" "WriteErrorFeatureDisabled"})

(s/def
 ::sri-message-signature-error
 #{"SignatureInputHeaderKeyIdLength"
   "SignatureHeaderValueIsParameterized"
   "ValidationFailedSignatureMismatch"
   "ValidationFailedIntegrityMismatch"
   "SignatureInputHeaderInvalidComponentName"
   "SignatureInputHeaderMissingRequiredParameters"
   "SignatureInputHeaderInvalidDerivedComponentParameter"
   "SignatureInputHeaderValueMissingComponents"
   "ValidationFailedInvalidLength" "InvalidSignatureHeader"
   "MissingSignatureInputHeader"
   "SignatureHeaderValueIsNotByteSequence"
   "SignatureInputHeaderInvalidComponentType"
   "ValidationFailedSignatureExpired" "MissingSignatureHeader"
   "SignatureHeaderValueIsIncorrectLength"
   "SignatureInputHeaderInvalidHeaderComponentParameter"
   "SignatureInputHeaderValueNotInnerList"
   "SignatureInputHeaderMissingLabel" "InvalidSignatureInputHeader"
   "SignatureInputHeaderInvalidParameter"})

(s/def
 ::unencoded-digest-error
 #{"MalformedDictionary" "UnknownAlgorithm" "IncorrectDigestLength"
   "IncorrectDigestType"})

(s/def
 ::connection-allowlist-error
 #{"ItemNotInnerList" "InvalidUrlPattern" "MoreThanOneList"
   "InvalidAllowlistItemType" "ReportingEndpointNotToken"
   "InvalidHeader"})

(s/def
 ::attribution-reporting-issue-details
 (s/keys
  :req-un
  [::violation-type]
  :opt-un
  [::request
   ::violating-node-id
   ::invalid-parameter]))

(s/def
 ::quirks-mode-issue-details
 (s/keys
  :req-un
  [::is-limited-quirks-mode
   ::document-node-id
   ::url
   ::frame-id
   ::loader-id]))

(s/def
 :user/navigator-user-agent-issue-details
 (s/keys :req-un [:user/url] :opt-un [:user/location]))

(s/def
 :user/shared-dictionary-issue-details
 (s/keys
  :req-un
  [:user/shared-dictionary-error :user/request]))

(s/def
 :user/sri-message-signature-issue-details
 (s/keys
  :req-un
  [:user/error
   :user/signature-base
   :user/integrity-assertions
   :user/request]))

(s/def
 :user/unencoded-digest-issue-details
 (s/keys :req-un [:user/error :user/request]))

(s/def
 :user/connection-allowlist-issue-details
 (s/keys :req-un [:user/error :user/request]))

(s/def
 :user/generic-issue-error-type
 #{"FormLabelForNameError"
   "AutofillAndManualTextPolicyControlledFeaturesInfo"
   "FormDuplicateIdForInputError"
   "FormModelContextMissingToolDescription"
   "AutofillPolicyControlledFeatureInfo"
   "FormInputHasWrongButWellIntendedAutocompleteValueError"
   "FormInputWithNoLabelError"
   "FormModelContextParameterMissingTitleAndDescription"
   "FormAriaLabelledByToNonExistingIdError"
   "FormLabelForMatchesNonExistingIdError"
   "FormModelContextMissingToolName" "ResponseWasBlockedByORB"
   "FormModelContextRequiredParameterMissingName"
   "FormAutocompleteAttributeEmptyError"
   "NavigationEntryMarkedSkippable"
   "FormEmptyIdAndNameAttributesForInputError"
   "ManualTextPolicyControlledFeatureInfo"
   "FormLabelHasNeitherForNorNestedInputError"
   "FormModelContextParameterMissingName"
   "FormInputAssignedAutocompleteValueToIdOrNameAttributeError"})

(s/def
 :user/generic-issue-details
 (s/keys
  :req-un
  [:user/error-type]
  :opt-un
  [:user/frame-id
   :user/violating-node-id
   :user/violating-node-attribute
   :user/request]))

(s/def
 :user/deprecation-issue-details
 (s/keys
  :req-un
  [:user/source-code-location :user/type]
  :opt-un
  [:user/affected-frame]))

(s/def
 :user/bounce-tracking-issue-details
 (s/keys :req-un [:user/tracking-sites]))

(s/def
 :user/cookie-deprecation-metadata-issue-details
 (s/keys
  :req-un
  [:user/allowed-sites
   :user/opt-out-percentage
   :user/is-opt-out-top-level
   :user/operation]))

(s/def
 :user/client-hint-issue-reason
 #{"MetaTagModifiedHTML" "MetaTagAllowListInvalidOrigin"})

(s/def
 :user/federated-auth-request-issue-details
 (s/keys
  :req-un
  [:user/federated-auth-request-issue-reason]))

(s/def
 :user/federated-auth-request-issue-reason
 #{"UiDismissedNoEmbargo" "AccountsInvalidContentType"
   "ConfigNoResponse" "WellKnownNoResponse" "IdTokenInvalidContentType"
   "WellKnownInvalidResponse" "WellKnownListEmpty" "IdTokenNoResponse"
   "TypeNotMatching" "SuppressedBySegmentationPlatform"
   "IdTokenInvalidRequest" "ConfigInvalidContentType"
   "AccountsListEmpty" "DisabledInSettings" "InvalidSigninResponse"
   "ErrorFetchingSignin" "ConfigHttpNotFound" "DisabledInFlags"
   "SilentMediationFailure" "ReplacedByActiveMode"
   "IdTokenCrossSiteIdpErrorResponse" "IdTokenIdpErrorResponse"
   "ConfigNotInWellKnown" "Canceled" "RelyingPartyOriginIsOpaque"
   "NotSignedInWithIdp" "AccountsHttpNotFound" "TooManyRequests"
   "IdTokenHttpNotFound" "ConfigInvalidResponse" "WellKnownTooBig"
   "AccountsNoResponse" "WellKnownHttpNotFound" "ShouldEmbargo"
   "CorsError" "AccountsInvalidResponse" "ErrorIdToken"
   "RpPageNotVisible" "IdTokenInvalidResponse"
   "MissingTransientUserActivation" "IdpNotPotentiallyTrustworthy"
   "WellKnownInvalidContentType"})

(s/def
 :user/federated-auth-user-info-request-issue-details
 (s/keys
  :req-un
  [:user/federated-auth-user-info-request-issue-reason]))

(s/def
 :user/federated-auth-user-info-request-issue-reason
 #{"NotPotentiallyTrustworthy" "InvalidAccountsResponse"
   "NoReturningUserFromFetchedAccounts" "NotIframe" "NoApiPermission"
   "InvalidConfigOrWellKnown" "NotSignedInWithIdp"
   "NoAccountSharingPermission" "NotSameOrigin"})

(s/def
 :user/client-hint-issue-details
 (s/keys
  :req-un
  [:user/source-code-location :user/client-hint-issue-reason]))

(s/def
 :user/failed-request-info
 (s/keys
  :req-un
  [:user/url :user/failure-message]
  :opt-un
  [:user/request-id]))

(s/def
 :user/partitioning-blob-url-info
 #{"EnforceNoopenerForNavigation" "BlockedCrossPartitionFetching"})

(s/def
 :user/partitioning-blob-url-issue-details
 (s/keys
  :req-un
  [:user/url :user/partitioning-blob-url-info]))

(s/def
 :user/element-accessibility-issue-reason
 #{"DisallowedOptGroupChild" "InteractiveContentLegendChild"
   "NonPhrasingContentOptionChild"
   "InteractiveContentSummaryDescendant"
   "InteractiveContentOptionChild" "DisallowedSelectChild"})

(s/def
 :user/element-accessibility-issue-details
 (s/keys
  :req-un
  [:user/node-id
   :user/element-accessibility-issue-reason
   :user/has-disallowed-attributes]))

(s/def
 :user/style-sheet-loading-issue-reason
 #{"RequestFailed" "LateImportRule"})

(s/def
 :user/stylesheet-loading-issue-details
 (s/keys
  :req-un
  [:user/source-code-location :user/style-sheet-loading-issue-reason]
  :opt-un
  [:user/failed-request-info]))

(s/def
 :user/property-rule-issue-reason
 #{"InvalidInherits" "InvalidSyntax" "InvalidInitialValue"
   "InvalidName"})

(s/def
 :user/property-rule-issue-details
 (s/keys
  :req-un
  [:user/source-code-location :user/property-rule-issue-reason]
  :opt-un
  [:user/property-value]))

(s/def
 :user/user-reidentification-issue-type
 #{"BlockedFrameNavigation" "BlockedSubresource"
   "NoisedCanvasReadback"})

(s/def
 :user/user-reidentification-issue-details
 (s/keys
  :req-un
  [:user/type]
  :opt-un
  [:user/request :user/source-code-location]))

(s/def
 :user/permission-element-issue-type
 #{"ActivationDisabled" "NonOpaqueColor" "PaddingRightUnsupported"
   "PaddingBottomUnsupported" "GeolocationDeprecated" "LowContrast"
   "CspFrameAncestorsMissing" "PermissionsPolicyBlocked"
   "InvalidDisplayStyle" "SecurityChecksFailed" "RequestInProgress"
   "FontSizeTooSmall" "FencedFrameDisallowed" "RegistrationFailed"
   "TypeNotSupported" "InvalidTypeActivation" "UntrustedEvent"
   "InsetBoxShadowUnsupported" "FontSizeTooLarge" "InvalidSizeValue"
   "InvalidType"})

(s/def
 :user/permission-element-issue-details
 (s/keys
  :req-un
  [:user/issue-type]
  :opt-un
  [:user/type
   :user/node-id
   :user/is-warning
   :user/permission-name
   :user/occluder-node-info
   :user/occluder-parent-node-info
   :user/disable-reason]))

(s/def
 :user/selective-permissions-intervention-issue-details
 (s/keys
  :req-un
  [:user/api-name :user/ad-ancestry]
  :opt-un
  [:user/stack-trace]))

(s/def
 :user/inspector-issue-code
 #{"PerformanceIssue" "AttributionReportingIssue"
   "UnencodedDigestIssue" "BlockedByResponseIssue"
   "UserReidentificationIssue" "MixedContentIssue"
   "BounceTrackingIssue" "ElementAccessibilityIssue" "ClientHintIssue"
   "SharedArrayBufferIssue" "SharedDictionaryIssue"
   "FederatedAuthRequestIssue" "ContentSecurityPolicyIssue"
   "DeprecationIssue" "GenericIssue" "SRIMessageSignatureIssue"
   "FederatedAuthUserInfoRequestIssue"
   "SelectivePermissionsInterventionIssue" "PropertyRuleIssue"
   "StylesheetLoadingIssue" "CorsIssue" "NavigatorUserAgentIssue"
   "ConnectionAllowlistIssue" "PermissionElementIssue" "CookieIssue"
   "QuirksModeIssue" "HeavyAdIssue" "PartitioningBlobURLIssue"
   "CookieDeprecationMetadataIssue"})

(s/def
 :user/inspector-issue-details
 (s/keys
  :opt-un
  [:user/cookie-issue-details
   :user/mixed-content-issue-details
   :user/blocked-by-response-issue-details
   :user/heavy-ad-issue-details
   :user/content-security-policy-issue-details
   :user/shared-array-buffer-issue-details
   :user/cors-issue-details
   :user/attribution-reporting-issue-details
   :user/quirks-mode-issue-details
   :user/partitioning-blob-url-issue-details
   :user/navigator-user-agent-issue-details
   :user/generic-issue-details
   :user/deprecation-issue-details
   :user/client-hint-issue-details
   :user/federated-auth-request-issue-details
   :user/bounce-tracking-issue-details
   :user/cookie-deprecation-metadata-issue-details
   :user/stylesheet-loading-issue-details
   :user/property-rule-issue-details
   :user/federated-auth-user-info-request-issue-details
   :user/shared-dictionary-issue-details
   :user/element-accessibility-issue-details
   :user/sri-message-signature-issue-details
   :user/unencoded-digest-issue-details
   :user/connection-allowlist-issue-details
   :user/user-reidentification-issue-details
   :user/permission-element-issue-details
   :user/performance-issue-details
   :user/selective-permissions-intervention-issue-details]))

(s/def :user/issue-id string?)

(s/def
 :user/inspector-issue
 (s/keys
  :req-un
  [:user/code :user/details]
  :opt-un
  [:user/issue-id]))
(defn
 get-encoded-response
 "Returns the response body and size if it were re-encoded with the specified settings. Only\napplies to images.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :request-id | Identifier of the network request to get content for.\n  :encoding   | The encoding to use.\n  :quality    | The quality of the encoding (0-1). (defaults to 1) (optional)\n  :size-only  | Whether to only return the size information (defaults to false). (optional)\n\nReturn map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :body          | The encoded body as a base64 string. Omitted if sizeOnly is true. (Encoded as a base64 string when passed over JSON) (optional)\n  :original-size | Size before re-encoding.\n  :encoded-size  | Size after re-encoding."
 ([]
  (get-encoded-response
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [request-id encoding quality size-only]}]
  (get-encoded-response
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [request-id encoding quality size-only]}]
  (cmd/command
   connection
   "Audits"
   "getEncodedResponse"
   params
   {:request-id "requestId",
    :encoding "encoding",
    :quality "quality",
    :size-only "sizeOnly"})))

(s/fdef
 get-encoded-response
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
     ::encoding]
    :opt-un
    [::quality
     ::size-only]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::request-id
     ::encoding]
    :opt-un
    [::quality
     ::size-only])))
 :ret
 (s/keys
  :req-un
  [::original-size
   ::encoded-size]
  :opt-un
  [::body]))

(defn
 disable
 "Disables issues domain, prevents further issues from being reported to the client."
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
   "Audits"
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
 enable
 "Enables issues domain, sends the issues collected so far to the client by means of the\n`issueAdded` event."
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
   "Audits"
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
 check-forms-issues
 "Runs the form issues check for the target page. Found issues are reported\nusing Audits.issueAdded event.\n\nReturn map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :form-issues | null"
 ([]
  (check-forms-issues
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (check-forms-issues
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Audits"
   "checkFormsIssues"
   params
   {})))

(s/fdef
 check-forms-issues
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
  [::form-issues]))
