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

  def setAxis(new_axis: LatAxis): Geo = this.copy(lataxis = new_axis)

  def setAxis(new_axis: LongAxis): Geo = this.copy(longaxis = new_axis)

  def setAxes(lat: LatAxis, lon: LongAxis): Geo = this.copy(lataxis=lat, longaxis = lon)

  def serialize(): Value = {
    val meta = Obj(
      "resolution" -> resolution,
      "showland" -> showland,
      "showlakes" -> showlakes,
      "coastlinewidth" -> coastlinewidth
    )

    val scope_ = scope.asOption match {
      case Some(x) => Obj("scope" -> x)
      case None => jsonMonoid.empty
    }

    val landcolor_ = landcolor.asOption match {
      case Some(x) => Obj("landcolor" -> x)
      case None => jsonMonoid.empty
    }

    val lakecolor_ = lakecolor.asOption match {
      case Some(x) => Obj("lakecolor" -> x)
      case None => jsonMonoid.empty
    }

    val projection_ = projection.asOption match {
      case Some(x) => Obj("projection" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val lataxis_ = lataxis.asOption match {
      case Some(x) => Obj("lataxis" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val longaxis_ = longaxis.asOption match {
      case Some(x) => Obj("lonaxis" -> x.serialize)
      case None => jsonMonoid.empty
    }

    List(meta, scope_, landcolor_, lakecolor_, projection_, lataxis_, longaxis_)
      .foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}