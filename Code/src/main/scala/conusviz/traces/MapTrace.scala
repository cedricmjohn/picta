package conusviz.traces

import conusviz.Utils._
import conusviz.options.LineOptions.Line
import ujson.{Obj, Value}

final case class MapTrace(lat: List[Double] = Nil, lon: List[Double] = Nil, trace_mode: Option[String] = None,
                     line: Option[Line] = None) extends Trace {

  val trace_name = "map"

  override def serialize: Value = {
    val acc = Obj("lat" -> lat, "lon" -> lon, "type" -> "scattergeo")

    val m = trace_mode match {
      case Some(m) => Obj("mode" -> m)
      case None => emptyObject
    }

    val l = line match {
      case Some(l) => Obj("line" -> l.serialize)
      case None => emptyObject
    }

    acc.obj ++ m.obj ++ l.obj
  }
}