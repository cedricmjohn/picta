package org.carbonateresearch.picta

import org.scalatest.funsuite.AnyFunSuite
import org.carbonateresearch.picta.common.Monoid._
import org.carbonateresearch.picta.options.{Axis, Line, Marker}
import org.carbonateresearch.picta.series.Mode.MARKERS
import XYChart.SCATTER
import org.carbonateresearch.picta.UnitTestUtils.{validateJson, x_int, y_int}
import upickle.default.write

class MarkerTests extends AnyFunSuite {

  val plotFlag = false

  test("Marker.Constructor.Default") {
    val marker = Marker()
    assert(jsonMonoid.empty.toString == write(marker.serialize))
  }

  test("Marker.Constructor.Full") {
    val marker = Marker() setSymbol "circle" setColor "red" setLine Line()
    val test = """{"symbol":"circle","color":"red","line":{"width":0.5}}"""
    assert(test == write(marker.serialize))
  }

  test("Marker.Composition.WithTrace") {
    val marker = Marker() setSymbol "circle" setColor "red" setLine Line()
    val x_axis = Axis(position = "x") setTitle "x variable"
    val y_axis = Axis(position = "y") setTitle "y variable"

    val series = XY(x_int, y_int, series_type = SCATTER, series_mode = MARKERS) setMarker marker
    val layout = Layout("Marker.Composition.WithTrace") setAxes(x_axis, y_axis)
    val chart = Chart() setData series setLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
