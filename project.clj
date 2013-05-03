(defproject org.timmc.sscce/macro-readstring "1.0.0"
  :description "An SSCCE for macro + read-string nonsense"
  :url "https://github.com/timmc/sscce-macro-readstring"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ;; Clojure >= 1.5 to control *read-eval* default binding
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :main org.timmc.sscce.macro-readstring
  ;; Set *read-eval* to :unknown at root
  :jvm-opts ["-Dclojure.read.eval=unknown"])
