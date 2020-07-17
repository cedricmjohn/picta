package org.carbonateresearch.picta

import almond.interpreter.api.OutputHandler
import org.carbonateresearch.picta.charts.Html.{plotChart, plotChartInline}
import org.carbonateresearch.picta.options.Subplot
import org.carbonateresearch.picta.common.Utils.genRandomText

final case class Canvas(subplot: Subplot = Subplot()) {

  def setSubplot(new_subplot: Subplot) = this.copy(subplot = new_subplot)

  def setChart(i: Int, j: Int, chart: Chart) = this.copy(subplot = (this.subplot(i, j) = chart))

  /* syntactic sugar for the alternative constructor for this method */
  def addCharts(charts: Chart*): Canvas = addCharts(charts.toList)

  /* adds charts in the first available place; overwrites existing chart */
  def addCharts(charts: List[Chart]): Canvas = {

    if (charts.length != subplot.grid.length)
      throw new IllegalArgumentException("The number of charts must exactly equal the number of places in the subplot grid")

    /* create a copy to avoid mutating the original Canvas */
    val new_canvas = this.copy()

    /* copy each chart with an updated index to the corresponding position inside the subplot grid */
    charts.zipWithIndex.foreach{ case (chart, index) => new_canvas.subplot.grid(index) = chart.copy(id=genRandomText) }

    new_canvas
  }

  def plot() = plotChart(subplot)

  def plotInline()(implicit publish: OutputHandler) = plotChartInline(subplot)
}