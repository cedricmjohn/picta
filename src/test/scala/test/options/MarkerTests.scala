package org.carbonateresearch.picta

import org.carbonateresearch.picta.UnitTestUtils.{validateJson, x_int, y_int}
import org.carbonateresearch.picta.common.Monoid._
import SymbolShape.CIRCLE
import org.carbonateresearch.picta.options.{Line, Marker}
import org.scalatest.funsuite.AnyFunSuite
import upickle.default.write

class MarkerTests extends AnyFunSuite {

  val plotFlag = false

  test("Marker.Constructor.Default") {
    val marker = Marker()
    assert(jsonMonoid.empty.toString == write(marker.serialize))
  }

  test("Marker.Constructor.Full") {
    val marker = Marker() setSymbol CIRCLE setColor "red" setLine Line() setSize(10)
    val test = """{"symbol":"circle","color":"red","line":{"width":0.5},"size":[10]}"""
    assert(test == write(marker.serialize))
  }

  test("Marker.Composition.WithTrace") {
    val marker = Marker() setSymbol CIRCLE setColor "red" setLine Line()
    val x_axis = XAxis() setTitle "x variable"
    val y_axis = YAxis() setTitle "y variable"

    val series = XY(x_int, y_int, `type` = SCATTER, style = MARKERS) setMarker marker
    val chart = Chart() addSeries series setTitle "Marker.Composition.WithTrace"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("Marker.SetAppearance") {
    // additional traces can simply be composed with an existing chart and added on
    val series1 = XY(x_int, x_int) asType SCATTER setName "Scatter" drawStyle MARKERS

    // lets give the second series a red marker. Again we can 'compose' a marker using smaller components
    val marker = (
      Marker()
        setSymbol CIRCLE
        setColor "red"
        setLine(width = 2, "black")
    )

    // we not put brackets in the 'addSeries' function to ensure that addSeries picks up the right series'
    val chart = Chart() addSeries (series1 setMarker marker)

    if (plotFlag) chart.plot

    assert(validateJson(chart.serialize.toString))
  }


}
