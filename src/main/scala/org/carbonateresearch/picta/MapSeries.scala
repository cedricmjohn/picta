package org.carbonateresearch.picta

import org.carbonateresearch.picta.ColorOptions.Color
import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid._
import org.carbonateresearch.picta.common.Utils.generateRandomText
import org.carbonateresearch.picta.options.Line
import ujson.{Obj, Value}

/** This sets the Series for a Map chart specifically.
 *
 * @param lat   : This is the list of latitudes.
 * @param lon   : This is the list of longitudes.
 * @param name  : This sets the series name.
 * @param style : This sets the series mode.
 * @param line  : This configures the line for the Map.
 */
final case class Map[T: Color](lat: List[Double] = Nil, lon: List[Double] = Nil, name: String = generateRandomText,
                               style: Opt[Style] = Blank, line: Opt[Line[T]] = Blank) extends Series {

  val classification: String = "map"

  def setName(new_name: String): Series = this.copy(name = new_name)

  def setLat(new_lat: List[Double]) = this.copy(lat = new_lat)

  def setLon(new_lon: List[Double]) = this.copy(lon = new_lon)

  def drawLine[Z: Color](l: Line[Z]): Map[Z] = this.copy(line = l)

  def drawSymbol(new_symbol: Style): Map[T] = this.copy(style = new_symbol)

  private[picta] def serialize: Value = {
    val meta = Obj("name" -> name, "lat" -> lat, "lon" -> lon, "type" -> "scattergeo")

    val series_mode_ = style.option match {
      case Some(x) => Obj("mode" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    val line_ = line.option match {
      case Some(x) => Obj("line" -> x.serialize)
      case None => jsonMonoid.empty
    }

    List(meta, series_mode_, line_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}