package conusviz.options

import conusviz.common.Component
import conusviz.options.ProjectionOptions.Projection
import ujson.{Obj, Value}
import conusviz.Utils._
import conusviz.options.MapAxisOptions.{LatAxis, LongAxis}

trait GeoOptions extends Component

object GeoOptions {
  case class Geo(resolution: Int = 50, scope: Option[String] = None, showland: Boolean = true, showlakes: Boolean = true,
                 landcolor: Option[String] = None, lakecolor: Option[String] = None,
                 projection: Option[Projection] = None, coastlinewidth: Int = 2, lataxis: Option[LatAxis] = None,
                 longaxis: Option[LongAxis] = None) extends GeoOptions {

    def +(l: LatAxis):Geo = this.copy(lataxis = Some(l))
    def +(l: LongAxis):Geo = this.copy(longaxis = Some(l))

    def serialize(): Value = {
      val acc = Obj(
        "resolution" -> resolution,
        "showland" -> showland,
        "showlakes" -> showlakes,
        "coastlinewidth" -> coastlinewidth
      )

      val s = scope match {
        case Some(s) => Obj("scope" -> s)
        case None => emptyObject
      }

      val landcol = landcolor match {
        case Some(l) => Obj("landcolor" -> l)
        case None => emptyObject
      }

      val lakecol = lakecolor match {
        case Some(l) => Obj("lakecolor" -> l)
        case None => emptyObject
      }

      val proj = projection match {
        case Some(p) => Obj("projection" -> p.serialize)
        case None => emptyObject
      }

      val latax = lataxis match {
        case Some(m) => Obj("lataxis" -> m.serialize)
        case None => emptyObject
      }

      val lonax = longaxis match {
        case Some(m) => Obj("lonaxis" -> m.serialize)
        case None => emptyObject
      }

      acc.obj ++ s.obj ++ landcol.obj ++ lakecol.obj ++ proj.obj ++ latax.obj ++ lonax.obj
    }
  }
}
