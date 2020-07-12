package picta

import org.scalatest.funsuite.AnyFunSuite
import picta.charts.Chart
import picta.options.{Geo, LatAxis, Layout, Line, LongAxis, Margin}
import picta.series.Map
import picta.series.Mode.LINES
import test.UnitTestUtils.{validateJson, config}

class MapTests extends AnyFunSuite {

  val plotFlag = false

  test("Map.Basic") {
    val lat = List(40.7127, 51.5072)
    val lon = List(-74.0059, 0.1275)
    val data = Map(lat, lon)
    val chart = Chart() setData data
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString))
  }

  test("Map.Geo") {
    val color = List("red")
    val line = Line(width = 2) setColor color
    val data = Map(List(40.7127, 51.5072), List(-74.0059, 0.1275), series_mode = LINES) setLine line

    val geo = Geo(landcolor = "rgb(204, 204, 204)", lakecolor = "rgb(255, 255, 255)") setAxes
      (LatAxis(List(20, 60)), LongAxis(List(-100, 20)))

    val layout = Layout(margin = Margin(0, 0, 0, 0), height = 800, width = 800) setGeo geo
    val chart = Chart() setData data setLayout layout setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
