(ns clj-chrome-devtools.commands.emulation
  "This domain emulates different environments for the page."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::safe-area-insets
 (s/keys
  :opt-un
  [::top
   ::top-max
   ::left
   ::left-max
   ::bottom
   ::bottom-max
   ::right
   ::right-max]))

(s/def
 ::screen-orientation
 (s/keys
  :req-un
  [::type
   ::angle]))

(s/def
 ::display-feature
 (s/keys
  :req-un
  [::orientation
   ::offset
   ::mask-length]))

(s/def
 ::device-posture
 (s/keys
  :req-un
  [::type]))

(s/def
 ::media-feature
 (s/keys
  :req-un
  [::name
   ::value]))

(s/def
 ::virtual-time-policy
 #{"advance" "pauseIfNetworkFetchesPending" "pause"})

(s/def
 ::user-agent-brand-version
 (s/keys
  :req-un
  [::brand
   ::version]))

(s/def
 ::user-agent-metadata
 (s/keys
  :req-un
  [::platform
   ::platform-version
   ::architecture
   ::model
   ::mobile]
  :opt-un
  [::brands
   ::full-version-list
   ::full-version
   ::bitness
   ::wow64
   ::form-factors]))

(s/def
 ::sensor-type
 #{"magnetometer" "ambient-light" "gyroscope" "gravity"
   "linear-acceleration" "absolute-orientation" "relative-orientation"
   "accelerometer"})

(s/def
 ::sensor-metadata
 (s/keys
  :opt-un
  [::available
   ::minimum-frequency
   ::maximum-frequency]))

(s/def
 ::sensor-reading-single
 (s/keys
  :req-un
  [::value]))

(s/def
 ::sensor-reading-xyz
 (s/keys
  :req-un
  [::x
   ::y
   ::z]))

(s/def
 ::sensor-reading-quaternion
 (s/keys
  :req-un
  [::x
   ::y
   ::z
   ::w]))

(s/def
 ::sensor-reading
 (s/keys
  :opt-un
  [::single
   ::xyz
   ::quaternion]))

(s/def
 ::pressure-source
 #{"cpu"})

(s/def
 ::pressure-state
 #{"serious" "critical" "fair" "nominal"})

(s/def
 ::pressure-metadata
 (s/keys
  :opt-un
  [::available]))

(s/def
 ::work-area-insets
 (s/keys
  :opt-un
  [::top
   ::left
   ::bottom
   ::right]))

(s/def
 ::screen-id
 string?)

(s/def
 ::screen-info
 (s/keys
  :req-un
  [::left
   ::top
   ::width
   ::height
   ::avail-left
   ::avail-top
   ::avail-width
   ::avail-height
   ::device-pixel-ratio
   ::orientation
   ::color-depth
   ::is-extended
   ::is-internal
   ::is-primary
   ::label
   ::id]))

(s/def
 ::disabled-image-type
 #{"jxl" "webp" "avif"})
(defn
 can-emulate
 "Tells whether emulation is supported.\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :result | True if emulation is supported."
 ([]
  (can-emulate
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (can-emulate
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Emulation"
   "canEmulate"
   params
   {})))

(s/fdef
 can-emulate
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
 clear-device-metrics-override
 "Clears the overridden device metrics."
 ([]
  (clear-device-metrics-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (clear-device-metrics-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Emulation"
   "clearDeviceMetricsOverride"
   params
   {})))

(s/fdef
 clear-device-metrics-override
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
 clear-geolocation-override
 "Clears the overridden Geolocation Position and Error."
 ([]
  (clear-geolocation-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (clear-geolocation-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Emulation"
   "clearGeolocationOverride"
   params
   {})))

(s/fdef
 clear-geolocation-override
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
 reset-page-scale-factor
 "Requests that page scale factor is reset to initial values."
 ([]
  (reset-page-scale-factor
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (reset-page-scale-factor
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Emulation"
   "resetPageScaleFactor"
   params
   {})))

(s/fdef
 reset-page-scale-factor
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
 set-focus-emulation-enabled
 "Enables or disables simulating a focused and active page.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :enabled | Whether to enable to disable focus emulation."
 ([]
  (set-focus-emulation-enabled
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enabled]}]
  (set-focus-emulation-enabled
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enabled]}]
  (cmd/command
   connection
   "Emulation"
   "setFocusEmulationEnabled"
   params
   {:enabled "enabled"})))

(s/fdef
 set-focus-emulation-enabled
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
 set-auto-dark-mode-override
 "Automatically render all web contents using a dark theme.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :enabled | Whether to enable or disable automatic dark mode.\nIf not specified, any existing override will be cleared. (optional)"
 ([]
  (set-auto-dark-mode-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enabled]}]
  (set-auto-dark-mode-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enabled]}]
  (cmd/command
   connection
   "Emulation"
   "setAutoDarkModeOverride"
   params
   {:enabled "enabled"})))

(s/fdef
 set-auto-dark-mode-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::enabled]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::enabled])))
 :ret
 (s/keys))

(defn
 set-cpu-throttling-rate
 "Enables CPU throttling to emulate slow CPUs.\n\nParameters map keys:\n\n\n  Key   | Description \n  ------|------------ \n  :rate | Throttling rate as a slowdown factor (1 is no throttle, 2 is 2x slowdown, etc)."
 ([]
  (set-cpu-throttling-rate
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [rate]}]
  (set-cpu-throttling-rate
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [rate]}]
  (cmd/command
   connection
   "Emulation"
   "setCPUThrottlingRate"
   params
   {:rate "rate"})))

(s/fdef
 set-cpu-throttling-rate
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::rate]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::rate])))
 :ret
 (s/keys))

(defn
 set-default-background-color-override
 "Sets or clears an override of the default background color of the frame. This override is used\nif the content does not specify one.\n\nParameters map keys:\n\n\n  Key    | Description \n  -------|------------ \n  :color | RGBA of the default background color. If not specified, any existing override will be\ncleared. (optional)"
 ([]
  (set-default-background-color-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [color]}]
  (set-default-background-color-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [color]}]
  (cmd/command
   connection
   "Emulation"
   "setDefaultBackgroundColorOverride"
   params
   {:color "color"})))

(s/fdef
 set-default-background-color-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::color]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::color])))
 :ret
 (s/keys))

(defn
 set-safe-area-insets-override
 "Overrides the values for env(safe-area-inset-*) and env(safe-area-max-inset-*). Unset values will cause the\nrespective variables to be undefined, even if previously overridden.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :insets | null"
 ([]
  (set-safe-area-insets-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [insets]}]
  (set-safe-area-insets-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [insets]}]
  (cmd/command
   connection
   "Emulation"
   "setSafeAreaInsetsOverride"
   params
   {:insets "insets"})))

(s/fdef
 set-safe-area-insets-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::insets]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::insets])))
 :ret
 (s/keys))

(defn
 set-device-metrics-override
 "Overrides the values of device screen dimensions (window.screen.width, window.screen.height,\nwindow.innerWidth, window.innerHeight, and \"device-width\"/\"device-height\"-related CSS media\nquery results).\n\nParameters map keys:\n\n\n  Key                                | Description \n  -----------------------------------|------------ \n  :width                             | Overriding width value in pixels (minimum 0, maximum 10000000). 0 disables the override.\n  :height                            | Overriding height value in pixels (minimum 0, maximum 10000000). 0 disables the override.\n  :device-scale-factor               | Overriding device scale factor value. 0 disables the override.\n  :mobile                            | Whether to emulate mobile device. This includes viewport meta tag, overlay scrollbars, text\nautosizing and more.\n  :scale                             | Scale to apply to resulting view image. (optional)\n  :screen-width                      | Overriding screen width value in pixels (minimum 0, maximum 10000000). (optional)\n  :screen-height                     | Overriding screen height value in pixels (minimum 0, maximum 10000000). (optional)\n  :position-x                        | Overriding view X position on screen in pixels (minimum 0, maximum 10000000). (optional)\n  :position-y                        | Overriding view Y position on screen in pixels (minimum 0, maximum 10000000). (optional)\n  :dont-set-visible-size             | Do not set visible view size, rely upon explicit setVisibleSize call. (optional)\n  :screen-orientation                | Screen orientation override. (optional)\n  :viewport                          | If set, the visible area of the page will be overridden to this viewport. This viewport\nchange is not observed by the page, e.g. viewport-relative elements do not change positions. (optional)\n  :display-feature                   | If set, the display feature of a multi-segment screen. If not set, multi-segment support\nis turned-off.\nDeprecated, use Emulation.setDisplayFeaturesOverride. (optional)\n  :device-posture                    | If set, the posture of a foldable device. If not set the posture is set\nto continuous.\nDeprecated, use Emulation.setDevicePostureOverride. (optional)\n  :scrollbar-type                    | Scrollbar type. Default: `default`. (optional)\n  :screen-orientation-lock-emulation | If set to true, enables screen orientation lock emulation, which\nintercepts screen.orientation.lock() calls from the page and reports\norientation changes via screenOrientationLockChanged events. This is\nuseful for emulating mobile device orientation lock behavior in\nresponsive design mode. (optional)"
 ([]
  (set-device-metrics-override
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [width
     height
     device-scale-factor
     mobile
     scale
     screen-width
     screen-height
     position-x
     position-y
     dont-set-visible-size
     screen-orientation
     viewport
     display-feature
     device-posture
     scrollbar-type
     screen-orientation-lock-emulation]}]
  (set-device-metrics-override
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [width
     height
     device-scale-factor
     mobile
     scale
     screen-width
     screen-height
     position-x
     position-y
     dont-set-visible-size
     screen-orientation
     viewport
     display-feature
     device-posture
     scrollbar-type
     screen-orientation-lock-emulation]}]
  (cmd/command
   connection
   "Emulation"
   "setDeviceMetricsOverride"
   params
   {:display-feature "displayFeature",
    :dont-set-visible-size "dontSetVisibleSize",
    :device-scale-factor "deviceScaleFactor",
    :screen-orientation "screenOrientation",
    :scale "scale",
    :width "width",
    :position-y "positionY",
    :position-x "positionX",
    :screen-orientation-lock-emulation
    "screenOrientationLockEmulation",
    :scrollbar-type "scrollbarType",
    :screen-height "screenHeight",
    :mobile "mobile",
    :viewport "viewport",
    :device-posture "devicePosture",
    :height "height",
    :screen-width "screenWidth"})))

(s/fdef
 set-device-metrics-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::width
     ::height
     ::device-scale-factor
     ::mobile]
    :opt-un
    [::scale
     ::screen-width
     ::screen-height
     ::position-x
     ::position-y
     ::dont-set-visible-size
     ::screen-orientation
     ::viewport
     ::display-feature
     ::device-posture
     ::scrollbar-type
     ::screen-orientation-lock-emulation]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::width
     ::height
     ::device-scale-factor
     ::mobile]
    :opt-un
    [::scale
     ::screen-width
     ::screen-height
     ::position-x
     ::position-y
     ::dont-set-visible-size
     ::screen-orientation
     ::viewport
     ::display-feature
     ::device-posture
     ::scrollbar-type
     ::screen-orientation-lock-emulation])))
 :ret
 (s/keys))

(defn
 set-device-posture-override
 "Start reporting the given posture value to the Device Posture API.\nThis override can also be set in setDeviceMetricsOverride().\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :posture | null"
 ([]
  (set-device-posture-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [posture]}]
  (set-device-posture-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [posture]}]
  (cmd/command
   connection
   "Emulation"
   "setDevicePostureOverride"
   params
   {:posture "posture"})))

(s/fdef
 set-device-posture-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::posture]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::posture])))
 :ret
 (s/keys))

(defn
 clear-device-posture-override
 "Clears a device posture override set with either setDeviceMetricsOverride()\nor setDevicePostureOverride() and starts using posture information from the\nplatform again.\nDoes nothing if no override is set."
 ([]
  (clear-device-posture-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (clear-device-posture-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Emulation"
   "clearDevicePostureOverride"
   params
   {})))

(s/fdef
 clear-device-posture-override
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
 set-display-features-override
 "Start using the given display features to pupulate the Viewport Segments API.\nThis override can also be set in setDeviceMetricsOverride().\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :features | null"
 ([]
  (set-display-features-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [features]}]
  (set-display-features-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [features]}]
  (cmd/command
   connection
   "Emulation"
   "setDisplayFeaturesOverride"
   params
   {:features "features"})))

(s/fdef
 set-display-features-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::features]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::features])))
 :ret
 (s/keys))

(defn
 clear-display-features-override
 "Clears the display features override set with either setDeviceMetricsOverride()\nor setDisplayFeaturesOverride() and starts using display features from the\nplatform again.\nDoes nothing if no override is set."
 ([]
  (clear-display-features-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (clear-display-features-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Emulation"
   "clearDisplayFeaturesOverride"
   params
   {})))

(s/fdef
 clear-display-features-override
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
 set-scrollbars-hidden
 "\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :hidden | Whether scrollbars should be always hidden."
 ([]
  (set-scrollbars-hidden
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [hidden]}]
  (set-scrollbars-hidden
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [hidden]}]
  (cmd/command
   connection
   "Emulation"
   "setScrollbarsHidden"
   params
   {:hidden "hidden"})))

(s/fdef
 set-scrollbars-hidden
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::hidden]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::hidden])))
 :ret
 (s/keys))

(defn
 set-document-cookie-disabled
 "\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :disabled | Whether document.coookie API should be disabled."
 ([]
  (set-document-cookie-disabled
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [disabled]}]
  (set-document-cookie-disabled
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [disabled]}]
  (cmd/command
   connection
   "Emulation"
   "setDocumentCookieDisabled"
   params
   {:disabled "disabled"})))

(s/fdef
 set-document-cookie-disabled
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::disabled]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::disabled])))
 :ret
 (s/keys))

(defn
 set-emit-touch-events-for-mouse
 "\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :enabled       | Whether touch emulation based on mouse input should be enabled.\n  :configuration | Touch/gesture events configuration. Default: current platform. (optional)"
 ([]
  (set-emit-touch-events-for-mouse
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enabled configuration]}]
  (set-emit-touch-events-for-mouse
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enabled configuration]}]
  (cmd/command
   connection
   "Emulation"
   "setEmitTouchEventsForMouse"
   params
   {:enabled "enabled", :configuration "configuration"})))

(s/fdef
 set-emit-touch-events-for-mouse
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::enabled]
    :opt-un
    [::configuration]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::enabled]
    :opt-un
    [::configuration])))
 :ret
 (s/keys))

(defn
 set-emulated-media
 "Emulates the given media type or media feature for CSS media queries.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :media    | Media type to emulate. Empty string disables the override. (optional)\n  :features | Media features to emulate. (optional)"
 ([]
  (set-emulated-media
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [media features]}]
  (set-emulated-media
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [media features]}]
  (cmd/command
   connection
   "Emulation"
   "setEmulatedMedia"
   params
   {:media "media", :features "features"})))

(s/fdef
 set-emulated-media
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::media
     ::features]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::media
     ::features])))
 :ret
 (s/keys))

(defn
 set-emulated-vision-deficiency
 "Emulates the given vision deficiency.\n\nParameters map keys:\n\n\n  Key   | Description \n  ------|------------ \n  :type | Vision deficiency to emulate. Order: best-effort emulations come first, followed by any\nphysiologically accurate emulations for medically recognized color vision deficiencies."
 ([]
  (set-emulated-vision-deficiency
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [type]}]
  (set-emulated-vision-deficiency
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [type]}]
  (cmd/command
   connection
   "Emulation"
   "setEmulatedVisionDeficiency"
   params
   {:type "type"})))

(s/fdef
 set-emulated-vision-deficiency
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::type]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::type])))
 :ret
 (s/keys))

(defn
 set-emulated-os-text-scale
 "Emulates the given OS text scale.\n\nParameters map keys:\n\n\n  Key    | Description \n  -------|------------ \n  :scale | null (optional)"
 ([]
  (set-emulated-os-text-scale
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [scale]}]
  (set-emulated-os-text-scale
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [scale]}]
  (cmd/command
   connection
   "Emulation"
   "setEmulatedOSTextScale"
   params
   {:scale "scale"})))

(s/fdef
 set-emulated-os-text-scale
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::scale]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::scale])))
 :ret
 (s/keys))

(defn
 set-geolocation-override
 "Overrides the Geolocation Position or Error. Omitting latitude, longitude or\naccuracy emulates position unavailable.\n\nParameters map keys:\n\n\n  Key                | Description \n  -------------------|------------ \n  :latitude          | Mock latitude (optional)\n  :longitude         | Mock longitude (optional)\n  :accuracy          | Mock accuracy (optional)\n  :altitude          | Mock altitude (optional)\n  :altitude-accuracy | Mock altitudeAccuracy (optional)\n  :heading           | Mock heading (optional)\n  :speed             | Mock speed (optional)"
 ([]
  (set-geolocation-override
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [latitude
     longitude
     accuracy
     altitude
     altitude-accuracy
     heading
     speed]}]
  (set-geolocation-override
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [latitude
     longitude
     accuracy
     altitude
     altitude-accuracy
     heading
     speed]}]
  (cmd/command
   connection
   "Emulation"
   "setGeolocationOverride"
   params
   {:latitude "latitude",
    :longitude "longitude",
    :accuracy "accuracy",
    :altitude "altitude",
    :altitude-accuracy "altitudeAccuracy",
    :heading "heading",
    :speed "speed"})))

(s/fdef
 set-geolocation-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::latitude
     ::longitude
     ::accuracy
     ::altitude
     ::altitude-accuracy
     ::heading
     ::speed]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::latitude
     ::longitude
     ::accuracy
     ::altitude
     ::altitude-accuracy
     ::heading
     ::speed])))
 :ret
 (s/keys))

(defn
 get-overridden-sensor-information
 "\n\nParameters map keys:\n\n\n  Key   | Description \n  ------|------------ \n  :type | null\n\nReturn map keys:\n\n\n  Key                           | Description \n  ------------------------------|------------ \n  :requested-sampling-frequency | null"
 ([]
  (get-overridden-sensor-information
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [type]}]
  (get-overridden-sensor-information
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [type]}]
  (cmd/command
   connection
   "Emulation"
   "getOverriddenSensorInformation"
   params
   {:type "type"})))

(s/fdef
 get-overridden-sensor-information
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::type]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::type])))
 :ret
 (s/keys
  :req-un
  [::requested-sampling-frequency]))

(defn
 set-sensor-override-enabled
 "Overrides a platform sensor of a given type. If |enabled| is true, calls to\nSensor.start() will use a virtual sensor as backend rather than fetching\ndata from a real hardware sensor. Otherwise, existing virtual\nsensor-backend Sensor objects will fire an error event and new calls to\nSensor.start() will attempt to use a real sensor instead.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :enabled  | null\n  :type     | null\n  :metadata | null (optional)"
 ([]
  (set-sensor-override-enabled
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enabled type metadata]}]
  (set-sensor-override-enabled
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enabled type metadata]}]
  (cmd/command
   connection
   "Emulation"
   "setSensorOverrideEnabled"
   params
   {:enabled "enabled", :type "type", :metadata "metadata"})))

(s/fdef
 set-sensor-override-enabled
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::enabled
     ::type]
    :opt-un
    [::metadata]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::enabled
     ::type]
    :opt-un
    [::metadata])))
 :ret
 (s/keys))

(defn
 set-sensor-override-readings
 "Updates the sensor readings reported by a sensor type previously overridden\nby setSensorOverrideEnabled.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :type    | null\n  :reading | null"
 ([]
  (set-sensor-override-readings
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [type reading]}]
  (set-sensor-override-readings
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [type reading]}]
  (cmd/command
   connection
   "Emulation"
   "setSensorOverrideReadings"
   params
   {:type "type", :reading "reading"})))

(s/fdef
 set-sensor-override-readings
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::type
     ::reading]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::type
     ::reading])))
 :ret
 (s/keys))

(defn
 set-pressure-source-override-enabled
 "Overrides a pressure source of a given type, as used by the Compute\nPressure API, so that updates to PressureObserver.observe() are provided\nvia setPressureStateOverride instead of being retrieved from\nplatform-provided telemetry data.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :enabled  | null\n  :source   | null\n  :metadata | null (optional)"
 ([]
  (set-pressure-source-override-enabled
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enabled source metadata]}]
  (set-pressure-source-override-enabled
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enabled source metadata]}]
  (cmd/command
   connection
   "Emulation"
   "setPressureSourceOverrideEnabled"
   params
   {:enabled "enabled", :source "source", :metadata "metadata"})))

(s/fdef
 set-pressure-source-override-enabled
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::enabled
     ::source]
    :opt-un
    [::metadata]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::enabled
     ::source]
    :opt-un
    [::metadata])))
 :ret
 (s/keys))

(defn
 set-pressure-state-override
 "TODO: OBSOLETE: To remove when setPressureDataOverride is merged.\nProvides a given pressure state that will be processed and eventually be\ndelivered to PressureObserver users. |source| must have been previously\noverridden by setPressureSourceOverrideEnabled.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :source | null\n  :state  | null"
 ([]
  (set-pressure-state-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [source state]}]
  (set-pressure-state-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [source state]}]
  (cmd/command
   connection
   "Emulation"
   "setPressureStateOverride"
   params
   {:source "source", :state "state"})))

(s/fdef
 set-pressure-state-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::source
     ::state]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::source
     ::state])))
 :ret
 (s/keys))

(defn
 set-pressure-data-override
 "Provides a given pressure data set that will be processed and eventually be\ndelivered to PressureObserver users. |source| must have been previously\noverridden by setPressureSourceOverrideEnabled.\n\nParameters map keys:\n\n\n  Key                        | Description \n  ---------------------------|------------ \n  :source                    | null\n  :state                     | null\n  :own-contribution-estimate | null (optional)"
 ([]
  (set-pressure-data-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [source state own-contribution-estimate]}]
  (set-pressure-data-override
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [source state own-contribution-estimate]}]
  (cmd/command
   connection
   "Emulation"
   "setPressureDataOverride"
   params
   {:source "source",
    :state "state",
    :own-contribution-estimate "ownContributionEstimate"})))

(s/fdef
 set-pressure-data-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::source
     ::state]
    :opt-un
    [::own-contribution-estimate]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::source
     ::state]
    :opt-un
    [::own-contribution-estimate])))
 :ret
 (s/keys))

(defn
 set-idle-override
 "Overrides the Idle state.\n\nParameters map keys:\n\n\n  Key                 | Description \n  --------------------|------------ \n  :is-user-active     | Mock isUserActive\n  :is-screen-unlocked | Mock isScreenUnlocked"
 ([]
  (set-idle-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [is-user-active is-screen-unlocked]}]
  (set-idle-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [is-user-active is-screen-unlocked]}]
  (cmd/command
   connection
   "Emulation"
   "setIdleOverride"
   params
   {:is-user-active "isUserActive",
    :is-screen-unlocked "isScreenUnlocked"})))

(s/fdef
 set-idle-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::is-user-active
     ::is-screen-unlocked]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::is-user-active
     ::is-screen-unlocked])))
 :ret
 (s/keys))

(defn
 clear-idle-override
 "Clears Idle state overrides."
 ([]
  (clear-idle-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (clear-idle-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Emulation"
   "clearIdleOverride"
   params
   {})))

(s/fdef
 clear-idle-override
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
 set-navigator-overrides
 "Overrides value returned by the javascript navigator object.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :platform | The platform navigator.platform should return."
 ([]
  (set-navigator-overrides
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [platform]}]
  (set-navigator-overrides
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [platform]}]
  (cmd/command
   connection
   "Emulation"
   "setNavigatorOverrides"
   params
   {:platform "platform"})))

(s/fdef
 set-navigator-overrides
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::platform]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::platform])))
 :ret
 (s/keys))

(defn
 set-page-scale-factor
 "Sets a specified page scale factor.\n\nParameters map keys:\n\n\n  Key                | Description \n  -------------------|------------ \n  :page-scale-factor | Page scale factor."
 ([]
  (set-page-scale-factor
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [page-scale-factor]}]
  (set-page-scale-factor
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [page-scale-factor]}]
  (cmd/command
   connection
   "Emulation"
   "setPageScaleFactor"
   params
   {:page-scale-factor "pageScaleFactor"})))

(s/fdef
 set-page-scale-factor
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::page-scale-factor]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::page-scale-factor])))
 :ret
 (s/keys))

(defn
 set-script-execution-disabled
 "Switches script execution in the page.\n\nParameters map keys:\n\n\n  Key    | Description \n  -------|------------ \n  :value | Whether script execution should be disabled in the page."
 ([]
  (set-script-execution-disabled
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [value]}]
  (set-script-execution-disabled
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [value]}]
  (cmd/command
   connection
   "Emulation"
   "setScriptExecutionDisabled"
   params
   {:value "value"})))

(s/fdef
 set-script-execution-disabled
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::value]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::value])))
 :ret
 (s/keys))

(defn
 set-touch-emulation-enabled
 "Enables touch on platforms which do not support them.\n\nParameters map keys:\n\n\n  Key               | Description \n  ------------------|------------ \n  :enabled          | Whether the touch event emulation should be enabled.\n  :max-touch-points | Maximum touch points supported. Defaults to one. (optional)"
 ([]
  (set-touch-emulation-enabled
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enabled max-touch-points]}]
  (set-touch-emulation-enabled
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enabled max-touch-points]}]
  (cmd/command
   connection
   "Emulation"
   "setTouchEmulationEnabled"
   params
   {:enabled "enabled", :max-touch-points "maxTouchPoints"})))

(s/fdef
 set-touch-emulation-enabled
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [:user/enabled]
    :opt-un
    [:user/max-touch-points]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/enabled]
    :opt-un
    [:user/max-touch-points])))
 :ret
 (s/keys))

(defn
 set-virtual-time-policy
 "Turns on virtual time for all frames (replacing real-time with a synthetic time source) and sets\nthe current virtual time policy.  Note this supersedes any previous time budget.\n\nParameters map keys:\n\n\n  Key                                     | Description \n  ----------------------------------------|------------ \n  :policy                                 | null\n  :budget                                 | If set, after this many virtual milliseconds have elapsed virtual time will be paused and a\nvirtualTimeBudgetExpired event is sent. (optional)\n  :max-virtual-time-task-starvation-count | If set this specifies the maximum number of tasks that can be run before virtual is forced\nforwards to prevent deadlock. (optional)\n  :initial-virtual-time                   | If set, base::Time::Now will be overridden to initially return this value. (optional)\n\nReturn map keys:\n\n\n  Key                      | Description \n  -------------------------|------------ \n  :virtual-time-ticks-base | Absolute timestamp at which virtual time was first enabled (up time in milliseconds)."
 ([]
  (set-virtual-time-policy
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [policy
     budget
     max-virtual-time-task-starvation-count
     initial-virtual-time]}]
  (set-virtual-time-policy
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [policy
     budget
     max-virtual-time-task-starvation-count
     initial-virtual-time]}]
  (cmd/command
   connection
   "Emulation"
   "setVirtualTimePolicy"
   params
   {:policy "policy",
    :budget "budget",
    :max-virtual-time-task-starvation-count
    "maxVirtualTimeTaskStarvationCount",
    :initial-virtual-time "initialVirtualTime"})))

(s/fdef
 set-virtual-time-policy
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [:user/policy]
    :opt-un
    [:user/budget
     :user/max-virtual-time-task-starvation-count
     :user/initial-virtual-time]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/policy]
    :opt-un
    [:user/budget
     :user/max-virtual-time-task-starvation-count
     :user/initial-virtual-time])))
 :ret
 (s/keys :req-un [:user/virtual-time-ticks-base]))

(defn
 set-locale-override
 "Overrides default host system locale with the specified one.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :locale | ICU style C locale (e.g. \"en_US\"). If not specified or empty, disables the override and\nrestores default host system locale. (optional)"
 ([]
  (set-locale-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [locale]}]
  (set-locale-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [locale]}]
  (cmd/command
   connection
   "Emulation"
   "setLocaleOverride"
   params
   {:locale "locale"})))

(s/fdef
 set-locale-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :opt-un [:user/locale]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :opt-un [:user/locale])))
 :ret
 (s/keys))

(defn
 set-timezone-override
 "Overrides default host system timezone with the specified one.\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :timezone-id | The timezone identifier. List of supported timezones:\nhttps://source.chromium.org/chromium/chromium/deps/icu.git/+/faee8bc70570192d82d2978a71e2a615788597d1:source/data/misc/metaZones.txt\nIf empty, disables the override and restores default host system timezone."
 ([]
  (set-timezone-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [timezone-id]}]
  (set-timezone-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [timezone-id]}]
  (cmd/command
   connection
   "Emulation"
   "setTimezoneOverride"
   params
   {:timezone-id "timezoneId"})))

(s/fdef
 set-timezone-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/timezone-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/timezone-id])))
 :ret
 (s/keys))

(defn
 set-visible-size
 "Resizes the frame/viewport of the page. Note that this does not affect the frame's container\n(e.g. browser window). Can be used to produce screenshots of the specified size. Not supported\non Android.\n\nParameters map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :width  | Frame width (DIP).\n  :height | Frame height (DIP)."
 ([]
  (set-visible-size
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [width height]}]
  (set-visible-size
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [width height]}]
  (cmd/command
   connection
   "Emulation"
   "setVisibleSize"
   params
   {:width "width", :height "height"})))

(s/fdef
 set-visible-size
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/width :user/height]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/width :user/height])))
 :ret
 (s/keys))

(defn
 set-disabled-image-types
 "\n\nParameters map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :image-types | Image types to disable."
 ([]
  (set-disabled-image-types
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [image-types]}]
  (set-disabled-image-types
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [image-types]}]
  (cmd/command
   connection
   "Emulation"
   "setDisabledImageTypes"
   params
   {:image-types "imageTypes"})))

(s/fdef
 set-disabled-image-types
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/image-types]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/image-types])))
 :ret
 (s/keys))

(defn
 set-data-saver-override
 "Override the value of navigator.connection.saveData\n\nParameters map keys:\n\n\n  Key                 | Description \n  --------------------|------------ \n  :data-saver-enabled | Override value. Omitting the parameter disables the override. (optional)"
 ([]
  (set-data-saver-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [data-saver-enabled]}]
  (set-data-saver-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [data-saver-enabled]}]
  (cmd/command
   connection
   "Emulation"
   "setDataSaverOverride"
   params
   {:data-saver-enabled "dataSaverEnabled"})))

(s/fdef
 set-data-saver-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :opt-un [:user/data-saver-enabled]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :opt-un [:user/data-saver-enabled])))
 :ret
 (s/keys))

(defn
 set-hardware-concurrency-override
 "\n\nParameters map keys:\n\n\n  Key                   | Description \n  ----------------------|------------ \n  :hardware-concurrency | Hardware concurrency to report"
 ([]
  (set-hardware-concurrency-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [hardware-concurrency]}]
  (set-hardware-concurrency-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [hardware-concurrency]}]
  (cmd/command
   connection
   "Emulation"
   "setHardwareConcurrencyOverride"
   params
   {:hardware-concurrency "hardwareConcurrency"})))

(s/fdef
 set-hardware-concurrency-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/hardware-concurrency]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/hardware-concurrency])))
 :ret
 (s/keys))

(defn
 set-user-agent-override
 "Allows overriding user agent with the given string.\n`userAgentMetadata` must be set for Client Hint headers to be sent.\n\nParameters map keys:\n\n\n  Key                  | Description \n  ---------------------|------------ \n  :user-agent          | User agent to use.\n  :accept-language     | Browser language to emulate. (optional)\n  :platform            | The platform navigator.platform should return. (optional)\n  :user-agent-metadata | To be sent in Sec-CH-UA-* headers and returned in navigator.userAgentData (optional)"
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
   "Emulation"
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
 set-automation-override
 "Allows overriding the automation flag.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :enabled | Whether the override should be enabled."
 ([]
  (set-automation-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enabled]}]
  (set-automation-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enabled]}]
  (cmd/command
   connection
   "Emulation"
   "setAutomationOverride"
   params
   {:enabled "enabled"})))

(s/fdef
 set-automation-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/enabled]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/enabled])))
 :ret
 (s/keys))

(defn
 set-small-viewport-height-difference-override
 "Allows overriding the difference between the small and large viewport sizes, which determine the\nvalue of the `svh` and `lvh` unit, respectively. Only supported for top-level frames.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :difference | This will cause an element of size 100svh to be `difference` pixels smaller than an element\nof size 100lvh."
 ([]
  (set-small-viewport-height-difference-override
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [difference]}]
  (set-small-viewport-height-difference-override
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [difference]}]
  (cmd/command
   connection
   "Emulation"
   "setSmallViewportHeightDifferenceOverride"
   params
   {:difference "difference"})))

(s/fdef
 set-small-viewport-height-difference-override
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/difference]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/difference])))
 :ret
 (s/keys))

(defn
 get-screen-infos
 "Returns device's screen configuration. In headful mode, the physical screens configuration is returned,\nwhereas in headless mode, a virtual headless screen configuration is provided instead.\n\nReturn map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :screen-infos | null"
 ([]
  (get-screen-infos
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (get-screen-infos
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "Emulation"
   "getScreenInfos"
   params
   {})))

(s/fdef
 get-screen-infos
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
 (s/keys :req-un [:user/screen-infos]))

(defn
 add-screen
 "Add a new screen to the device. Only supported in headless mode.\n\nParameters map keys:\n\n\n  Key                 | Description \n  --------------------|------------ \n  :left               | Offset of the left edge of the screen in pixels.\n  :top                | Offset of the top edge of the screen in pixels.\n  :width              | The width of the screen in pixels.\n  :height             | The height of the screen in pixels.\n  :work-area-insets   | Specifies the screen's work area. Default is entire screen. (optional)\n  :device-pixel-ratio | Specifies the screen's device pixel ratio. Default is 1. (optional)\n  :rotation           | Specifies the screen's rotation angle. Available values are 0, 90, 180 and 270. Default is 0. (optional)\n  :color-depth        | Specifies the screen's color depth in bits. Default is 24. (optional)\n  :label              | Specifies the descriptive label for the screen. Default is none. (optional)\n  :is-internal        | Indicates whether the screen is internal to the device or external, attached to the device. Default is false. (optional)\n\nReturn map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :screen-info | null"
 ([]
  (add-screen
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [left
     top
     width
     height
     work-area-insets
     device-pixel-ratio
     rotation
     color-depth
     label
     is-internal]}]
  (add-screen
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [left
     top
     width
     height
     work-area-insets
     device-pixel-ratio
     rotation
     color-depth
     label
     is-internal]}]
  (cmd/command
   connection
   "Emulation"
   "addScreen"
   params
   {:rotation "rotation",
    :top "top",
    :work-area-insets "workAreaInsets",
    :width "width",
    :is-internal "isInternal",
    :device-pixel-ratio "devicePixelRatio",
    :label "label",
    :color-depth "colorDepth",
    :height "height",
    :left "left"})))

(s/fdef
 add-screen
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [:user/left :user/top :user/width :user/height]
    :opt-un
    [:user/work-area-insets
     :user/device-pixel-ratio
     :user/rotation
     :user/color-depth
     :user/label
     :user/is-internal]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/left :user/top :user/width :user/height]
    :opt-un
    [:user/work-area-insets
     :user/device-pixel-ratio
     :user/rotation
     :user/color-depth
     :user/label
     :user/is-internal])))
 :ret
 (s/keys :req-un [:user/screen-info]))

(defn
 update-screen
 "Updates specified screen parameters. Only supported in headless mode.\n\nParameters map keys:\n\n\n  Key                 | Description \n  --------------------|------------ \n  :screen-id          | Target screen identifier.\n  :left               | Offset of the left edge of the screen in pixels. (optional)\n  :top                | Offset of the top edge of the screen in pixels. (optional)\n  :width              | The width of the screen in pixels. (optional)\n  :height             | The height of the screen in pixels. (optional)\n  :work-area-insets   | Specifies the screen's work area. (optional)\n  :device-pixel-ratio | Specifies the screen's device pixel ratio. (optional)\n  :rotation           | Specifies the screen's rotation angle. Available values are 0, 90, 180 and 270. (optional)\n  :color-depth        | Specifies the screen's color depth in bits. (optional)\n  :label              | Specifies the descriptive label for the screen. (optional)\n  :is-internal        | Indicates whether the screen is internal to the device or external, attached to the device. Default is false. (optional)\n\nReturn map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :screen-info | null"
 ([]
  (update-screen
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [screen-id
     left
     top
     width
     height
     work-area-insets
     device-pixel-ratio
     rotation
     color-depth
     label
     is-internal]}]
  (update-screen
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [screen-id
     left
     top
     width
     height
     work-area-insets
     device-pixel-ratio
     rotation
     color-depth
     label
     is-internal]}]
  (cmd/command
   connection
   "Emulation"
   "updateScreen"
   params
   {:rotation "rotation",
    :top "top",
    :work-area-insets "workAreaInsets",
    :width "width",
    :is-internal "isInternal",
    :device-pixel-ratio "devicePixelRatio",
    :label "label",
    :screen-id "screenId",
    :color-depth "colorDepth",
    :height "height",
    :left "left"})))

(s/fdef
 update-screen
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [:user/screen-id]
    :opt-un
    [:user/left
     :user/top
     :user/width
     :user/height
     :user/work-area-insets
     :user/device-pixel-ratio
     :user/rotation
     :user/color-depth
     :user/label
     :user/is-internal]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/screen-id]
    :opt-un
    [:user/left
     :user/top
     :user/width
     :user/height
     :user/work-area-insets
     :user/device-pixel-ratio
     :user/rotation
     :user/color-depth
     :user/label
     :user/is-internal])))
 :ret
 (s/keys :req-un [:user/screen-info]))

(defn
 remove-screen
 "Remove screen from the device. Only supported in headless mode.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :screen-id | null"
 ([]
  (remove-screen
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [screen-id]}]
  (remove-screen
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [screen-id]}]
  (cmd/command
   connection
   "Emulation"
   "removeScreen"
   params
   {:screen-id "screenId"})))

(s/fdef
 remove-screen
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/screen-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/screen-id])))
 :ret
 (s/keys))

(defn
 set-primary-screen
 "Set primary screen. Only supported in headless mode.\nNote that this changes the coordinate system origin to the top-left\nof the new primary screen, updating the bounds and work areas\nof all existing screens accordingly.\n\nParameters map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :screen-id | null"
 ([]
  (set-primary-screen
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [screen-id]}]
  (set-primary-screen
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [screen-id]}]
  (cmd/command
   connection
   "Emulation"
   "setPrimaryScreen"
   params
   {:screen-id "screenId"})))

(s/fdef
 set-primary-screen
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/screen-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys :req-un [:user/screen-id])))
 :ret
 (s/keys))
