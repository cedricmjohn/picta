package conusviz.charts

import almond.interpreter.api.OutputHandler
import conusviz.Html.{plotChart, plotChart_inline}
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import conusviz.traces.MapTrace
import ujson.{Obj, Value}

//case class MapChart(trace: MapTrace, layout: Layout = Layout(), config: Config = Config()) extends ChartInterface {
//
//  val t: List[Value] = List(trace.serialize)
//  val l: Value = layout.serialize
//  val c: Value = config.serialize
//
//  def +(new_trace: MapTrace): MapChart = this.copy(trace = new_trace)
//  def +(new_layout: Layout): MapChart = this.copy(layout = new_layout)
//  def +(new_config: Config): MapChart = this.copy(config = new_config)
//
//  def serialize: Value = Obj(
//    "traces" -> List(trace.serialize),
//    "layout" -> layout.serialize,
//    "config" -> config.serialize,
//  )
//
//  def plot(): Unit = plotChart(t, l, c)
//  def plot_inline()(implicit publish: OutputHandler): Unit = plotChart_inline(t, l, c)
//}
