package conusviz

/*{
  title: 'Template Title',

  annotations: [{
  text: 'First point',
  name:'first',
  yref: 'y', xref: 'x',
  ay: 40, ax: 30,
  font: {size: 16}
  }],

  showlegend: false
};*/

import upickle.default._
import ujson.{Obj, Value}
import upickle.default.{ReadWriter => RW}

sealed trait LayoutOption {
  def createLayout: Value
}

object LayoutOption {
  implicit def layout_rw: RW[Layout] = macroRW

  case class Layout(title: String, showlegend: Boolean) extends LayoutOption {
    def createLayout(): Value = {
      val layout = Obj("title" -> title, "showlegend" -> showlegend)
      transform(layout).to(Value)
    }
  }
}
