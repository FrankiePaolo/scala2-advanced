package lectures.part4implicits

object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0

    def sqrt: Double = Math.sqrt(value)

    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }

      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }

  }

  new RichInt(42).sqrt
  42.isEven // new RichInt(42).isEven
  // type enrichment = pimping
  1 to 10

  import scala.concurrent.duration._

  3.seconds

  /*
    Enrich the String class
    - asInt
    - encrypt

    Keep enriching the Int class
    - times(function)
    3.times(() => ...)
    - *
    3 * List(1,2) => List(1,2,1,2,1,2)
   */

  implicit class RichString(string: String) {
    def asInt: Int = Integer.valueOf(string) // java.lang.Integer -> Int

    def encrypt(cypherDistance: Int): String = string.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  3.times(() => println("Scala Rocks!"))
  println(2 * List(1, 2, 3))


}

