package picta.series

import picta.Serializer
import ujson.{Obj, Value}
import picta.Utils._
import picta.series.ModeType.ModeType

trait XYZSeries extends Series

object XYZChartType extends Enumeration {
  type XYZChartType = Value
  val CONTOUR, HEATMAP, SCATTER3D, SURFACE = Value
}

import XYZChartType._

/**
 * @constructor: A Series for a 3d chart.
 * @param x:
 * @param y:
 * @param z:
 * @param series_name:
 * @param series_mode:
 * @param n:
 */
final case class XYZ[T0 : Serializer, T1: Serializer, T2: Serializer]
(x: List[T0], y: List[T1], z: List[T2], series_name: String = genRandomText, series_type: XYZChartType, series_mode: Option[ModeType] = None,
 n: Int = 0) extends XYZSeries {

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
    if (x.length != n)
      throw new IllegalArgumentException("The length of a single list inside 'z' must be equal to the length of 'x'")

    if (z.length / n != y.length)
      throw new IllegalArgumentException("The total number of lists inside 'z' must be equal to the length of 'y'")

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
    val name = Obj("name" -> series_name, "type" -> series_type.toString.toLowerCase())

    val series_mode_ = series_mode match {
      case Some(x) => Obj("mode" -> x.toString.toLowerCase)
      case None => emptyObject
    }

    List(name, series_mode_, createSeries).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}

object XYZ {
  implicit def liftToOption[T](x: T): Option[T] = Option[T](x)

  def apply[T0: Serializer](z: List[List[T0]], series_name: String, series_type: XYZChartType): XYZ[T0, T0, T0] = {
    XYZ(x=Nil, y=Nil, z=z.flatten, series_name=series_name, series_type=series_type, n=z(0).length)
  }

  def apply[T0: Serializer, T1: Serializer, T2: Serializer]
  (x: List[T0], y: List[T1], z: List[List[T2]], series_name: String, series_type: XYZChartType): XYZ[T0, T1, T2] = {
    XYZ(x=x, y=y, z=z.flatten,series_name=series_name, series_type=series_type, n=z(0).length)
  }

  def apply[T0 : Serializer, T1: Serializer, T2: Serializer]
  (x: List[T0], y: List[T1], z: List[T2], series_name: String, series_type: XYZChartType, series_mode: ModeType): XYZ[T0, T1, T2] = {
    XYZ(x=x, y=y, z=z, series_name=series_name, series_type=series_type, series_mode=Some(series_mode))
  }
}