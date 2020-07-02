package picta.options

import picta.common.Component
import picta.options.Axis._
import picta.options.Grid
import picta.options.Legend
import ujson.{Obj, Value}
import picta.Utils._
import picta.options.Geo

/*
* Class for specifying chart layout options
* @param title: sets the chart title
* @param showLegend: specify whether to show the legend
* */
final case class Layout
(title: Option[String] = None, axs: Option[List[Axis]] = None, showlegend: Boolean = true, legend: Option[Legend] = None,
 height: Int = 500, width: Int = 800, grid: Option[Grid] = None, geo: Option[Geo] = None) extends Component {

  def +(new_axis: Axis): Layout = axs match {
    case Some(lst) => this.copy(axs = Some(new_axis :: lst))
    case None => this.copy(axs = Some(List(new_axis)))
  }

  def +(new_axes: List[Axis]): Layout = axs match {
    case Some(lst) => this.copy(axs = Some(new_axes ::: lst))
    case None => this.copy(axs = Some(new_axes))
  }

  def +(new_geo: Geo): Layout = this.copy(geo = Some(new_geo))
  def +(new_legend: Legend): Layout = this.copy(legend = Some(new_legend))
  def +(new_grid: Grid): Layout = this.copy(grid = Some(new_grid))

  def serialize(): Value = {
    val acc = Obj("height" -> height, "width" -> width)

    val title_ = title match {
      case Some(t) => Obj("title" -> Obj("text" -> t))
      case None => emptyObject
    }

    val legend_ = legend match {
      case Some(l) => Obj("legend" -> l.serialize)
      case None => emptyObject
    }

    val grid_ = grid match {
      case Some(g) => Obj("grid" ->g.serialize)
      case None => emptyObject
    }

    val geo_ = geo match {
      case Some(g) => Obj("geo" -> g.serialize)
      case None => emptyObject
    }

    val combined = List(title_, legend_, grid_, geo_).foldLeft(acc)((a, x) => a.obj ++ x.obj)

    axs match {
      case Some(lst) => lst.foldLeft( combined )((a, x) => a.obj ++ x.serialize().obj)
      case _ => combined
    }
  }
}

object Layout {
  def defaultAxis(): List[Axis] = {
    val x = Axis(key="xaxis", title="x")
    val y = Axis(key="yaxis", title="y")
    List(x, y)
  }
}