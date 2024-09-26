package lectures.part5ts

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object HigherKindedTypes extends App {

  trait AHigherKindedType[F[_]]

  trait MyList[A] {
    def flatMap[B](f: A => MyList[B]): MyList[B]
  }

  trait MyOption[A] {
    def flatMap[B](f: A => MyOption[B]): MyOption[B]
  }

  trait MyFuture[A] {
    def flatMap[B](f: A => MyFuture[B]): MyFuture[B]
  }

  // combine/multiply List(1,2) x List("a", "b") => List(1a, 1b, 2a, 2b)

  def multiply[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  def multiply[A, B](optionA: Option[A], optionB: Option[B]): Option[(A, B)] =
    for {
      a <- optionA
      b <- optionB
    } yield (a, b)

  def multiply[A, B](futureA: Future[A], futureB: Future[B]): Future[(A, B)] =
    for {
      a <- futureA
      b <- futureB
    } yield (a, b)

  println(multiply(List(1, 2), List("a", "b")))

  // use HKT

  trait Monad[F[_], A] { // Higher Kinded Type class
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  implicit class MonadList[A](list: List[A]) extends Monad[List, A] {
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: A => B): List[B] = list.map(f)
  }

  implicit class MonadOption[A](option: Option[A]) extends Monad[Option, A] {
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: A => B): Option[B] = option.map(f)
  }

  def multiply[F[_], A, B](implicit monadA: Monad[F, A], monadB: Monad[F, B]): F[(A, B)] = {
    for {
      a <- monadA
      b <- monadB
    } yield (a, b)
  }

  /*
    ma.flatMap(a => mb.map(b => (a, b)))
   */

  val monadList = new MonadList(List(1, 2))
  monadList.flatMap(x => List(x, x + 1)) // List[Int]
  // Monad[List, Int] => List[Int]
  monadList.map(_ * 2) // List[Int]
  // Monad[List, Int] => List[Int]
  println(multiply(List(1,2),List("a", "b")))
  println(multiply(Some(2),Some("scala")))
}
