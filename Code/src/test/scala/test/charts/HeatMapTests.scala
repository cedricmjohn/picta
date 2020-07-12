package test.charts

import org.scalatest.funsuite.AnyFunSuite
import picta.charts.Chart
import picta.options.Layout
import picta.series.XYZ
import picta.series.XYZChart.HEATMAP
import test.UnitTestUtils.{config, validateJson, z_surface}

class HeatmapTests extends AnyFunSuite {

  val plotFlag = false

  test("XYZ.Heatmap") {
    val data = List(List(1, 2, 3), List(4, 5, 6))
    val series = XYZ(data, series_type = HEATMAP)
    val layout = Layout("XYZ.Heatmap")
    val chart = Chart(List(series), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XYZ.HeatmapWithXY") {
    val x: List[String] = List("a", "b", "c", "d", "e", "f")
    val y: List[Int] = List.range(1, 16).toList
    val data = XYZ(x, y, z_surface, HEATMAP)
    val layout = Layout("XYZ.Heatmap")
    val chart = Chart(List(data), layout, config)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
