# ambient-weather-clj

Ambient Weather Client for Clojure

## Installation

### Leiningen

```
[ambient-weather-clj "0.1.0"]
```

be sure to include clojars in your project.clj

```
:repositories [["clojars" "https://clojars.org/repo"]]
```

## Usage

To get started you'll need an application key 
and an api key. Follow the link to see how to 
obtain your own keys. https://ambientweather.docs.apiary.io/#introduction/authentication

#### List the user's devices

<pre><code>(require '[ambient-weather-clj :as aw])

(aw/user-devices "your-app-key" "your-api-key")
</code></pre>

#### Get historical device data

<pre><code>(require '[ambient-weather-clj :as aw])

(def query {:app-key "your-app-key"
            :api-key "your-api-key"
            :mac-addr "BB:AA:CC:DD:11:00"
            :date "2020-08-21T03:52:00.000Z"
            :limit 5}

(aw/device-data query)
</code></pre>

### Realtime Data

Create a connected socket and add event listners

<pre><code>(require '[ambient-weather-clj :as aw])

(def event-listners {:subscribed   (fn [& args]
                                     (println (apply str args)))
                     :unsubscribed (fn [& args]
                                     (println (apply str args)))
                     :data         (fn [& args]
                                     (println (apply str args)))})

(def socket (aw/socket "your-app-key" event-listners)
</code></pre>

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
