package conusviz.charts

import conusviz.Chart.{plotChart, plotChart_inline, minJs}
import conusviz.Trace.SurfaceTrace
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import ujson.Value
import upickle.default._
import almond.interpreter.api.OutputHandler

sealed trait Surface {
  val traces: List[Value]
  val layout: Value
  val config: Value
}

case class SurfaceChart[T0](val data: List[SurfaceTrace[T0]],
                                 l: Layout = Layout("Chart", true),
                                 c: Config = Config(true, true)) extends Surface {

  // convert the traces, layout and config arguments to a Json value format
  val traces: List[Value] = data.map(t => t.value)
  val layout: Value = transform(l.createLayout).to(Value)
  val config: Value = transform(c.createConfig).to(Value)

  // simply inject traces, layout and config into the the function and generate the HTML
  def plot(): Unit = plotChart(traces, layout, config, minJs)
  def plot_inline()(implicit publish: OutputHandler): Unit = plotChart_inline(traces, layout, config)
}

object SurfaceChart {
  val compatibleChartSet = Set(
    "surface"
  )
}