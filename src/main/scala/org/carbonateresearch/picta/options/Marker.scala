package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.OptionWrapper.{Blank, Empty, Opt}
import org.carbonateresearch.picta.SymbolShape.SymbolShape
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import org.carbonateresearch.picta.common.Serializer
import org.carbonateresearch.picta.options.ColorOptions.Color
import ujson.{Obj, Value}

/**
 * @constructor: Configures a Marker component for the chart.
 * @param symbol : Specifies what symbol the marker will use on the chart.
 * @param color  : Specifies the color for the symbol on a chart.
 * @param line   : This configures the line for the marker. This is another Line component.
 *
 */
final case class Marker[T0: Color, T1: Color]
(symbol: Opt[SymbolShape] = Blank, color: Opt[List[T0]] = Empty, line: Opt[Line[T1]] = Blank, size: Opt[List[Int]] = Empty,
 colorbar_options: Opt[ColorBar] = Blank) extends Component {

  private val c0 = implicitly[Color[T0]]
  private val s0 = implicitly[Serializer[Int]]

  def setSymbol(new_symbol: SymbolShape): Marker[T0, T1] = this.copy(symbol = new_symbol)

  def setColor[Z: Color](new_color: List[Z]): Marker[Z, T1] = this.copy(color = new_color)

  def setColor[Z: Color](new_color: Z): Marker[Z, T1] = this.copy(color = List(new_color))

  def setLine[Z: Color](new_line: Line[Z]): Marker[T0, Z] = this.copy(line = new_line)

  def setLine[Z: Color](width: Double = 0.5, color: Opt[Z] = Blank): Marker[T0, Z] = {
    val new_line = Line(width, color.toList)
    this.copy(line = new_line)
  }

  def setSize(new_size: List[Int]): Marker[T0, T1] = this.copy(size = new_size)

  def setSize(new_size: Int*): Marker[T0, T1] = this.copy(size = new_size.toList)

  def setColorBar(new_colorbar: ColorBar) = this.copy(colorbar_options = new_colorbar)

  private[picta] def serialize(): Value = {
    val symbol_ = symbol.option match {
      case Some(x) => Obj("symbol" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    val color_ = color.option match {
      case Some(lst: List[T0]) => Obj("color" -> c0.serialize(lst))
      case None => jsonMonoid.empty
    }

    val line_ = line.option match {
      case Some(x) => Obj("line" -> x.serialize())
      case None => jsonMonoid.empty
    }

    val size_ = size.option match {
      case Some(x) => Obj("size" -> s0.serialize(x))
      case None => jsonMonoid.empty
    }

    val colorbar_options_ = colorbar_options.option match {
      case Some(x) => Obj("colorbar" -> x.serialize)
      case None => jsonMonoid.empty
    }

    List(symbol_, color_, line_, size_, colorbar_options_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}
