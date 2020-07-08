package picta.options.histogram

import picta.common.Component
import picta.common.Monoid._
import picta.common.OptionWrapper._
import ujson.{Obj, Value}

case class Cumulative(enabled: Opt[Boolean] = Blank, direction: Opt[String] = Blank,
                      currentbin: Opt[String] = Blank) extends Component {

  def serialize: Value = {
    val enabled_ = enabled.asOption match {
      case Some(x) => Obj("enabled" -> x)
      case None => jsonMonoid.empty
    }

    val direction_ = direction.asOption match {
      case Some(x) => Obj("direction" -> x)
      case None => jsonMonoid.empty
    }

    val currentbin_ = currentbin.asOption match {
      case Some(x) => Obj("currentbin" -> x)
      case None => jsonMonoid.empty
    }

    List(enabled_, direction_, currentbin_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}