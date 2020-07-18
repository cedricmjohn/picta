package org.carbonateresearch.picta

import almond.interpreter.api.OutputHandler
import org.carbonateresearch.picta.OptionWrapper.{Opt, Blank}
import org.carbonateresearch.picta.charts.Html.{plotChart, plotChartInline}
import org.carbonateresearch.picta.common.Utils.genRandomText

final case class Canvas(rows: Int = 1, columns: Int = 1, private val grid: Opt[Array[Chart]] = Blank) {

  val id = genRandomText()
  val grid_ = grid.option match {
    case Some(x) => x
    case _ => Array.fill[Chart](rows * columns)(Chart(id = "not_set"))
  }

  def setChart(i: Int, j: Int, chart: Chart): Canvas = {
    val copy = this.copy(grid = this.grid_)
    copy.grid_(i * columns + j) = chart.copy(id = genRandomText())
    copy
  }

  /* syntactic sugar for the alternative constructor for this method */
  def addCharts(charts: Chart*): Canvas = addCharts(charts.toList)

  /* adds charts in the first available place; overwrites existing chart */
  def addCharts(charts: List[Chart]): Canvas = {

    if (charts.length != this.grid_.length)
      throw new IllegalArgumentException("The number of charts must exactly equal the number of places in the subplot grid")

    /* create a copy to avoid mutating the original Canvas */
    val new_canvas = this.copy()

    /* copy each chart with an updated index to the corresponding position inside the subplot grid */
    charts.zipWithIndex.foreach{ case (chart, index) => new_canvas.grid_(index) = chart.copy(id=genRandomText) }

    new_canvas
  }

  def plot() = plotChart(rows, columns, grid_, id)

  def plotInline()(implicit publish: OutputHandler) = plotChartInline(rows, columns, grid_, id)
}