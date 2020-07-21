package org.carbonateresearch.picta.options

import org.carbonateresearch.picta.Component
import org.carbonateresearch.picta.common.Monoid._
import org.carbonateresearch.picta.OptionWrapper._
import ujson.{Obj, Value}

sealed trait Region
case object WORLD extends Region
case object USA extends Region
case object EUROPE extends Region
case object ASIA extends Region
case object AFRICA extends Region
case object NORTH_AMERICA extends Region
case object SOUTH_AMERICA extends Region

/**
 * @constructor: This is configures the Chart for a Map.
 * @param resolution     : This sets the resolution.
 * @param region          : This determines the geographic scope for the map.
 * @param showland       : Specifies whether the land is shown on the map.
 * @param showlakes      : Specifies whether lakes are shown on the map.
 * @param landcolor      : Specifcies the landcolor.
 * @param lakecolor      : Specifies the lakecolor.
 * @param projection     :
 * @param coastlinewidth : Specifies the coast line width on the map.
 * @param lataxis        : This is the component that configures the lataxis.
 * @param longaxis       : This is the component that configures the longaxis.
 */
final case class MapOptions(region: Opt[Region] = Blank, landcolor: Opt[String] = Blank, lakecolor: Opt[String] = Blank,
                            projection: Opt[Projection] = Blank, lataxis: Opt[LatAxis] = Blank, longaxis: Opt[LongAxis] = Blank,
                            showland: Boolean = true, showlakes: Boolean = true, resolution: Int = 50, coastlinewidth: Int = 2) extends Component {

  def setRegion(new_region: Region) = this.copy(region = new_region)

  def setLandColor(new_landcolor: String) = this.copy(landcolor = new_landcolor)

  def setLakeColor(new_lakecolor: String) = this.copy(lakecolor = new_lakecolor)

  def setProject(new_projection: Projection) = this.copy(projection = new_projection)

  def setShowLand(new_showland: Boolean) = this.copy(showland = new_showland)

  def setShowLakes(new_showlakes: Boolean) = this.copy(showlakes = new_showlakes)

  def setResolution(new_resolution: Int) = this.copy(resolution = new_resolution)

  def setCoastLineWidth(new_coastlinewidth: Int) = this.copy(coastlinewidth = new_coastlinewidth)

  def setMapAxis(new_axis: LatAxis): MapOptions = this.copy(lataxis = new_axis)

  def setMapAxis(new_axis: LongAxis): MapOptions = this.copy(longaxis = new_axis)

  def setMapAxes(lat: LatAxis, lon: LongAxis): MapOptions = this.copy(lataxis = lat, longaxis = lon)

  private[picta] def serialize(): Value = {
    val meta = Obj(
      "resolution" -> resolution,
      "showland" -> showland,
      "showlakes" -> showlakes,
      "coastlinewidth" -> coastlinewidth
    )

    val scope_ = region.option match {
      case Some(x) => Obj("scope" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    val landcolor_ = landcolor.option match {
      case Some(x) => Obj("landcolor" -> x)
      case None => jsonMonoid.empty
    }

    val lakecolor_ = lakecolor.option match {
      case Some(x) => Obj("lakecolor" -> x)
      case None => jsonMonoid.empty
    }

    val projection_ = projection.option match {
      case Some(x) => Obj("projection" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val lataxis_ = lataxis.option match {
      case Some(x) => Obj("lataxis" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val longaxis_ = longaxis.option match {
      case Some(x) => Obj("lonaxis" -> x.serialize)
      case None => jsonMonoid.empty
    }

    List(meta, scope_, landcolor_, lakecolor_, projection_, lataxis_, longaxis_)
      .foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}