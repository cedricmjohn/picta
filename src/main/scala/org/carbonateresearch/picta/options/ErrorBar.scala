package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.OptionWrapper.{Blank, Opt, Empty}
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import ujson.Obj

private[picta] trait ErrorMode
case object PERCENT extends ErrorMode
case object CONSTANT extends ErrorMode
case object SQRT extends ErrorMode
case object DATA extends ErrorMode

trait ErrorBar extends Component {
  val mode: ErrorMode
  private[picta] val visible: Boolean

  val value: Opt[Double]
  val array: Opt[List[Double]]

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