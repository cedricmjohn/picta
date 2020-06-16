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
  case class Layout(title: String, showlegend: Boolean) extends LayoutOptions {
    def createLayout(): Value = {
      val layout = Obj("title" -> title, "showlegend" -> showlegend)
      transform(layout).to(Value)
    }
  }
}
