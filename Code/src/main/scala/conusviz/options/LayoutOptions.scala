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
  final case class Layout(title: String,
                          showlegend: Boolean,
                          xaxis: String = "x",
                          yaxis: String = "y",
                          // only bar chart specific
                          barmode: String = "") extends LayoutOptions {

    def createLayout(): Value = {
      val x_axis = Obj("title" -> xaxis)
      val y_axis = Obj("title" -> yaxis)
      val layout = Obj(
        "title" -> title,
        "showlegend" -> showlegend,
        "xaxis" -> x_axis,
        "yaxis" -> y_axis,
        "barmode" -> barmode,
      )
      transform(layout).to(Value)
    }
  }
}

