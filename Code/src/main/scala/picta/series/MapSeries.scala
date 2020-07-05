package picta.series

import picta.common.Monoid._
import picta.common.OptionWrapper._
import picta.options.ColorOptions.Color
import picta.options.Line
import picta.series.ModeType.ModeType
import ujson.{Obj, Value}

/**
 * @constructor: This sets the Series for a Map chart specifically.
 * @param lat         : This is the list of latitudes.
 * @param lon         : This is the list of longitudes.
 * @param series_name : This sets the series name.
 * @param series_mode : This sets the series mode.
 * @param line        : This configures the line for the Map.
 */
final case class Map[T: Color](lat: List[Double] = Nil, lon: List[Double] = Nil, series_name: String = "map",
                               series_mode: Opt[ModeType] = Blank, line: Opt[Line[T]] = Blank) extends Series {

  def +[Z: Color](l: Line[Z]): Map[Z] = this.copy(line = l)

  override def serialize: Value = {
    val acc = Obj("lat" -> lat, "lon" -> lon, "type" -> "scattergeo")

    val series_mode_ = series_mode.asOption match {
      case Some(x) => Obj("mode" -> x.toString.toLowerCase)
      case None => JsonMonoid.empty
    }

    val line_ = line.asOption match {
      case Some(x) => Obj("line" -> x.serialize)
      case None => JsonMonoid.empty
    }

    List(acc, series_mode_, line_).foldLeft(JsonMonoid.empty)((a, x) => a |+| x)
  }
}