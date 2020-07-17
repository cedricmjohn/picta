package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.common.Serializer
import org.carbonateresearch.picta.options.ColorOptions.Color
import org.carbonateresearch.picta.{Chart, Component, Series, XY, XYSeries}
import org.carbonateresearch.picta.common.Utils.genRandomText

import ujson.{Obj, Value}


/**
 * @constructor: This configures a grid for subplots.
 * @param rows    : Sets the number of rows.
 * @param columns : Sets the number of columns.
 */
case class Subplot(rows: Int = 1, columns: Int = 1) {
  private[picta] val id = genRandomText()
  private[picta] val grid = Array.fill[Chart](rows * columns)(Chart(id = "not_set"))

  def apply(i: Int, j: Int) = this.grid(i * columns + j)

  def update(i: Int, j: Int, chart: Chart) = {
    val copy = this.copy()
    copy.grid(i * columns + j) = chart.copy(id = genRandomText())
    copy
  }
}
