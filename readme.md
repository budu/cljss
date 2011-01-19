# cljss

The `cljss` library help you generate CSS rules using Clojure code, with
support for:

 * nested CSS rules;
 * multiple selectors per rules;
 * property value as keyword;
 * property value variables.

## Usage

    (def css-vars
      {:font-title "'Palatino Linotype','Book Antiqua',Palatino,FreeSerif,serif"
       :font-main  "Verdana, Arial"
       :color-main "#404a3e"})
    
    (def sample-style
      (css css-vars
       ;; tags
       [:body :font-family :$font-main]
       ;; ids
       [:#wrapper :background-color :$color-main
                  :border "solid 5px $color-main"
                  :margin "0 5%"]
       [(each :#header :#content :#footer) :padding :1em]
       [(each :#header :#footer) :color :white]
       [:#content :background-color :#eee
                  :border-radius :1em
                  :clear :both
                  :color :$color-main]
       ;; links
       [:a :color :#69a :text-decoration :none]
       [:a:hover :color :#8aa :text-decoration :underline]
       [:a:visited :color :#479]
       ;; header
       [:#header
        [:h1 :font-family :$font-title
             :font-style :italic
             :font-size :4em]
        [:ul :display :inline
             :float :right
             :list-style :none
             :padding 0
         [:li :display :inline
              :margin "0 0.5em"]]]
       [($ :#header :ul :li)
        [(each :a :a.visited) :border-radius :0.5em
                              :background-color :#243
                              :color :#fa0
                              :padding "0.5em 1.5em"]
        [:a:hover :background-color :#687
                  :text-decoration :none]]
       ;; footer
       [($ :#footer :p) :font-size :0.7em]))

## License

Copyright (C) 2010 Nicolas Buduroi. All rights reserved

Distributed under the Eclipse Public License, the same as Clojure. See
the file epl-v10.html in the project root directory.
