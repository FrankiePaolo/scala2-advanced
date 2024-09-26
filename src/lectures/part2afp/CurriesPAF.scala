package lectures.part2afp

object CurriesPAF extends App{

  // curried funcitons
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5))

  def curriedAdder(x: Int)(y: Int): Int = x + y

  val add4: Int => Int = curriedAdder(4) //lifting = ETA-EXPANSION

  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1
  List(1,2,3).map(inc) // ETA-expansion

  // Partial function applications
  val add5 = curriedAdder(5) _ // Int => Int

  // EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y => 7 + y
  val add71 = curriedAddMethod(7) _ // PAF
  val add72 = curriedAddMethod(7)(_)
  val add73 = simpleAddMethod(7, _)
  val add74 = simpleAddFunction(7, _)
  val add75 = simpleAddFunction.curried(7)

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c
  val insertName = concatenator("Hello, I'm ", _, ", how are you?")
  println(insertName("Frank"))

  val fillInTheBlanks = concatenator("Hello, ", _, _)
  println(fillInTheBlanks("Frank", " Scala is awesome!"))

  // EXERCISES
  /*
    1. Process a list of numbers and return their string representations with different formats
       Use the %4.2f, %8.6f and %14.12f with a curried formatter function.
   */
  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)
  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _
  println(numbers.map(simpleFormat))
  println(numbers.map(seriousFormat))
  println(numbers.map(preciseFormat))

  /*
    2. difference between
       - functions vs methods
       - parameters: by-name vs 0-lambda
   */
  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42

  /*
    calling byName and byFunction
    - int
    - method
    - parenMethod
    - lambda
    - PAF
   */
  byName(23) // ok
  byName(method) // ok
  byName(parenMethod()) // ok
  byName(parenMethod) // ok but beware ==> byName(parenMethod())
  byName((()=>42)()) // ok
  // byName(() => 42) // not ok
  // byFunction(45) // not ok
  byFunction(() => 23) // ok
  byFunction(() => method) // ok
  byFunction(parenMethod) // ok but beware
  // byFunction(method) // not ok - The compiler does not do ETA-expansion for methods
  // byFunction(parenMethod()) // not ok


}
