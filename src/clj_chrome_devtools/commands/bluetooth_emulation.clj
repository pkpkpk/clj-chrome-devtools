(ns clj-chrome-devtools.commands.bluetooth-emulation
  "This domain allows configuring virtual Bluetooth devices to test\nthe web-bluetooth API."
  (:require [clojure.spec.alpha :as s]
            [clj-chrome-devtools.impl.command :as cmd]
            [clj-chrome-devtools.impl.connection :as c]))

(s/def
 ::central-state
 #{"powered-on" "absent" "powered-off"})

(s/def
 ::gatt-operation-type
 #{"connection" "discovery"})

(s/def
 ::characteristic-write-type
 #{"write-without-response" "write-default-deprecated"
   "write-with-response"})

(s/def
 ::characteristic-operation-type
 #{"unsubscribe-from-notifications" "read" "subscribe-to-notifications"
   "write"})

(s/def
 ::descriptor-operation-type
 #{"read" "write"})

(s/def
 ::manufacturer-data
 (s/keys
  :req-un
  [::key
   ::data]))

(s/def
 ::scan-record
 (s/keys
  :opt-un
  [::name
   ::uuids
   ::appearance
   ::tx-power
   ::manufacturer-data]))

(s/def
 ::scan-entry
 (s/keys
  :req-un
  [::device-address
   ::rssi
   ::scan-record]))

(s/def
 ::characteristic-properties
 (s/keys
  :opt-un
  [::broadcast
   ::read
   ::write-without-response
   ::write
   ::notify
   ::indicate
   ::authenticated-signed-writes
   ::extended-properties]))
(defn
 enable
 "Enable the BluetoothEmulation domain.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :state        | State of the simulated central.\n  :le-supported | If the simulated central supports low-energy."
 ([]
  (enable
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [state le-supported]}]
  (enable
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [state le-supported]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "enable"
   params
   {:state "state", :le-supported "leSupported"})))

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
    :req-un
    [::state
     ::le-supported]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::state
     ::le-supported])))
 :ret
 (s/keys))

(defn
 set-simulated-central-state
 "Set the state of the simulated central.\n\nParameters map keys:\n\n\n  Key    | Description \n  -------|------------ \n  :state | State of the simulated central."
 ([]
  (set-simulated-central-state
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [state]}]
  (set-simulated-central-state
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [state]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "setSimulatedCentralState"
   params
   {:state "state"})))

(s/fdef
 set-simulated-central-state
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::state]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::state])))
 :ret
 (s/keys))

(defn
 disable
 "Disable the BluetoothEmulation domain."
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
   "BluetoothEmulation"
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
 simulate-preconnected-peripheral
 "Simulates a peripheral with |address|, |name| and |knownServiceUuids|\nthat has already been connected to the system.\n\nParameters map keys:\n\n\n  Key                  | Description \n  ---------------------|------------ \n  :address             | null\n  :name                | null\n  :manufacturer-data   | null\n  :known-service-uuids | null"
 ([]
  (simulate-preconnected-peripheral
   (c/get-current-connection)
   {}))
 ([{:as params,
    :keys [address name manufacturer-data known-service-uuids]}]
  (simulate-preconnected-peripheral
   (c/get-current-connection)
   params))
 ([connection
   {:as params,
    :keys [address name manufacturer-data known-service-uuids]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "simulatePreconnectedPeripheral"
   params
   {:address "address",
    :name "name",
    :manufacturer-data "manufacturerData",
    :known-service-uuids "knownServiceUuids"})))

(s/fdef
 simulate-preconnected-peripheral
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::address
     ::name
     ::manufacturer-data
     ::known-service-uuids]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::address
     ::name
     ::manufacturer-data
     ::known-service-uuids])))
 :ret
 (s/keys))

(defn
 simulate-advertisement
 "Simulates an advertisement packet described in |entry| being received by\nthe central.\n\nParameters map keys:\n\n\n  Key    | Description \n  -------|------------ \n  :entry | null"
 ([]
  (simulate-advertisement
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [entry]}]
  (simulate-advertisement
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [entry]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "simulateAdvertisement"
   params
   {:entry "entry"})))

(s/fdef
 simulate-advertisement
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::entry]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::entry])))
 :ret
 (s/keys))

(defn
 simulate-gatt-operation-response
 "Simulates the response code from the peripheral with |address| for a\nGATT operation of |type|. The |code| value follows the HCI Error Codes from\nBluetooth Core Specification Vol 2 Part D 1.3 List Of Error Codes.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :address | null\n  :type    | null\n  :code    | null"
 ([]
  (simulate-gatt-operation-response
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [address type code]}]
  (simulate-gatt-operation-response
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [address type code]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "simulateGATTOperationResponse"
   params
   {:address "address", :type "type", :code "code"})))

(s/fdef
 simulate-gatt-operation-response
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::address
     ::type
     ::code]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::address
     ::type
     ::code])))
 :ret
 (s/keys))

(defn
 simulate-characteristic-operation-response
 "Simulates the response from the characteristic with |characteristicId| for a\ncharacteristic operation of |type|. The |code| value follows the Error\nCodes from Bluetooth Core Specification Vol 3 Part F 3.4.1.1 Error Response.\nThe |data| is expected to exist when simulating a successful read operation\nresponse.\n\nParameters map keys:\n\n\n  Key                | Description \n  -------------------|------------ \n  :characteristic-id | null\n  :type              | null\n  :code              | null\n  :data              | null (optional)"
 ([]
  (simulate-characteristic-operation-response
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [characteristic-id type code data]}]
  (simulate-characteristic-operation-response
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [characteristic-id type code data]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "simulateCharacteristicOperationResponse"
   params
   {:characteristic-id "characteristicId",
    :type "type",
    :code "code",
    :data "data"})))

(s/fdef
 simulate-characteristic-operation-response
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::characteristic-id
     ::type
     ::code]
    :opt-un
    [::data]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::characteristic-id
     ::type
     ::code]
    :opt-un
    [::data])))
 :ret
 (s/keys))

(defn
 simulate-descriptor-operation-response
 "Simulates the response from the descriptor with |descriptorId| for a\ndescriptor operation of |type|. The |code| value follows the Error\nCodes from Bluetooth Core Specification Vol 3 Part F 3.4.1.1 Error Response.\nThe |data| is expected to exist when simulating a successful read operation\nresponse.\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :descriptor-id | null\n  :type          | null\n  :code          | null\n  :data          | null (optional)"
 ([]
  (simulate-descriptor-operation-response
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [descriptor-id type code data]}]
  (simulate-descriptor-operation-response
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [descriptor-id type code data]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "simulateDescriptorOperationResponse"
   params
   {:descriptor-id "descriptorId",
    :type "type",
    :code "code",
    :data "data"})))

(s/fdef
 simulate-descriptor-operation-response
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::descriptor-id
     ::type
     ::code]
    :opt-un
    [::data]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::descriptor-id
     ::type
     ::code]
    :opt-un
    [::data])))
 :ret
 (s/keys))

(defn
 add-service
 "Adds a service with |serviceUuid| to the peripheral with |address|.\n\nParameters map keys:\n\n\n  Key           | Description \n  --------------|------------ \n  :address      | null\n  :service-uuid | null\n\nReturn map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :service-id | An identifier that uniquely represents this service."
 ([]
  (add-service
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [address service-uuid]}]
  (add-service
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [address service-uuid]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "addService"
   params
   {:address "address", :service-uuid "serviceUuid"})))

(s/fdef
 add-service
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::address
     ::service-uuid]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::address
     ::service-uuid])))
 :ret
 (s/keys
  :req-un
  [::service-id]))

(defn
 remove-service
 "Removes the service respresented by |serviceId| from the simulated central.\n\nParameters map keys:\n\n\n  Key         | Description \n  ------------|------------ \n  :service-id | null"
 ([]
  (remove-service
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [service-id]}]
  (remove-service
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [service-id]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "removeService"
   params
   {:service-id "serviceId"})))

(s/fdef
 remove-service
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::service-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::service-id])))
 :ret
 (s/keys))

(defn
 add-characteristic
 "Adds a characteristic with |characteristicUuid| and |properties| to the\nservice represented by |serviceId|.\n\nParameters map keys:\n\n\n  Key                  | Description \n  ---------------------|------------ \n  :service-id          | null\n  :characteristic-uuid | null\n  :properties          | null\n\nReturn map keys:\n\n\n  Key                | Description \n  -------------------|------------ \n  :characteristic-id | An identifier that uniquely represents this characteristic."
 ([]
  (add-characteristic
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [service-id characteristic-uuid properties]}]
  (add-characteristic
   (c/get-current-connection)
   params))
 ([connection
   {:as params, :keys [service-id characteristic-uuid properties]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "addCharacteristic"
   params
   {:service-id "serviceId",
    :characteristic-uuid "characteristicUuid",
    :properties "properties"})))

(s/fdef
 add-characteristic
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::service-id
     ::characteristic-uuid
     ::properties]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::service-id
     ::characteristic-uuid
     ::properties])))
 :ret
 (s/keys
  :req-un
  [::characteristic-id]))

(defn
 remove-characteristic
 "Removes the characteristic respresented by |characteristicId| from the\nsimulated central.\n\nParameters map keys:\n\n\n  Key                | Description \n  -------------------|------------ \n  :characteristic-id | null"
 ([]
  (remove-characteristic
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [characteristic-id]}]
  (remove-characteristic
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [characteristic-id]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "removeCharacteristic"
   params
   {:characteristic-id "characteristicId"})))

(s/fdef
 remove-characteristic
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::characteristic-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::characteristic-id])))
 :ret
 (s/keys))

(defn
 add-descriptor
 "Adds a descriptor with |descriptorUuid| to the characteristic respresented\nby |characteristicId|.\n\nParameters map keys:\n\n\n  Key                | Description \n  -------------------|------------ \n  :characteristic-id | null\n  :descriptor-uuid   | null\n\nReturn map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :descriptor-id | An identifier that uniquely represents this descriptor."
 ([]
  (add-descriptor
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [characteristic-id descriptor-uuid]}]
  (add-descriptor
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [characteristic-id descriptor-uuid]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "addDescriptor"
   params
   {:characteristic-id "characteristicId",
    :descriptor-uuid "descriptorUuid"})))

(s/fdef
 add-descriptor
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::characteristic-id
     ::descriptor-uuid]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::characteristic-id
     ::descriptor-uuid])))
 :ret
 (s/keys
  :req-un
  [::descriptor-id]))

(defn
 remove-descriptor
 "Removes the descriptor with |descriptorId| from the simulated central.\n\nParameters map keys:\n\n\n  Key            | Description \n  ---------------|------------ \n  :descriptor-id | null"
 ([]
  (remove-descriptor
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [descriptor-id]}]
  (remove-descriptor
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [descriptor-id]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "removeDescriptor"
   params
   {:descriptor-id "descriptorId"})))

(s/fdef
 remove-descriptor
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::descriptor-id]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::descriptor-id])))
 :ret
 (s/keys))

(defn
 simulate-gatt-disconnection
 "Simulates a GATT disconnection from the peripheral with |address|.\n\nParameters map keys:\n\n\n  Key      | Description \n  ---------|------------ \n  :address | null"
 ([]
  (simulate-gatt-disconnection
   (c/get-current-connection)
   {}))
 ([{:as params, :keys [address]}]
  (simulate-gatt-disconnection
   (c/get-current-connection)
   params))
 ([connection {:as params, :keys [address]}]
  (cmd/command
   connection
   "BluetoothEmulation"
   "simulateGATTDisconnection"
   params
   {:address "address"})))

(s/fdef
 simulate-gatt-disconnection
 :args
 (s/or
  :no-args
  (s/cat)
  :just-params
  (s/cat
   :params
   (s/keys
    :req-un
    [::address]))
  :connection-and-params
  (s/cat
   :connection
   (s/?
    c/connection?)
   :params
   (s/keys
    :req-un
    [::address])))
 :ret
 (s/keys))
