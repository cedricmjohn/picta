package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.OptionWrapper.{Blank, Empty, Opt}
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import ujson.Obj

/** This specifies the behaviour of an error bar. */
trait ErrorBar extends Component {

  /** This specifies how the error is calculated. */
  val mode: ErrorMode
  /** This is only used if we use SQRT, CONSTANT or PERCENT as mode - specifies the value of the error. */
  val value: Opt[Double]
  /** This is only used if we want to pass in a per point error value. */
  val array: Opt[List[Double]]
  /** This specifies whether the error bar is visible on the rendered chart. */
  private[picta] val visible: Boolean

  private[picta] def serialize = {
    val meta = Obj(
      "type" -> mode.toString.toLowerCase,
      "visible" -> visible,
    )

    val value_ = value.option match {
      case Some(x) => Obj("value" -> x)
      case None => jsonMonoid.empty
    }

    val array_ = array.option match {
      case Some(x) => Obj("array" -> x)
      case None => jsonMonoid.empty
    }

    List(meta, value_, array_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}

case class XError(mode: ErrorMode, visible: Boolean = true, value: Opt[Double] = Blank, array: Opt[List[Double]] = Empty) extends ErrorBar

case class YError(mode: ErrorMode, visible: Boolean = true, value: Opt[Double] = Blank, array: Opt[List[Double]] = Empty) extends ErrorBar