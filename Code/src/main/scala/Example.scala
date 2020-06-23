import java.io.IOException
import java.net.{HttpURLConnection, InetSocketAddress, URL}

import conusviz.{Col, Serializer, Table}
import org.carbonateresearch.conus.calibration.{Calibrator, InRange, LargerThan, SmallerThan, ValueEqualTo}
import org.carbonateresearch.conus.common.{ModelVariable, SingleModelResults, SteppedModel}
import org.carbonateresearch.conus.grids.{AllCells, PerCell}
import org.carbonateresearch.conus.modelzoo.GeneralGeology._
import org.carbonateresearch.conus.modelzoo.PasseyHenkesClumpedDiffusionModel._
import conusviz.Utils._
import conusviz.charts.{XYChart, XYZChart}
import conusviz.options.AxisOptions.Axis
import conusviz.options.ConfigOptions.Config
import conusviz.options.GridOptions.Grid
import conusviz.options.LayoutOptions.Layout
import conusviz.traces.{XYTrace, XYZTrace}
import spire.random.rng.Serial
import ujson.{Obj, Value}
import upickle.default._
import upickle.default.{macroRW, ReadWriter => RW}


object Example extends App {
//
//  // some dummy data
//  val col1 = Col(List("a", "b", "c", "d", "e"))
//  val col2 = Col(List(12, 200, 80900, 201200, 124420000))
//  val col3 = Col(List(121, 12121, 71240000, 44356, 845))
//
//  val header: Col[String] = Col(List("a1", "b1", "c1"))
//  val data = List(col1, col2, col3)
//
//  val table = Table(header, data)
//  table.plot()
//

  val grid = Grid(1, 2, "independent")

  println(grid.serialize())






}

