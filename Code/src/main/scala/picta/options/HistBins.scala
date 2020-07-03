package picta.options

import picta.Utils.emptyObject
import picta.common.Component
import ujson.{Obj, Value}

class HistBins(start: Option[Double] = None, end: Option[Double] = None, size: Option[Double] = None) extends Component {
  def serialize: Value = {
    val start_ = start match {
      case Some(x) => Obj("start" -> x)
      case None => emptyObject
    }

    val end_ = start match {
      case Some(x) => Obj("end" -> x)
      case None => emptyObject
    }

    val size_ = start match {
      case Some(x) => Obj("size" -> x)
      case None => emptyObject
    }

    List(start_, end_, size_).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}

case class Xbins(start: Option[Double] = None, end: Option[Double] = None, size: Option[Double] = None) extends
  HistBins(start=start, end=end, size=size)

case class Ybins(start: Option[Double] = None, end: Option[Double] = None, size: Option[Double] = None) extends
  HistBins(start=start, end=end, size=size)


