package org.carbonateresearch.picta

import org.carbonateresearch.picta.common.Utils.generateRandomText
import org.carbonateresearch.picta.options.{Axis, MapOptions, Legend, Margin, MultiChart, XAxis, YAxis}
import ujson.{Obj, Value}
import upickle.default._

final case class Chart
(data: List[Series] = Nil, layout: Layout = Layout(), config: Config = Config(),
 animated: Boolean = false, transition_duration: Int = 100, private[picta] val id: String = generateRandomText()) extends Component {

  private val frames_labels = animated match {
    case false => (Nil, Nil)
    case true => createFramesAndLabels(data)
  }

  private[picta] val frames = transform(frames_labels._1).to(Value)

  private[picta] val labels = frames_labels._2

  private[picta] val data_ : List[Value] = data.map(t => t.serialize)

  private[picta] val layout_ : Value = layout.serialize

  private[picta] val config_ : Value = config.serialize

  def addSeries(new_series: List[Series]): Chart = this.copy(data = new_series ::: data)

  def addSeries(new_series: Series*): Chart = this.copy(data = new_series.toList ::: data)

  def setLayout[Z0, Z1, Z2, Z3](new_layout: Layout): Chart= this.copy(layout = new_layout)

  def setConfig(new_config: Config): Chart = this.copy(config = new_config)

  /* helper methods that make wrangling all the sub-components a lot easier */
  def setTitle(title: String): Chart = {
    val new_layout = this.layout setTitle title
    this.copy(layout = new_layout)
  }

  def addAxes(new_xaxis: XAxis, new_yaxis: YAxis): Chart = {
    val new_layout = this.layout setAxes(new_xaxis, new_yaxis)
    this.copy(layout = new_layout)
  }

  def addAxes(new_axis: Axis*) = {
    val new_layout = this.layout setAxes new_axis.toList
    this.copy(layout = new_layout)
  }

  def showLegend(showlegend: Boolean) = {
    val new_layout = this.layout showLegend showlegend
    this.copy(layout = new_layout)
  }

  def setLegend(new_legend: Legend) = {
    val new_layout = this.layout setLegend new_legend
    this.copy(layout = new_layout)
  }

  def setMapOptions(new_map_options: MapOptions) = {
    val new_layout = this.layout setGeo new_map_options
    this.copy(layout = new_layout)
  }

  def setMargin(l: Int, r: Int, t: Int, b: Int) = {
    val new_margin: Margin = Margin(l=l, r=r, t=t, b=b)
    val new_layout = this.layout setMargin new_margin
    this.copy(layout = new_layout)
  }

  def setDimensions(height: Int = this.layout.height, width: Int = this.layout.width) = {
    val new_layout = this.layout.copy(height=height, width=width)
    this.copy(layout = new_layout)
  }

  def asMultiChart(rows: Int, columns: Int) = {
    if (this.animated) throw new IllegalArgumentException("Animated Charts cannot be transformed to MultiCharts")

    val new_layout = this.layout setMultiChart MultiChart(rows, columns)
    this.copy(layout = new_layout)
  }


  private[picta] def serialize: Value = Obj("traces" -> data_, "layout" -> layout_, "config" -> config_)

  private def createFramesAndLabels(lst: List[Series], frames: List[Obj] = Nil, labels: List[String] = Nil): (List[Obj], List[String]) = {
    lst match {
      case Nil => (frames, labels)
      case hd :: tl => createFramesAndLabels(tl, frames :+ Obj("name" -> hd.name, "data" -> List(hd.serialize)), labels :+ hd.name)
    }
  }
}