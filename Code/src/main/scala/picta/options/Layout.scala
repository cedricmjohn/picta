package picta.options

import picta.common.OptionWrapper._
import picta.common.Monoid._

import picta.common.Component
import ujson.{Obj, Value}

/**
  * @constructor: Specifies the layout for the chart.
  * @param title: Sets the chart title.
  * @param axs: Sets the chart title.
  * @param showlegend: Specifies whether to show the legend.
  * @param legend: This is the component that configures the legend for the chart..
  * @param height: This sets the height for the chart.
  * @param width: This sets the width for the chart.
  * @param grid: This is a component that configures the grid.
  * @param geo: this is used for Map charts only and configures the geo component for a Map chart..
  */
final case class Layout
(title: Opt[String] = Blank, axs: Opt[List[Axis]] = Empty, showlegend: Boolean = true, legend: Opt[Legend]=Blank,
 height: Int = 500, width: Int = 800, grid: Opt[Grid]=Blank, geo: Opt[Geo]=Blank) extends Component {

  def +(new_axis: Axis): Layout = axs.asOption match {
    case Some(lst) => this.copy(axs = new_axis :: lst)
    case None => this.copy(axs = List(new_axis))
  }

  def +(new_axes: List[Axis]): Layout = axs.asOption match {
    case Some(lst) => this.copy(axs = new_axes ::: lst)
    case None => this.copy(axs = new_axes)
  }

  def +(new_geo: Geo): Layout = this.copy(geo = new_geo)
  def +(new_legend: Legend): Layout = this.copy(legend = new_legend)
  def +(new_grid: Grid): Layout = this.copy(grid = new_grid)

  def serialize(): Value = {
    val dim = Obj("height" -> height, "width" -> width)

    val title_ = title.asOption match {
      case Some(x) => Obj("title" -> Obj("text" -> x))
      case None => JsonMonoid.empty
    }

    val legend_ = legend.asOption match {
      case Some(x) => Obj("legend" -> x.serialize)
      case None => JsonMonoid.empty
    }

    val grid_ = grid.asOption match {
      case Some(x) => Obj("grid" -> x.serialize)
      case None => JsonMonoid.empty
    }

    val geo_ = geo.asOption match {
      case Some(x) => Obj("geo" -> x.serialize)
      case None => JsonMonoid.empty
    }

    val combined = List(dim, title_, legend_, grid_, geo_).foldLeft(JsonMonoid.empty)((a, x) => a |+| x)

    axs.asOption match {
      case Some(lst) => lst.foldLeft( combined )((a, x) => a |+| x.serialize())
      case _ => combined
    }
  }
}