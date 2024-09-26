package lectures.part5ts

object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  // per-instance
  val outer = new Outer
  val inner = new outer.Inner // outer.Inner is a TYPE

  val oo = new Outer
  val otherInner: oo.Inner = new oo.Inner

  outer.print(inner)


  // path-dependent types

  // Outer#Inner
  outer.printGeneral(otherInner)
  oo.printGeneral(inner) // Outer#Inner

  /*
    Exercise
    DB keyed by Int or String, but maybe others
   */

  /*
    use path-dependent types
    abstract type members and/or type aliases
   */

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): Item[ItemType] = ???

  get[IntItem](42) // returns IntItem
  get[StringItem]("home") // returns StringItem

  //get[IntItem]("scala") // should NOT compile
}
