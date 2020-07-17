package org.carbonateresearch.picta

import org.carbonateresearch.picta.options.{MultiChart, Subplot, XAxis, YAxis}
import org.carbonateresearch.picta.UnitTestUtils._
import org.scalatest.funsuite.AnyFunSuite


case class SubplotTests() extends AnyFunSuite {
    test("XY.Grid") {
      val plotFlag = false

      val ax1 = XAxis() setTitle "x axis 1"
      val ax2 = XAxis(2) setTitle "x axis 2"

      val series1 = XY(x = List(1, 2, 3), y = List(2, 4, 5)) asType SCATTER drawSymbol MARKERS
      val series2 = XY(x = List(10, 12, 13), y = List(20, 31)) asType SCATTER drawSymbol MARKERS setAxes(XAxis(2), YAxis(2))

      val layout = Layout(title = "XY.Axis.Composition") setAxes(ax1, ax2) setMultiChart MultiChart(1, 2)

      val chart1 = Chart() addSeries(series1, series2) setLayout layout setConfig config
      val chart2 = Chart() addSeries(series2, series1) setLayout layout setConfig config

      val canvas = Canvas() setSubplot Subplot(1, 2) addCharts(chart1, chart2)

      canvas.subplot.grid.foreach(chart => println(chart.id))

      if (true) canvas.plot

      assert(validateJson(chart1.serialize.toString))
      assert(validateJson(chart2.serialize.toString))
    }

//  test("Grid.SetGrid") {
//
//    val plotFlag = false
//
//    val series1 = XY(x_double, y_double).setName("a")
//    val series2 = XY(y_int, y_double).setName("b")
//    val series3 = XY(x_int, y_double).setName("c")
//    val series4 = XY(z_int, z_double).setName("c")
//
//    val ax1 = XAxis(position = 1, title = "x axis 1")
//    val ax2 = XAxis(position = 2, title = "x axis 2")
//    val ax3 = XAxis(position = 3, title = "x axis 3")
//    val ax4 = XAxis(position = 4, title = "x axis 4")
//
//    val ax6 = YAxis(position = 1, title = "y axis 1")
//    val ax7 = YAxis(position = 2, title = "y axis 2")
//    val ax8 = YAxis(position = 3, title = "y axis 3")
//    val ax9 = YAxis(position = 4, title = "y axis 4")
//
//    val grid = Grid(2, 2)
//
//    grid(0, 1).setAxis(ax1, ax6).addSeries(series1)
//    grid(1, 1).setAxis(ax2, ax7).addSeries(series2)
//    grid(0, 0).setAxis(ax3, ax8).addSeries(series3)
//    grid(1, 0).setAxis(ax4, ax9).addSeries(series4)
//
//    val layout = Layout("please") setSubplot grid
//
//    val chart = Chart() setLayout layout
//
//    if (plotFlag) chart.plot()
//
//    assert(validateJson(chart.serialize.toString))
//  }
}
