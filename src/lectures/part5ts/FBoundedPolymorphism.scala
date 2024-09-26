package lectures.part5ts

object FBoundedPolymorphism extends App {

  // Solution 1 - naive
//  trait Animal {
//    def breed: List[Animal]
//  }
//
//  class Cat extends Animal {
//    override def breed: List[Animal] = List(new Cat)
//  }
//
//  class Dog extends Animal {
//    override def breed: List[Animal] = List(new Dog)
//  }

  // Solution 2 - FBP
  trait Animal[A <: Animal[A]] { // recursive type: F-Bounded Polymorphism
    def breed: List[Animal[A]]
  }

  class Cat extends Animal[Cat] {
    override def breed: List[Animal[Cat]] = List(new Cat)
  }

  class Dog extends Animal[Dog] {
    override def breed: List[Animal[Dog]] = List(new Dog)
  }

  trait Entity[E <: Entity[E]] // ORM

  class Person extends Comparable[Person] { // FBP
    override def compareTo(o: Person): Int = ???
  }

// Solution 3 - FBP + self-types
  trait Animal2[A <: Animal2[A]] { self: A =>
    def breed: List[Animal2[A]]
  }

  class Cat2 extends Animal2[Cat2] {
    override def breed: List[Animal2[Cat2]] = List(new Cat2)
  }

  class Dog2 extends Animal2[Dog2] {
    override def breed: List[Animal2[Dog2]] = List(new Dog2)
  }

  trait Fish extends Animal2[Fish]
  class Shark extends Fish {
    override def breed: List[Animal2[Fish]] = List(new Shark)
  }

  class Cod extends Fish {
    override def breed: List[Animal2[Fish]] = List(new Shark) // wrong
  }

  // Exercise

  // Solution 4: type classes!

  trait Animal3 // pure type classes

  trait CanBreed[A] {
    def breed(a: A): List[A]
  }

  class Dog3 extends Animal3
  object Dog3 {
    implicit object DogsCanBreed extends CanBreed[Dog3] {
      def breed(a: Dog3): List[Dog3] = List()
    }
  }

  implicit class CanBreedOps[A](animal: A) {
    def breed(implicit canBreed: CanBreed[A]): List[A] =
      canBreed.breed(animal)
  }

  val dog3 = new Dog3
  //dog3.breed // List[Dog3]!

  /*
    new CanBreedOps[Dog3](dog3).breed(Dog3.DogsCanBreed)

    implicit value to pass to breed: Dog3.DogsCanBreed
   */

  class Cat3 extends Animal3
  object Cat3 {
    implicit object CatsCanBreed extends CanBreed[Cat3] {
      def breed(a: Cat3): List[Cat3] = List()
    }
  }

  val cat3 = new Cat3
  //cat3.breed

  // Solution 5
  trait Animal5[A] { // pure type classes
    def breed(a: A): List[A]
  }

  class Dog5
  object Dog5 {
    implicit object DogAnimal5 extends Animal5[Dog5] {
      def breed(a: Dog5): List[Dog5] = List()
    }
  }

  class Cat5
  object Cat5 {
    implicit object CatAnimal5 extends Animal5[Cat5] {
      def breed(a: Cat5): List[Cat5] = List()
    }
  }

  implicit class Animal5Ops[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal5[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val dog5 = new Dog5
  //dog5.breed


}
