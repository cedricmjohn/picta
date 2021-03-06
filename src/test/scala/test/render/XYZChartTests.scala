/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta.render

import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta._
import org.scalatest.funsuite.AnyFunSuite

/**
 * This tests XYZ Chart and it's various methods.
 */
class XYZTests extends AnyFunSuite {

  val plotFlag = false

  test("XYZ.Scatter3D") {
    val series = XYZ(x_double, y_double, z_double) asType SCATTER3D drawStyle MARKERS
    val chart = Chart() addSeries series setTitle "XYZ.Scatter3D" setConfig(false, false)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.line3D") {
    val series = XYZ(x_double, y_double, z_double) asType SCATTER3D drawStyle LINES
    val chart = Chart() addSeries series setTitle "XYZ.line3D" setConfig(false, false)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Surface") {
    val series = XYZ(z = z_surface.flatten, n = z_surface(0).length) asType SURFACE
    val chart = Chart() addSeries series setTitle "XYZ.Surface" setConfig(false, false)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Heatmap") {
    val data = List(List(1, 2, 3), List(4, 5, 6))
    val n = data(0).length
    val series = XYZ(z = data.flatten, n = n) asType HEATMAP
    val chart = Chart() addSeries series setTitle "XYZ.Heatmap" setConfig(false, false)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.HeatmapWithXY") {
    val x: List[String] = List("a", "b", "c", "d", "e", "f")
    val y: List[Int] = List.range(1, 16)
    val z = z_surface.flatten
    val series = XYZ(x = x, y = y, z = z, n = z_surface(0).length) asType HEATMAP
    val chart = Chart() addSeries series setTitle "XYZ.HeatmapWithXY"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Surface.Exception") {
    assertThrows[IllegalArgumentException] {
      XYZ(x_double, y_double, z_double) asType SURFACE
    }
  }

  test("XYZ.Heatmap.Exception") {
    assertThrows[IllegalArgumentException] {
      XYZ(Nil, Nil, z_double) asType HEATMAP
    }
  }

  test("XYZ.Contour.Exception") {
    assertThrows[IllegalArgumentException] {
      XYZ(Nil, Nil, z_double) asType CONTOUR
    }
  }

  test("XYZ.AsScatter3d.Exceptions") {
    assertThrows[IllegalArgumentException] {
      XYZ(Nil, y_double, z_double) asType SCATTER3D
    }

    assertThrows[IllegalArgumentException] {
      XYZ(x_double, Nil, z_double) asType SCATTER3D
    }

    assertThrows[IllegalArgumentException] {
      XYZ(x_double, y_double, Nil) asType SCATTER3D
    }
  }

  test("XYZ.HEATMAP.InvalidDimensions") {
    assertThrows[IllegalArgumentException] {
      val x: List[String] = List("a", "b", "c", "d", "e", "f")
      val y: List[Int] = List.range(1, 16)
      val z = z_surface.flatten
      XYZ(x = y, y = x, z = z, n = z_surface(0).length) asType HEATMAP
    }
  }

  test("XYZ.Heatmap.SetColorBar") {
    val data = List(List(1, 2, 3), List(4, 5, 6))
    val n = data(0).length
    val series = XYZ(z = data.flatten, n = n) asType HEATMAP
    val chart = (
      Chart()
        addSeries (series setColorBar "my colorbar")
        setTitle "XYZ.Heatmap.SetColorBar"
      )

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }


}
