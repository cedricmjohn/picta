package picta.options

import picta.common.Component
import ujson.{Value}
import upickle.default._

//sealed trait Color extends Component

object ColorOptions {
  sealed trait Color[T] {
    def serialize(seq: List[T]): Value
  }

  trait LowPriorityOption {
    implicit object ColorString extends Color[String] {
      def serialize(seq: List[String]): Value = {
        seq.length match {
          case 1 => transform(seq(0)).to(Value)
          case _ => transform(seq).to(Value)
        }
      }
    }
  }

  object Color extends LowPriorityOption {
    implicit object ColorDouble extends Color[Double] {
      def serialize(seq: List[Double]): Value = {
        seq.length match {
          case 1 => transform(seq(0)).to(Value)
          case _ => transform(seq).to(Value)
        }
      }
    }
  }








//  case class Colors(color: List[String]) extends Color {
//    def serialize(): Value = {
//      color.length match {
//        case 1 =>Obj("color" -> color(0))
//        case _ => Obj("color" -> color)
//      }
//    }
//  }
//
//  case class ColorValues(color: List[Double]) extends Color {
//    def serialize(): Value = {
//      color.length match {
//        case 1 =>Obj("color" -> color(0))
//        case _ => Obj("color" -> color)
//      }
//    }
//  }
}
