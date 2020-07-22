package org.carbonateresearch.picta

import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta.options.SymbolShape.CIRCLE
import org.carbonateresearch.picta.options.{Line, Marker}
import org.scalatest.funsuite.AnyFunSuite
import upickle.default.write

class LineTests extends AnyFunSuite {

  val plotFlag = false

  test("Line.Constructor.Basic") {
    val line = Line() setColor "rgb(255, 255, 255, 1)"
    val test = """{"width":0.5,"color":"rgb(255, 255, 255, 1)"}"""
    assert(test == write(line.serialize))
  }

  test("Line.Constructor.Advanced") {
    val line = Line() setWidth 0.1 setColor("rgb(255, 255, 255, 1)", "rgb(255, 255, 255, 1)")
    val test = """{"width":0.1,"color":["rgb(255, 255, 255, 1)","rgb(255, 255, 255, 1)"]}"""
    assert(test == write(line.serialize))
  }

  test("Line.withMarker") {
    val marker = Marker() setSymbol CIRCLE setColor "rgb(17, 157, 255)" setLine Line(width = 2)
    val data = XY(x_int, y_int) setName  "test" asType SCATTER drawStyle MARKERS setMarker marker
    val chart = Chart() addSeries data setTitle "Line.withMarker" setConfig(false, false)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}