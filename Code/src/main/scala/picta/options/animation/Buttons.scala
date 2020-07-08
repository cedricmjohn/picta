package picta.options.animation

import picta.common.Component
import picta.common.Monoid.jsonMonoid
import picta.common.OptionWrapper.{Blank, Opt}
import ujson.{Null, Obj, Value}
import upickle.default.transform

class Button(method: Opt[String] = Blank, args: Opt[Args] = Blank, label: Opt[String] = Blank) extends Component {

  protected val method_ = method.asOption match {
    case Some(x) => Obj("method" -> x)
    case None => jsonMonoid.empty
  }

  protected val args_ = args.asOption match {
    case Some(x) => Obj("args" -> List(Null, x.serialize))
    case None => jsonMonoid.empty
  }

  protected val label_ = label.asOption match {
    case Some(x) => Obj("label" -> x)
    case None => jsonMonoid.empty
  }

  override def serialize: Value = {
    List(method_, args_, label_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}

final case class PauseButton(method: Opt[String] = Blank, args: Opt[Args] = Blank, label: Opt[String] = Blank) extends
  Button(method = method, args = args, label = label) {
  override val args_ : Value = args.asOption match {
    case Some(x) =>
      val z = transform(List(Null)).to(Value)
      Obj("args" -> List(z, x.serialize))
    case None => jsonMonoid.empty
  }
}

final case class PlayButton(method: Opt[String] = Blank, args: Opt[Args] = Blank, label: Opt[String] = Blank) extends
  Button(method = method, args = args, label = label)