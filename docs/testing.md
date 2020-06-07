# Testing Vanilla

See also  https://clojurescript.org/tools/testing


Vanilla support automated testing using both:

    lein test

 and

    lein test-refresh


## [lein test](https://github.com/technomancy/leiningen/blob/stable/doc/TUTORIAL.md#tests)

The built-in Leiningen test runner will execute all the tests located in the `/test` directory *once*.
This makes it well suited to regression testing during development or using as part of a
[CI/CD pipeline](https://devops.com/optimizing-effective-cicd-pipeline/)
to automate quality assurance.


## [lein test-refresh](https://github.com/jakemcc/lein-test-refresh)

The Leiningen plug-in `lein-test-refresh` also runs all the test in the `/test/clj` directory, but only
first time after it is run. After that, it watches the `/src/clj` and `/test/clj` folders and
only re-runs the tests if a namespace changes. This makes it well suited to be used when developing
at the REPL. Just start the runner

    lein test-refresh

in a separate terminal window and leave it running while you work on the code. Each time you change
either a test or one of the functions that is *being* tested, that test will automatically be run again
and you will see a report on test success or failure.

Reports look something like:

``` text
*********************************************
*************** Running tests ***************
:reloading (vanilla.example-test)

Testing vanilla.example-test

FAIL in (another-test) (example_test.clj:9)
expected: true
  actual: false
    diff: - true
          +
Ran 2 tests containing 2 assertions.
1 failures, 0 errors.

Failed 1 of 2 assertions
Finished at 10:57:39.766 (run time: 0.034s)
```

You can see that the test

``` clojure
(deftest another-test
  (is (= true false)))
```

*should* fail, and the test report shows that. It also shows which part of the test fails,
the expected value, and the actual value used in the `(is...)` check


## Organizing Tests



## Using clojure.spec for Testing



## Generative Testing



## Resources

- [Cursive Test Integration](https://cursive-ide.com/userguide/testing.html)
- [Example-based Unit Testing in Clojure](https://purelyfunctional.tv/mini-guide/example-based-unit-testing-in-clojure/)
- [Testing in Clojure (practicalli)](https://practicalli.github.io/clojure/testing/)