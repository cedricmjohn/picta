package picta.options.animation

import picta.common.Component
import picta.common.Monoid.JsonMonoid
import picta.common.OptionWrapper.{Blank, Opt}
import ujson.{Obj, Value}

case class Setting(duration: Opt[Double]=Blank, redraw: Opt[Boolean]=Blank) extends Component {
  override def serialize: Value = {
    val duration_ = duration.asOption match {
      case Some(x) => Obj("duration" -> x)
      case None => JsonMonoid.empty
    }

    val redraw_ = redraw.asOption match {
      case Some(x) => Obj("redraw" -> x)
      case None => JsonMonoid.empty
    }

    List(duration_, redraw_).foldLeft(JsonMonoid.empty)((a, x) => a |+| x)
  }
}

case class Args(mode: String = "immediate", fromcurrent: Opt[Boolean] = Blank, transition: Opt[Setting]=Blank,
                frame: Opt[Setting]=Blank) extends Component {

  override def serialize: Value = {
    val mode_ = Obj("mode" -> mode)

    val fromcurrent_ = fromcurrent.asOption match {
      case Some(x) => Obj("fromcurrent" -> x)
      case None => JsonMonoid.empty
    }

    val transition_ = transition.asOption match {
      case Some(x) => Obj("transition" -> x.serialize)
      case None => JsonMonoid.empty
    }

    val frame_ = frame.asOption match {
      case Some(x) => Obj("frame" -> x.serialize)
      case None => JsonMonoid.empty
    }

    List(mode_, fromcurrent_, transition_, frame_).foldLeft(JsonMonoid.empty)((a, x) => a |+| x)
  }
}
