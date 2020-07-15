package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper.{Blank, Empty, Opt}
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import org.carbonateresearch.picta.options._
import ujson.{Obj, Value}

/**
 * @constructor: Specifies the layout for the chart.
 * @param title      : Sets the chart title.
 * @param axs        : Sets the chart title.
 * @param showlegend : Specifies whether to show the legend.
 * @param autosize   : This is the component that configures the legend for the chart..
 * @param legend     : This is the component that configures the legend for the chart..
 * @param height     : This sets the height for the chart.
 * @param width      : This sets the width for the chart.
 * @param grid       : This is a component that configures the grid.
 * @param geo        : this is used for Map charts only and configures the geo component for a Map chart..
 */
final case class Layout
(title: Opt[String] = Blank, axs: Opt[List[Axis]] = Empty, legend: Opt[Legend] = Blank, autosize: Opt[Boolean] = Blank,
 margin: Opt[Margin] = Blank, grid: Opt[Subplot] = Blank, geo: Opt[Geo] = Blank, showlegend: Boolean = false,
 hovermode: String = "closest", height: Int = 550, width: Int = 600) extends Component {

  def setAxes(new_axes: List[Axis]): Layout = axs.option match {
    case Some(lst) => this.copy(axs = new_axes ::: lst)
    case None => this.copy(axs = new_axes)
  }

  def setAxes(new_axis: Axis*): Layout = axs.option match {
    case Some(lst) => this.copy(axs = new_axis.toList ::: lst)
    case None => this.copy(axs = new_axis.toList)
  }

  def setGeo(new_geo: Geo): Layout = this.copy(geo = new_geo)

  def setLegend(new_legend: Legend): Layout = this.copy(legend = new_legend, showlegend = true)

  def setSubplot(new_grid: Subplot): Layout = this.copy(grid = new_grid)

  def setMargin(new_margin: Margin): Layout = this.copy(margin = new_margin)

  private[picta] def serialize(): Value = {
    val dim = Obj("height" -> height, "width" -> width, "hovermode" -> hovermode)

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

    val grid_ = grid.option match {
      case Some(x) => Obj("grid" -> x.serialize)
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

    val combined = List(dim, title_, showlegend_, legend_, autosize_, grid_, geo_, margin_)
      .foldLeft(jsonMonoid.empty)((a, x) => a |+| x)

    axs.option match {
      case Some(lst) => lst.foldLeft(combined)((a, x) => a |+| x.serialize())
      case _ => combined
    }
  }
}
