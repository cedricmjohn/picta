package org.carbonateresearch.picta

import org.carbonateresearch.picta.ColorOptions.Color
import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid._
import ujson.{Obj, Value}

/** Sets the region for a map chart. */
sealed trait Region

case object WORLD extends Region

case object USA extends Region

case object EUROPE extends Region

case object ASIA extends Region

case object AFRICA extends Region

case object NORTH_AMERICA extends Region

case object SOUTH_AMERICA extends Region

/** Sets how the projection is calculated on a map. */
sealed trait Projection

case object EQURECTANGULAR extends Projection

case object MERCATOR extends Projection

case object ORTHOGRAPHIC extends Projection

case object NATURAL_EARTH extends Projection {
  override def toString: String = "natural earth"
}

case object KAVRAYSKIY7 extends Projection

case object MILLER extends Projection

case object ROBINSON extends Projection

case object ECKERT4 extends Projection

case object AZIMUTHAL_EQUAL_AREA extends Projection {
  override def toString: String = "azimuthal equal area"
}

case object AZIMUTHAL_EQUIDISTANT extends Projection {
  override def toString: String = "azimuthal equidistant"
}

case object CONIC_EQUAL_AREA extends Projection {
  override def toString: String = "conic equal area"
}

case object CONIC_CONFORMAL extends Projection {
  override def toString: String = "conic conformal"
}

case object CONIC_EQUAL_EQUIDISTANT extends Projection {
  override def toString: String = "conic equal equidistant"
}

case object GNOMONIC extends Projection

case object STEROGRAPHIC extends Projection

case object MOLLWEIDE extends Projection

case object HAMMER extends Projection

case object TRANSVERSE_MERCATOR extends Projection {
  override def toString: String = "transverse mercator"
}

case object ALBERS_USA extends Projection {
  override def toString: String = "albers usa"
}

case object WINKEL_TRIPEL extends Projection {
  override def toString: String = "winkel tripel"
}

case object AITOFF extends Projection

case object SINUSOIDAL extends Projection

trait MapOption extends Component

/** This is configures the Chart for a Map.
 *
 * @param resolution     : This sets the resolution.
 * @param region         : This determines the geographic scope for the map.
 * @param showland       : Specifies whether the land is shown on the map.
 * @param showlakes      : Specifies whether lakes are shown on the map.
 * @param landcolor      : Specifcies the landcolor.
 * @param lakecolor      : Specifies the lakecolor.
 * @param projection     : TODO
 * @param coastlinewidth : Specifies the coast line width on the map.
 * @param lataxis        : This is the component that configures the lataxis.
 * @param longaxis       : This is the component that configures the longaxis.
 */
final case class MapOptions[T0: Color, T1: Color]
(region: Opt[Region] = Blank, landcolor: Opt[T0] = Blank, lakecolor: Opt[T1] = Blank,
 projection: Opt[Projection] = Blank, lataxis: Opt[LatAxis] = Blank, longaxis: Opt[LongAxis] = Blank,
 showland: Boolean = true, showlakes: Boolean = true, resolution: Int = 50, coastlinewidth: Int = 2) extends MapOption {

  private val c0 = implicitly[Color[T0]]
  private val c1 = implicitly[Color[T1]]

  def setRegion(new_region: Region) = this.copy(region = new_region)

  def setLandColor(new_landcolor: String) = this.copy(landcolor = new_landcolor)

  def setLakeColor(new_lakecolor: String) = this.copy(lakecolor = new_lakecolor)

  def setProject(new_projection: Projection) = this.copy(projection = new_projection)

  def setShowLand(new_showland: Boolean) = this.copy(showland = new_showland)

  def setShowLakes(new_showlakes: Boolean) = this.copy(showlakes = new_showlakes)

  def setResolution(new_resolution: Int) = this.copy(resolution = new_resolution)

  def setCoastLineWidth(new_coastlinewidth: Int) = this.copy(coastlinewidth = new_coastlinewidth)

  def setMapAxis(new_axis: LatAxis) = this.copy(lataxis = new_axis)

  def setMapAxis(new_axis: LongAxis) = this.copy(longaxis = new_axis)

  def setMapAxes(lat: LatAxis, lon: LongAxis) = this.copy(lataxis = lat, longaxis = lon)

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
      case Some(x) => Obj("landcolor" -> c0.serialize(List(x)))
      case None => jsonMonoid.empty
    }

    val lakecolor_ = lakecolor.option match {
      case Some(x) => Obj("lakecolor" -> c1.serialize(List(x)))
      case None => jsonMonoid.empty
    }

    val projection_ = projection.option match {
      case Some(x) => Obj("projection" -> x.toString.toLowerCase)
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