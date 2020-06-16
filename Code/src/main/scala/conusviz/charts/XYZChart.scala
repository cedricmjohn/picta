package conusviz.charts

import almond.api.JupyterApi
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

final case class XYZChart[T0, T1, T2](val data: List[XYZTrace[T0, T1, T2]],
                                l: Layout = Layout("Chart", true),
                                c: Config = Config(true, true)) extends XYZ {

  // convert the traces, layout and config arguments to a Json value format
  val traces: List[Value] = data.map(t => t.value)
  val layout: Value = transform(l.createLayout).to(Value)
  val config: Value = transform(c.createConfig).to(Value)

  // simply inject traces, layout and config into the the function and generate the HTML
  def plot(): Unit = plotChart(traces, layout, config, minJs)
  def plot_inline()(implicit publish: OutputHandler): Unit =
    plotChart_inline(traces, layout, config)
}

object XYZChart {
  val compatibleChartSet = Set(
    "contour",
    "heatmap",
    "scatter3d",
  )
}
