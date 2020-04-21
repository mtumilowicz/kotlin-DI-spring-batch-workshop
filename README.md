# kotlin-DI-spring-batch-workshop

* https://www.manning.com/books/kotlin-in-action
* https://marcin-chwedczuk.github.io/lambda-expressions-in-kotlin
* https://www.packtpub.com/application-development/programming-kotlin

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
    * intermediate and terminal operations
        * map passes function to the constructor of a class that stores it in a property
            ```
            fun <T, R> Sequence<T>.map(transform: (T) -> R): Sequence<R> {
                return TransformingSequence(this, transform)
            }
            ```
        * needs non-inline representation of lambda - passed is an anonymous class implementing 
        a function interface

## functions
* Under the hood, function types are declared as regular interfaces: a variable of a func-
  tion type is an implementation of a FunctionN interface
  * Kotlin standard library
    defines a series of interfaces, corresponding to different numbers of function argu-
    ments: Function0<R> (this function takes no arguments), Function1<P1, R> (this
    function takes one argument), and so on
  * Each interface defines a single invoke
    method, and calling it will execute the function
  * Java 8 lamb-
    das are automatically converted to values of function types
* In members of a class, this refers to the class instance. In extension functions, this refers
  to the instance that the extension function was applied to.
* Kotlin allows us to take this a step further by supporting functions declared inside other
  functions. These are called local or nested functions. Functions can even be nested multiple
  times.
* In addition to member functions and local functions, Kotlin also supports declaring top-
  level functions. These are functions that exist outside of any class, object, or interface and
  are defined directly inside a file.
* Named parameters
* Default parameters
* Single abstract methods
    * Kotlin has support for converting a function literal directly
      into a SAM
    
    val threadPool = Executors.newFixedThreadPool(4)
    threadPool.submit {
    println("I don't have a lot of work to do")
    }
    
    same as
    
    threadPool.submit(object : Runnable {
    override fun run() {
    println("I don't have a lot of work to do")
    }
    })
    * works for interfaces and not abstract classes,
      even if the abstract class only has a single method
    * Kotlin will not perform this conversion on SAMs that are defined in Kotlin
      itself. This is because in Kotlin, you can define your function to accept
      another function, making this kind of pattern redundant
      ```
      interface X : () -> Unit {
          fun x(): Unit
      }
      
      class Y {
          fun z(x: Runnable) {
          }
      }
      
      fun main() {
          val x: X = { println("a") }
          Y().z { println("a") }
      }
      ```
## lambda
* a lambda encodes a small piece of behavior that you can pass around as a value
* syntax
    ```
    val sum = { x: Int, y: Int -> x + y }
    println(sum(1, 2)  
    ```
* in Kotlin, a syntactic convention lets you move a lambda expression out of parentheses if it’s 
the last argument in a function call.
   * When the lambda is the only argument to a function, you can also remove the empty
     parentheses from the call:
   ```
   * people.maxBy({ p: Person -> p.age })
   * people.maxBy() { p: Person -> p.age }
   * people.maxBy { p: Person -> p.age }
   * people.maxBy { p -> p.age } // Parameter type inferred
   * people.maxBy { it.age } // it syntax
  ```
* If you use a lambda in a function, you can access the parameters of that function as well as the local 
variables declared before the lambda
    * One important difference between Kotlin and Java is that in Kotlin, you aren’t restricted to accessing 
    final variables
    * You can also modify variables from within a lambda
    * You know that when you declare an anonymous inner class in a function, you can refer
      to parameters and local variables of that function from inside the class
        * only final or effectively final
* When you capture a final variable, its value is stored together with the lambda code that uses it
* For non-final variables, the value is enclosed in a special wrapper that lets you change it, and the 
reference to the wrapper is stored together with the lambda
  * java: When you want to capture a mutable variable, you can use one of the following tricks: either 
  declare an array of one element in which to store the mutable value, or create an instance of a wrapper 
  class that stores the reference that can be changed
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
* Member references
    * val getAge = { person: Person -> person.age }
    * val getAge = Person::age
    * You can have a reference to a function that’s declared at the top level (and isn’t a
      member of a class)
      * run(::salute)
    * function taking several parameters
        val action = { person: Person, message: String ->
        sendEmail(person, message)
        }
        val nextAction = ::sendEmail
    * val createPerson = ::Person // constructor reference
    * Note that you can also reference extension functions the same way:
      fun Person.isAdult() = age >= 21
      val predicate = Person::isAdult
    * A bound member reference
        val dmitrysAgeFunction = p::age
        println(dmitrysAgeFunction())
* Kotlin allows you to use lambdas when calling Java methods that take functional interfaces as 
parameters, ensuring that your Kotlin code remains clean and idiomatic

### compilation
* creating an anonymous object that implements Runnable explicitly:
  postponeComputation(1000, object : Runnable {
    override fun run() {
    println(42)
    }
  })
  * When you explicitly declare an object, a new instance is cre-
    ated on each invocation. With a lambda, the situation is different: if the lambda
    doesn’t access any variables from the function where it’s defined, the corresponding
    anonymous class instance is reused between calls:
    postponeComputation(1000) { println(42) } // One instance of Runnable is created for the entire program
  * If the lambda captures variables from the surrounding scope, it’s no longer possible to
    reuse the same instance for every invocation. In that case, the compiler creates a new
    object for every call and stores the values of the captured variables in that object
* Lambda implementation details
    * As of Kotlin 1.0, every lambda expression is compiled into an anonymous class,
      unless it’s an inline lambda
    * The name of the class is derived by add-
      ing a suffix from the name of the function in which the lambda is declared: Handle-
      Computation$1
    * From the compiler’s point of view, the lambda is a block of code, not an object, and
      you can’t refer to it as an object. The this reference in a lambda refers to a sur-
      rounding class.
* Inline functions: removing the overhead of lambdas
    * we explained that lambdas are normally compiled to anonymous
      classes. But that means every time you use a lambda expression, an extra class is cre-
      ated; and if the lambda captures some variables, then a new object is created on every
      invocation
    *  introduces runtime overhead, causing an implementation that uses a
      lambda to be less efficient than a function that executes the same code directly
    * If you mark a function with the inline
      modifier, the compiler won’t generate a function call when this function is used and
      instead will replace every call to the function with the actual code implementing the
      function
    * When you declare a function as inline , its body is inlined—in other words, it’s substi-
      tuted directly into places where the function is called instead of being invoked nor-
      mally
    * lambdas used to
      process a sequence aren’t inlined. Each intermediate sequence is represented as an
      object storing a lambda in its field, and the terminal operation causes a chain of calls
      through each intermediate sequence to be performed
    * For regular function calls, the JVM already provides powerful inlining support. It
      analyzes the execution of your code and inlines calls whenever doing so provides the
      most benefit. This happens automatically while translating bytecode to machine code.
    * If we were to mark the withResource function as inline, then the Kotlin compiler would
      not generate this as an invocation on a new instance, but instead would generate the code at
      the call site.
      Firstly, we annotate the function with the keyword:
      inline fun <T : AutoCloseable, U> withResource(resource: T, fn: (T) ->
      U): U {
      try {
      return fn(resource)
      } finally {
      resource.close()
      }
      }
      The compiler would translate an invocation of characterCount into the following:
      fun characterCountExpanded(filename: String): Int {
      val input = Files.newInputStream(Paths.get(filename))
      try {
      return input.buffered().reader().readText().length
      } finally {
      input.close()
      }
      }
* It turns out that all functions in Kotlin are compiled into instances of classes called
  Function0 , Function1 , Function2 , and so on
  * For example, a function with the signature
    (Int)->Boolean would show the type as Function1<Int, Boolean> 
  * Each of the
    function classes also has an invoke member function that is used to apply the body of the
    function.
  ```
  /** A function that takes 1 argument. */
  public interface Function1<in P1, out R> : Function<R> {
  /** Invokes the function with the specified argument. */
  public operator fun invoke(p1: P1): R
  }
  ```
  *  Since functions have no state other than their inputs, they can be modeled as a
    singleton instance via a static method.
    Closures are implemented by increasing the arity of the function to accept
    extra parameters, which are the closed-over variables. The compiler inserts
    this automatically.

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
