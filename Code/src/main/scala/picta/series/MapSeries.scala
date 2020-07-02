package picta.series

import picta.Utils._
import picta.options.ColorOptions.Color
import picta.options.Line
import ujson.{Obj, Value}

/**
  * @constructor: This sets the Series for a Map chart specifically.
  * @param lat: This is the list of latitudes.
  * @param lon: This is the list of longitudes.
  * @param series_name: This sets the series name.
  * @param series_mode: This sets the series mode.
  * @param line: This configures the line for the Map.
  */
final case class Map[T: Color](lat: List[Double] = Nil, lon: List[Double] = Nil, series_name: String = "map",
                               series_mode: Option[String] = None, line: Option[Line[T]] = None) extends Series {

  def +[Z: Color](l: Line[Z]): Map[Z] = this.copy(line = Some(l))

  override def serialize: Value = {
    val acc = Obj("lat" -> lat, "lon" -> lon, "type" -> "scattergeo")

    val series_mode_ = series_mode match {
      case Some(x) => Obj("mode" -> x)
      case None => emptyObject
    }

    val line_ = line match {
      case Some(x) => Obj("line" -> x.serialize)
      case None => emptyObject
    }

    List(acc, series_mode_, line_).foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}

object Map {
  def apply[T: Color](lat: List[Double], lon: List[Double], series_mode: String): Map[T] =
    Map(lat=lat, lon=lon, series_mode=series_mode)
}