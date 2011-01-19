; Copyright (c) Nicolas Buduroi. All rights reserved.
; The use and distribution terms for this software are covered by the
; Eclipse Public License 1.0 which can be found in the file
; epl-v10.html at the root of this distribution. By using this software
; in any fashion, you are agreeing to be bound by the terms of this
; license.
; You must not remove this notice, or any other, from this software.

(ns cljss.core
  "Lightweight CSS generating library."
  (:refer-clojure :exclude [replace])
  (:use (clojure [string :only [join replace]])
        [clojure.contrib.def :only [defalias]]))

(def *css-vars* {})

(defn sub-css-vars [s]
  (replace s #"\$([\w-]+)"
           #(let [k (-> % second keyword)]
              (if (contains? *css-vars* k)
                (*css-vars* k)
                (name k)))))

(defn as-str [o]
  (if (keyword? o)
    (name o)
    (str o)))

(defn css-property* [k v]
  (str (name k) ":" (-> v as-str sub-css-vars)))

(defn css-property [[k v]]
  (let [p (css-property* k v)]
    (if (= k :border-radius)
      [p (css-property* :-moz-border-radius v)]
      p)))

(defn ->vec [o]
  (if (vector? o) o [o]))

(defn gen-rule [selector properties]
  (let [selector (->vec selector)]
    (str (->> selector (map name) (join \space)) " {"
         (->> properties (partition 2) (map css-property) flatten (join \;))
         "}")))

(declare gen-rules)

(defn gen-rules* [s [ss & p]]
  (gen-rules (vec (conj p (if (seq? ss)
                            (map #(conj s %) ss)
                            (conj s ss))))))

(defn gen-rules [rules]
  (let [[s & p] rules
        ps (filter (comp not vector?) p)
        sr (seq (filter vector? p))]
    (if (seq? s)
      (map #(gen-rules (apply vector % p)) s)
      (concat [(gen-rule s ps)]
              (map (partial gen-rules* (->vec s)) sr)))))

(defn css
  {:argslist '([vars? & rules])}
  [& rules]
  (let [[vars rules] (if (map? (first rules))
                       [(first rules) (next rules)]
                       [*css-vars* rules])]
    (binding [*css-vars* vars]
      (->> rules
           (map gen-rules)
           flatten
           (join \newline)))))

(defalias each list)

(defalias $ vector)
