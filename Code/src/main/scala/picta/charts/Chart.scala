package picta.charts

import almond.interpreter.api.OutputHandler
import picta.Html.{plotChart, plotChart_inline}
import picta.common.Component
import picta.options.Config
import picta.options.Layout
import picta.series.{Series}
import ujson.{Obj, Value}

trait ChartInterface extends Component {
  val t: List[Value]
  val l: Value
  val c: Value
  def plot(): Unit
  def plot_inline()(implicit publish: OutputHandler): Unit
}

/**
  * @constructor Creates a new chart with 'data', 'layout' and 'config' components
  * @param data: This is a list of 'series' that we wish to add to the plot.
  * @param layout: This is the layout that determines how the chart is visually laid out.
  * @param config: This is the config that controls how the plot behaves with user interaction.
  */
final case class Chart(data: List[Series] = Nil, layout: Layout = Layout(), config: Config = Config()) extends ChartInterface {
  val t: List[Value] = data.map(t => t.serialize)
  val l: Value = layout.serialize
  val c: Value = config.serialize

  def +(new_series: Series): Chart = this.copy(data = new_series::data)
  def +(new_series: List[Series]): Chart = this.copy(data = new_series:::data)
  def +(new_layout: Layout): Chart = this.copy(layout = new_layout)
  def +(new_config: Config): Chart = this.copy(config = new_config)

  def serialize: Value = Obj("traces" -> t, "layout" -> l, "config" -> c)
  def plot(): Unit = plotChart(t, l, c)
  def plot_inline()(implicit publish: OutputHandler): Unit = plotChart_inline(t, l, c)
}