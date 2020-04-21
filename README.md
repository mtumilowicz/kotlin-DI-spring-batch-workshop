# kotlin-DI-spring-batch-workshop

* https://www.manning.com/books/kotlin-in-action
* https://marcin-chwedczuk.github.io/lambda-expressions-in-kotlin
* https://www.packtpub.com/application-development/programming-kotlin
* https://stackoverflow.com/questions/53810536/how-can-i-get-sam-interfaces-object-in-kotlin
* https://github.com/JetBrains/kotlin/blob/master/spec-docs/function-types.md

## preface
* goals of this workshop:
    * introduce basic kotlin syntax
    * discuss kotlin's concept of function and lambda
    * gentle introduction into spring batch fundamentals
* workshop: workshop package, answers: answers package

## task

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
    
### return
* Return statements in lambdas: return from an enclosing function
    * If you use the return keyword in a lambda, it returns from the function in which you called
      the lambda, not just from the lambda itself
    * Such a return statement is called a non-
      local return, because it returns from a larger block than the block containing the
      return statement.
    * To understand the logic behind the rule, think about using a return keyword in a
      for loop or a synchronized block in a Java method. It’s obvious that it returns from
      the function and not from the loop or block. Kotlin allows you to preserve the same
      behavior when you switch from language features to functions that take lambdas as
      arguments.
    * Using the return expres-
      sion in lambdas passed to non-inline functions isn’t allowed. A non-inline function
      can save the lambda passed to it in a variable and execute it later, when the function
      has already returned, so it’s too late for the lambda to affect when the surrounding
      function returns.
    * You can write a local return from a lambda expression as well. A local return in a
      lambda is similar to a break expression in a for loop.
      * Returns from a lambda use
        the “@” character to mark a label
      * people.forEach label@{
            if (it.name == "Alice") return@label
        }
        people.forEach @{
                    if (it.name == "Alice") return@forEach
                }
#### Anonymous functions return
* Anonymous functions: local returns by default
    fun lookForAlice(people: List<Person>) {
    people.forEach(fun (person) {
    if (person.name == "Alice") return // “return” refers to the closest function: an anonymous function
    println("${person.name} is not Alice")
    })
    }
    * The rule is simple: return returns
      from the closest function declared using the fun keyword. Lambda expressions don’t use the
      fun keyword, so a return in a lambda returns from the outer function.
