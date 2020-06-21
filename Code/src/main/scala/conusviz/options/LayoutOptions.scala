package conusviz.options

import conusviz.options.AxisOptions._
import ujson.{Obj, Value}
import upickle.default._

sealed trait LayoutOptions {
  def createLayout: Value
}

object LayoutOptions {
  /*
  * Class for specifying chart layout options
  * @param title: sets the chart title
  * @param showLegend: specify whether to show the legend
  * */
  // TODO - Create Alternative Constructor for All Combinations
  final case class Layout
  (title: String, axs: List[Axis], showlegend: Boolean = true, height: Int = 500, width: Int = 1000) extends LayoutOptions {
    def createLayout(): Value = {
      val meta_data: Value =
        transform(Obj("title" -> title, "showlegend" -> showlegend, "height" -> height, "width" -> width)).to(Value)
      axs.foldLeft(meta_data)((acc, x) => acc.obj ++ x.createAxis().obj)
    }
  }

  object Layout {
    def defaultAxis(): List[Axis] = {
      val x = Axis(name="xaxis", title="x")
      val y = Axis(name="yaxis", title="y")
      List(x, y)
    }

    def apply(title: String): Layout = Layout(title=title, axs=defaultAxis())

    def apply(title: String, showlegend: Boolean): Layout = Layout(title=title, axs=defaultAxis(), showlegend=showlegend)

    def apply(title: String, showlegend: Boolean, height: Int, width: Int): Layout = {
      Layout(title=title, axs=defaultAxis(), showlegend=showlegend, height=height, width=width)
    }
  }
}