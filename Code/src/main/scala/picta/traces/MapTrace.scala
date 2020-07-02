package picta.traces

import picta.Utils._
import picta.options.ColorOptions.Color
import picta.options.Line
import ujson.{Obj, Value}

object MapTrace {
  final case class Map[T: Color](lat: List[Double] = Nil, lon: List[Double] = Nil, trace_mode: Option[String] = None,
                       line: Option[Line[T]] = None) extends Trace {

    val trace_name = "map"

    def +[Z: Color](l: Line[Z]): Map[Z] = this.copy(line = Some(l))

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
}


