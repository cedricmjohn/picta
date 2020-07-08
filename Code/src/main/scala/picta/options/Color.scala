package picta.options

import ujson.Value
import upickle.default._

/** This object creates a priority implicit which allows either a List[String] or List[Double] to be added as chart
 * components.
 */
object ColorOptions {

  sealed trait Color[T] {
    def serialize(seq: List[T]): Value
  }

  /** The lower priority option is the List[String]; the compiler matches with this second. */
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

  /** This is first priority option; List[Double]. If the list does not match either List[Double] or List[String],
   * an error is thrown.
   */
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
