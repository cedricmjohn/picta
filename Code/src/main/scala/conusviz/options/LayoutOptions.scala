package conusviz.options

import ujson.{Obj, Value}
import upickle.default.{ReadWriter => RW, _}

sealed trait LayoutOptions {
  def createLayout: Value
}

object LayoutOptions {
  // implicit definition to allow Config case class to become seriazable
  implicit def layout_rw: RW[Layout] = macroRW

  /*
  * Class for specifying chart layout options
  * @param title: sets the chart title
  * @param showLegend: specify whether to show the legend
  * */
  final case class Layout(title: String, showlegend: Boolean, x_label: String = "x", y_label: String = "y") extends LayoutOptions {
    def createLayout(): Value = {
      val x_axis = Obj("title" -> x_label)
      val y_axis = Obj("title" -> y_label)
      val layout = Obj("title" -> title, "showlegend" -> showlegend, "xaxis" -> x_axis, "yaxis" -> y_axis)
      transform(layout).to(Value)
    }
  }
}
