package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.{Chart, Layout, XY}
import org.carbonateresearch.picta.options.{Axis, Marker}
import org.carbonateresearch.picta.options.histogram.HistOptions
import org.carbonateresearch.picta.options.histogram2d.Hist2dOptions
import org.carbonateresearch.picta.options.histogram.HistOrientation.HORIZONTAL
import org.carbonateresearch.picta.series.Mode.MARKERS
import org.carbonateresearch.picta.XYChart.{HISTOGRAM, HISTOGRAM2DCONTOUR, SCATTER}
import org.carbonateresearch.picta.UnitTestUtils.{config, validateJson, x_double, y_double}
import org.scalatest.funsuite.AnyFunSuite

class Histogram2DContourTests extends AnyFunSuite {

  val plotFlag = false

  test("XY.Histogram2dContour") {
    val series = XY(x_double, y_double, series_type = HISTOGRAM2DCONTOUR)
    val layout = Layout("XY.Histogram2dContour")
    val chart = Chart() setData series setLayout layout setConfig config
    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }

  test("XY.Histogram2dContour.WithDensity") {
    val marker = Marker() setColor "rgb(102,0,0)"
    val series1 = XY(x_double, y_double, series_mode = MARKERS, series_type = SCATTER) setName "points" setMarker marker

    val hist2d_options = Hist2dOptions(ncontours = 20, reversescale = false, showscale = true)
    val series2 = XY(x_double, y_double, series_type = HISTOGRAM2DCONTOUR, series_name = "density") setHist2dOptions hist2d_options

    val series3 = XY(x = x_double, xaxis = "x", yaxis = "y2", series_type = HISTOGRAM) setName "histogram"
    val series4 = (XY(y_double, xaxis = "x2", series_type = HISTOGRAM, series_name = "y density") setMarker marker
      setHistOptions HistOptions(orientation = HORIZONTAL))

    val ax1 = Axis(position = "x", showgrid = false) setDomain(0.0, 0.85)
    val ax2 = Axis(position = "y", showgrid = false) setDomain(0.0, 0.85)
    val ax3 = Axis(position = "x2", showgrid = false) setDomain(0.85, 1.0)
    val ax4 = Axis(position = "y2", showgrid = false) setDomain(0.85, 1.0)

    val chart = Chart() setData List(series1, series2, series3, series4) setLayout
      (picta.Layout("XY.Histogram2dContour.WithDensity", autosize = false) setAxes(ax1, ax2, ax3, ax4))

    if (plotFlag) chart.plot
    assert(validateJson(chart.serialize.toString))
  }
}
