package lectures.part1as

object AdvancedPatternMatching extends App {
  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => s"The only element is $head."
    case _ =>
  }

  println(description)

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic like above
   */

  // custom pattern matching
  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a years old."
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status."
  }

  println(legalStatus)

  /*
    Exercise.
   */
  // 1. Implement a custom pattern matching for a Social Security Number
  // SSN - 3 digits, a hyphen, 2 digits, a hyphen, 4 digits
  object SSN {
    def unapply(ssn: String): Option[(Int, Int, Int)] = {
      val ssnPattern = "([0-9]{3})-([0-9]{2})-([0-9]{4})".r
      ssn match {
        case ssnPattern(first, second, third) => Some(first.toInt, second.toInt, third.toInt)
        case _ => None
      }
    }
  }

  val ssn = "123-45-6789"
  val ssnMatch = ssn match {
    case SSN(first, second, third) => s"Found an SSN with parts: $first-$second-$third"
    case _ => "Not an SSN"
  }

  println(ssnMatch)

  // infix patterns
  case class Or[A, B](a: A, b: B) // Either
  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is written as $string"
  }
  println(humanDescription)

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "Starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "Starting with 1, 2"
    case _ => "Something else"
  }
  println(decomposed)

  // custom return types for unapply
  // isEmpty: Boolean, get: something
  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty: Boolean = false
      def get: String = person.name
    }
  }

  println(bob match {
    case PersonWrapper(name) => s"This person's name is $name"
    case _ => "An alien"
  })




}
