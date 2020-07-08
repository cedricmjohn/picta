package picta.options.histogram2d

import picta.common.Component
import picta.common.Monoid._
import picta.common.OptionWrapper._
import ujson.{Obj, Value}

case class Hist2dOptions(ncontours: Opt[Int] = Blank, reversescale: Opt[Boolean] = Opt(Option(true)),
                         showscale: Opt[Boolean] = Blank) extends Component {

  def serialize: Value = {
    val ncountours_ = ncontours.asOption match {
      case Some(x) => Obj("ncontours" -> x)
      case _ => jsonMonoid.empty
    }

    val reversescale_ = reversescale.asOption match {
      case Some(x) => Obj("reversescale" -> x)
      case _ => jsonMonoid.empty
    }

    val showscale_ = showscale.asOption match {
      case Some(x) => Obj("showscale" -> x)
      case _ => jsonMonoid.empty
    }

    List(ncountours_, reversescale_, showscale_).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}
