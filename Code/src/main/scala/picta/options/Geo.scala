package picta.options

import picta.common.Component
import picta.common.Monoid._
import picta.common.OptionWrapper._
import ujson.{Obj, Value}

/**
 * @constructor: This is configures the Chart for a Map.
 * @param resolution     : This sets the resolution.
 * @param scope          : This determines the geographic scope for the map.
 * @param showland       : Specifies whether the land is shown on the map.
 * @param showlakes      : Specifies whether lakes are shown on the map.
 * @param landcolor      : Specifcies the landcolor.
 * @param lakecolor      : Specifies the lakecolor.
 * @param projection     :
 * @param coastlinewidth : Specifies the coast line width on the map.
 * @param lataxis        : This is the component that configures the lataxis.
 * @param longaxis       : This is the component that configures the longaxis.
 */
case class Geo(scope: Opt[String] = Blank, landcolor: Opt[String] = Blank, lakecolor: Opt[String] = Blank,
               projection: Opt[Projection] = Blank, lataxis: Opt[LatAxis] = Blank, longaxis: Opt[LongAxis] = Blank,
               showland: Boolean = true, showlakes: Boolean = true, resolution: Int = 50, coastlinewidth: Int = 2) extends Component {

  def +(new_axis: LatAxis): Geo = this.copy(lataxis = new_axis)

  def +(new_axis: LongAxis): Geo = this.copy(longaxis = new_axis)

  def serialize(): Value = {
    val meta = Obj(
      "resolution" -> resolution,
      "showland" -> showland,
      "showlakes" -> showlakes,
      "coastlinewidth" -> coastlinewidth
    )

    val scope_ = scope.asOption match {
      case Some(x) => Obj("scope" -> x)
      case None => JsonMonoid.empty
    }

    val landcolor_ = landcolor.asOption match {
      case Some(x) => Obj("landcolor" -> x)
      case None => JsonMonoid.empty
    }

    val lakecolor_ = lakecolor.asOption match {
      case Some(x) => Obj("lakecolor" -> x)
      case None => JsonMonoid.empty
    }

    val projection_ = projection.asOption match {
      case Some(x) => Obj("projection" -> x.serialize)
      case None => JsonMonoid.empty
    }

    val lataxis_ = lataxis.asOption match {
      case Some(x) => Obj("lataxis" -> x.serialize)
      case None => JsonMonoid.empty
    }

    val longaxis_ = longaxis.asOption match {
      case Some(x) => Obj("lonaxis" -> x.serialize)
      case None => JsonMonoid.empty
    }

    List(meta, scope_, landcolor_, lakecolor_, projection_, lataxis_, longaxis_)
      .foldLeft(JsonMonoid.empty)((a, x) => a |+| x)
  }
}