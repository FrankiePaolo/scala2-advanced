package excercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
  /*
    EXERCISE - Implement a functional set
   */
  def apply(elem: A): Boolean = contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]  // union

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

  /*
    EXERCISE #2
      - removing an element
      - intersection with another set
      - difference with another set
   */
  def -(elem: A): MySet[A]
  def --(anotherSet: MySet[A]): MySet[A]  // difference
  def &(anotherSet: MySet[A]): MySet[A]

  // EXERCISE #3 - implement unary_! = NEGATION of a set
  // set[1,2,3] => negation = all the elements NOT in the set
  def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A] {
  def contains(elem: A): Boolean = false
  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()
  def -(elem: A): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = this
  def &(anotherSet: MySet[A]): MySet[A] = this
  def unary_! : MySet[A] = new PropertyBasedSet[A](x => true)
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(elem: A): Boolean = elem == head || tail.contains(elem)

  def +(elem: A): MySet[A] = if (this.contains(elem)) this else new NonEmptySet[A](elem, this)

  /*
    [1 2 3] ++ [4 5] =
    [2 3] ++ [4 5] + 1 =
    [3] ++ [4 5] + 1 + 2 =
    [] ++ [4 5] + 1 + 2 + 3 =
    [4 5] + 1 + 2 + 3 = [4 5 1 2 3]
   */
  def ++(anotherSet: MySet[A]): MySet[A] = tail ++ anotherSet + head

  def map[B](f: A => B): MySet[B] = tail.map(f) + f(head)

  def flatMap[B](f: A => MySet[B]): MySet[B] = tail.flatMap(f) ++ f(head)

  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail.filter(predicate)
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
  def -(elem: A): MySet[A] = if (head == elem) tail else tail - elem + head
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // intersection = filtering!
  def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet(x))
  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}

// all elements of type A which satisfy a property
// { x in A | property(x) }
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A]{
  def contains(elem: A): Boolean = property(elem)
  def +(elem: A): MySet[A] = new PropertyBasedSet[A](x => property(x) || x == elem)
  def ++(anotherSet: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => property(x) || anotherSet(x))
  // all integers => (_ % 3) => [0 1 2]
  def map[B](f: A => B): MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  def foreach(f: A => Unit): Unit = politelyFail
  def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))
  def -(elem: A): MySet[A] = filter(x => x != elem)
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // intersection = filtering!
  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))
  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1, 2, 3, 4)
  s + 5 ++ MySet(-1, -2) + 3 flatMap (x => MySet(x, x * 10)) filter(_ % 2 == 0) foreach println

  val negative = !s // s.unary_! = all the naturals not equal to 1,2,3,4
  println(negative(2))

  val negativeEven = negative.filter(_ % 2 == 0) + 5
  println(negativeEven(5))

  val mapped = s.map(x => x * 2)
  println(mapped(2))

}

