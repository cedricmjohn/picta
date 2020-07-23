package org.carbonateresearch.picta.render

import org.carbonateresearch.picta.UnitTestUtils.validateJson
import org.carbonateresearch.picta.options._
import org.carbonateresearch.picta.{Chart, LINES, LatAxis, LongAxis, Map, MapOptions}
import org.scalatest.funsuite.AnyFunSuite

class MapTests extends AnyFunSuite {

  val plotFlag = false

  test("Map.Basic") {
    val lat = List(40.7127, 51.5072)
    val lon = List(-74.0059, 0.1275)
    val series = Map(lat, lon)
    val chart = Chart() addSeries series
    if (plotFlag) chart.plot()
    assert(validateJson(chart.serialize.toString))
  }

  test("Map.Geo") {
    val color = List("red")
    val line = Line(width = 2) setColor color
    val data = Map(List(40.7127, 51.5072), List(-74.0059, 0.1275)) drawSymbol LINES drawLine line

    val map_options = MapOptions(landcolor = "rgb(204, 204, 204)", lakecolor = "rgb(255, 255, 255)")
              .setMapAxes(LatAxis(List(20, 60)), LongAxis(List(-100, 20)))

    val chart = (
      Chart()
        addSeries data
        setDimensions(height = 800, width = 800)
        setMapOptions map_options
        setMargin (0, 0, 0, 0)
        setConfig(false, false)
      )

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
