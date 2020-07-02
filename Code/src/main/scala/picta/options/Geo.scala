package picta.options

import picta.common.Component
import picta.options.Projection
import ujson.{Obj, Value}
import picta.Utils._
import picta.options.{MapAxis, LatAxis, LongAxis}

case class Geo(resolution: Int = 50, scope: Option[String] = None, showland: Boolean = true, showlakes: Boolean = true,
               landcolor: Option[String] = None, lakecolor: Option[String] = None, projection: Option[Projection] = None,
               coastlinewidth: Int = 2, lataxis: Option[LatAxis] = None, longaxis: Option[LongAxis] = None) extends Component {

  def +(l: LatAxis):Geo = this.copy(lataxis = Some(l))
  def +(l: LongAxis):Geo = this.copy(longaxis = Some(l))

  def serialize(): Value = {
    val acc = Obj(
      "resolution" -> resolution,
      "showland" -> showland,
      "showlakes" -> showlakes,
      "coastlinewidth" -> coastlinewidth
    )

    val scope_ = scope match {
      case Some(s) => Obj("scope" -> s)
      case None => emptyObject
    }

    val landcolor_ = landcolor match {
      case Some(l) => Obj("landcolor" -> l)
      case None => emptyObject
    }

    val lakecolor_ = lakecolor match {
      case Some(l) => Obj("lakecolor" -> l)
      case None => emptyObject
    }

    val projection_ = projection match {
      case Some(p) => Obj("projection" -> p.serialize)
      case None => emptyObject
    }

    val lataxis_ = lataxis match {
      case Some(m) => Obj("lataxis" -> m.serialize)
      case None => emptyObject
    }

    val longaxis_ = longaxis match {
      case Some(m) => Obj("lonaxis" -> m.serialize)
      case None => emptyObject
    }

    List(scope_, landcolor_, lakecolor_, projection_, lataxis_, longaxis_).foldLeft(acc)((a, x) => a.obj ++ x.obj)
  }
}
