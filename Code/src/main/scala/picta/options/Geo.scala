package picta.options

import picta.common.Component
import ujson.{Obj, Value}
import picta.Utils._

/**
  * @constructor: This is configures the Chart for a Map.
 *  @param resolution: This sets the resolution.
 *  @param scope: This determines the geographic scope for the map.
 *  @param showland: Specifies whether the land is shown on the map.
 *  @param showlakes: Specifies whether lakes are shown on the map.
 *  @param landcolor: Specifcies the landcolor.
 *  @param lakecolor: Specifies the lakecolor.
 *  @param projection:
 *  @param coastlinewidth: Specifies the coast line width on the map.
 *  @param lataxis: This is the component that configures the lataxis.
 *  @param longaxis: This is the component that configures the longaxis.
 */
case class Geo(resolution: Int = 50, scope: Option[String] = None, showland: Boolean = true, showlakes: Boolean = true,
               landcolor: Option[String] = None, lakecolor: Option[String] = None, projection: Option[Projection] = None,
               coastlinewidth: Int = 2, lataxis: Option[LatAxis] = None, longaxis: Option[LongAxis] = None) extends Component {

  def +(l: LatAxis):Geo = this.copy(lataxis = Some(l))
  def +(l: LongAxis):Geo = this.copy(longaxis = Some(l))

  def serialize(): Value = {
    val meta = Obj(
      "resolution" -> resolution,
      "showland" -> showland,
      "showlakes" -> showlakes,
      "coastlinewidth" -> coastlinewidth
    )

    val scope_ = scope match {
      case Some(x) => Obj("scope" -> x)
      case None => emptyObject
    }

    val landcolor_ = landcolor match {
      case Some(x) => Obj("landcolor" -> x)
      case None => emptyObject
    }

    val lakecolor_ = lakecolor match {
      case Some(x) => Obj("lakecolor" -> x)
      case None => emptyObject
    }

    val projection_ = projection match {
      case Some(x) => Obj("projection" -> x.serialize)
      case None => emptyObject
    }

    val lataxis_ = lataxis match {
      case Some(x) => Obj("lataxis" -> x.serialize)
      case None => emptyObject
    }

    val longaxis_ = longaxis match {
      case Some(x) => Obj("lonaxis" -> x.serialize)
      case None => emptyObject
    }

    List(meta, scope_, landcolor_, lakecolor_, projection_, lataxis_, longaxis_)
      .foldLeft(emptyObject)((a, x) => a.obj ++ x.obj)
  }
}

object Geo {
  implicit def liftToOption[T](x: T): Option[T] = Option[T](x)
}
