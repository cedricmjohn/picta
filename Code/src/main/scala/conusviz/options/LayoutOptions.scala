package conusviz.options

import conusviz.common.Component
import conusviz.options.AxisOptions._
import conusviz.options.GridOptions.Grid
import conusviz.options.LegendOptions.Legend
import ujson.{Obj, Value}
import conusviz.Utils._
import conusviz.options.GeoOptions.Geo

sealed trait LayoutOptions extends Component

object LayoutOptions {
  /*
  * Class for specifying chart layout options
  * @param title: sets the chart title
  * @param showLegend: specify whether to show the legend
  * */
  final case class Layout
  (title: Option[String] = None, axs: Option[List[Axis]] = None, showlegend: Boolean = true, legend: Option[Legend] = None,
   height: Int = 500, width: Int = 800, grid: Option[Grid] = None, geo: Option[Geo] = None) extends LayoutOptions {

    def +(new_axis: Axis): Layout = axs match {
        case Some(lst) => this.copy(axs = Some(new_axis :: lst))
        case None => this.copy(axs = Some(List(new_axis)))
    }

    def +(new_geo: Geo): Layout = this.copy(geo = Some(new_geo))
    def +(new_legend: Legend): Layout = this.copy(legend = Some(new_legend))


    def serialize(): Value = {
      val dim = Obj("height" -> height, "width" -> width)

      val t = title match {
        case Some(t) => Obj("title" -> Obj("text" -> t))
        case None => emptyObject
      }

      val l = legend match {
        case Some(l) => Obj("legend" -> l.serialize)
        case None => emptyObject
      }

      val gr = grid match {
        case Some(g) => Obj("grid" ->g.serialize)
        case None => emptyObject
      }

      val ge = geo match {
        case Some(g) => Obj("geo" -> g.serialize)
        case None => emptyObject
      }

      val acc = dim.obj ++ t.obj ++ l.obj ++ gr.obj ++ ge.obj

      axs match {
        case Some(lst) => lst.foldLeft( acc )((a, x) => a.obj ++ x.serialize().obj)
        case _ => acc
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
}