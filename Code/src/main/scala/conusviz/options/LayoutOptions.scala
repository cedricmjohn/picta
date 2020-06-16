package conusviz.options

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

import ujson.{Obj, Value}
import upickle.default.{ReadWriter => RW, _}

sealed trait LayoutOptions {
  def createLayout: Value
}

object LayoutOptions {
  implicit def layout_rw: RW[Layout] = macroRW

  case class Layout(title: String, showlegend: Boolean) extends LayoutOptions {
    def createLayout(): Value = {
      val layout = Obj("title" -> title, "showlegend" -> showlegend)
      transform(layout).to(Value)
    }
  }
}
