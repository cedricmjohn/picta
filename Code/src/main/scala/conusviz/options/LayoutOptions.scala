package conusviz.options

import conusviz.common.Component
import conusviz.options.AxisOptions._
import conusviz.options.GridOptions.Grid
import conusviz.options.LegendOptions.Legend
import ujson.{Obj, Value}
import conusviz.Utils._

sealed trait LayoutOptions extends Component

object LayoutOptions {
  /*
  * Class for specifying chart layout options
  * @param title: sets the chart title
  * @param showLegend: specify whether to show the legend
  * */
  // TODO - Create Alternative Constructor for All Combinations
  final case class Layout(title: String, axs: Option[List[Axis]] = None, showlegend: Boolean = true, legend: Option[Legend] = None,
                          height: Int = 500, width: Int = 800, grid: Option[Grid] = None) extends LayoutOptions {

    def serialize(): Value = {
      val meta = Obj(
        "title" -> Obj("text" -> title),
        "height" -> height,
        "width" -> width,
      )

      val l = legend match {
        case Some(l) => Obj("legend" -> l.serialize)
        case None => emptyObject
      }

      val g = grid match {
        case Some(g) => Obj("grid" ->g.serialize)
        case None => emptyObject
      }

      val acc = meta.obj ++ l.obj ++ g.obj

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