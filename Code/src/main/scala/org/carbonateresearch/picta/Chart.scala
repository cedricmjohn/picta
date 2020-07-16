package org.carbonateresearch.picta

import almond.interpreter.api.OutputHandler
import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.charts.Html.{plotChart, plotChartInline}
import org.carbonateresearch.picta.common.Serializer
import org.carbonateresearch.picta.options.ColorOptions.Color
import org.carbonateresearch.picta.options.{Axis, Grid, XAxis, YAxis}
import ujson.{Obj, Value}
import upickle.default._

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
final case class Chart[T0, T1, T2, T3]
(data: List[Series] = Nil, layout: Layout[T0, T1, T2, T3] = Layout(), config: Config = Config(),
 animated: Boolean = false, transition_duration: Int = 100) extends Component {

  private val frames_labels = animated match {
    case false => (Nil, Nil)
    case true => createFramesAndLabels(data)
  }

  private val frames = transform(frames_labels._1).to(Value)

  private val labels = frames_labels._2

  private val data_ : List[Value] = data.map(t => t.serialize)

  private val layout_ : Value = layout.serialize

  private val config_ : Value = config.serialize

  def addSeries(new_series: List[Series]): Chart[T0, T1, T2, T3] = this.copy(data = new_series ::: data)

  def addSeries(new_series: Series*): Chart[T0, T1, T2, T3] = this.copy(data = new_series.toList ::: data)

  def setLayout[Z0, Z1, Z2, Z3]
  (new_layout: Layout[Z0, Z1, Z2, Z3]): Chart[Z0, Z1, Z2, Z3] = {

    var axeslist = scala.collection.mutable.ListBuffer.empty[Axis]
    var serieslist = scala.collection.mutable.ListBuffer.empty[Series]

    new_layout.grid.option match {
      case Some(x) =>
        for(i <- 0 until x.rows) {
          for(j <- 0 until x.columns) {
            val element = x.data(i)(j)
            axeslist += element.xaxis
            axeslist += element.yaxis
            serieslist += element.series
          }
        }

        this.copy(layout = new_layout.setAxes(axeslist.toList), data = serieslist.toList)

      case _ => this.copy(layout = new_layout)
    }

  }

  def setConfig(new_config: Config): Chart[T0, T1, T2, T3] = this.copy(config = new_config)

  private[picta] def serialize: Value = Obj("traces" -> data_, "layout" -> layout_, "config" -> config_)

  def plot(): Unit = {
    val labels_ : Value = transform(labels).to(Value)
    animated match {
      case true => plotChart(traces = List(data_(0)), frames = frames, transition_duration = transition_duration,
        labels = labels_, layout = layout_, config = config_)
      case false => plotChart(traces = data_, layout = layout_, config = config_)
    }
  }

  def plotInline()(implicit publish: OutputHandler): Unit = {
    val labels_ : Value = transform(labels).to(Value)
    animated match {
      case true => plotChartInline(traces = List(data_(0)), frames = frames, transition_duration = transition_duration,
        labels = labels_, layout = layout_, config = config_)
      case false => plotChartInline(traces = data_, layout = layout_, config = config_)
    }
  }

  private def createFramesAndLabels(lst: List[Series], frames: List[Obj] = Nil, labels: List[String] = Nil): (List[Obj], List[String]) = {
    lst match {
      case Nil => (frames, labels)
      case hd :: tl => createFramesAndLabels(tl, frames :+ Obj("name" -> hd.name, "data" -> List(hd.serialize)), labels :+ hd.name)
    }
  }
}