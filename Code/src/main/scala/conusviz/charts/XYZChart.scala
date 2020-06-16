package conusviz.charts

import conusviz.Chart.{minJs, plotChart, plotChart_inline}
import conusviz.Trace.XYZTrace
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import ujson.Value
import upickle.default._
import almond.interpreter.api.OutputHandler

sealed trait XYZ {
  val traces: List[Value]
  val layout: Value
  val config: Value
}

/*
* Class for constructing a XYZ plot. This is typically three-dimensional data
* @param data: A list of traces that we wish to display on the surface plot
* @param l: This is a layout class instance specifying the options to do with layout
* @param c: This is a config class instance specifying the options to do with general configuration
* */
final case class XYZChart[T0, T1, T2](val data: List[XYZTrace[T0, T1, T2]],
                                l: Layout = Layout("Chart", true),
                                c: Config = Config(true, true)) extends XYZ {

  // convert the traces, layout and config arguments to a Value format as that is easier to work with
  val traces: List[Value] = data.map(t => t.value)
  val layout: Value = transform(l.createLayout).to(Value)
  val config: Value = transform(c.createConfig).to(Value)

  def plot(): Unit = plotChart(traces, layout, config, minJs)
  def plot_inline()(implicit publish: OutputHandler): Unit = plotChart_inline(traces, layout, config)
}

object XYZChart {
  val compatibleChartSet = Set(
    "contour",
    "heatmap",
    "scatter3d",
  )
}
