package org.carbonateresearch.picta.render

import org.carbonateresearch.picta.UnitTestUtils.{validateJson, x_int, x_random}
import org.carbonateresearch.picta.options.histogram._
import org.carbonateresearch.picta.options.{Line, Marker}
import org.carbonateresearch.picta._
import org.scalatest.funsuite.AnyFunSuite

/**
 * This tests the Histogram component and it's various methods.
 */
class HistogramTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Histogram.Basic") {
    val series = XY(x = x_int) asType HISTOGRAM
    val chart = Chart() addSeries series setTitle "XY.Histogram.Basic"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Horizontal") {
    val series = XY(x = x_int) asType HISTOGRAM setHistOptions (orientation = HORIZONTAL)
    val layout = ChartLayout()
    val chart = Chart() addSeries series setTitle "XY.Histogram.Horizontal"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Color.String") {
    val marker = Marker() setColor "rgba(255, 100, 102, 0.4)" setLine Line()
    val series = XY(x_random) asType HISTOGRAM setMarker marker setHistOptions (orientation = HORIZONTAL)
    val chart = Chart() addSeries series setTitle "XY.Histogram.ColorString"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Color.RGB") {
    val marker = Marker() setColor RGB(255, 100, 102) setLine Line()
    val series = XY(x_random) asType HISTOGRAM setMarker marker setHistOptions (orientation = HORIZONTAL)
    val chart = Chart() addSeries series setTitle "XY.Histogram.Color"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.ColorRGBA") {
    val marker = Marker() setColor RGBA(255, 100, 102, 0.4) setLine Line()
    val series = XY(x_random) asType HISTOGRAM setMarker marker setHistOptions (orientation = HORIZONTAL)
    val chart = Chart() addSeries series setTitle "XY.Histogram.Color.RGBA"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }


  test("XY.Histogram.withLines") {
    val marker = Marker() setColor "rgba(255, 100, 102, 0.4)" setLine Line()
    val series = XY(x_random) asType HISTOGRAM setMarker marker
    val chart = Chart() addSeries series setTitle "XY.Histogram.Advanced" setConfig(false, false)
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Cumulative") {
    val series = XY(x_random) asType HISTOGRAM setHistOptions(histnorm = NUMBER, cumulative = Cumulative(true))
    val chart = Chart() addSeries series setTitle "XY.Histogram.Cumulative"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.WithOptions") {
    val series = (
      XY(x_random)
        asType HISTOGRAM
        setHistOptions(histnorm = NUMBER, xbins = Xbins(start = 0.5, end = 3.0, size = 0.05))
      )

    val chart = Chart() addSeries series setTitle "XY.Histogram.WithOptions"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.SpecifyBinningFunction") {
    val x = List("Apples", "Apples", "Apples", "Oranges", "Bananas")
    val y = List("5", "10", "3", "10", "5")
    val t1 = XY(x = x, y = y) asType HISTOGRAM setHistOptions (histfunc = COUNT)
    val t2 = XY(x = x, y = y) asType HISTOGRAM setHistOptions (histfunc = SUM)
    val chart = Chart() addSeries(t1, t2) setTitle "XY.Histogram.SpecifyBinningFunction"
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
