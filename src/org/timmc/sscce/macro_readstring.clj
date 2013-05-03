(ns org.timmc.sscce.macro-readstring)

(defmacro breaker
  []
  ;; Wrap in a fn whose class initialization will fail
  ;; (I don't know why this is required.)
  `((fn fail-init []
      (println "You're in this namespace:"
               ;; That tilde should be *outside* the str call. Instead of a
               ;; namespace string, the compiler emits a namespace object
               ;; which is then pr-str'd and read at compile time.
               (str ~*ns*)))))

(defn -main
  "Entrance point for demo."
  [& args]
  ;; There are a few extraneous lines in here just to demonstrate that the
  ;; line number in the stack trace does not point to the bad code.
  (println "This is above the bad code, but does not get run.")
  ;; And here's where the bad code is emitted:
  (breaker))