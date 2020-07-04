package picta.options.histogram

import picta.common.OptionWrapper._
import picta.common.Monoid._

import picta.common.Component
import ujson.{Obj, Value}

case class Cumulative(enabled: Opt[Boolean] = Blank, direction: Opt[String] = Blank,
                      currentbin: Opt[String] = Blank) extends Component {

  def serialize: Value = {
    val enabled_ = enabled.asOption match {
      case Some(x) => Obj("enabled" -> x)
      case None => JsonMonoid.empty
    }

    val direction_ = direction.asOption match {
      case Some(x) => Obj("direction" -> x)
      case None => JsonMonoid.empty
    }

    val currentbin_ = currentbin.asOption match {
      case Some(x) => Obj("currentbin" -> x)
      case None => JsonMonoid.empty
    }

    List(enabled_, direction_, currentbin_).foldLeft(JsonMonoid.empty)((a, x) => a |+| x)
  }
}