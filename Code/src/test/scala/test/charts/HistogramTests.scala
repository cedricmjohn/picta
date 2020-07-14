package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.{Chart, Layout, XY}
import org.carbonateresearch.picta.options.{Line, Marker}
import org.carbonateresearch.picta.options.histogram.HistFunction.{COUNT, SUM}
import org.carbonateresearch.picta.options.histogram.HistNormType.NUMBER
import org.carbonateresearch.picta.options.histogram.HistOrientation.HORIZONTAL
import org.carbonateresearch.picta.options.histogram.{Cumulative, HistOptions, Xbins}
import org.carbonateresearch.picta.XYChart.HISTOGRAM
import org.carbonateresearch.picta.UnitTestUtils.{config, validateJson, x_int, x_random}
import org.scalatest.funsuite.AnyFunSuite

class HistogramTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Histogram.Basic") {
    val series = XY(x = x_int, series_type = HISTOGRAM)
    val layout = Layout("XY.Histogram.Basic")
    val chart = Chart() setData series setLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Horizontal") {
    // change xkey to y to get a horizontal histogram
    val series = XY(x = x_int, series_type = HISTOGRAM) setHistOptions HistOptions(orientation = HORIZONTAL)
    val layout = picta.Layout("XY.Histogram.Horizontal")
    val chart = Chart() setData series setLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Color") {
    val marker = Marker() setColor List("rgba(255, 100, 102, 0.4)") setLine Line()
    // change xkey to y to get a horizontal histogram
    val series = XY(x_random, series_type = HISTOGRAM, marker = marker) setHistOptions HistOptions(orientation = HORIZONTAL)
    val chart = Chart() setData series setLayout picta.Layout("XY.Histogram.Color")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.withLines") {
    val marker = Marker() setColor List("rgba(255, 100, 102, 0.4)") setLine Line()
    val series = XY(x_random, series_type = HISTOGRAM) setMarker marker
    val layout = picta.Layout("XY.Histogram.Advanced")
    val chart = Chart() setData series setLayout layout setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Cumulative") {
    val hist_options = HistOptions(histnorm = NUMBER) setCumulative Cumulative(enabled = true)
    val series = XY(x_random, series_type = HISTOGRAM) setHistOptions hist_options
    val chart = Chart() setData series setLayout picta.Layout("XY.Histogram.Cumulative")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.WithOptions") {
    val hist_options = HistOptions(histnorm = NUMBER) setXbins Xbins(start = 0.5, end = 3.0, size = 0.05)
    val series = XY(x_random, series_type = HISTOGRAM) setHistOptions hist_options
    val chart = Chart() setData series setLayout picta.Layout("XY.Histogram.WithOptions")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.SpecifyBinningFunction") {
    val x = List("Apples", "Apples", "Apples", "Oranges", "Bananas")
    val y = List("5", "10", "3", "10", "5")
    val ho1 = HistOptions(histfunc = COUNT)
    val ho2 = HistOptions(histfunc = SUM)
    val t1 = XY(x = x, y = y, series_type = HISTOGRAM) setHistOptions ho1
    val t2 = XY(x = x, y = y, series_type = HISTOGRAM) setHistOptions ho2
    val chart = Chart() setData(t1, t2) setLayout picta.Layout("XY.Histogram.SpecifyBinningFunction")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
