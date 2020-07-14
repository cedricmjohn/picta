package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.{Chart, Layout, XYZ}
import org.carbonateresearch.picta.series.Mode.LINES
import org.carbonateresearch.picta.XYZChart.{SCATTER3D, SURFACE}
import org.carbonateresearch.picta.UnitTestUtils.{config, validateJson, x_double, y_double, z_double, z_surface}
import org.scalatest.funsuite.AnyFunSuite

class XYZTests extends AnyFunSuite {

  val plotFlag = false

  test("XYZ.Scatter3D") {
    val data = XYZ(x_double, y_double, z_double, series_type = SCATTER3D)
    val layout = picta.Layout("XYZ.Scatter3D")
    val chart = Chart(List(data), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.line3D") {
    val data = XYZ(x_double, y_double, z_double, series_type = SCATTER3D, series_mode = LINES)
    val layout = picta.Layout("XYZ.line3D")
    val chart = Chart(List(data), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.Surface") {
    val data = XYZ(z_surface, series_type = SURFACE)
    val layout = picta.Layout("XYZ.Surface")
    val chart = Chart(List(data), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
