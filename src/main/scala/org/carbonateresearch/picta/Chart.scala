package org.carbonateresearch.picta

import scala.language.postfixOps

import almond.interpreter.api.OutputHandler
import org.carbonateresearch.picta.OptionWrapper.{Blank, Opt}
import org.carbonateresearch.picta.common.Utils.generateRandomText
import org.carbonateresearch.picta.options._
import ujson.{Obj, Value}
import upickle.default._

/** This is a single chart that renders in a slot in the Canvas grid.
 *
 * @param series: A list of series we wish to plot on the page.
 * @param layout: This specifies how the chart will be laid out.
 * @param config: This configures the chart.
 * @param animated: This specifies whether the chart is animated.
 * @param transition_duration: If the chart is animated, this specifies the duration between the frames.
 * @param id: This is used by the Picta library internally for book-keeping purposes.
 */
final case class Chart
(series: List[Series] = Nil, layout: ChartLayout = ChartLayout(), config: Config = Config(),
 animated: Boolean = false, transition_duration: Int = 100, private[picta] val id: String = generateRandomText()) extends Component {

  private val XYZ: Boolean = {
    if (series.length == 0) false
    else series.head.classification match {
        case "xyz" => true
        case _ => false
      }
  }

  private val frames_labels = animated match {
    case false => (Nil, Nil)
    case true => createFramesAndLabels(series)
  }

  private def createAnimatedData() = {
    series match {
      case Nil => Nil
      case x =>
        val head = series.head setName "Frame 0"
        List(head.serialize)
    }
  }

  private[picta] val frames = transform(frames_labels._1).to(Value)

  private[picta] val labels = frames_labels._2

  private[picta] val data_ : List[Value] = animated match {
    case true => createAnimatedData()
    case false => series.map(x => x.serialize)
  }

  private[picta] val layout_ : Value = layout.serialize

  private[picta] val config_ : Value = config.serialize

  def plot() = Canvas().addCharts(this).plot

  def plotInline()(implicit publish: OutputHandler) = Canvas().addCharts(this).plotInline

  def addSeries(new_series: List[Series]): Chart = this.copy(series = series ::: new_series )

  def addSeries(new_series: Series*): Chart = this.copy(series =  series ::: new_series.toList)

  def setChartLayout[Z0, Z1, Z2, Z3](new_layout: ChartLayout): Chart= this.copy(layout = new_layout.copy(XYZ = XYZ))

  def setConfig(responsive: Boolean = true, scrollZoom: Boolean = true): Chart = {
    val new_config = Config(responsive=responsive, scrollZoom=scrollZoom)
    this.copy(config = new_config)
  }

  /* helper methods that make wrangling all the sub-components a lot easier */
  def setTitle(title: String): Chart = {
    val new_layout = this.layout setTitle title setXYZ XYZ
    this.copy(layout = new_layout)
  }

  def setLegend(x: Opt[Double] = Blank, y: Opt[Double] = Blank, orientation: Orientation = HORIZONTAL,
                xanchor: Opt[Anchor] = Blank, yanchor: Opt[Anchor] = Blank) = {

    val new_legend = Legend(x=x, y=y, orientation=orientation, xanchor=xanchor, yanchor=yanchor)
    val new_layout = this.layout setLegend new_legend setXYZ XYZ
    this.copy(layout = new_layout)
  }

  // higher order function that allows various attributes of an axis to be set
  private def setAxis(key: String, f: Axis => Axis) = {
    def setAxisProperty(x: Axis)(f: Axis => Axis): Axis = f(x)

    val existing_axes = this.layout.axes.option.getOrElse(Nil).filter(axis => axis.convertPosition(axis.getPosition) == key)

    val new_axis = key match {
      /* if we have no existing axes, we create a new one from scratch */
      case "xaxis" if (existing_axes.length == 0) => setAxisProperty(XAxis())(f)
      case "yaxis"if (existing_axes.length == 0) => setAxisProperty(YAxis())(f)
      case "zaxis"if (existing_axes.length == 0) => setAxisProperty(ZAxis())(f)
      /* create update the existing axis */
      case _ => setAxisProperty(existing_axes.last)(f)
    }

    /* now get all the axes EXCEPT the original axis */
    val filtered_axes = this.layout.axes.option.getOrElse(Nil).filter(axis => axis.convertPosition(axis.getPosition) != key)

    /* if we have other axes, then we need to combine with them */
    val new_layout = this.layout setAxes (filtered_axes :+ new_axis) setXYZ XYZ

    this.copy(layout = new_layout)
  }

  private def setAxisTitle(title: String): Axis => Axis = Axis.setTitle(title)(_)

  def setXAxisTitle(title: String) = setAxis(key="xaxis", setAxisTitle(title))

  def setYAxisTitle(title: String) = setAxis(key="yaxis", setAxisTitle(title))

  def setZAxisTitle(title: String) = setAxis(key="zaxis", setAxisTitle(title))

  private def setAxisLimits(lower_limit: Double = 0, upper_limit: Double): Axis => Axis = Axis.setLimits(lower_limit, upper_limit)(_)

  def setXAxisLimits(lower_limit: Double = 0, upper_limit: Double) = setAxis("xaxis", setAxisLimits(lower_limit, upper_limit))

  def setYAxisLimits(lower_limit: Double = 0, upper_limit: Double) = setAxis("yaxis", setAxisLimits(lower_limit, upper_limit))

  def setZAxisLimits(lower_limit: Double = 0, upper_limit: Double) = setAxis("zaxis", setAxisLimits(lower_limit, upper_limit))

  private def drawAxisLog(log: Boolean): Axis => Axis = Axis.drawLog(log)(_)

  def drawXAxisLog(log: Boolean = true) = setAxis(key="xaxis", drawAxisLog(log))

  def drawYAxisLog(log: Boolean = true) = setAxis(key="yaxis", drawAxisLog(log))

  def drawZAxisLog(log: Boolean = true) = setAxis(key="zaxis", drawAxisLog(log))

  private def drawAxisReversed(reversed: Boolean): Axis => Axis = Axis.drawReverseAxis(reversed)(_)

  def drawXAxisReversed(reverse: Boolean = true) = setAxis("xaxis", drawAxisReversed(reverse))

  def drawYAxisReversed(reverse: Boolean = true) = setAxis("yaxis", drawAxisReversed(reverse))

  private def setAxisTickGap(gap: Double): Axis => Axis = Axis.setTickGap(gap)(_)

  def setXAxisTickGap(gap: Double = 0) = setAxis(key = "xaxis", setAxisTickGap(gap))

  def setYAxisTickGap(gap: Double = 0) = setAxis(key = "yaxis", setAxisTickGap(gap))

  def setZAxisTickGap(gap: Double = 0) = setAxis(key = "zaxis", setAxisTickGap(gap))

  private def setAxisStartTick(start_tick: Double) = Axis.setStartingTick(start_tick)(_)

  def setXAxisStartTick(start_tick: Double = 0) = setAxis(key="xaxis", setAxisStartTick(start_tick))

  def setYAxisStartTick(start_tick: Double = 0) = setAxis(key="yaxis", setAxisStartTick(start_tick))

  def setZAxisStartTick(start_tick: Double = 0) = setAxis(key="zaxis", setAxisStartTick(start_tick))

  def addAxes(new_xaxis: XAxis, new_yaxis: YAxis): Chart = {
    val new_layout = this.layout setAxes(new_xaxis, new_yaxis) setXYZ XYZ
    this.copy(layout = new_layout)
  }

  def addAxes(new_axis: Axis*) = {
    val new_layout = this.layout setAxes new_axis.toList setXYZ XYZ
    this.copy(layout = new_layout)
  }

  def showLegend(showlegend: Boolean) = {
    val new_layout = this.layout showLegend showlegend setXYZ XYZ
    this.copy(layout = new_layout)
  }

  def setMapOptions(new_map_options: MapOptions) = {
    val new_layout = this.layout setMapOption new_map_options setXYZ XYZ
    this.copy(layout = new_layout)
  }

  def setMapOptions(region: Opt[Region] = Blank, landcolor: Opt[String] = Blank, lakecolor: Opt[String] = Blank,
                    projection: Opt[Projection] = Blank, lataxis: Opt[LatAxis] = Blank, longaxis: Opt[LongAxis] = Blank,
                    showland: Boolean = true, showlakes: Boolean = true, resolution: Int = 50, coastlinewidth: Int = 2) = {

    val map_option =
      MapOptions(region, landcolor=landcolor, lakecolor=lakecolor, projection, lataxis, longaxis,
      showland=showland, showlakes=showlakes, resolution=resolution, coastlinewidth=coastlinewidth)

    val new_layout = this.layout setMapOption map_option setXYZ XYZ

    this.copy(layout = new_layout)
  }

  def setMargin(l: Int, r: Int, t: Int, b: Int) = {
    val new_margin: Margin = Margin(l=l, r=r, t=t, b=b)
    val new_layout = this.layout setMargin new_margin setXYZ XYZ
    this.copy(layout = new_layout)
  }

  def setDimensions(height: Int = this.layout.height, width: Int = this.layout.width) = {
    val new_layout = this.layout.copy(height=height, width=width) setXYZ XYZ
    this.copy(layout = new_layout)
  }

  def asMultiChart(rows: Int, columns: Int) = {
    if (this.animated) throw new IllegalArgumentException("Animated Charts cannot be transformed to MultiCharts")

    val new_layout = this.layout setMultiChart MultiChart(rows, columns) setXYZ XYZ
    this.copy(layout = new_layout)
  }

  private[picta] def serialize: Value = Obj("traces" -> data_, "layout" -> layout_, "config" -> config_)

  private def createFramesAndLabels(lst: List[Series], frames: List[Obj] = Nil, labels: List[String] = Nil, index: Int = 0): (List[Obj], List[String]) = {
    lst match {
      case Nil => (frames, labels)
      case hd :: tl =>
        val new_hd = hd setName s"Frame ${index}"
        createFramesAndLabels(tl, frames :+ Obj("name" -> s"Frame ${index}", "data" -> List(new_hd.serialize)),
          labels :+ s"Frame ${index}", index+1 )
    }
  }
}