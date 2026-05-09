(ns clj-chrome-devtools.commands.css
  "This domain exposes CSS read/write operations. All CSS objects (stylesheets, rules, and styles)\nhave an associated `id` used in subsequent operations on the related object. Each object type has\na specific `id` structure, and those are not interchangeable between objects of different kinds.\nCSS objects can be loaded using the `get*ForNode()` calls (which accept a DOM node id). A client\ncan also keep track of stylesheets via the `styleSheetAdded`/`styleSheetRemoved` events and\nsubsequently load the required stylesheet contents using the `getStyleSheet[Text]()` methods."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::style-sheet-origin
 #{"user-agent" "injected" "regular" "inspector"})

(s/def
 ::pseudo-element-matches
 (s/keys
  :req-un
  [::pseudo-type
   ::matches]
  :opt-un
  [::pseudo-identifier]))

(s/def
 ::css-animation-style
 (s/keys
  :req-un
  [::style]
  :opt-un
  [::name]))

(s/def
 ::inherited-style-entry
 (s/keys
  :req-un
  [::matched-css-rules]
  :opt-un
  [::inline-style]))

(s/def
 ::inherited-animated-style-entry
 (s/keys
  :opt-un
  [::animation-styles
   ::transitions-style]))

(s/def
 ::inherited-pseudo-element-matches
 (s/keys
  :req-un
  [::pseudo-elements]))

(s/def
 ::rule-match
 (s/keys
  :req-un
  [::rule
   ::matching-selectors]))

(s/def
 ::value
 (s/keys
  :req-un
  [::text]
  :opt-un
  [::range
   ::specificity]))

(s/def
 ::specificity
 (s/keys
  :req-un
  [::a
   ::b
   ::c]))

(s/def
 ::selector-list
 (s/keys
  :req-un
  [::selectors
   ::text]))

(s/def
 ::css-style-sheet-header
 (s/keys
  :req-un
  [::style-sheet-id
   ::frame-id
   ::source-url
   ::origin
   ::title
   ::disabled
   ::is-inline
   ::is-mutable
   ::is-constructed
   ::start-line
   ::start-column
   ::length
   ::end-line
   ::end-column]
  :opt-un
  [::source-map-url
   ::owner-node
   ::has-source-url
   ::loading-failed]))

(s/def
 ::css-rule
 (s/keys
  :req-un
  [::selector-list
   ::origin
   ::style]
  :opt-un
  [::style-sheet-id
   ::nesting-selectors
   ::origin-tree-scope-node-id
   ::media
   ::container-queries
   ::supports
   ::layers
   ::scopes
   ::rule-types
   ::starting-styles
   ::navigations]))

(s/def
 ::css-rule-type
 #{"LayerRule" "StartingStyleRule" "ScopeRule" "NavigationRule"
   "ContainerRule" "SupportsRule" "MediaRule" "StyleRule"})

(s/def
 ::rule-usage
 (s/keys
  :req-un
  [::style-sheet-id
   ::start-offset
   ::end-offset
   ::used]))

(s/def
 ::source-range
 (s/keys
  :req-un
  [::start-line
   ::start-column
   ::end-line
   ::end-column]))

(s/def
 ::shorthand-entry
 (s/keys
  :req-un
  [::name
   ::value]
  :opt-un
  [::important]))

(s/def
 ::css-computed-style-property
 (s/keys
  :req-un
  [::name
   ::value]))

(s/def
 ::computed-style-extra-fields
 (s/keys
  :req-un
  [::is-appearance-base]))

(s/def
 ::css-style
 (s/keys
  :req-un
  [::css-properties
   ::shorthand-entries]
  :opt-un
  [::style-sheet-id
   ::css-text
   ::range]))

(s/def
 ::css-property
 (s/keys
  :req-un
  [::name
   ::value]
  :opt-un
  [::important
   ::implicit
   ::text
   ::parsed-ok
   ::disabled
   ::range
   ::longhand-properties]))

(s/def
 ::css-media
 (s/keys
  :req-un
  [::text
   ::source]
  :opt-un
  [::source-url
   ::range
   ::style-sheet-id
   ::media-list]))

(s/def
 ::media-query
 (s/keys
  :req-un
  [::expressions
   ::active]))

(s/def
 ::media-query-expression
 (s/keys
  :req-un
  [::value
   ::unit
   ::feature]
  :opt-un
  [::value-range
   ::computed-length]))

(s/def
 ::css-container-query
 (s/keys
  :req-un
  [::text
   ::condition-text]
  :opt-un
  [::range
   ::style-sheet-id
   ::name
   ::physical-axes
   ::logical-axes
   ::queries-scroll-state
   ::queries-anchored]))

(s/def
 ::css-supports
 (s/keys
  :req-un
  [::text
   ::active]
  :opt-un
  [::range
   ::style-sheet-id]))

(s/def
 ::css-navigation
 (s/keys
  :req-un
  [::text]
  :opt-un
  [::active
   ::range
   ::style-sheet-id]))

(s/def
 ::css-scope
 (s/keys
  :req-un
  [::text]
  :opt-un
  [::range
   ::style-sheet-id]))

(s/def
 ::css-layer
 (s/keys
  :req-un
  [::text]
  :opt-un
  [::range
   ::style-sheet-id]))

(s/def
 ::css-starting-style
 (s/keys
  :opt-un
  [::range
   ::style-sheet-id]))

(s/def
 ::css-layer-data
 (s/keys
  :req-un
  [::name
   ::order]
  :opt-un
  [::sub-layers]))

(s/def
 ::platform-font-usage
 (s/keys
  :req-un
  [::family-name
   ::post-script-name
   ::is-custom-font
   ::glyph-count]))

(s/def
 ::font-variation-axis
 (s/keys
  :req-un
  [::tag
   ::name
   ::min-value
   ::max-value
   ::default-value]))

(s/def
 :user/font-face
 (s/keys
  :req-un
  [:user/font-family
   :user/font-style
   :user/font-variant
   :user/font-weight
   :user/font-stretch
   :user/font-display
   :user/unicode-range
   :user/src
   :user/platform-font-family]
  :opt-un
  [:user/font-variation-axes]))

(s/def
 :user/css-try-rule
 (s/keys
  :req-un
  [:user/origin :user/style]
  :opt-un
  [:user/style-sheet-id]))

(s/def
 :user/css-position-try-rule
 (s/keys
  :req-un
  [:user/name :user/origin :user/style :user/active]
  :opt-un
  [:user/style-sheet-id]))

(s/def
 :user/css-keyframes-rule
 (s/keys
  :req-un
  [:user/animation-name :user/keyframes]))

(s/def
 :user/css-property-registration
 (s/keys
  :req-un
  [:user/property-name :user/inherits :user/syntax]
  :opt-un
  [:user/initial-value]))

(s/def
 :user/css-at-rule
 (s/keys
  :req-un
  [:user/type :user/origin :user/style]
  :opt-un
  [:user/subsection :user/name :user/style-sheet-id]))

(s/def
 :user/css-property-rule
 (s/keys
  :req-un
  [:user/origin :user/property-name :user/style]
  :opt-un
  [:user/style-sheet-id]))

(s/def
 :user/css-function-parameter
 (s/keys :req-un [:user/name :user/type]))

(s/def
 :user/css-function-condition-node
 (s/keys
  :req-un
  [:user/children :user/condition-text]
  :opt-un
  [:user/media
   :user/container-queries
   :user/supports
   :user/navigation]))

(s/def
 :user/css-function-node
 (s/keys :opt-un [:user/condition :user/style]))

(s/def
 :user/css-function-rule
 (s/keys
  :req-un
  [:user/name :user/origin :user/parameters :user/children]
  :opt-un
  [:user/style-sheet-id :user/origin-tree-scope-node-id]))

(s/def
 :user/css-keyframe-rule
 (s/keys
  :req-un
  [:user/origin :user/key-text :user/style]
  :opt-un
  [:user/style-sheet-id]))

(s/def
 :user/style-declaration-edit
 (s/keys
  :req-un
  [:user/style-sheet-id :user/range :user/text]))
(defn
 add-rule
 "Inserts a new rule with the given `ruleText` in a stylesheet with given `styleSheetId`, at the\nposition specified by `location`.\n\nParameters map keys:\n\n\n  Key                                  | Description \n  -------------------------------------|------------ \n  :style-sheet-id                      | The css style sheet identifier where a new rule should be inserted.\n  :rule-text                           | The text of a new rule.\n  :location                            | Text position of a new rule in the target style sheet.\n  :node-for-property-syntax-validation | NodeId for the DOM node in whose context custom property declarations for registered properties should be\nvalidated. If omitted, declarations in the new rule text can only be validated statically, which may produce\nincorrect results if the declaration contains a var() for example. (optional)\n\nReturn map keys:\n\n\n  Key   | Description \n  ------|------------ \n  :rule | The newly created rule."
 ([]
  (add-rule
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [style-sheet-id
     rule-text
     location
     node-for-property-syntax-validation]}]
  (add-rule
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [style-sheet-id
     rule-text
     location
     node-for-property-syntax-validation]}]
  (cmd/command
   connection
   "CSS"
   "addRule"
   params
   {:style-sheet-id "styleSheetId",
    :rule-text "ruleText",
    :location "location",
    :node-for-property-syntax-validation
    "nodeForPropertySyntaxValidation"})))

(s/fdef
 add-rule
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::rule-text
     ::location]
    :opt-un
    [::node-for-property-syntax-validation]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::rule-text
     ::location]
    :opt-un
    [::node-for-property-syntax-validation])))
 :ret
 (s/keys
  :req-un
  [::rule]))

(defn
 collect-class-names
 "Returns all class names from specified stylesheet.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n\nReturn map keys:\n\n\n  Key          | Description \n  -------------|------------ \n  :class-names | Class name list."
 ([]
  (collect-class-names
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id]}]
  (collect-class-names
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id]}]
  (cmd/command
   connection
   "CSS"
   "collectClassNames"
   params
   {:style-sheet-id "styleSheetId"})))

(s/fdef
 collect-class-names
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id])))
 :ret
 (s/keys
  :req-un
  [::class-names]))

(defn
 create-style-sheet
 "Creates a new special \"via-inspector\" stylesheet in the frame with given `frameId`.\n\nParameters map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :frame-id | Identifier of the frame where \"via-inspector\" stylesheet should be created.\n  :force    | If true, creates a new stylesheet for every call. If false,\nreturns a stylesheet previously created by a call with force=false\nfor the frame's document if it exists or creates a new stylesheet\n(default: false). (optional)\n\nReturn map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | Identifier of the created \"via-inspector\" stylesheet."
 ([]
  (create-style-sheet
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [frame-id force]}]
  (create-style-sheet
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [frame-id force]}]
  (cmd/command
   connection
   "CSS"
   "createStyleSheet"
   params
   {:frame-id "frameId", :force "force"})))

(s/fdef
 create-style-sheet
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::frame-id]
    :opt-un
    [::force]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::frame-id]
    :opt-un
    [::force])))
 :ret
 (s/keys
  :req-un
  [::style-sheet-id]))

(defn
 disable
 "Disables the CSS agent for the given page."
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
   "CSS"
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
 "Enables the CSS agent for the given page. Clients should not assume that the CSS agent has been\nenabled until the result of this command is received."
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
   "CSS"
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
 force-pseudo-state
 "Ensures that the given node will have specified pseudo-classes whenever its style is computed by\nthe browser.\n\nParameters map keys:\n\n\n  Key                    | Description \n  -----------------------|------------ \n  :node-id               | The element id for which to force the pseudo state.\n  :forced-pseudo-classes | Element pseudo classes to force when computing the element's style."
 ([]
  (force-pseudo-state
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id forced-pseudo-classes]}]
  (force-pseudo-state
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id forced-pseudo-classes]}]
  (cmd/command
   connection
   "CSS"
   "forcePseudoState"
   params
   {:node-id "nodeId", :forced-pseudo-classes "forcedPseudoClasses"})))

(s/fdef
 force-pseudo-state
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::node-id
     ::forced-pseudo-classes]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::node-id
     ::forced-pseudo-classes])))
 :ret
 (s/keys))

(defn
 force-starting-style
 "Ensures that the given node is in its starting-style state.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :node-id | The element id for which to force the starting-style state.\n  :forced  | Boolean indicating if this is on or off."
 ([]
  (force-starting-style
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id forced]}]
  (force-starting-style
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id forced]}]
  (cmd/command
   connection
   "CSS"
   "forceStartingStyle"
   params
   {:node-id "nodeId", :forced "forced"})))

(s/fdef
 force-starting-style
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::node-id
     ::forced]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::node-id
     ::forced])))
 :ret
 (s/keys))

(defn
 get-background-colors
 "\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :node-id | Id of the node to get background colors for.\n\nReturn map keys:\n\n\n  Key                   | Description \n  ----------------------|------------ \n  :background-colors    | The range of background colors behind this element, if it contains any visible text. If no\nvisible text is present, this will be undefined. In the case of a flat background color,\nthis will consist of simply that color. In the case of a gradient, this will consist of each\nof the color stops. For anything more complicated, this will be an empty array. Images will\nbe ignored (as if the image had failed to load). (optional)\n  :computed-font-size   | The computed font size for this node, as a CSS computed value string (e.g. '12px'). (optional)\n  :computed-font-weight | The computed font weight for this node, as a CSS computed value string (e.g. 'normal' or\n'100'). (optional)"
 ([]
  (get-background-colors
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id]}]
  (get-background-colors
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id]}]
  (cmd/command
   connection
   "CSS"
   "getBackgroundColors"
   params
   {:node-id "nodeId"})))

(s/fdef
 get-background-colors
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::node-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::node-id])))
 :ret
 (s/keys
  :opt-un
  [::background-colors
   ::computed-font-size
   ::computed-font-weight]))

(defn
 get-computed-style-for-node
 "Returns the computed style for a DOM node identified by `nodeId`.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :node-id | null\n\nReturn map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :computed-style | Computed style for the specified DOM node.\n  :extra-fields   | A list of non-standard \"extra fields\" which blink stores alongside each\ncomputed style."
 ([]
  (get-computed-style-for-node
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id]}]
  (get-computed-style-for-node
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id]}]
  (cmd/command
   connection
   "CSS"
   "getComputedStyleForNode"
   params
   {:node-id "nodeId"})))

(s/fdef
 get-computed-style-for-node
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::node-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::node-id])))
 :ret
 (s/keys
  :req-un
  [::computed-style
   ::extra-fields]))

(defn
 resolve-values
 "Resolve the specified values in the context of the provided element.\nFor example, a value of '1em' is evaluated according to the computed\n'font-size' of the element and a value 'calc(1px + 2px)' will be\nresolved to '3px'.\nIf the `propertyName` was specified the `values` are resolved as if\nthey were property's declaration. If a value cannot be parsed according\nto the provided property syntax, the value is parsed using combined\nsyntax as if null `propertyName` was provided. If the value cannot be\nresolved even then, return the provided value without any changes.\nNote: this function currently does not resolve CSS random() function,\nit returns unmodified random() function parts.`\n\nParameters map keys:\n\n\n  Key                | Description \n  -------------------|------------ \n  :values            | Cascade-dependent keywords (revert/revert-layer) do not work.\n  :node-id           | Id of the node in whose context the expression is evaluated\n  :property-name     | Only longhands and custom property names are accepted. (optional)\n  :pseudo-type       | Pseudo element type, only works for pseudo elements that generate\nelements in the tree, such as ::before and ::after. (optional)\n  :pseudo-identifier | Pseudo element custom ident. (optional)\n\nReturn map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :results | null"
 ([]
  (resolve-values
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys
    [values node-id property-name pseudo-type pseudo-identifier]}]
  (resolve-values
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys
    [values node-id property-name pseudo-type pseudo-identifier]}]
  (cmd/command
   connection
   "CSS"
   "resolveValues"
   params
   {:values "values",
    :node-id "nodeId",
    :property-name "propertyName",
    :pseudo-type "pseudoType",
    :pseudo-identifier "pseudoIdentifier"})))

(s/fdef
 resolve-values
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::values
     ::node-id]
    :opt-un
    [::property-name
     ::pseudo-type
     ::pseudo-identifier]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::values
     ::node-id]
    :opt-un
    [::property-name
     ::pseudo-type
     ::pseudo-identifier])))
 :ret
 (s/keys
  :req-un
  [::results]))

(defn
 get-longhand-properties
 "\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :shorthand-name | null\n  :value          | null\n\nReturn map keys:\n\n\n  Key                  | Description \n  ---------------------|------------ \n  :longhand-properties | null"
 ([]
  (get-longhand-properties
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [shorthand-name value]}]
  (get-longhand-properties
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [shorthand-name value]}]
  (cmd/command
   connection
   "CSS"
   "getLonghandProperties"
   params
   {:shorthand-name "shorthandName", :value "value"})))

(s/fdef
 get-longhand-properties
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::shorthand-name
     ::value]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::shorthand-name
     ::value])))
 :ret
 (s/keys
  :req-un
  [::longhand-properties]))

(defn
 get-inline-styles-for-node
 "Returns the styles defined inline (explicitly in the \"style\" attribute and implicitly, using DOM\nattributes) for a DOM node identified by `nodeId`.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :node-id | null\n\nReturn map keys:\n\n\n  Key               | Description \n  ------------------|------------ \n  :inline-style     | Inline style for the specified DOM node. (optional)\n  :attributes-style | Attribute-defined element style (e.g. resulting from \"width=20 height=100%\"). (optional)"
 ([]
  (get-inline-styles-for-node
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id]}]
  (get-inline-styles-for-node
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id]}]
  (cmd/command
   connection
   "CSS"
   "getInlineStylesForNode"
   params
   {:node-id "nodeId"})))

(s/fdef
 get-inline-styles-for-node
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::node-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::node-id])))
 :ret
 (s/keys
  :opt-un
  [::inline-style
   ::attributes-style]))

(defn
 get-animated-styles-for-node
 "Returns the styles coming from animations & transitions\nincluding the animation & transition styles coming from inheritance chain.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :node-id | null\n\nReturn map keys:\n\n\n  Key                | Description \n  -------------------|------------ \n  :animation-styles  | Styles coming from animations. (optional)\n  :transitions-style | Style coming from transitions. (optional)\n  :inherited         | Inherited style entries for animationsStyle and transitionsStyle from\nthe inheritance chain of the element. (optional)"
 ([]
  (get-animated-styles-for-node
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id]}]
  (get-animated-styles-for-node
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id]}]
  (cmd/command
   connection
   "CSS"
   "getAnimatedStylesForNode"
   params
   {:node-id "nodeId"})))

(s/fdef
 get-animated-styles-for-node
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::node-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::node-id])))
 :ret
 (s/keys
  :opt-un
  [::animation-styles
   ::transitions-style
   ::inherited]))

(defn
 get-matched-styles-for-node
 "Returns requested styles for a DOM node identified by `nodeId`.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :node-id | null\n\nReturn map keys:\n\n\n  Key                             | Description \n  --------------------------------|------------ \n  :inline-style                   | Inline style for the specified DOM node. (optional)\n  :attributes-style               | Attribute-defined element style (e.g. resulting from \"width=20 height=100%\"). (optional)\n  :matched-css-rules              | CSS rules matching this node, from all applicable stylesheets. (optional)\n  :pseudo-elements                | Pseudo style matches for this node. (optional)\n  :inherited                      | A chain of inherited styles (from the immediate node parent up to the DOM tree root). (optional)\n  :inherited-pseudo-elements      | A chain of inherited pseudo element styles (from the immediate node parent up to the DOM tree root). (optional)\n  :css-keyframes-rules            | A list of CSS keyframed animations matching this node. (optional)\n  :css-position-try-rules         | A list of CSS @position-try rules matching this node, based on the position-try-fallbacks property. (optional)\n  :active-position-fallback-index | Index of the active fallback in the applied position-try-fallback property,\nwill not be set if there is no active position-try fallback. (optional)\n  :css-property-rules             | A list of CSS at-property rules matching this node. (optional)\n  :css-property-registrations     | A list of CSS property registrations matching this node. (optional)\n  :css-at-rules                   | A list of simple @rules matching this node or its pseudo-elements. (optional)\n  :parent-layout-node-id          | Id of the first parent element that does not have display: contents. (optional)\n  :css-function-rules             | A list of CSS at-function rules referenced by styles of this node. (optional)"
 ([]
  (get-matched-styles-for-node
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id]}]
  (get-matched-styles-for-node
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id]}]
  (cmd/command
   connection
   "CSS"
   "getMatchedStylesForNode"
   params
   {:node-id "nodeId"})))

(s/fdef
 get-matched-styles-for-node
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::node-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::node-id])))
 :ret
 (s/keys
  :opt-un
  [::inline-style
   ::attributes-style
   ::matched-css-rules
   ::pseudo-elements
   ::inherited
   ::inherited-pseudo-elements
   ::css-keyframes-rules
   ::css-position-try-rules
   ::active-position-fallback-index
   ::css-property-rules
   ::css-property-registrations
   ::css-at-rules
   ::parent-layout-node-id
   ::css-function-rules]))

(defn
 get-environment-variables
 "Returns the values of the default UA-defined environment variables used in env()\n\nReturn map keys:\n\n\n  Key                    | Description \n  -----------------------|------------ \n  :environment-variables | null"
 ([]
  (get-environment-variables
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (get-environment-variables
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "CSS"
   "getEnvironmentVariables"
   params
   {})))

(s/fdef
 get-environment-variables
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
  [::environment-variables]))

(defn
 get-media-queries
 "Returns all media queries parsed by the rendering engine.\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :medias | null"
 ([]
  (get-media-queries
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (get-media-queries
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "CSS"
   "getMediaQueries"
   params
   {})))

(s/fdef
 get-media-queries
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
  [::medias]))

(defn
 get-platform-fonts-for-node
 "Requests information about platform fonts which we used to render child TextNodes in the given\nnode.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :node-id | null\n\nReturn map keys:\n\n\n  Key    | Description \n  -------|------------ \n  :fonts | Usage statistics for every employed platform font."
 ([]
  (get-platform-fonts-for-node
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id]}]
  (get-platform-fonts-for-node
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id]}]
  (cmd/command
   connection
   "CSS"
   "getPlatformFontsForNode"
   params
   {:node-id "nodeId"})))

(s/fdef
 get-platform-fonts-for-node
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::node-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::node-id])))
 :ret
 (s/keys
  :req-un
  [::fonts]))

(defn
 get-style-sheet-text
 "Returns the current textual content for a stylesheet.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n\nReturn map keys:\n\n\n  Key   | Description \n  ------|------------ \n  :text | The stylesheet text."
 ([]
  (get-style-sheet-text
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id]}]
  (get-style-sheet-text
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id]}]
  (cmd/command
   connection
   "CSS"
   "getStyleSheetText"
   params
   {:style-sheet-id "styleSheetId"})))

(s/fdef
 get-style-sheet-text
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id])))
 :ret
 (s/keys
  :req-un
  [::text]))

(defn
 get-layers-for-node
 "Returns all layers parsed by the rendering engine for the tree scope of a node.\nGiven a DOM element identified by nodeId, getLayersForNode returns the root\nlayer for the nearest ancestor document or shadow root. The layer root contains\nthe full layer tree for the tree scope and their ordering.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :node-id | null\n\nReturn map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :root-layer | null"
 ([]
  (get-layers-for-node
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id]}]
  (get-layers-for-node
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id]}]
  (cmd/command
   connection
   "CSS"
   "getLayersForNode"
   params
   {:node-id "nodeId"})))

(s/fdef
 get-layers-for-node
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::node-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::node-id])))
 :ret
 (s/keys
  :req-un
  [::root-layer]))

(defn
 get-location-for-selector
 "Given a CSS selector text and a style sheet ID, getLocationForSelector\nreturns an array of locations of the CSS selector in the style sheet.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :selector-text  | null\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :ranges | null"
 ([]
  (get-location-for-selector
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id selector-text]}]
  (get-location-for-selector
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id selector-text]}]
  (cmd/command
   connection
   "CSS"
   "getLocationForSelector"
   params
   {:style-sheet-id "styleSheetId", :selector-text "selectorText"})))

(s/fdef
 get-location-for-selector
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::selector-text]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::selector-text])))
 :ret
 (s/keys
  :req-un
  [::ranges]))

(defn
 track-computed-style-updates-for-node
 "Starts tracking the given node for the computed style updates\nand whenever the computed style is updated for node, it queues\na `computedStyleUpdated` event with throttling.\nThere can only be 1 node tracked for computed style updates\nso passing a new node id removes tracking from the previous node.\nPass `undefined` to disable tracking.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :node-id | null (optional)"
 ([]
  (track-computed-style-updates-for-node
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id]}]
  (track-computed-style-updates-for-node
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id]}]
  (cmd/command
   connection
   "CSS"
   "trackComputedStyleUpdatesForNode"
   params
   {:node-id "nodeId"})))

(s/fdef
 track-computed-style-updates-for-node
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :opt-un
    [::node-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :opt-un
    [::node-id])))
 :ret
 (s/keys))

(defn
 track-computed-style-updates
 "Starts tracking the given computed styles for updates. The specified array of properties\nreplaces the one previously specified. Pass empty array to disable tracking.\nUse takeComputedStyleUpdates to retrieve the list of nodes that had properties modified.\nThe changes to computed style properties are only tracked for nodes pushed to the front-end\nby the DOM agent. If no changes to the tracked properties occur after the node has been pushed\nto the front-end, no updates will be issued for the node.\n\nParameters map keys:\n\n\n  Key                  | Description \n  ---------------------|------------ \n  :properties-to-track | null"
 ([]
  (track-computed-style-updates
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [properties-to-track]}]
  (track-computed-style-updates
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [properties-to-track]}]
  (cmd/command
   connection
   "CSS"
   "trackComputedStyleUpdates"
   params
   {:properties-to-track "propertiesToTrack"})))

(s/fdef
 track-computed-style-updates
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::properties-to-track]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::properties-to-track])))
 :ret
 (s/keys))

(defn
 take-computed-style-updates
 "Polls the next batch of computed style updates.\n\nReturn map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :node-ids | The list of node Ids that have their tracked computed styles updated."
 ([]
  (take-computed-style-updates
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (take-computed-style-updates
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "CSS"
   "takeComputedStyleUpdates"
   params
   {})))

(s/fdef
 take-computed-style-updates
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
  [::node-ids]))

(defn
 set-effective-property-value-for-node
 "Find a rule with the given active property for the given node and set the new value for this\nproperty\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :node-id       | The element id for which to set property.\n  :property-name | null\n  :value         | null"
 ([]
  (set-effective-property-value-for-node
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [node-id property-name value]}]
  (set-effective-property-value-for-node
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [node-id property-name value]}]
  (cmd/command
   connection
   "CSS"
   "setEffectivePropertyValueForNode"
   params
   {:node-id "nodeId", :property-name "propertyName", :value "value"})))

(s/fdef
 set-effective-property-value-for-node
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::node-id
     ::property-name
     ::value]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::node-id
     ::property-name
     ::value])))
 :ret
 (s/keys))

(defn
 set-property-rule-property-name
 "Modifies the property rule property name.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :range          | null\n  :property-name  | null\n\nReturn map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :property-name | The resulting key text after modification."
 ([]
  (set-property-rule-property-name
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id range property-name]}]
  (set-property-rule-property-name
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id range property-name]}]
  (cmd/command
   connection
   "CSS"
   "setPropertyRulePropertyName"
   params
   {:style-sheet-id "styleSheetId",
    :range "range",
    :property-name "propertyName"})))

(s/fdef
 set-property-rule-property-name
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::property-name]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::property-name])))
 :ret
 (s/keys
  :req-un
  [::property-name]))

(defn
 set-keyframe-key
 "Modifies the keyframe rule key text.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :range          | null\n  :key-text       | null\n\nReturn map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :key-text | The resulting key text after modification."
 ([]
  (set-keyframe-key
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id range key-text]}]
  (set-keyframe-key
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id range key-text]}]
  (cmd/command
   connection
   "CSS"
   "setKeyframeKey"
   params
   {:style-sheet-id "styleSheetId",
    :range "range",
    :key-text "keyText"})))

(s/fdef
 set-keyframe-key
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::key-text]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::key-text])))
 :ret
 (s/keys
  :req-un
  [::key-text]))

(defn
 set-media-text
 "Modifies the rule selector.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :range          | null\n  :text           | null\n\nReturn map keys:\n\n\n  Key    | Description \n  -------|------------ \n  :media | The resulting CSS media rule after modification."
 ([]
  (set-media-text
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id range text]}]
  (set-media-text
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id range text]}]
  (cmd/command
   connection
   "CSS"
   "setMediaText"
   params
   {:style-sheet-id "styleSheetId", :range "range", :text "text"})))

(s/fdef
 set-media-text
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text])))
 :ret
 (s/keys
  :req-un
  [::media]))

(defn
 set-container-query-text
 "Modifies the expression of a container query.\nDeprecated. Use setContainerQueryConditionText instead.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :range          | null\n  :text           | null\n\nReturn map keys:\n\n\n  Key              | Description \n  -----------------|------------ \n  :container-query | The resulting CSS container query rule after modification."
 ([]
  (set-container-query-text
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id range text]}]
  (set-container-query-text
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id range text]}]
  (cmd/command
   connection
   "CSS"
   "setContainerQueryText"
   params
   {:style-sheet-id "styleSheetId", :range "range", :text "text"})))

(s/fdef
 set-container-query-text
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text])))
 :ret
 (s/keys
  :req-un
  [::container-query]))

(defn
 set-container-query-condition-text
 "\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :range          | null\n  :text           | null\n\nReturn map keys:\n\n\n  Key              | Description \n  -----------------|------------ \n  :container-query | The resulting CSS container query rule after modification."
 ([]
  (set-container-query-condition-text
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id range text]}]
  (set-container-query-condition-text
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id range text]}]
  (cmd/command
   connection
   "CSS"
   "setContainerQueryConditionText"
   params
   {:style-sheet-id "styleSheetId", :range "range", :text "text"})))

(s/fdef
 set-container-query-condition-text
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text])))
 :ret
 (s/keys
  :req-un
  [::container-query]))

(defn
 set-supports-text
 "Modifies the expression of a supports at-rule.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :range          | null\n  :text           | null\n\nReturn map keys:\n\n\n  Key       | Description \n  ----------|------------ \n  :supports | The resulting CSS Supports rule after modification."
 ([]
  (set-supports-text
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id range text]}]
  (set-supports-text
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id range text]}]
  (cmd/command
   connection
   "CSS"
   "setSupportsText"
   params
   {:style-sheet-id "styleSheetId", :range "range", :text "text"})))

(s/fdef
 set-supports-text
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text])))
 :ret
 (s/keys
  :req-un
  [::supports]))

(defn
 set-navigation-text
 "Modifies the expression of a navigation at-rule.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :range          | null\n  :text           | null\n\nReturn map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :navigation | The resulting CSS Navigation rule after modification."
 ([]
  (set-navigation-text
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id range text]}]
  (set-navigation-text
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id range text]}]
  (cmd/command
   connection
   "CSS"
   "setNavigationText"
   params
   {:style-sheet-id "styleSheetId", :range "range", :text "text"})))

(s/fdef
 set-navigation-text
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text])))
 :ret
 (s/keys
  :req-un
  [::navigation]))

(defn
 set-scope-text
 "Modifies the expression of a scope at-rule.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :range          | null\n  :text           | null\n\nReturn map keys:\n\n\n  Key    | Description \n  -------|------------ \n  :scope | The resulting CSS Scope rule after modification."
 ([]
  (set-scope-text
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id range text]}]
  (set-scope-text
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id range text]}]
  (cmd/command
   connection
   "CSS"
   "setScopeText"
   params
   {:style-sheet-id "styleSheetId", :range "range", :text "text"})))

(s/fdef
 set-scope-text
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::style-sheet-id
     ::range
     ::text])))
 :ret
 (s/keys
  :req-un
  [::scope]))

(defn
 set-rule-selector
 "Modifies the rule selector.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :range          | null\n  :selector       | null\n\nReturn map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :selector-list | The resulting selector list after modification."
 ([]
  (set-rule-selector
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id range selector]}]
  (set-rule-selector
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id range selector]}]
  (cmd/command
   connection
   "CSS"
   "setRuleSelector"
   params
   {:style-sheet-id "styleSheetId",
    :range "range",
    :selector "selector"})))

(s/fdef
 set-rule-selector
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [:user/style-sheet-id :user/range :user/selector]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/style-sheet-id :user/range :user/selector])))
 :ret
 (s/keys :req-un [:user/selector-list]))

(defn
 set-style-sheet-text
 "Sets the new stylesheet text.\n\nParameters map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :style-sheet-id | null\n  :text           | null\n\nReturn map keys:\n\n\n  Key             | Description \n  ----------------|------------ \n  :source-map-url | URL of source map associated with script (if any). (optional)"
 ([]
  (set-style-sheet-text
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [style-sheet-id text]}]
  (set-style-sheet-text
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [style-sheet-id text]}]
  (cmd/command
   connection
   "CSS"
   "setStyleSheetText"
   params
   {:style-sheet-id "styleSheetId", :text "text"})))

(s/fdef
 set-style-sheet-text
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys :req-un [:user/style-sheet-id :user/text]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/style-sheet-id :user/text])))
 :ret
 (s/keys :opt-un [:user/source-map-url]))

(defn
 set-style-texts
 "Applies specified style edits one after another in the given order.\n\nParameters map keys:\n\n\n  Key                                  | Description \n  -------------------------------------|------------ \n  :edits                               | null\n  :node-for-property-syntax-validation | NodeId for the DOM node in whose context custom property declarations for registered properties should be\nvalidated. If omitted, declarations in the new rule text can only be validated statically, which may produce\nincorrect results if the declaration contains a var() for example. (optional)\n\nReturn map keys:\n\n\n  Key     | Description \n  --------|------------ \n  :styles | The resulting styles after modification."
 ([]
  (set-style-texts
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [edits node-for-property-syntax-validation]}]
  (set-style-texts
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [edits node-for-property-syntax-validation]}]
  (cmd/command
   connection
   "CSS"
   "setStyleTexts"
   params
   {:edits "edits",
    :node-for-property-syntax-validation
    "nodeForPropertySyntaxValidation"})))

(s/fdef
 set-style-texts
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [:user/edits]
    :opt-un
    [:user/node-for-property-syntax-validation]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [:user/edits]
    :opt-un
    [:user/node-for-property-syntax-validation])))
 :ret
 (s/keys :req-un [:user/styles]))

(defn
 start-rule-usage-tracking
 "Enables the selector recording."
 ([]
  (start-rule-usage-tracking
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (start-rule-usage-tracking
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "CSS"
   "startRuleUsageTracking"
   params
   {})))

(s/fdef
 start-rule-usage-tracking
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
 stop-rule-usage-tracking
 "Stop tracking rule usage and return the list of rules that were used since last call to\n`takeCoverageDelta` (or since start of coverage instrumentation).\n\nReturn map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :rule-usage | null"
 ([]
  (stop-rule-usage-tracking
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (stop-rule-usage-tracking
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "CSS"
   "stopRuleUsageTracking"
   params
   {})))

(s/fdef
 stop-rule-usage-tracking
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
 (s/keys :req-un [:user/rule-usage]))

(defn
 take-coverage-delta
 "Obtain list of rules that became used since last call to this method (or since start of coverage\ninstrumentation).\n\nReturn map keys:\n\n\n  Key        | Description \n  -----------|------------ \n  :coverage  | null\n  :timestamp | Monotonically increasing time, in seconds."
 ([]
  (take-coverage-delta
   (c/get-current-connection)
   {}))
 ([{:as params, :keys []}]
  (take-coverage-delta
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys []}]
  (cmd/command
   connection
   "CSS"
   "takeCoverageDelta"
   params
   {})))

(s/fdef
 take-coverage-delta
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
 (s/keys :req-un [:user/coverage :user/timestamp]))

(defn
 set-local-fonts-enabled
 "Enables/disables rendering of local CSS fonts (enabled by default).\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :enabled | Whether rendering of local fonts is enabled."
 ([]
  (set-local-fonts-enabled
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [enabled]}]
  (set-local-fonts-enabled
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [enabled]}]
  (cmd/command
   connection
   "CSS"
   "setLocalFontsEnabled"
   params
   {:enabled "enabled"})))

(s/fdef
 set-local-fonts-enabled
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
