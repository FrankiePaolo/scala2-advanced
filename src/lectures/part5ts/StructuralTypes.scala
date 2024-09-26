package lectures.part5ts

object StructuralTypes extends App {
  //  structural types
  type JavaClosable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("yeah yeah I'm closing")
  }

  // def closeQuietly(closable: JavaClosable OR HipsterCloseable)
  type UnifiedCloseable = {
    def close(): Unit
  } // STRUCTURAL TYPE

  def closeQuietly(unifiedCloseable: UnifiedCloseable): Unit = unifiedCloseable.close()

  closeQuietly(new JavaClosable {
    override def close(): Unit = println("Java closable closing")
  })
  closeQuietly(new HipsterCloseable)

  // TYPE REFINEMENTS
  type AdvancedCloseable = JavaClosable {
    def closeSilently(): Unit
  }

  class AdvancedJavaCloseable extends JavaClosable {
    override def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShh(advancedCloseable: AdvancedCloseable): Unit = advancedCloseable.closeSilently()

  closeShh(new AdvancedJavaCloseable)

  // Using structural types as standalone types
  def altClose(closable: { def close(): Unit }): Unit = closable.close()

  // type-checking => duck typing
  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark")
  }

  class Car {
    def makeSound(): Unit = println("vroom")
  }

  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car

  // static duck typing

  // CAVEAT: based on reflection

  /*
    Exercises
   */

  // 1.
  trait CBL[+T] {
    def head: T
    def tail: CBL[T]
  }

  class Human {
    def head: Brain = new Brain
  }

  class Brain {
    override def toString: String = "BRAINZ!"
  }

  def f[T](somethingWithAHead: { def head: T }): Unit = println(somethingWithAHead.head)

  /*
    f is compatible with a CBL and with a Human?
   */

  f(new Human) // ? Yes
  f(new CBL[Brain] {
    override def head: Brain = new Brain
    override def tail: CBL[Brain] = new CBL[Brain] {
      override def head: Brain = new Brain
      override def tail: CBL[Brain] = this
    }
  })



  // 2.
  object HeadEqualizer {
    type Headable[T] = { def head: T }

    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }

  HeadEqualizer.===(new Human, new Human)

  /*
    is compatible with a CBL and with a Human?
   */

}
