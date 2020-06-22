package conusviz.charts

import conusviz.Html.{minJs, plotChart, plotChart_inline}
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import ujson.{Obj, Value}
import upickle.default._
import almond.interpreter.api.OutputHandler
import conusviz.Serializer
import conusviz.traces.{Trace, XYTrace}
import conusviz.charts.Geometry

sealed trait XY {
  val traces: List[Value]
  val layout: Value
  val config: Value
}

/*
* Class for constructing a XY plot. This is typically two-dimensional data
* @param data: A list of traces that we wish to display on the surface plot
* @param l: This is a layout class instance specifying the options to do with layout
* @param c: This is a config class instance specifying the options to do with general configuration
* */
final case class XYChart
(val data: List[Trace], l: Layout = Layout("Chart"), c: Config = Config(true, true)) extends XY {

  val traces: List[Value] = data.map(t => t.value)
  val layout: Value = transform(l.serialize).to(Value)
  val config: Value = transform(c.serialize).to(Value)

  def serialize: Value = Obj("traces" -> traces, "layout" -> layout, "config" -> config)
  def plot(): Unit = plotChart(traces, layout, config)
  def plot_inline()(implicit publish: OutputHandler): Unit = plotChart_inline(traces, layout, config)
}

object XYChart {
  val compatibleChartSet = Set(
    "scatter",
    "scattergl",
    "bar",
    "histogram2dcontour",
    "histogram"
  )
}