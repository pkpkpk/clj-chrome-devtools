(ns clj-chrome-devtools.commands.pwa
  "This domain allows interacting with the browser to control PWAs."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::file-handler-accept
 (s/keys
  :req-un
  [::media-type
   ::file-extensions]))

(s/def
 ::file-handler
 (s/keys
  :req-un
  [::action
   ::accepts
   ::display-name]))

(s/def
 ::display-mode
 #{"browser" "standalone"})
(defn
 get-os-app-state
 "Returns the following OS state for the given manifest id.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :manifest-id | The id from the webapp's manifest file, commonly it's the url of the\nsite installing the webapp. See\nhttps://web.dev/learn/pwa/web-app-manifest.\n\nReturn map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :badge-count   | null\n  :file-handlers | null"
 ([]
  (get-os-app-state
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [manifest-id]}]
  (get-os-app-state
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [manifest-id]}]
  (cmd/command
   connection
   "PWA"
   "getOsAppState"
   params
   {:manifest-id "manifestId"})))

(s/fdef
 get-os-app-state
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::manifest-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::manifest-id])))
 :ret
 (s/keys
  :req-un
  [::badge-count
   ::file-handlers]))

(defn
 install
 "Installs the given manifest identity, optionally using the given installUrlOrBundleUrl\n\nIWA-specific install description:\nmanifestId corresponds to isolated-app:// + web_package::SignedWebBundleId\n\nFile installation mode:\nThe installUrlOrBundleUrl can be either file:// or http(s):// pointing\nto a signed web bundle (.swbn). In this case SignedWebBundleId must correspond to\nThe .swbn file's signing key.\n\nDev proxy installation mode:\ninstallUrlOrBundleUrl must be http(s):// that serves dev mode IWA.\nweb_package::SignedWebBundleId must be of type dev proxy.\n\nThe advantage of dev proxy mode is that all changes to IWA\nautomatically will be reflected in the running app without\nreinstallation.\n\nTo generate bundle id for proxy mode:\n1. Generate 32 random bytes.\n2. Add a specific suffix at the end following the documentation\n   https://github.com/WICG/isolated-web-apps/blob/main/Scheme.md#suffix\n3. Encode the entire sequence using Base32 without padding.\n\nIf Chrome is not in IWA dev\nmode, the installation will fail, regardless of the state of the allowlist.\n\nParameters map keys:\n\n\n  Key                        | Description \n  ---------------------------|------------ \n  :manifest-id               | null\n  :install-url-or-bundle-url | The location of the app or bundle overriding the one derived from the\nmanifestId. (optional)"
 ([]
  (install
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [manifest-id install-url-or-bundle-url]}]
  (install
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [manifest-id install-url-or-bundle-url]}]
  (cmd/command
   connection
   "PWA"
   "install"
   params
   {:manifest-id "manifestId",
    :install-url-or-bundle-url "installUrlOrBundleUrl"})))

(s/fdef
 install
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::manifest-id]
    :opt-un
    [::install-url-or-bundle-url]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::manifest-id]
    :opt-un
    [::install-url-or-bundle-url])))
 :ret
 (s/keys))

(defn
 uninstall
 "Uninstalls the given manifest_id and closes any opened app windows.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :manifest-id | null"
 ([]
  (uninstall
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [manifest-id]}]
  (uninstall
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [manifest-id]}]
  (cmd/command
   connection
   "PWA"
   "uninstall"
   params
   {:manifest-id "manifestId"})))

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
    [::manifest-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::manifest-id])))
 :ret
 (s/keys))

(defn
 launch
 "Launches the installed web app, or an url in the same web app instead of the\ndefault start url if it is provided. Returns a page Target.TargetID which\ncan be used to attach to via Target.attachToTarget or similar APIs.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :manifest-id | null\n  :url         | null (optional)\n\nReturn map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :target-id | ID of the tab target created as a result."
 ([]
  (launch
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [manifest-id url]}]
  (launch
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [manifest-id url]}]
  (cmd/command
   connection
   "PWA"
   "launch"
   params
   {:manifest-id "manifestId", :url "url"})))

(s/fdef
 launch
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::manifest-id]
    :opt-un
    [::url]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::manifest-id]
    :opt-un
    [::url])))
 :ret
 (s/keys
  :req-un
  [::target-id]))

(defn
 launch-files-in-app
 "Opens one or more local files from an installed web app identified by its\nmanifestId. The web app needs to have file handlers registered to process\nthe files. The API returns one or more page Target.TargetIDs which can be\nused to attach to via Target.attachToTarget or similar APIs.\nIf some files in the parameters cannot be handled by the web app, they will\nbe ignored. If none of the files can be handled, this API returns an error.\nIf no files are provided as the parameter, this API also returns an error.\n\nAccording to the definition of the file handlers in the manifest file, one\nTarget.TargetID may represent a page handling one or more files. The order\nof the returned Target.TargetIDs is not guaranteed.\n\nTODO(crbug.com/339454034): Check the existences of the input files.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :manifest-id | null\n  :files       | null\n\nReturn map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :target-ids | IDs of the tab targets created as the result."
 ([]
  (launch-files-in-app
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [manifest-id files]}]
  (launch-files-in-app
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [manifest-id files]}]
  (cmd/command
   connection
   "PWA"
   "launchFilesInApp"
   params
   {:manifest-id "manifestId", :files "files"})))

(s/fdef
 launch-files-in-app
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::manifest-id
     ::files]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::manifest-id
     ::files])))
 :ret
 (s/keys
  :req-un
  [::target-ids]))

(defn
 open-current-page-in-app
 "Opens the current page in its web app identified by the manifest id, needs\nto be called on a page target. This function returns immediately without\nwaiting for the app to finish loading.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :manifest-id | null"
 ([]
  (open-current-page-in-app
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [manifest-id]}]
  (open-current-page-in-app
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [manifest-id]}]
  (cmd/command
   connection
   "PWA"
   "openCurrentPageInApp"
   params
   {:manifest-id "manifestId"})))

(s/fdef
 open-current-page-in-app
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::manifest-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::manifest-id])))
 :ret
 (s/keys))

(defn
 change-app-user-settings
 "Changes user settings of the web app identified by its manifestId. If the\napp was not installed, this command returns an error. Unset parameters will\nbe ignored; unrecognized values will cause an error.\n\nUnlike the ones defined in the manifest files of the web apps, these\nsettings are provided by the browser and controlled by the users, they\nimpact the way the browser handling the web apps.\n\nSee the comment of each parameter.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :manifest-id    | null\n  :link-capturing | If user allows the links clicked on by the user in the app's scope, or\nextended scope if the manifest has scope extensions and the flags\n`DesktopPWAsLinkCapturingWithScopeExtensions` and\n`WebAppEnableScopeExtensions` are enabled.\n\nNote, the API does not support resetting the linkCapturing to the\ninitial value, uninstalling and installing the web app again will reset\nit.\n\nTODO(crbug.com/339453269): Setting this value on ChromeOS is not\nsupported yet. (optional)\n  :display-mode   | null (optional)"
 ([]
  (change-app-user-settings
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [manifest-id link-capturing display-mode]}]
  (change-app-user-settings
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [manifest-id link-capturing display-mode]}]
  (cmd/command
   connection
   "PWA"
   "changeAppUserSettings"
   params
   {:manifest-id "manifestId",
    :link-capturing "linkCapturing",
    :display-mode "displayMode"})))

(s/fdef
 change-app-user-settings
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::manifest-id]
    :opt-un
    [::link-capturing
     ::display-mode]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::manifest-id]
    :opt-un
    [::link-capturing
     ::display-mode])))
 :ret
 (s/keys))
