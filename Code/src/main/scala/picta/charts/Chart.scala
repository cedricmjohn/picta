package picta.charts

import almond.interpreter.api.OutputHandler
import picta.Html.{plotChart, plotChart_inline}
import picta.common.Component
import picta.options.Config
import picta.options.Layout
import picta.traces.{Trace}
import ujson.{Obj, Value}

/*
* Interface for Chart
* */
trait ChartInterface extends Component {
  val t: List[Value]
  val l: Value
  val c: Value
  def plot(): Unit
  def plot_inline()(implicit publish: OutputHandler): Unit
}

final case class Chart(data: List[Trace] = Nil, layout: Layout = Layout(), config: Config = Config()) extends ChartInterface {
  val t: List[Value] = data.map(t => t.serialize)
  val l: Value = layout.serialize
  val c: Value = config.serialize

  def +(new_trace: Trace): Chart = this.copy(data = new_trace::data)
  def +(new_traces: List[Trace]): Chart = this.copy(data = new_traces:::data)
  def +(new_layout: Layout): Chart = this.copy(layout = new_layout)
  def +(new_config: Config): Chart = this.copy(config = new_config)

  def serialize: Value = Obj("traces" -> t, "layout" -> l, "config" -> c)
  def plot(): Unit = plotChart(t, l, c)
  def plot_inline()(implicit publish: OutputHandler): Unit = plotChart_inline(t, l, c)
}
