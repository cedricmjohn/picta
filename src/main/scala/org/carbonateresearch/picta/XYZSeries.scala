package org.carbonateresearch.picta

import org.carbonateresearch.picta.OptionWrapper._
import org.carbonateresearch.picta.common.Monoid.jsonMonoid
import org.carbonateresearch.picta.common.Serializer
import org.carbonateresearch.picta.common.Utils._
import org.carbonateresearch.picta.options.ColorBar
import ujson.{Obj, Value}

import scala.language.postfixOps

/** ENUM for the XYZ chart series types */
sealed trait XYZType

case object SCATTER3D extends XYZType

case object CONTOUR extends XYZType

case object HEATMAP extends XYZType

case object SURFACE extends XYZType

/**
 * * A Series for a 3d chart.
 *
 * @param x: The data series for the first dimension.
 * @param y: The data series for the second dimension.
 * @param z: The data series for the third dimension.
 * @param name: The name of the data series.
 * @param `type`: The type of the data series.
 * @param style: Specifies the style of chart.
 * @param n: Only used when working with a nested list that has been flattened. Specifies the number of elements in the
 *           nested list before it was flattened.
 * @param colorbar_options: Specifies the color bar options for the chart.
 * @tparam T0
 * @tparam T1
 * @tparam T2
 */
final case class XYZ[T0: Serializer, T1: Serializer, T2: Serializer]
(x: Opt[List[T0]] = Empty, y: Opt[List[T1]] = Empty, z: List[T2], name: String = generateRandomText, `type`: XYZType = SCATTER3D,
 style: Opt[Style] = Blank, n: Opt[Int] = Blank, colorbar_options: Opt[ColorBar] = Blank) extends Series {

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

  val classification: String = "xyz"

  def setName(new_name: String): XYZ[T0, T1, T2] = this.copy(name = new_name)

  def asType(new_type: XYZType): XYZ[T0, T1, T2] = {
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

  def drawStyle(mode: Style): XYZ[T0, T1, T2] = this.copy(style = mode)

  def drawLines: XYZ[T0, T1, T2] = this.copy(style = LINES)

  def drawMarkers: XYZ[T0, T1, T2] = this.copy(style = MARKERS)

  def drawTEXT: XYZ[T0, T1, T2] = this.copy(style = LINES)

  def drawLinesMarkers: XYZ[T0, T1, T2] = this.copy(style = LINES_MARKERS)

  def setColorBar(title: String = "", title_side: Side = RIGHT_SIDE) = {
    val title_side_ = title_side.option match {
      case Some(x) => x
      case _ => RIGHT_SIDE
    }

    val new_colorbar_options = ColorBar(title = title, title_side = title_side_)
    this.copy(colorbar_options = new_colorbar_options)
  }

  private[picta] def serialize(): Value = {
    val name_ = Obj("name" -> name, "type" -> `type`.toString.toLowerCase())

    val colorbar_options_ = colorbar_options.option match {
      case Some(x) if `type` == HEATMAP || `type` == SURFACE => Obj("colorbar" -> x.serialize)
      case None => jsonMonoid.empty
    }

    val mode_ = style.option match {
      case Some(x) => Obj("mode" -> x.toString.toLowerCase)
      case None => jsonMonoid.empty
    }

    List(name_, colorbar_options_, mode_, createSeries).foldLeft(jsonMonoid.empty)((a, x) => a |+| x)
  }

  private def createSeries(): Value =
    (x.getOrElse(Nil), y.getOrElse(Nil), z, n.getOrElse(0)) match {
      case (x, y, z, 0) => createXYZ(x, y, z)
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
}