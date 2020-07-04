package picta.options.histogram

import picta.Utils.emptyObject
import picta.common.Component
import ujson.{Obj, Value}

//sealed trait HistNormType
//case object NONE extends HistNormType {
//  override def toString: String = ""
//}
//case object PERCENT extends HistNormType
//case object DENSITY extends HistNormType
//case object PROBABILITY_DENSITY extends HistNormType
//
//sealed trait HistFuncType
//case object COUNT extends HistFuncType
//case object SUM extends HistFuncType
//case object AVG extends HistFuncType
//case object MIN extends HistFuncType
//case object MAX extends HistFuncType

object HistNormType extends Enumeration {
  type HistNormType = Value
  val NUMBER = Value("")
  val PERCENT, DENSITY, PROBABILITY_DENSITY = Value
}

import HistNormType.{HistNormType, NUMBER}

object HistFuncType extends Enumeration {
  type HistFuncType = Value
  val COUNT, SUM, AVG, MIN, MAX = Value
}

import HistFuncType.HistFuncType

case class HistOptions(cumulative: Option[Cumulative]=None, histnorm: Option[HistNormType]=None, histfunc: Option[HistFuncType]=None,
                       xbins: Option[Xbins]=None, ybins: Option[Ybins]=None) extends Component {

  def +(new_cumulative: Cumulative): HistOptions = this.copy(cumulative = Some(new_cumulative))
  def +(new_xbins: Xbins): HistOptions = this.copy(xbins = Some(new_xbins))
  def +(new_ybins: Ybins): HistOptions = this.copy(ybins = Some(new_ybins))

  def serialize(): Value = {
    val cumulative_ = cumulative match {
      case Some(x) => Obj("cumulative" -> x.serialize)
      case None => emptyObject
    }

    val histnorm_ = histnorm match {
      case Some(x) => Obj("histnorm" -> x.toString.toLowerCase)
      case None => emptyObject
    }

    val histfunc_ = histfunc match {
      case Some(x) => Obj("histfunc" -> x.toString.toLowerCase)
      case None => emptyObject
    }

    val xbins_ = xbins match {
      case Some(x) => Obj("xbins" -> x.serialize)
      case None => emptyObject
    }

    val ybins_ = ybins match {
      case Some(x) => Obj("ybins" -> x.serialize)
      case None => emptyObject
    }

    List(cumulative_, histnorm_, histfunc_, xbins_, ybins_).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}

object HistOptions {
  implicit def liftToOption[T](x: T): Option[T] = Option[T](x)
}