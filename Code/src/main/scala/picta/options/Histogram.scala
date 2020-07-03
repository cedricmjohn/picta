package picta.options

import ujson.{Obj, Value}
import picta.Utils.emptyObject
import picta.common.Component

case class Histogram(cumulative: Option[Boolean] = None, histnorm: Option[String] = None, histfunc: Option[String] = None,
                     autobinx: Option[Boolean] = None, autobiny: Option[Boolean] = None, xbins: Option[Xbins],
                     ybins: Option[Ybins]) extends Component {

  def serialize: Value = {
    val cumulative_ = cumulative match {
      case Some(x) => Obj("cumulative" -> Obj("enabled" -> x))
      case None => emptyObject
    }

    val histnorm_ = histnorm match {
      case Some(x) => Obj("histnorm" -> x)
      case None => emptyObject
    }

    val histfunc_ = histfunc match {
      case Some(x) => Obj("histfunc" -> x)
      case None => emptyObject
    }

    val xbin_ = xbins match {
      case Some(x) => Obj("xbins" -> x.serialize)
      case None => emptyObject
    }

    val ybin_ = ybins match {
      case Some(x) => Obj("ybins" -> x.serialize)
      case None => emptyObject
    }

    List(cumulative_, histnorm_, histfunc_, xbin_, ybin_).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}