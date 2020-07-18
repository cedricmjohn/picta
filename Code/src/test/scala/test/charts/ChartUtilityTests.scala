package org.carbonateresearch.picta.charts

import org.carbonateresearch.picta
import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta.options.{Legend, MultiChart, RIGHT, VERTICAL, XAxis, YAxis}
import org.carbonateresearch.picta.{MARKERS, SCATTER, _}
import org.scalatest.funsuite.AnyFunSuite

class ChartUtilityTests extends AnyFunSuite {

  val plotFlag = true

//  test("Chart.SetTitle") {
//    val series = XY(x_int, y_int) asType SCATTER drawSymbol MARKERS
//    val chart = Chart() addSeries series setTitle "Chart.SetTitle"
//    val canvas = Canvas() addCharts chart
//    if (plotFlag) canvas.plot
//    assert(validateJson(chart.serialize.toString))
//  }

//  test("Chart.Axes") {
//    val series1 = XY(x_int, y_int) asType SCATTER drawSymbol MARKERS
//    val series2 = XY(x_int, y_double) asType SCATTER drawSymbol MARKERS
//    val series3 = XY(x_int, z_int) asType SCATTER drawSymbol MARKERS setAxis YAxis(2)
//
//    val chart = Chart() setTitle "Chart.Axes" addSeries(series1, series2, series3) setAxes YAxis(position = 2, title = "second y axis", overlaying = "y", side = RIGHT)
//
//    val canvas = Canvas() setChart(0, 0, chart)
//    if (plotFlag) canvas.plot
//    assert(validateJson(chart.serialize.toString))
//  }

//    test("Chart.ShowLegend") {
//      val series1 = XY(x_int, y_int) asType SCATTER drawSymbol MARKERS setName "First Series"
//      val series2 = XY(y_int, x_int) asType SCATTER drawSymbol MARKERS setName "Second Series"
//      val chart = Chart() addSeries(series1, series2) setTitle "Chart.ShowLegend" showLegend true
//      val canvas = Canvas() addCharts chart
//      if (plotFlag) canvas.plot
//      assert(validateJson(chart.serialize.toString))
//    }

//  test("Chart.ShowLegend") {
//    val series1 = XY(x_int, y_int) asType SCATTER drawSymbol MARKERS setName "First Series"
//    val series2 = XY(y_int, x_int) asType SCATTER drawSymbol MARKERS setName "Second Series"
//    val chart = Chart() addSeries(series1, series2) setTitle "Chart.ShowLegend" showLegend true
//    val canvas = Canvas() addCharts chart
//    if (plotFlag) canvas.plot
//    assert(validateJson(chart.serialize.toString))
//  }

//  test("Chart.setLegend") {
//    val series1 = XY(x_int, y_int) asType SCATTER drawSymbol MARKERS setName "First Series"
//    val series2 = XY(y_int, x_int) asType SCATTER drawSymbol MARKERS setName "Second Series"
//    val chart = Chart()
//      .addSeries(series1, series2)
//      .setTitle("Chart.ShowLegend")
//      .showLegend(true)
//      .setLegend(Legend(orientation = VERTICAL))
//
//    val canvas = Canvas() addCharts chart
//    if (plotFlag) canvas.plot
//    assert(validateJson(chart.serialize.toString))
//  }


    test("Chart.Axis.Composition") {
      val ax1 = XAxis() setTitle "x axis 1"
      val ax2 = XAxis(2) setTitle "x axis 2"

      val series1 = XY(x = List(1, 2, 3), y = List(2, 4, 5)) asType SCATTER drawSymbol MARKERS
      val series2 = XY(x = List(10, 12, 13), y = List(20, 31)) asType SCATTER drawSymbol MARKERS setAxes(XAxis(2), YAxis(2))

      val chart1 =
        Chart()
          .addSeries(series1, series2)
          .setTitle("Chart.Axis.Composition")
          .setAxes(ax1, ax2).setConfig(config)
          .asMultiChart(1, 2)

      val canvas = Canvas() addCharts chart1

      if (plotFlag) canvas.plot

      assert(validateJson(chart1.serialize.toString))
    }


}



