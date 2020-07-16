package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.common.Serializer
import org.carbonateresearch.picta.options.ColorOptions.Color
import org.carbonateresearch.picta.{Component, Series, XY, XYSeries}
import ujson.{Obj, Value}

final case class GridElement[T0, T1, T2, T3](var xaxis: XAxis = XAxis(), var yaxis: YAxis = YAxis(), var series: Series = XY(Nil)) {
  def setAxis(new_xaxis: XAxis, new_yaxis: YAxis) = {
    this.xaxis = new_xaxis
    this.yaxis = new_yaxis
    this
  }
  def addSeries[Z0, Z1, Z2, Z3](new_series: XYSeries[Z0, Z1, Z2, Z3]) = {
    this.series = new_series setAxes(this.xaxis, this.yaxis)
    this
  }
}

/**
 * @constructor: This configures a grid for subplots.
 * @param rows    : Sets the number of rows.
 * @param columns : Sets the number of columns.
 * @param pattern : Specifies the pattern for the grid.
 */
final case class Grid[T0: Serializer, T1: Serializer, T2: Color, T3: Color]
(rows: Int = 1, columns: Int = 1, pattern: String = "independent") extends Component {
  val data = Array.fill[GridElement[T0, T1, T2, T3]](rows, columns)(GridElement())

  def apply(row: Int, col: Int): GridElement[T0, T1, T2, T3] = this.data(row)(col)

  private[picta] def serialize(): Value = Obj("rows" -> rows, "columns" -> columns, "pattern" -> pattern)
}
