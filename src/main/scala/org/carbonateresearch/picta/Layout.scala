package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper.{Blank, Empty, Opt}
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import org.carbonateresearch.picta.options._
import ujson.IndexedValue.False
import ujson.{Obj, Value}

trait HoverMode

case object X extends HoverMode
case object Y extends HoverMode
case object CLOSEST extends HoverMode
case object FALSE extends HoverMode
case object X_UNIFIED extends HoverMode
case object Y_UNIFIED extends HoverMode

/**
 * @constructor: Specifies the layout for the chart.
 * @param title      : Sets the chart title.
 * @param axes        : Sets the chart title.
 * @param showlegend : Specifies whether to show the legend.
 * @param autosize   : This is the component that configures the legend for the chart..
 * @param legend     : This is the component that configures the legend for the chart..
 * @param height     : This sets the height for the chart.
 * @param width      : This sets the width for the chart.
 * @param geo        : this is used for Map charts only and configures the geo component for a Map chart..
 */
final case class Layout
(title: Opt[String] = Blank, axes: Opt[List[Axis]] = Empty, legend: Opt[Legend] = Blank, autosize: Opt[Boolean] = Blank,
 margin: Opt[Margin] = Blank, geo: Opt[MapOptions] = Blank, multichart: Opt[MultiChart] = Blank, showlegend: Boolean = false,
 hovermode: HoverMode = CLOSEST, height: Int = 550, width: Int = 600) extends Component {

  def setTitle(new_title: String) = this.copy(title = new_title)

  def showLegend(new_showlegend: Boolean) = this.copy(showlegend = new_showlegend)

  def setAxes(new_axes: List[Axis]): Layout = axes.option match {
    case Some(lst) => this.copy(axes = new_axes ::: lst)
    case None => this.copy(axes = new_axes)
  }

  def setAxes(new_axis: Axis*): Layout = axes.option match {
    case Some(lst) => this.copy(axes = new_axis.toList ::: lst)
    case None => this.copy(axes = new_axis.toList)
  }

  def setMapOption(new_geo: MapOptions): Layout = this.copy(geo = new_geo)

  def setLegend(new_legend: Legend): Layout = this.copy(legend = new_legend, showlegend = true)

  def setMargin(new_margin: Margin): Layout = this.copy(margin = new_margin)

  def setMultiChart(new_minigrid: MultiChart) = this.copy(multichart = new_minigrid)

  private[picta] def serialize(): Value = {
    val dim = Obj("height" -> height, "width" -> width, "hovermode" -> hovermode.toString.toLowerCase)

    val title_ = title.option match {
      case Some(x) => Obj("title" -> Obj("text" -> x))
      case None => jsonMonoid.empty
    }

    val showlegend_ = Obj("showlegend" -> showlegend)

    val legend_ = legend.option match {
      case Some(x) => Obj("legend" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val autosize_ = autosize.option match {
      case Some(x) => Obj("autosize" -> x)
      case None => jsonMonoid.empty
    }

    val geo_ = geo.option match {
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

    val combined = List(dim, title_, showlegend_, legend_, autosize_, geo_, margin_, minigrid_)
      .foldLeft(jsonMonoid.empty)((a, x) => a |+| x)

    axes.option match {
      case Some(lst) => lst.foldLeft(combined)((a, x) => a |+| x.serialize())
      case _ => combined
    }
  }
}
