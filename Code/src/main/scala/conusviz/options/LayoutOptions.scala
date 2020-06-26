package conusviz.options

import conusviz.common.Component
import conusviz.options.AxisOptions._
import conusviz.options.GridOptions.Grid
import conusviz.options.LegendOptions.Legend
import ujson.{Obj, Value}
import upickle.default._
import conusviz.Utils._

sealed trait LayoutOptions extends Component

object LayoutOptions {
  /*
  * Class for specifying chart layout options
  * @param title: sets the chart title
  * @param showLegend: specify whether to show the legend
  * */
  // TODO - Create Alternative Constructor for All Combinations
  final case class Layout(title: String, axs: List[Axis], showlegend: Boolean = true, legend: Option[Legend] = None,
                          height: Int = 500, width: Int = 800, grid: Option[Grid] = None) extends LayoutOptions {

    def serialize(): Value = {
      var acc = emptyObject.obj ++ Obj(
        "title" -> Obj("text" -> title),
        "height" -> height,
        "width" -> width,
      ).obj

      legend match {
        case Some(l) => acc.obj ++ Obj("legend" -> l.serialize).obj
        case None => ()
      }

      grid match {
        case Some(g) => acc.obj ++ Obj("grid" ->g.serialize).obj
        case None => ()
      }

      axs.foldLeft(acc)((acc, x) => acc.obj ++ x.serialize().obj)
    }
  }

  object Layout {
    def defaultAxis(): List[Axis] = {
      val x = Axis(key="xaxis", title="x")
      val y = Axis(key="yaxis", title="y")
      List(x, y)
    }

    def apply(title: String): Layout = Layout(title=title, axs=defaultAxis())
    def apply(title: String, grid: Grid): Layout = Layout(title=title, axs=defaultAxis(), grid=Some(grid))

//    def apply(title: String, showlegend: Boolean): Layout = Layout(title=title, axs=defaultAxis())

//    def apply(title: String, showlegend: Boolean, height: Int, width: Int): Layout = {
//      Layout(title=title, axs=defaultAxis(),, height=height, width=width)
//    }
  }
}