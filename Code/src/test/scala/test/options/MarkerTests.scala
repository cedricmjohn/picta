package org.carbonateresearch.picta

import org.carbonateresearch.picta.UnitTestUtils.{validateJson, x_int, y_int}
import org.carbonateresearch.picta.common.Monoid._
import org.carbonateresearch.picta.options.{Axis, Line, Marker, XAxis, YAxis}
import org.scalatest.funsuite.AnyFunSuite
import upickle.default.write

class MarkerTests extends AnyFunSuite {

  val plotFlag = false

  test("Marker.Constructor.Default") {
    val marker = Marker()
    assert(jsonMonoid.empty.toString == write(marker.serialize))
  }

  test("Marker.Constructor.Full") {
    val marker = Marker() setSymbol "circle" setColor "red" setLine Line() setSize(10)
    val test = """{"symbol":"circle","color":"red","line":{"width":0.5},"size":[10]}"""
    assert(test == write(marker.serialize))
  }

  test("Marker.Composition.WithTrace") {
    val marker = Marker() setSymbol "circle" setColor "red" setLine Line()
    val x_axis = XAxis() setTitle "x variable"
    val y_axis = YAxis() setTitle "y variable"

    val series = XY(x_int, y_int, `type` = SCATTER, symbol = MARKERS) setMarker marker
    val layout = Layout("Marker.Composition.WithTrace") setAxes(x_axis, y_axis)
    val chart = Chart() addSeries series setLayout layout
    val canvas = Canvas()
    canvas.subplot(0, 0) = chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }
}
