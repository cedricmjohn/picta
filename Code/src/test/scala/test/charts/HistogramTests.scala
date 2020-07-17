package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils.{config, validateJson, x_int, x_random}
import org.carbonateresearch.picta.options.histogram._
import org.carbonateresearch.picta.options.{Line, Marker}
import org.carbonateresearch.picta.{Canvas, Chart, HISTOGRAM, Layout, XY}
import org.scalatest.funsuite.AnyFunSuite

class HistogramTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Histogram.Basic") {
    val series = XY(x = x_int) asType HISTOGRAM
    val layout = Layout("XY.Histogram.Basic")
    val chart = Chart() addSeries series setLayout layout
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Horizontal") {
    val series = XY(x = x_int) asType HISTOGRAM setHistOptions HistOptions(orientation = HORIZONTAL)
    val layout = Layout("XY.Histogram.Horizontal")
    val chart = Chart() addSeries series setLayout layout
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Color") {
    val marker = Marker() setColor List("rgba(255, 100, 102, 0.4)") setLine Line()
    val series = XY(x_random) asType HISTOGRAM setMarker marker setHistOptions HistOptions(orientation = HORIZONTAL)
    val chart = Chart() addSeries series setLayout picta.Layout("XY.Histogram.Color")
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.withLines") {
    val marker = Marker() setColor "rgba(255, 100, 102, 0.4)" setLine Line()
    val series = XY(x_random) asType HISTOGRAM setMarker marker
    val layout = Layout("XY.Histogram.Advanced")
    val chart = Chart() addSeries series setLayout layout setConfig config
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.Cumulative") {
    val hist_options = HistOptions(histnorm = NUMBER) setCumulative Cumulative(enabled = true)
    val series = XY(x_random) asType HISTOGRAM setHistOptions hist_options
    val chart = Chart() addSeries series setLayout Layout("XY.Histogram.Cumulative")
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.WithOptions") {
    val hist_options = HistOptions(histnorm = NUMBER) setXbins Xbins(start = 0.5, end = 3.0, size = 0.05)
    val series = XY(x_random) asType HISTOGRAM setHistOptions hist_options
    val chart = Chart() addSeries series setLayout Layout("XY.Histogram.WithOptions")
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram.SpecifyBinningFunction") {
    val x = List("Apples", "Apples", "Apples", "Oranges", "Bananas")
    val y = List("5", "10", "3", "10", "5")
    val ho1 = HistOptions(histfunc = COUNT)
    val ho2 = HistOptions(histfunc = SUM)
    val t1 = XY(x = x, y = y) asType HISTOGRAM setHistOptions ho1
    val t2 = XY(x = x, y = y) asType HISTOGRAM setHistOptions ho2
    val chart = Chart() addSeries(t1, t2) setLayout Layout("XY.Histogram.SpecifyBinningFunction")
    val canvas = Canvas() addCharts chart
    if (plotFlag) canvas.plot
    assert(validateJson(chart.serialize.toString))
  }
}
