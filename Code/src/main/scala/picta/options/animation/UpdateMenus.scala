package picta.options.animation

import picta.common.Component
import picta.common.Monoid.JsonMonoid
import picta.common.OptionWrapper._
import picta.options.Margin
import ujson.{Null, Obj, Value}

case class UpdateMenus(x: Opt[Double]=Blank, y: Opt[Double]=Blank, xanchor: Opt[String]=Blank, yanchor: Opt[String]=Blank,
                       showactive: Opt[Boolean]=Blank, direction: Opt[String] = Blank, menu_type: Opt[String]=Blank,
                       pad: Opt[Margin]=Blank, buttons: Opt[List[Button]]=Empty, slider: Opt[Slider]=Blank) extends Component {


  override def serialize: Value = {

    val x_ = x.asOption match {
      case Some(x) => Obj("x" -> x)
      case None => JsonMonoid.empty
    }

    val y_ = y.asOption match {
      case Some(x) => Obj("y" -> x)
      case None => JsonMonoid.empty
    }

    val xanchor_ = xanchor.asOption match {
      case Some(x) => Obj("xanchor" -> x)
      case None => JsonMonoid.empty
    }

    val yanchor_ = yanchor.asOption match {
      case Some(x) => Obj("yanchor" -> x)
      case None => JsonMonoid.empty
    }

    val showactive_ = showactive.asOption match {
      case Some(x) => Obj("showactive" -> x)
      case None => JsonMonoid.empty
    }

    val direction_ = direction.asOption match {
      case Some(x) => Obj("direction" -> x)
      case None => JsonMonoid.empty
    }

    val menu_type_ = menu_type.asOption match {
      case Some(x) => Obj("type" -> x)
      case None => JsonMonoid.empty
    }

    val pad_ =  pad.asOption match {
      case Some(x) => Obj("pad" -> x.serialize)
      case None => JsonMonoid.empty
    }

    val buttons_ =  buttons.asOption match {
      case Some(x) => Obj("buttons" -> x.map(b => b.serialize))
      case None => JsonMonoid.empty
    }

    val slider_ =  slider.asOption match {
      case Some(x) => Obj("slider" -> x.serialize)
      case None => JsonMonoid.empty
    }

    List(x_, y_, xanchor_, yanchor_, showactive_, direction_, menu_type_, pad_, buttons_, slider_)
      .foldLeft(JsonMonoid.empty)((a, x) => a |+| x)
  }
}
