package picta.options.histogram

import picta.Utils.emptyObject
import picta.common.Component
import ujson.{Obj, Value}

case class HistOptions(cumulative: Option[Cumulative] = None, histnorm: Option[String] = None, histfunc: Option[String] = None,
                       autobinx: Option[Boolean] = None, autobiny: Option[Boolean] = None, xbins: Option[Xbins],
                       ybins: Option[Ybins]) extends Component {

  def serialize: Value = {
    val cumulative_ = cumulative match {
      case Some(x) => Obj("cumulative" -> x.serialize)
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

    val autobinx_ = autobinx match {
      case Some(x) => Obj("autobinx" -> x)
      case None => emptyObject
    }

    val autobiny_ = autobiny match {
      case Some(x) => Obj("autobiny" -> x)
      case None => emptyObject
    }

    val xbins_ = xbins match {
      case Some(x) => Obj("xbins" -> x.serialize)
      case None => emptyObject
    }

    val ybins_ = xbins match {
      case Some(x) => Obj("ybins" -> x.serialize)
      case None => emptyObject
    }

    List(cumulative_, histnorm_, histfunc_, autobinx_, autobiny_, xbins_, ybins_).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}