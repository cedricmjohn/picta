package conusviz.options

import conusviz.common.Component
import ujson.{Obj, Value}
import upickle.default.{ReadWriter => RW, _}

sealed trait ConfigOptions extends Component

object ConfigOptions {
  /*
* Class for specifying chart config options
* @param responsive: specify whether the chart should be responsive to window size
* @param scrollZoom: specifiy whether mousewheel or two-finger scroll zooms the plot
* */
  final case class Config(responsive: Boolean = true, scrollZoom: Boolean = true) extends ConfigOptions {
    def serialize(): Value = {
      val config = Obj("responsive" -> responsive, "scrollZoom" -> scrollZoom, "displaylogo" -> false)
      transform(config).to(Value)
    }
  }
}
