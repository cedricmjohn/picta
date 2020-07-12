package picta.charts

import almond.interpreter.api.OutputHandler
import Html.{plotChart, plotChart_inline}
import picta.common.Component
import picta.common.OptionWrapper._
import picta.options.{Config, Layout}
import picta.series.Series
import ujson.{Obj, Value}
import upickle.default._

trait ChartInterface extends Component {
  def plot(): Unit

  def plot_inline()(implicit publish: OutputHandler): Unit
}

/**
 * This is the chart base class with which we can compose all other elements of the grammar that determine how the chart
 * is displayed.
 *
 * @constructor Creates a new chart with 'data', 'layout' and 'config' components.
 * @param data                : This is a list of 'series' type that we wish to add to the plot.
 * @param layout              : This is the layout that determines how the chart is visually laid out.
 * @param config              : This is the config that controls how the plot behaves with user interaction.
 * @param animated            : Specifies whether the chart is animated or static.
 * @param transition_duration : If the chart is animated, this specifies the time (ms) it takes to change frame.
 */
final case class Chart(data: List[Series] = Nil, layout: Layout = Layout(), config: Config = Config(),
                       animated: Boolean = false, transition_duration: Int = 100) extends ChartInterface {

  private val frames_labels = animated match {
    case false => (Nil, Nil)
    case true => createFramesAndLabels(data)
  }

  private val frames = transform(frames_labels._1).to(Value)

  private val labels = frames_labels._2

  private val data_ : List[Value] = data.map(t => t.serialize)

  private val layout_ : Value = layout.serialize

  private val config_ : Value = config.serialize

  def setData(new_series: List[Series]): Chart = this.copy(data = new_series ::: data)

  def setData(new_series: Series*): Chart = this.copy(data = new_series.toList ::: data)

  def setLayout(new_layout: Layout): Chart = this.copy(layout = new_layout)

  def setConfig(new_config: Config): Chart = this.copy(config = new_config)

  def serialize: Value = Obj("traces" -> data_, "layout" -> layout_, "config" -> config_)

  def plot(): Unit = {
    val labels_ : Value = transform(labels).to(Value)
    animated match {
      case true => plotChart(traces = List(data_(0)), frames = frames, transition_duration = transition_duration,
        labels = labels_, layout = layout_, config = config_)
      case false => plotChart(traces = data_, layout = layout_, config = config_)
    }
  }

  def plot_inline()(implicit publish: OutputHandler): Unit = {
    val labels_ : Value = transform(labels).to(Value)
    animated match {
      case true => plotChart_inline(traces = List(data_(0)), frames = frames, transition_duration = transition_duration,
        labels = labels_, layout = layout_, config = config_)
      case false => plotChart_inline(traces = data_, layout = layout_, config = config_)
    }
  }

  private def createFramesAndLabels(lst: List[Series], frames: List[Obj] = Nil, labels: List[String] = Nil): (List[Obj], List[String]) = {
    lst match {
      case Nil => (frames, labels)
      case hd :: tl => createFramesAndLabels(tl, frames :+ Obj("name" -> hd.series_name, "data" -> List(hd.serialize)), labels :+ hd.series_name)
    }
  }
}