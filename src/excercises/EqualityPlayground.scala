package excercises

import lectures.part4implicits.TypeClasses.{HtmlSerializer, User}

object EqualityPlayground extends App {

  /*
  Equality
 */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(a, b)
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  val john: User = User("John", 32, "john@prova.com")
  val anotherJohn: User = User("John", 45, "anotherjohn@prova.com")
  println(Equal(john, anotherJohn))
  // AD-HOC polymorphism

  /*
  Exercise - improve the Equal TC with an implicit conversion class
  ===(a, b)
  !==(a, b)
 */

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, other)
    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, other)
  }

  println(john === anotherJohn)
  println(john !== anotherJohn)

  /*
     john.===(anotherJohn)
      new TypeSafeEqual(john).===(anotherJohn)
      new TypeSafeEqual(john).===(anotherJohn)(NameEquality)
   */

  // context bounds
  def htmlBoilerplate[T](content: T)(implicit serializer: HtmlSerializer[T]): String =
    s"<html><body>${serializer.serialize(content)}</body></html>"

  def htmlSugar[T: HtmlSerializer](content: T): String = {
    val serializer = implicitly[HtmlSerializer[T]]
    // use serializer
    s"<html><body>${serializer.serialize(content)}</body></html>"
  }

  println(htmlBoilerplate(john))
  println(htmlSugar(john))

}


