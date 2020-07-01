import java.io.File

import com.github.tototoshi.csv._
import os._
import picta.IO.IO._
import ujson.Obj

object Example extends App {
  sealed trait Property[T] {
    def serialize(seq: List[T]): String
  }

  trait LowPrioritySerializer {
    implicit object StringSerializer extends Property[String] {
      def serialize(seq: List[String]): String = seq.toString()
    }
  }

  object Property extends LowPrioritySerializer {
    implicit object IntSerializer extends Property[Int] {
      def serialize(seq: List[Int]): String = seq.toString()
    }
  }

  case class A[T: Property](lst: Option[List[T]] = None) {
    def +[T: Property](new_list: List[T]): A[T] = this.copy(Some(new_list))
  }

  case class B[T1: Property, T2: Property](value: Option[A[T1]] = None, value2: List[T2]) {
    def +[Z: Property](new_A: A[Z]): B[Z, T2] = B[Z, T2](value=Some(new_A), value2=value2)
  }
}

//  val wd = get_wd
//  val filepath = get_wd + "/iris_csv.csv"
//  val df = read_csv(wd, filepath)
//  println(df.keySet)
//  println(df("petalwidth"))

//trait Base {
//g.
//[success] Total time: 2 s, completed 1 Jul 2020, 18:54:26
//[info] running Example
//[success] Total time: 1 s, completed 1 Jul 2020, 18:54:27
//[IJ]sbt:picta>
//}
//    def serialize(): String
//  }
//
//  trait Animal extends Base
//  trait Mammal extends Animal
//  trait Reptile extends Animal
//
//  case class Lizard(name: String, tail: Boolean) extends Reptile {
//    def serialize(): String = s"""{name: $name, tail: $tail}"""
//  }
//
//  case class Cat(name: String, age: Int) extends Mammal {
//    def serialize(): String = s"""{name: $name, age: $age}"""
//  }
//
//  case class Fish(species: String) extends Animal {
//    def serialize(): String = s"""{species: $species}"""
//  }
//
//  case class Pets(group_name: String, cat: Option[Cat] = None, lizard: Option[Lizard] = None, fish: Fish) extends Base {
//    override def serialize(): String = {
//
//      // cat and lizard serialize in a similar fashion
//      val cat_object = cat match {
//        case Some(c) => s"""cats: ${c.serialize()}"""
//        case _ => ""
//      }
//
//      val lizard_object = lizard match {
//        case Some(d) => s"""lizards: ${d.serialize()}"""
//        case _ => ""
//      }
//
//      // fish serializes in a different way
//      val fish_object = s"""fish: ${fish.serialize()}"""
//
//      s"""{$lizard_object, $cat_object, $fish_object}"""
//    }
//  }
//
//  val bob = Cat("Bob", 42)
//  val jill = Lizard("Jill", true)
//
//  val pets = Pets("my group", Some(bob), Some(jill), Fish("goldfish")).serialize()
//
//  println(pets)