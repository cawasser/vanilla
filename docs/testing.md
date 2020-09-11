# Testing

Our plan for creating and running tests is based on the information
found [here](https://practicalli.github.io/clojure/testing/unit-testing/).


## Running the tests

To run all the tests in the [test](../test) directory, simply run

```shell script
lein test
```


To run a specific test use an extended form of the above code as such:

```shell script
lein test vanilla.example-test
```

where you can replace 'vanilla.example-test' with any namespace in the
test directory.