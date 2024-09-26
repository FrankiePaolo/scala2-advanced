package lectures.part5ts

object Variance extends App {

    trait Animal
    class Dog extends Animal
    class Cat extends Animal
    class Crocodile extends Animal

    // What is variance?
    // "inheritance" - type substitution of generics

    class Cage[T]
    // should a Cage[Cat] also inherit from Cage[Animal]?
    // 1. yes: covariance
    class CovariantCage[+T]
    val ccage: CovariantCage[Animal] = new CovariantCage[Cat]

    // 2. no: invariance
    class InvariantCage[T]
    // val icage: InvariantCage[Animal] = new InvariantCage[Cat] // doesn't compile

    // 3. hell, no! contravariance
    class ContravariantCage[-T]
    val xcage: ContravariantCage[Cat] = new ContravariantCage[Animal]

    class InvariantCage2[T](val animal: T) // invariant

    // covariant positions
    class CovariantCage2[+T](val animal: T) // COVARIANT POSITION

    // class ContravariantCage2[-T](val animal: T) // doesn't compile

  /*
    val catCate: ContravariantCage2[Cat] = new ContravariantCage2[Animal](new Crocodile)
   */

  // class CovariantVariableCage[+T](var animal: T) // types of vars are in CONTRAVARIANT POSITION
  /*
    val ccage: CovariantVariableCage[Animal] = new CovariantVariableCage[Cat](new Cat)
    ccage.animal = new Crocodile
   */
    //class ContravariantVariableCage[-T](val animal: T)  // also in CONTRAVARIANT POSITION
    class InvariantVariableCage[T](val animal: T) //ok

//    trait AnotherCovariantCage[+T]{
//        def addAnimal(animal: T)  //CONTRAVARIANT POSITION
//    }

//    val contravariantCage : ContravariantCage[Animal] = new ContravariantCage[Dog]
//    contravariantCage.add(new Cat)

    class AnotherContravariantCage[-T]{
        def addAnimal(animal:T) = true
    }
    val anotherContravariantCage : AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
    anotherContravariantCage.addAnimal(new Cat)
    class Kitty extends Cat
    anotherContravariantCage.addAnimal(new Kitty)
    class MyList[+A]{
        def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
    }
    val emptyList = new MyList[Kitty]
    val animals = emptyList.add(new Kitty)
    val moreAnimals = animals.add(new Cat)
    val evenMoreAnimals = moreAnimals.add(new Dog)

    // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

    // return types
    class PetShop[-T]{
        // def get(isItAPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
        /*
            val catShop = new PetShop[Animal]{
                def get(isItAPuppy: Boolean): Animal = new Cat
            }
            val dogShop: PetShop[Dog] = catShop
            dogShop.get(true) // EVIL CAT!
         */
        def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
    }

    val shop: PetShop[Dog] = new PetShop[Animal]
    // val evilCat = shop.get(true, new Cat) CAT DOES NOT EXTEND DOG
    class TerraNova extends Dog
    val bigFurry = shop.get(true, new TerraNova)

    /*
        Big rule:
        - method arguments are in CONTRAVARIANT position
        - return types are in COVARIANT position
     */

  /*
        1. Invariant, covariant, contravariant
            Parking[T](things: List[T]) {
              park(vehicle: T)
              impound(vehicles: List[T])
              checkVehicles(conditions: String): List[T]
            }

        2. used someone else's API: IList[T]
        3. Parking = monad!
          - flatMap
   */

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle
  class IList[T]

  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](vehicles: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  class XParking[-T](vehicles: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: Function1[R, XParking[S]]): XParking[S] = ???

  }

  /*
    Rule of thumb
    - use covariance = COLLECTION OF THINGS
    - use contravariance = GROUP OF ACTIONS
   */

  class CParking2[+T](vehicles: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ???
    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class XParking2[-T](vehicles: IList[T]) {
    def park(vehicle: T): XParking2[T] = ???
    def impound[S <: T](vehicles: IList[S]): XParking2[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }





}
