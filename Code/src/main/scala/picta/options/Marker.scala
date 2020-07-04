package picta.options

import picta.common.OptionWrapper._
import picta.common.Component
import picta.common.Monoid._

import picta.options.ColorOptions._
import ujson.{Obj, Value}

/**
 * @constructor: Configures a Marker component for the chart.
 * @param symbol: Specifies what symbol the marker will use on the chart.
 * @param color: Specifies the color for the symbol on a chart.
 * @param line: This configures the line for the marker. This is another Line component.
 *
 */
case class Marker[T0: Color, T1: Color]
(symbol: Opt[String]=Blank, color: Opt[List[T0]] = Empty, line: Opt[Line[T1]] = Blank) extends Component {

  private val c0 = implicitly[Color[T0]]

  def +(new_symbol: String): Marker[T0, T1] = this.copy(symbol = new_symbol)
  def +[Z: Color](new_color: List[Z]): Marker[Z, T1] = this.copy(color = new_color)
  def +[Z: Color](new_line: Line[Z]): Marker[T0, Z] = this.copy(line = new_line)

  def serialize(): Value = {
    val symbol_ = symbol.asOption match {
      case Some(x) => Obj("symbol" -> x)
      case None => JsonMonoid.empty
    }

    val color_ = color.asOption match {
      case Some(lst: List[T0]) => Obj("color" -> c0.serialize(lst))
      case None => JsonMonoid.empty
    }

    val line_ = line.asOption match {
      case Some(x) => Obj("line" -> x.serialize())
      case None => JsonMonoid.empty
    }

    List(symbol_, color_, line_).foldLeft(JsonMonoid.empty)((a, x) => a |+| x)
  }
}