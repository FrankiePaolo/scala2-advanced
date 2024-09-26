package lectures.part4implicits
import java.{util => ju}

object ScalaJavaConversions extends App {
  import scala.jdk.CollectionConverters._

  val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)

  val scalaSet = javaSet.asScala

  /*
    Iterator
    Iterable
    ju.List - collection.mutable.Buffer
    ju.Set - collection.mutable.Set
    ju.Map - collection.mutable.Map
   */

  import collection.mutable._
  val numbersBuffer = ArrayBuffer[Int](1, 2, 3)
  val juNumbersBuffer = numbersBuffer.asJava

  println(juNumbersBuffer.asScala eq numbersBuffer) // true

  val numbers = List(1, 2, 3)
  val juNumbers = numbers.asJava
  val backToScala = juNumbers.asScala

  println(backToScala eq numbers) // false
  println(backToScala == numbers) // true

  // juNumbers.add(7) // UnsupportedOperationException

  /*
    Exercise
    create a Scala-Java Optional-Option
      .asScala
   */

  class ToScala[T](value: => T) {
    def asScala: T = value
  }

  implicit def toScalaOption[T](option: ju.Optional[T]): ToScala[Option[T]] = new ToScala(
    if (option.isPresent) Some(option.get) else None
  )

  val juOptional: ju.Optional[Int] = ju.Optional.of(2)
  val scalaOption = juOptional.asScala
  println(scalaOption) // Some(2)

}
