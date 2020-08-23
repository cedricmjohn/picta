package org.carbonateresearch.picta

import ujson.Value
import upickle.default._

/**
 * This case class represents an RGB color.
 *
 * @param r: Red.
 * @param g: Green.
 * @param b: Blue.
 */
case class RGB(r: Double, g: Double, b: Double) {
  override def toString: String = s"rgb(${r}, ${g}, ${b})"
}

/**
 * This case class represents and RGBA color
 *
 * @param r: Red.
 * @param g: Green.
 * @param b: Blue.
 * @param a: Alpha.
 */
case class RGBA(r: Double, g: Double, b: Double, a: Double) {
  override def toString: String = s"rgba(${r}, ${g}, ${b}, ${a})"
}

/** This object creates a priority implicit which allows either a List[String] or List[Double]
 * to be added as chart components.
 * */
object ColorOptions {

  sealed trait Color[T] {
    def serialize(seq: List[T]): Value
  }

  /** The lower priority option is the List[String]; the compiler matches with this second. */
  trait StringColor {

    implicit object ColorString extends Color[String] {
      def serialize(seq: List[String]): Value = {
        seq.length match {
          case 1 => transform(seq(0)).to(Value)
          case _ => transform(seq).to(Value)
        }
      }
    }

  }

  private[picta] trait RGBColor extends StringColor {

    implicit object RGBColor extends Color[RGB] {
      def serialize(lst: List[RGB]): Value = {
        lst.length match {
          case 1 => transform(lst(0).toString).to(Value)
          case _ => transform(lst.map(x => x.toString)).to(Value)
        }
      }
    }

  }

  private[picta] trait RGBAColor extends RGBColor {

    implicit object RGBAColor extends Color[RGBA] {
      def serialize(lst: List[RGBA]): Value = {
        lst.length match {
          case 1 => transform(lst(0).toString).to(Value)
          case _ => transform(lst.map(x => x.toString)).to(Value)
        }
      }
    }

  }

  /** This is first priority option; List[Double]. If the list does not match either List[Double] or List[String],
   * an error is thrown.
   */
  object Color extends RGBAColor {

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
