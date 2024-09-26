package lectures.part4implicits

import excercises.EqualityPlayground.Equal

object TypeClasses extends App {

  trait HtmlWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HtmlWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  User("John", 32, "john@prova.com").toHtml

  /*
    1 - for the types WE write
    2 - ONE implementation out of quite a number
   */

  // option 2 - pattern matching
  object HtmlSerializerPM {
    def serializeToHtml(value: Any) = value match {
      case User(name, age, email) =>  s"<div>$name ($age yo) <a href=$email/> </div>"
      //case java.util.Date =>
      case _ =>
    }
  }

  /*
    1 - lost type safety
    2 - need to modify the code every time
    3 - still ONE implementation
   */

  trait HtmlSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HtmlSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  private val john: User = User("John", 32, "john@prova.com")
  println(UserSerializer.serialize(john))

  // 1 - we can define serializers for other types
  import java.util.Date
  object DateSerializer extends HtmlSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }

  // 2 - we can define multiple serializers for a single type
  object PartialUserSerializer extends HtmlSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  object HtmlSerializer {
    def serialize[T](value: T)(implicit serializer: HtmlSerializer[T]): String = {
      serializer.serialize(value)
    }

    def apply[T](implicit serializer: HtmlSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HtmlSerializer[Int] {
    override def serialize(value: Int): String = s"<div style='color: blue;'>$value</div>"
  }

  println(HtmlSerializer.serialize(42))
  println(HtmlSerializer.serialize(john))
  // access to the entire type class interface
  println(HtmlSerializer[User].serialize(john))

  implicit class HtmlEnrichment[T](value: T) {
    def toHtml(implicit serializer: HtmlSerializer[T]): String = {
      serializer.serialize(value)
    }
  }
  println(john.toHtml)
  println(2.toHtml)

  /*
    - extend to new types
    - choose implementation
    - super expressive
   */

  /*
    - type class itself --- HtmlSerializer[T] { .. }
    - type class instances (some of which are implicit) --- UserSerializer, IntSerializer
    - conversion with implicit classes --- HtmlEnrichment
   */

}
