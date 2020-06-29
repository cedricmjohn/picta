package conusviz.charts

import conusviz.Html.{plotChart, plotChart_inline}
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import ujson.{Obj, Value}
import almond.interpreter.api.OutputHandler
import conusviz.traces.XYTrace._
import conusviz.traces.{XYTrace}

/*
* Class for constructing a XY plot. This is typically two-dimensional data
* @param data: A list of traces that we wish to display on the surface plot
* @param l: This is a layout class instance specifying the options to do with layout
* @param c: This is a config class instance specifying the options to do with general configuration
* */

//
//final case class XYChart
//(data: List[XYTrace] = Nil, layout: Layout = Layout(), config: Config = Config()) extends Chart {
//
//  val t: List[Value] = data.map(t => t.serialize)
//  val l: Value = layout.serialize
//  val c: Value = config.serialize
//
//  def +(new_trace: XYTrace): XYChart = this.copy(data = new_trace::data)
//  def +(new_traces: List[XYTrace]): XYChart = this.copy(data = new_traces:::data)
//  def +(new_layout: Layout): XYChart = this.copy(layout = new_layout)
//  def +(new_config: Config): XYChart = this.copy(config = new_config)
//
//  def serialize: Value = Obj("traces" -> t, "layout" -> l, "config" -> c)
//  def plot(): Unit = plotChart(t, l, c)
//  def plot_inline()(implicit publish: OutputHandler): Unit = plotChart_inline(t, l, c)
//}