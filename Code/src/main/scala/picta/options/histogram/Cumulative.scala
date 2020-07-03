package picta.options.histogram

import picta.Utils.emptyObject
import picta.common.Component
import ujson.{Obj, Value}

case class Cumulative(enabled: Option[Boolean] = None, direction: Option[String] = None,
                      currentbin: Option[String] = None) extends Component {

  def serialize: Value = {
    val enabled_ = enabled match {
      case Some(x) => Obj("enabled" -> x)
      case None => emptyObject
    }

    val direction_ = direction match {
      case Some(x) => Obj("direction" -> x)
      case None => emptyObject
    }

    val currentbin_ = currentbin match {
      case Some(x) => Obj("currentbin" -> x)
      case None => emptyObject
    }

    List(enabled_, direction_, currentbin_).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}
