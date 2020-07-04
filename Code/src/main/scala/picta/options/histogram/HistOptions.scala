package picta.options.histogram

import picta.common.OptionWrapper._
import picta.common.Monoid._
import picta.common.Component

import ujson.{Obj, Value}

object HistNormType extends Enumeration {
  type HistNormType = Value
  val NUMBER = Value("")
  val PERCENT, DENSITY, PROBABILITY_DENSITY = Value
}

import HistNormType.HistNormType

object HistFuncType extends Enumeration {
  type HistFuncType = Value
  val COUNT, SUM, AVG, MIN, MAX = Value
}

import HistFuncType.HistFuncType

case class HistOptions(cumulative: Opt[Cumulative]=Blank, histnorm: Opt[HistNormType]=Blank, histfunc: Opt[HistFuncType]=Blank,
                       xbins: Opt[Xbins]=Blank, ybins: Opt[Ybins]=Blank) extends Component {

  def +(new_cumulative: Cumulative): HistOptions = this.copy(cumulative = new_cumulative)
  def +(new_xbins: Xbins): HistOptions = this.copy(xbins = new_xbins)
  def +(new_ybins: Ybins): HistOptions = this.copy(ybins = new_ybins)

  def serialize(): Value = {
    val cumulative_ = cumulative.asOption match {
      case Some(x) => Obj("cumulative" -> x.serialize)
      case None => JsonMonoid.empty
    }

    val histnorm_ = histnorm.asOption match {
      case Some(x) => Obj("histnorm" -> x.toString.toLowerCase)
      case None => JsonMonoid.empty
    }

    val histfunc_ = histfunc.asOption match {
      case Some(x) => Obj("histfunc" -> x.toString.toLowerCase)
      case None => JsonMonoid.empty
    }

    val xbins_ = xbins.asOption match {
      case Some(x) => Obj("xbins" -> x.serialize)
      case None => JsonMonoid.empty
    }

    val ybins_ = ybins.asOption match {
      case Some(x) => Obj("ybins" -> x.serialize)
      case None => JsonMonoid.empty
    }

    List(cumulative_, histnorm_, histfunc_, xbins_, ybins_).foldLeft(JsonMonoid.empty)((a, x) => a |+| x)
  }
}