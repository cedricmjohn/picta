package picta.charts

import almond.interpreter.api.OutputHandler
import picta.Html.{plotChart, plotChart_inline}
import picta.common.Component
import picta.common.OptionWrapper._
import picta.options.animation.{Args, Setting, Slider, SliderStep}
import picta.options.{Config, Layout}
import picta.series.Series
import ujson.{Obj, Value}
import upickle.default._

trait ChartInterface extends Component {
  def plot(): Unit
  def plot_inline()(implicit publish: OutputHandler): Unit
}

/**
 * @constructor Creates a new chart with 'data', 'layout' and 'config' components
 * @param data   : This is a list of 'series' that we wish to add to the plot.
 * @param layout : This is the layout that determines how the chart is visually laid out.
 * @param config : This is the config that controls how the plot behaves with user interaction.
 */
final case class Chart(data: List[Series] = Nil, layout: Layout = Layout(), config: Config = Config(),
                       animated: Boolean = false, method: String = "animate") extends ChartInterface {


  private val frames_labels = animated match {
    case false => (Nil, Nil)
    case true => createFramesAndLabels(data)
  }

  private val frames = transform(frames_labels._1).to(Value)

  private val labels = frames_labels._2

  private val data_ : List[Value] = data.map(t => t.serialize)

  private val layout_ : Value = animated match {
    case false => layout.serialize
    case true => generateAnimatedLayout()
  }

  private val config_ : Value = config.serialize

  def +(new_series: Series): Chart = this.copy(data = new_series :: data)

  def +(new_series: List[Series]): Chart = this.copy(data = new_series ::: data)

  def +(new_layout: Layout): Chart = this.copy(layout = new_layout)

  def +(new_config: Config): Chart = this.copy(config = new_config)

  def serialize: Value = Obj("traces" -> data_, "layout" -> layout_, "config" -> config_)

  def plot(): Unit =
    animated match {
      case true => plotChart(traces = List(data_(0)), frames = frames, layout = layout_, config = config_)
      case false => plotChart(traces = data_, layout = layout_, config = config_)
    }

  def plot_inline()(implicit publish: OutputHandler): Unit = animated match {
    case true => plotChart_inline(traces = List(data_(0)), frames = frames, layout = layout_, config = config_)
    case false => plotChart_inline(traces=data_, layout=layout_, config=config_)
  }


  private def createFramesAndLabels(lst: List[Series], frames: List[Obj] = Nil, labels: List[String] = Nil): (List[Obj], List[String]) = {
    lst match {
      case Nil => (frames, labels)
      case hd :: tl => createFramesAndLabels(tl, frames :+ Obj("name" -> hd.series_name, "data" -> List(hd.serialize)), labels :+ hd.series_name)
    }
  }

  private def createSliderSteps(method: String, labels: List[String], count: Int = 0): List[SliderStep] = {
    val args = Args(transition = Setting(300.0), frame = Setting(300.0, false))
    labels match {
      case Nil => Nil
      case hd :: tl =>
        SliderStep(method = method, key = hd, label = count.toString, args = args) :: createSliderSteps(method = method, tl, count + 1)
    }
  }

  private def generateAnimatedLayout(): Value = {
    val slider_steps: List[SliderStep] = createSliderSteps(method = method, labels = labels)

    val new_slider: Slider = layout.sliders.asOption match {
      case Some(x) => x
      case _ => Slider()
    }

    val layout_with_steps = layout + (new_slider + slider_steps)
    layout_with_steps.serialize
  }
}