package conusviz.options

import conusviz.common.Component
import conusviz.options.AxisOptions._
import conusviz.options.LegendOptions.Legend
import ujson.{Obj, Value}
import upickle.default._

sealed trait LayoutOptions extends Component

object LayoutOptions {
  /*
  * Class for specifying chart layout options
  * @param title: sets the chart title
  * @param showLegend: specify whether to show the legend
  * */
  // TODO - Create Alternative Constructor for All Combinations
  final case class Layout
  (title: String, axs: List[Axis], legend: Legend = Legend(true, 0, 1), height: Int = 500, width: Int = 800) extends LayoutOptions {
    def serialize(): Value = {
      val meta_data: Value =
        transform(Obj("title" -> title, "legend" -> legend.serialize, "height" -> height, "width" -> width)).to(Value)
      axs.foldLeft(meta_data)((acc, x) => acc.obj ++ x.serialize().obj)
    }
  }

  object Layout {
    def defaultAxis(): List[Axis] = {
      val x = Axis(key="xaxis", title="x")
      val y = Axis(key="yaxis", title="y")
      List(x, y)
    }

    def apply(title: String): Layout = Layout(title=title, axs=defaultAxis())

//    def apply(title: String, showlegend: Boolean): Layout = Layout(title=title, axs=defaultAxis())

//    def apply(title: String, showlegend: Boolean, height: Int, width: Int): Layout = {
//      Layout(title=title, axs=defaultAxis(),, height=height, width=width)
//    }
  }
}