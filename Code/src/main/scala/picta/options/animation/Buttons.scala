package picta.options.animation

import picta.common.Component
import picta.common.Monoid.JsonMonoid
import picta.common.OptionWrapper.{Blank, Opt}
import ujson.{Null, Obj, Value}
import upickle.default.transform
import upickle.default.{ReadWriter => RW, macroRW}

class Button(method: Opt[String]=Blank, args: Opt[Args]=Blank, label: Opt[String]=Blank) extends Component {

  val method_ = method.asOption match {
    case Some(x) => Obj("method" -> x)
    case None => JsonMonoid.empty
  }

  val args_ = args.asOption match {
    case Some(x) => Obj("args" -> List(Null, x.serialize))
    case None => JsonMonoid.empty
  }

  val label_ = label.asOption match {
    case Some(x) => Obj("label" -> x)
    case None => JsonMonoid.empty
  }

  override def serialize: Value = {
    List(method_, args_, label_).foldLeft(JsonMonoid.empty)((a, x) => a |+| x)
  }
}

final case class PauseButton(method: Opt[String]=Blank, args: Opt[Args]=Blank, label: Opt[String]=Blank) extends
  Button(method=method, args=args, label=label) {
  override val args_ : Value = args.asOption match {
    case Some(x) =>
      val z = transform(List(Null)).to(Value)
      Obj("args" -> List(z, x.serialize))
    case None => JsonMonoid.empty
  }
}

final case class PlayButton(method: Opt[String]=Blank, args: Opt[Args]=Blank, label: Opt[String]=Blank) extends
  Button(method=method, args=args, label=label)