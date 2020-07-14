package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.{Chart, Layout, XYZ}
import org.carbonateresearch.picta.XYZChart.HEATMAP
import org.carbonateresearch.picta.UnitTestUtils.{config, validateJson, z_surface}
import org.scalatest.funsuite.AnyFunSuite

class HeatmapTests extends AnyFunSuite {

  val plotFlag = false

  test("XYZ.Heatmap") {
    val data = List(List(1, 2, 3), List(4, 5, 6))
    val series = XYZ(data, series_type = HEATMAP)
    val layout = picta.Layout("XYZ.Heatmap")
    val chart = Chart(List(series), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.HeatmapWithXY") {
    val x: List[String] = List("a", "b", "c", "d", "e", "f")
    val y: List[Int] = List.range(1, 16).toList
    val data = XYZ(x, y, z_surface, HEATMAP)
    val layout = picta.Layout("XYZ.Heatmap")
    val chart = Chart(List(data), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
