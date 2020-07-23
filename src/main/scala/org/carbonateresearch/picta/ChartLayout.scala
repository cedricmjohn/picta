package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper.{Blank, Empty, Opt}
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import org.carbonateresearch.picta.options._
import ujson.{Obj, Value}

trait HoverMode
case object X extends HoverMode
case object Y extends HoverMode
case object CLOSEST extends HoverMode
case object FALSE extends HoverMode
case object X_UNIFIED extends HoverMode
case object Y_UNIFIED extends HoverMode

/**
 * Specifies the layout for the chart.
 * @param title      : Sets the chart title.
 * @param axes        : Sets the chart title.
 * @param showlegend : Specifies whether to show the legend.
 * @param autosize   : This is the component that configures the legend for the chart..
 * @param legend     : This is the component that configures the legend for the chart..
 * @param height     : This sets the height for the chart.
 * @param width      : This sets the width for the chart.
 * @param mapoption  : this is used for Map charts only and configures the geo component for a Map chart.
 */
final case class ChartLayout
(title: Opt[String] = Blank, axes: Opt[List[Axis]] = Empty, legend: Legend = Legend(), autosize: Opt[Boolean] = Blank,
 margin: Opt[Margin] = Blank, mapoption: Opt[MapOptions] = Blank, multichart: Opt[MultiChart] = Blank, showlegend: Boolean = true,
 hovermode: HoverMode = CLOSEST, height: Int = 550, width: Int = 600) extends Component {

  def setTitle(new_title: String) = this.copy(title = new_title)

  def setAxes(new_axes: List[Axis]): ChartLayout = axes.option match {
    case Some(lst) => this.copy(axes = new_axes ::: lst)
    case None => this.copy(axes = new_axes)
  }

  def setAxes(new_axis: Axis*): ChartLayout = axes.option match {
    case Some(lst) => this.copy(axes = new_axis.toList ::: lst)
    case None => this.copy(axes = new_axis.toList)
  }

  def setLegend(new_legend: Legend): ChartLayout = this.copy(legend = new_legend, showlegend = true)

  def setLegend(x: Opt[Double] = Blank, y: Opt[Double] = Blank, orientation: Orientation = VERTICAL,
                xanchor: Opt[Anchor] = Blank, yanchor: Opt[Anchor] = Blank) = {

    val new_legend = Legend(x=x, y=y, orientation=orientation, xanchor=xanchor, yanchor=yanchor)
    this.copy(legend = new_legend)
  }

  def setAutosize(new_autosize: Boolean) = this.copy(autosize = new_autosize)

  def setMargin(new_margin: Margin): ChartLayout = this.copy(margin = new_margin)

  def setMapOption(new_geo: MapOptions): ChartLayout = this.copy(mapoption = new_geo)

  def setMultiChart(new_minigrid: MultiChart) = this.copy(multichart = new_minigrid)

  def showLegend(new_showlegend: Boolean) = this.copy(showlegend = new_showlegend)

  def setHoverMode(new_hovermode: HoverMode) = this.copy(hovermode = new_hovermode)

  def setHeight(new_height: Int) = this.copy(height = new_height)

  def setWidth(new_width: Int) = this.copy(width = new_width)

  def setDimensions(new_height: Int, new_width: Int) = this.copy(height = new_height, width = new_width)

  private[picta] def serialize(): Value = {
    val dim = Obj("height" -> height, "width" -> width, "hovermode" -> hovermode.toString.toLowerCase, "legend" -> legend.serialize)

    val title_ = title.option match {
      case Some(x) => Obj("title" -> Obj("text" -> x))
      case None => jsonMonoid.empty
    }

    val showlegend_ = Obj("showlegend" -> showlegend)

    val autosize_ = autosize.option match {
      case Some(x) => Obj("autosize" -> x)
      case None => jsonMonoid.empty
    }

    val geo_ = mapoption.option match {
      case Some(x) => Obj("geo" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val margin_ = margin.option match {
      case Some(x) => Obj("margin" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val minigrid_ = multichart.option match {
      case Some(x) => Obj("grid" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val combined = List(dim, title_, showlegend_, autosize_, geo_, margin_, minigrid_)
      .foldLeft(jsonMonoid.empty)((a, x) => a |+| x)

    axes.option match {
      case Some(lst) => lst.foldLeft(combined)((a, x) => a |+| x.serialize())
      case _ => combined
    }
  }
}
