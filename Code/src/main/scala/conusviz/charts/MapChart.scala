package conusviz.charts

import almond.interpreter.api.OutputHandler
import conusviz.Html.{plotChart, plotChart_inline}
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import conusviz.traces.MapTrace
import ujson.{Obj, Value}

case class MapChart(trace: MapTrace, layout: Layout = Layout(), config: Config = Config()) extends Chart {
  def serialize: Value = Obj(
    "traces" -> List(trace.serialize),
    "layout" -> layout.serialize,
    "config" -> config.serialize,
  )

  def plot(): Unit = plotChart(List(trace.serialize), layout.serialize, config.serialize)
  def plot_inline()(implicit publish: OutputHandler): Unit = plotChart_inline(List(trace.serialize), layout.serialize,
    config.serialize)
}
