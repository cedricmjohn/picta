package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta._
import org.carbonateresearch.picta.UnitTestUtils._
import org.scalatest.funsuite.AnyFunSuite

class XYZTests extends AnyFunSuite {

  val plotFlag = false

  test("XYZ.Scatter3D") {
    val series = XYZ(x_double, y_double, z_double) as SCATTER3D draw MARKERS
    val chart = Chart() addSeries series setLayout Layout("XYZ.Scatter3D") setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.line3D") {
    val series = XYZ(x_double, y_double, z_double) as SCATTER3D draw LINES
    val chart = Chart() addSeries series setLayout Layout("XYZ.line3D") setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Surface") {
    val n = z_surface.length
    val series = XYZ(z=z_surface.flatten, n = z_surface(0).length) as SURFACE
    val chart = Chart() addSeries series setLayout Layout("XYZ.Surface") setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Heatmap") {
    val data = List(List(1, 2, 3), List(4, 5, 6))
    val n = data(0).length
    val series = XYZ(z=data.flatten, n=n) as HEATMAP
    val chart = Chart() addSeries List(series) setLayout Layout("XYZ.Heatmap") setConfig  config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.HeatmapWithXY") {
    val x: List[String] = List("a", "b", "c", "d", "e", "f")
    val y: List[Int] = List.range(1, 16)
    val z = z_surface.flatten
    val series = XYZ(x=x, y=y, z=z, n=z_surface(0).length) as HEATMAP
    val chart = Chart() addSeries List(series) setLayout Layout("XYZ.HeatmapWithXY")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Surface.Exception") {
    assertThrows[IllegalArgumentException] {
      XYZ(x_double, y_double, z_double) as SURFACE
    }
  }

  test("XYZ.Heatmap.Exception") {
    assertThrows[IllegalArgumentException] {
      XYZ(Nil, Nil, z_double) as HEATMAP
    }
  }

  test("XYZ.Contour.Exception") {
    assertThrows[IllegalArgumentException] {
      XYZ(Nil, Nil, z_double) as CONTOUR
    }
  }

  test("XYZ.AsScatter3d.Exceptions") {
    assertThrows[IllegalArgumentException] {
      XYZ(Nil, y_double, z_double) as SCATTER3D
    }

    assertThrows[IllegalArgumentException] {
      XYZ(x_double, Nil, z_double) as SCATTER3D
    }

    assertThrows[IllegalArgumentException] {
      XYZ(x_double, y_double, Nil) as SCATTER3D
    }
  }

  test("XYZ.HEATMAP.InvalidDimensions") {
    assertThrows[IllegalArgumentException] {
      val x: List[String] = List("a", "b", "c", "d", "e", "f")
      val y: List[Int] = List.range(1, 16)
      val z = z_surface.flatten
      XYZ(x=y, y=x, z=z, n=z_surface(0).length) as HEATMAP
    }
  }
}
