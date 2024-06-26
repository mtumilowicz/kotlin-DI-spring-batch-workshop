[![Build Status](https://app.travis-ci.com/mtumilowicz/kotlin-DI-spring-batch-workshop.svg?branch=master)](https://travis-ci.com/mtumilowicz/kotlin-DI-spring-batch-workshop)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
# kotlin-DI-spring-batch-workshop

* https://www.manning.com/books/kotlin-in-action
* https://marcin-chwedczuk.github.io/lambda-expressions-in-kotlin
* https://www.packtpub.com/application-development/programming-kotlin
* https://stackoverflow.com/questions/53810536/how-can-i-get-sam-interfaces-object-in-kotlin
* https://github.com/JetBrains/kotlin/blob/master/spec-docs/function-types.md
* https://docs.spring.io/spring-batch/docs/4.2.x/reference/html/index.html
* https://docs.spring.io/spring-batch/docs/4.2.x/reference/html/readersAndWriters.html#JsonItemReader

## preface
* goals of this workshop:
    * introduce basic kotlin syntax
    * discuss kotlin's concept of function and lambda
    * introduction to parse file part by part without loading it into memory (spring batch)
    * introduction to parsing JSON with Gson
    * implementing domain in a framework-agnostic manner (by applying hexagonal architecture)
    * show canonical example of DI container usage to decouple configuration and implementation
* workshop: exemplary project is in: `app.answers` with full tests, but feel free to start with
own ideas and experiment

## task
* `task/anomaly_detection.pdf`

### exemplary solution
1. domain is framework-agnostic by applying hexagonal architecture
1. all integrations with files (measures and anomalies) are configured and performed in 
`infrastructure.configuration.repo`
    * measures are read one by one using spring batch API
    * anomalies are read all at once during startup to the app memory using gson API
1. paths to files are defined in `application-env.properties`, where `env` is a spring profile name that we want to 
trigger
    * `application.properties` stays for `dev`
    * there are defined tasks (in `build.gradle`) to facilitate running app using the specific profile
        * tasks: `bootRunDev`, `bootRunProd`
        * profiles: `dev`, `prod`
1. after start of application - `ApplicationRunner` is triggered to handle printing of anomalies - it is configured in
main file: `App`
1. to add new anomaly feature, simply:
    * create new package: `domain.anomaly.newanomalyname`
    * implement anomaly definition by implementing interface `AnomalyDefinition`
    * implement anomaly detector by implementing interface `AnomalyDetector`
    * configure and produce it in `AnomalyDetectorConfig`
    * rest will be handled by DI container and `MeasurementService` automatically
1. all tests (unit + functional) are implemented in `tests.groovy.app`
1. all builds are performed by TravisCI which verifies compliance of branches and pull-requests as well

## basic kotlin syntax
* `val` vs `var` = `final` vs `non-final`
* string templates: "#{person.name} has accomplished test with $score"
* `===` vs `==`: referential equality (java's `==`) vs structural equality (java's `equals`)
* class
    ```
    class Person( // primary constructor
        // fields with automatic getters
    ) {
        constructor(...): this(...) { // through primary constructor
            // other things
        }
  
        fun method(...): return_type {
  
        }
    }
    ```
* data class
    * `data` keyword before class
    * `equals()/hashCode()` pair
    * `toString()`
    * `componentN()` functions - destructuring
    * `copy()`
* companion object - way of declaring static properties
* named parameters, default parameters
* operators
    ```
    operator fun compareTo(limit: Limit): Int = compareValues(raw, limit.raw)
    ```
* null safety
    * `String? = String + null`
    * `val name: String = null` // does not compile
    * `val name: String? = null` // compile
* safe call operator: `?.`
    * `b?.length ~ b != null ? b.length : null`
    * in kotlin there is no ternary operator
* Elvis operator: `?:`
    * `b?.length ?: -1 ~ b != null ? b.length : -1`
* lists
    * `listOf()`
    * both filter and map return a list
        * it becomes much less efficient if you have a million
* sequences
    ```
    public interface Sequence<out T> {
        public operator fun iterator(): Iterator<T>
    }
    ```
    * equivalent of Stream types
    * a sequence of elements that can be enumerated one by one
    * intermediate sequence is represented as an object storing a lambda in its field, and the 
    terminal operation causes a chain of calls through each intermediate sequence to be performed
        ```
        fun <T, R> Sequence<T>.map(transform: (T) -> R): Sequence<R> {
            return TransformingSequence(this, transform)
        }
        ```

## function
```
fun run() {}
val run: () -> Unit = ::run
```
* a variable of a function type is an instance of a class implementing `FunctionN` interface
    ```
    public interface Function1<in P1, out R> : Function<R> {
        public operator fun invoke(p1: P1): R
    }
    ```
* support for top-level functions - defined directly inside a file
* `this`
    * top-level - no context
    * in members of a class - refers to the class instance
    * in extension functions - refers to the instance that the extension function was applied to
* local or nested functions - functions declared inside other functions
* java context - automatic SAM conversion
    ```
    val threadPool = Executors.newFixedThreadPool(4)
    val hello: () -> Unit = { println("hello") }
    threadPool.submit(hello)
    run(hello)
  
    fun run(block: Runnable) = block.run()
    ```
    under the hood is
    ```
    threadPool.submit(object : Runnable { // anonymous inner class implementing Runnable
        override fun run() {
            println("hello")
        }
    })
    ```
    but there is no explicit conversion between java's functional interfaces and kotlin's function types 
    ```
    var javaRun: Runnable = Runnable { println("run!") }
    var kotlinRun: () -> Unit = { println("run!") }
    javaRun = kotlinRun
    kotlinRun = javaRun
    ```
  
## lambda
* encodes a small piece of behavior that you can pass around as a value
* syntax
    ```
    val sum = { x: Int, y: Int -> x + y }
    println(sum(1, 2)  
    ```
* lambda could be moved out of parentheses if it’s the last argument in a function call.
    * if the only argument to a function - empty parentheses could be removed
    ```
    people.maxBy({ p: Person -> p.age })
    people.maxBy() { p: Person -> p.age }
    people.maxBy { p: Person -> p.age }
    people.maxBy { p -> p.age } // Parameter type inferred
    people.maxBy { it.age } // it syntax
    ```
* in lambda you aren’t restricted to accessing effectively final variables
  * java: when you want to capture a mutable variable - array or wrapper
  * kotlin:
    ```
    var counter = 0
    val inc = { counter++ }
    ```
    which is under the hood
    ```
    class Ref<T>(var value: T)
    val counter = Ref(0)
    val inc = { counter.value++ }    
    ```
* static references
    * val getAge = Person::age // member reference
    * val getAge = p::age // bound member reference
    * run(::salute) // top-level reference
    * val nextAction = ::sendEmail // several parameters - sendEmail(person, message)
    * val createPerson = ::Person // constructor reference
* return from an enclosing function (not just from the lambda itself)
    * rule is simple: 
        * return returns from the closest function declared using the fun keyword
        * lambda has no fun keyword, so a return returns from the outer function
    * it is called a non-local return as it returns from a larger block than the block containing the 
    return statement
    * think about return in a for loop - also returns from the function and not from the loop or block
    * a local return in a lambda is similar to a break expression in a for loop
        ```
        people.forEach label@{ if (it.name == "Alice") return@label }
        ```
    * anonymous functions has local returns
        ```
        people.forEach(fun (person) {
            if(...) return // “return” refers to the closest function: an anonymous function
            ...
        })
        ```
      
### compilation
* Kotlin 1.0 - every lambda expression is compiled into an anonymous class, unless it’s an inline lambda
* name: `Method$1`, where `Method` - name of the function in which the lambda is declared
* the lambda is a block of code, not an objectc - `this` refers to a surrounding class
* if the lambda doesn’t access any variables from the function where it’s defined, the corresponding 
anonymous class instance is reused between calls
    * no state - they can be modeled as a singleton instance via a static method
    * a singleton is created that is used for every subsequent method call
    * capturing variables - new object for every call with variables stored inside it
    
### inline functions: removing the overhead of lambdas
* lambda compilation introduces runtime overhead, causing an implementation that uses a
  lambda to be less efficient than a function that executes the same code directly
* inline modifier - the compiler will replace every call with the body of lambda
* lambdas used to process a sequence can’t be inlined
* remember that JVM already provides powerful inlining support (JIT)

## spring batch
* automated, complex processing of large volumes of information that is most efficiently processed 
without user interaction
    * typically include time-based events (such as month-end calculations, notices, or correspondence)
* provides support for reading and Writing an array of JSON objects corresponding to individual items
    * interface `JsonItemReader` - intended to be implemented by using a streaming API to read JSON 
    objects in chunks
    * to be able to process JSON records, the following is needed:
        * resource - the JSON file to read
        * `JsonObjectReader` - to parse and bind JSON objects to items
            ```
            public JsonItemReader<Trade> jsonItemReader() {
               return new JsonItemReaderBuilder<Trade>() // class that we are parsing JSON into
                             .jsonObjectReader(new JacksonJsonObjectReader<>(Trade.class))
                             .resource(new ClassPathResource("trades.json")) // path to resource, could be FileSystemResource as well
                             .name("tradeJsonItemReader") // name in case of producing as a bean
                             .build();
            }
            ```
