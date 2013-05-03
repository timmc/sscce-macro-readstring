# SSCCE: macro-readstring

A [Short, Self-Contained, Correct Example][SSCCE] of how an incorrect macro
can cause `read-string` failures that are incomprehensible to a programmer
who is even moderately familiar with Clojure internals.

[SSCCE]: http://sscce.org/

## Usage

`lein run`

## Stack trace

```
$ lein run
About to hit the bad code...
Exception in thread "main" java.lang.ExceptionInInitializerError
	at org.timmc.sscce.macro_readstring$_main.doInvoke(macro_readstring.clj:22)
	at clojure.lang.RestFn.invoke(RestFn.java:397)
	at clojure.lang.Var.invoke(Var.java:411)
	at user$eval23.invoke(NO_SOURCE_FILE:1)
	at clojure.lang.Compiler.eval(Compiler.java:6619)
	at clojure.lang.Compiler.eval(Compiler.java:6609)
	at clojure.lang.Compiler.eval(Compiler.java:6582)
	at clojure.core$eval.invoke(core.clj:2852)
	at clojure.main$eval_opt.invoke(main.clj:308)
	at clojure.main$initialize.invoke(main.clj:327)
	at clojure.main$null_opt.invoke(main.clj:362)
	at clojure.main$main.doInvoke(main.clj:440)
	at clojure.lang.RestFn.invoke(RestFn.java:421)
	at clojure.lang.Var.invoke(Var.java:419)
	at clojure.lang.AFn.applyToHelper(AFn.java:163)
	at clojure.lang.Var.applyTo(Var.java:532)
	at clojure.main.main(main.java:37)
Caused by: java.lang.RuntimeException: Reading disallowed - *read-eval* bound to :unknown
	at clojure.lang.Util.runtimeException(Util.java:219)
	at clojure.lang.LispReader.read(LispReader.java:156)
	at clojure.lang.RT.readString(RT.java:1738)
	at org.timmc.sscce.macro_readstring$_main$fail_init__18.<clinit>(macro_readstring.clj:22)
	... 17 more
```

The key is `org.timmc.sscce.macro_readstring$_main$fail_init__18.<clinit>`.
Let's see what the compiler emits...

## Bytecode excerpt

```
$ lein repl
org.timmc.sscce.macro-readstring=> (compile 'org.timmc.sscce.macro-readstring)
org.timmc.sscce.macro-readstring

$ ls -1 target/classes/org/timmc/sscce/
macro_readstring$breaker.class
macro_readstring.class
macro_readstring$fn__1166.class
macro_readstring__init.class
macro_readstring$loading__4910__auto__.class
macro_readstring$_main.class
macro_readstring$_main$fail_init__1169.class
```

Check the bytecode of fail-init, remembering to convert `$` to `.`:

```
$ javap -verbose -private -l -s -c -classpath target/classes/ org.timmc.sscce.macro_readstring._main.fail_init__1169

...line 46: The compiler has emitted a readable representation of an ns:
const #31 = Asciz	#=(find-ns org.timmc.sscce.macro-readstring);

...line 106: Preparing to call clojure.core/str:
   13:	ldc	#14; //String clojure.core
   15:	ldc	#28; //String str
   17:	invokestatic	#22; //Method clojure/lang/RT.var:(Ljava/lang/String;Ljava/lang/String;)Lclojure/lang/Var;
   20:	checkcast	#24; //class clojure/lang/Var
   23:	putstatic	#30; //Field const__1:Lclojure/lang/Var;

...line 111: Inflate the ns "syntax" using read-string -- and die:
   26:	ldc	#32; //String #=(find-ns org.timmc.sscce.macro-readstring)
   28:	invokestatic	#36; //Method clojure/lang/RT.readString:(Ljava/lang/String;)Ljava/lang/Object;
```

Full bytecode in [fail-init.javap](fail-init.javap).

## License

Copyright © 2013 Tim McCormack

Distributed under the Eclipse Public License, the same as Clojure.
