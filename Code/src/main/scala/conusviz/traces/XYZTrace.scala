package conusviz.traces

import conusviz.Serializer
import ujson.{Obj, Value}
import conusviz.Utils._
import conusviz.traces.Trace.XYZChartType._

trait XYZTrace extends Trace

object XYZTrace {

  final case class XYZ[T0 : Serializer, T1: Serializer, T2: Serializer]
  (x: List[T0], y: List[T1], z: List[T2], trace_name: String, trace_type: XYZChartType, trace_mode: Option[String] = None,
   n: Int = 0) extends XYZTrace {

    private def createSeriesXYZ[T0 : Serializer, T1: Serializer, T2: Serializer]
    (x: List[T0], y: List[T1], z: List[T2])(implicit s0: Serializer[T0], s1: Serializer[T1], s2: Serializer[T2]): Value = {
      Obj("x" -> s0.serialize(x), "y" -> s1.serialize(y), "z" -> s2.serialize(z))
    }

    private def createSeriesXYZ[T : Serializer](z: List[T], n: Int)(implicit s0: Serializer[T]): Value = {
      if (z.length % n != 0) throw new IllegalArgumentException("The length of 'z' must be divisible by 'n'")
      val list = z.grouped(n).toList.map(e => s0.serialize(e))
      Obj("z" -> list)
    }

    private def createSeriesXYZ[T0 : Serializer, T1: Serializer, T2: Serializer]
    (x: List[T0], y: List[T1], z: List[T2], n: Int)(implicit s0: Serializer[T0], s1: Serializer[T1], s2: Serializer[T2]): Value = {
      if (n != x.length)
        throw new IllegalArgumentException("The number of elements in 'z' must be divisible by length of 'y'")

      if (z.length / n != y.length)
        throw new IllegalArgumentException("The number of elements in 'x' must be divisible by length of an element in 'z'")

      val list: List[Value] = z.grouped(n).toList.map(e => s2.serialize(e))
      Obj("x"-> s0.serialize(x), "y" -> s1.serialize(y), "z" -> list)
    }

    private def createSeries(): Value = n match {
      case 0 => createSeriesXYZ(x, y, z)
      case _ => (x, y) match {
        case (Nil, Nil)  => createSeriesXYZ(z, n)
        case (_, _) => createSeriesXYZ(x, y, z, n)
      }
    }

    def serialize(): Value = {
      val name = Obj("name" -> trace_name, "type" -> trace_type.toString.toLowerCase())

      val t = trace_mode match {
        case Some(t) => Obj("mode" -> t)
        case None => emptyObject
      }

      name.obj ++ t.obj ++ createSeries().obj
    }
  }

  object XYZ {
    def apply[T0: Serializer](z: List[List[T0]], trace_name: String, trace_type: XYZChartType): XYZ[T0, T0, T0] = {
      XYZ(x=Nil, y=Nil, z=z.flatten, trace_name=trace_name, trace_type=trace_type, n=z.length)
    }

    def apply[T0: Serializer, T1: Serializer, T2: Serializer]
    (x: List[T0], y: List[T1], z: List[List[T2]], trace_name: String, trace_type: XYZChartType): XYZ[T0, T1, T2] = {
      XYZ(x=x, y=y, z=z.flatten,trace_name=trace_name, trace_type=trace_type, n=z.length)
    }
  }
}

