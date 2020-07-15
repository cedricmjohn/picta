package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import org.carbonateresearch.picta.common.Serializer
import org.carbonateresearch.picta.common.Utils._
import ujson.{Obj, Value}

trait XYZSeries extends Series

/** ENUM for the XYZ chart series types */
private[picta] sealed trait XYZType

case object SCATTER3D extends XYZType

case object CONTOUR extends XYZType

case object HEATMAP extends XYZType

case object SURFACE extends XYZType

/**
 * @constructor: A Series for a 3d chart.
 * @param x           :
 * @param y           :
 * @param z           :
 * @param name :
 * @param mode :
 */
final case class XYZ[T0: Serializer, T1: Serializer, T2: Serializer]
(x: Opt[List[T0]] = Empty, y: Opt[List[T1]] = Empty, z: List[T2], name: String = genRandomText, `type`: XYZType = SCATTER3D,
 mode: Opt[Mode] = Blank, n: Opt[Int] = Blank) extends XYZSeries {

  /* Error handling is done at the topmost level so that exceptions are thrown as soon as possible */
  (x.getOrElse(Nil), y.getOrElse(Nil), z, n.getOrElse(0), `type`) match {
      /* a surface chart must always specify n */
    case (_, _, _, 0, SURFACE) =>
      throw new IllegalArgumentException("'n' cannot be equal to zero if 'type' is 'SURFACE' or 'HEATMAP'")

    /* if we just have z for a heatmap, we cannot have it equal to 0 */
    case (Nil, Nil, _, 0, HEATMAP | CONTOUR) =>
      throw new IllegalArgumentException("'n' cannot be equal to zero if 'type' is 'HEATMAP' or 'CONTOUR' and 'x' and 'y' are unspecified")

    /* if we are passing in a single series, make sure dimensions of z and n are consistent*/
    case (Nil, Nil, z, n, HEATMAP | SURFACE | CONTOUR) =>
      if (z.length % n != 0) throw new IllegalArgumentException("The length of 'z' must be divisible by 'n'")

    /* we are passing in x, y as well as z, where z is a nested series. Make sure the dimensions are consistent */
    case (x, y, z, n, HEATMAP | SURFACE | CONTOUR) =>
      if (n == 0) throw new IllegalArgumentException("'n' cannot be equal to zero")

      if (x.length != n)
        throw new IllegalArgumentException("The length of a single list inside 'z' must be equal to the length of 'x'")

      if (z.length / n != y.length)
        throw new IllegalArgumentException("The total number of lists inside 'z' must be equal to the length of 'y'")

    /* accept all other cases */
    case (_, _, _, _, _) => ()
  }

  def as(new_type: XYZType): XYZ[T0, T1, T2] = {
    new_type match {
      case CONTOUR => this.asContour
      case HEATMAP => this.asHeatmap
      case SCATTER3D => this.asScatter3d
      case SURFACE => this.asSurface
    }
  }

  def asScatter3d: XYZ[T0, T1, T2] = (x.getOrElse(Nil), y.getOrElse(Nil), z) match {
    case (Nil, _, _) | (_, Nil, _) | (_, _, Nil) => throw new IllegalArgumentException("All three series must be specified")
    case _ => this.copy(`type` = `type`)
  }

  def asContour: XYZ[T0, T1, T2] = this.copy(`type` = CONTOUR)

  def asHeatmap: XYZ[T0, T1, T2] = this.copy(`type` = HEATMAP)

  def asSurface: XYZ[T0, T1, T2] = this.copy(`type` = SURFACE)

  def draw(mode: Mode): XYZ[T0, T1, T2] = this.copy(mode = mode)

  def drawLines: XYZ[T0, T1, T2] = this.copy(mode = LINES)

  def drawMarkers: XYZ[T0, T1, T2] = this.copy(mode = MARKERS)

  def drawTEXT: XYZ[T0, T1, T2] = this.copy(mode = LINES)

  def drawLinesMarkers: XYZ[T0, T1, T2] = this.copy(mode = LINES_MARKERS)

  private def createSeries(): Value =
    (x.getOrElse(Nil), y.getOrElse(Nil), z, n.getOrElse(0)) match {
      case (x, y, z, 0) =>  createXYZ(x, y, z)
      case (Nil, Nil, z, n) => createXYZ(z, n)
      case (x, y, z, n) if (n != 0) => createXYZ(x, y, z, n)
      case _ => throw new IllegalArgumentException("'n' cannot be equal to zero")
  }

  /** list has a nested form, no other axis specified */
  private def createXYZ[T: Serializer](z: List[T], n: Int)(implicit s0: Serializer[T]): Value = {
    val list = z.grouped(n).toList.map(e => s0.serialize(e))
    Obj("z" -> list)
  }

  /** x, y and z are specified */
  private def createXYZ[T0: Serializer, T1: Serializer, T2: Serializer]
  (x: List[T0], y: List[T1], z: List[T2])(implicit s0: Serializer[T0], s1: Serializer[T1], s2: Serializer[T2]): Value = {
    Obj("x" -> s0.serialize(x), "y" -> s1.serialize(y), "z" -> s2.serialize(z))
  }

  private def createXYZ[T0: Serializer, T1: Serializer, T2: Serializer]
  (x: List[T0], y: List[T1], z: List[T2], n: Int)(implicit s0: Serializer[T0], s1: Serializer[T1], s2: Serializer[T2]): Value = {
    val list: List[Value] = z.grouped(n).toList.map(e => s2.serialize(e))
    Obj("x" -> s0.serialize(x), "y" -> s1.serialize(y), "z" -> list)
  }

  private[picta] def serialize(): Value = {
    val name_ = Obj("name" -> name, "type" -> `type`.toString.toLowerCase())

    val mode_ = mode.option match {
      case Some(x) => Obj("mode" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    List(name_, mode_, createSeries).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }
}