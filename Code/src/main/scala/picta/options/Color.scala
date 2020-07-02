package picta.options

import ujson.{Value}
import upickle.default._

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
}
