package lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {
  // JVM threads
  /*val aThread = new Thread(() => println("I'm running in parallel"))
  aThread.start() // gives the signal to the JVM to start a JVM thread
  // creates a JVM thread => OS thread
  aThread.join()

  val threadHello = new Thread(()=> (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(()=> (1 to 5).foreach(_ => println("goodbye")))

  threadHello.start()
  threadGoodbye.start()

  // executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  pool.shutdown()
  //pool.execute(() => println("should not appear")) // throws an exception in the calling thread

  //pool.shutdownNow()
  println(pool.isShutdown)*/

  def runInParallel = {
    var x = 0

    val thread1 = new Thread(() => x = 1)
    val thread2 = new Thread(() => x = 2)

    thread1.start()
    thread2.start()

    println(x)
  }
  // for (_ <- 1 to 100) runInParallel
  // race condition

  class BankAccount(var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    println("I've bought " + thing)
    println("my account is now " + account)
  }

  //option1: use synchronized()
  def buySafe(account: BankAccount, thing: String, price: Int) = {
    account.synchronized {
      // Can't be executed by two threads at the same time
      account.amount -= price
      println("I've bought " + thing)
      println("my account is now " + account)
    }
  }

  //option2: use @volatile

  /*
   * Excercises
   *
   * 1) Construct 50 "inception" threads
   *      Thread1 -> Thread2 -> Thread3 -> ...
   *      println("hello from thread #3")
   *    in REVERSE ORDER
   */

  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
    if (i < maxThreads) {
      val newThread = inceptionThreads(maxThreads, i + 1)
      newThread.start()
      newThread.join()
    }
    println(s"Hello from thread $i")
  })

  inceptionThreads(50).start()

   /*
   * 2)
   *
   */
  var x = 0
  val threads = (1 to 50).map(_ => new Thread(() => x += 1))
  threads.foreach(_.start())
  /*
    1) what is the biggest value possible for x? 50
    2) what is the smallest value possible for x? 1
   */
  threads.foreach(_.join())
  println(x)
  /*
    * 3) sleep fallacy
   */
  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "Scala is awesome"
  })

  /*
    What's the value of message at the end of the below code? Almost always "Scala is awesome"
    Is it guaranteed? NO!

    Why? Because of the thread scheduler
    Let's see an example:
    (main thread)
      message = "Scala sucks"
      awesomeThread.start()
      sleep() - relieves execution
    (awesome thread)
      sleep() - relieves execution
    (OS gives the CPU to some important thread - takes CPU for more than 2 seconds)
    (OS gives the CPU back to the main thread)
      println("Scala sucks")
    (OS gives the CPU to awesome thread)
      message = "Scala is awesome" - never happens
   */

  // How do we fix this?
  // Syncronizing does not work here



  message = "Scala sucks"
  awesomeThread.start()
  Thread.sleep(2001)
  awesomeThread.join() // wait for the awesome thread to finish
  println(message)


  /*for (_ <- 1 to 1000) {
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buySafe(account, "shoes", 3000))
    val thread2 = new Thread(() => buySafe(account, "iphone12", 4000))

    thread1.start()
    thread2.start()
    Thread.sleep(100)
    if (account.amount != 43000) println("AHA: " + account.amount)

  }*/


}
