package picta

import org.scalatest.funsuite.AnyFunSuite
import picta.charts.Chart
import picta.options.{Layout, Line, Marker}
import picta.options.histogram.HistFunction.{COUNT, SUM}
import picta.options.histogram.HistNormType.NUMBER
import picta.options.histogram.HistOrientation.HORIZONTAL
import picta.options.histogram.{Cumulative, HistOptions, Xbins}
import picta.series.XY
import picta.series.XYChartType.HISTOGRAM
import test.UnitTestUtils.{validateJson, x_int, x_random, config}

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
    val layout = Layout("XY.Histogram.Horizontal")
    val chart = Chart() setData series setLayout layout
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Color") {
    val marker = Marker() setColors List("rgba(255, 100, 102, 0.4)") setLine Line()
    // change xkey to y to get a horizontal histogram
    val series = XY(x_random, series_type = HISTOGRAM, marker = marker) setHistOptions HistOptions(orientation = HORIZONTAL)
    val chart = Chart() setData series setLayout Layout("XY.Histogram.Color")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.withLines") {
    val marker = Marker() setColors List("rgba(255, 100, 102, 0.4)") setLine Line()
    val series = XY(x_random, series_type = HISTOGRAM) setMarker marker
    val layout = Layout("XY.Histogram.Advanced")
    val chart = Chart() setData series setLayout layout setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Cumulative") {
    val hist_options = HistOptions(histnorm = NUMBER) setCumulative Cumulative(enabled = true)
    val series = XY(x_random, series_type = HISTOGRAM) setHistOptions hist_options
    val chart = Chart() setData series setLayout Layout("XY.Histogram.Cumulative")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.WithOptions") {
    val hist_options = HistOptions(histnorm = NUMBER) setXbins Xbins(start = 0.5, end = 3.0, size = 0.05)
    val series = XY(x_random, series_type = HISTOGRAM) setHistOptions hist_options
    val chart = Chart() setData series setLayout Layout("XY.Histogram.WithOptions")
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
    val chart = Chart() setData(t1, t2) setLayout Layout("XY.Histogram.SpecifyBinningFunction")
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
