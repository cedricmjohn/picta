import java.io.File

import com.github.tototoshi.csv._
import os._
import picta.IO.IO._
import picta.Serializer
import ujson.Obj

import scala.collection.mutable

object Example extends App {

  case class Marker[T: Serializer](lst: List[T])

  case class test[T: Serializer](value: Option[Marker[T]] = None)(implicit s0: Serializer[T]) {
    def serialize() = value match {
      case Some(m) => println(s0.serialize(m.lst))
      case _ => println("...")
    }
  }

  val x = Marker(List(1, 2, 3))

  val t = test(Some(x))

  println(t.serialize())







}

//  val wd = get_wd
//  val filepath = get_wd + "/iris_csv.csv"
//  val df = read_csv(wd, filepath)
//  println(df.keySet)
//  println(df("petalwidth"))

//trait Base {
//
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