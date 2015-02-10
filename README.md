# sajens

A Clojure library exposing additional metric types for the
[clojure-metrics](https://github.com/sjl/metrics-clojure) library.

![sajens](http://i.imgur.com/244rqfB.jpg)


## Usage

```clojure
(let [c (->Spiral "a-spiral" 5000)]
    (println (.getCount c))
    (.update c 1)
    (Thread/sleep 1000)
    (println (.getCount c))
    (.update c 2)
    (Thread/sleep 1000)
    (println (.getCount c))
    (.update c 3)
    (Thread/sleep 1000)
    (println (.getCount c))
    (Thread/sleep 1000)
    (println (.getCount c))
    (Thread/sleep 1000)
    (println (.getCount c))
    (Thread/sleep 1000)
    (println (.getCount c))
    (Thread/sleep 1000))

;; => 0
;; => 1
;; => 3
;; => 6
;; => 6
;; => 5
;; => 3
;; => nil
```
## License

Copyright © 2015 Aaron France

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
