// priority implicits
sealed trait Stringifier[T] {
  def stringify(lst: List[T]): String
}

trait Int_Stringifier {
  implicit object IntStringifier extends  Stringifier[Int] {
    def stringify(lst: List[Int]): String = lst.toString()
  }
}

object Double_Stringifier extends Int_Stringifier {
  implicit object DoubleStringifier extends Stringifier[Double] {
    def stringify(lst: List[Double]): String = lst.toString()
  }
}

import Double_Stringifier._

object Example extends App {

  trait Animal[T0] {
    def incrementAge(): Animal[T0]
  }

  case class Food[T0: Stringifier]() {
    def getCalories  = 100
  }

  case class Dog[T0]
  (age: Int = 0, food: Food[T0] = Food()) extends Animal[String] {
    def incrementAge(): Dog[T0] = this.copy(age = age + 1)
  }
}
