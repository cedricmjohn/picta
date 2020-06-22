import java.io.IOException
import java.net.{HttpURLConnection, InetSocketAddress, URL}

import conusviz.{Col, Serializer, Table}
import org.carbonateresearch.conus.calibration.{Calibrator, InRange, LargerThan, SmallerThan, ValueEqualTo}
import org.carbonateresearch.conus.common.{ModelVariable, SingleModelResults, SteppedModel}
import org.carbonateresearch.conus.grids.{AllCells, PerCell}
import org.carbonateresearch.conus.modelzoo.GeneralGeology._
import org.carbonateresearch.conus.modelzoo.PasseyHenkesClumpedDiffusionModel._
import conusviz.Utils._
import conusviz.charts.XYZChart
import conusviz.options.AxisOptions.Axis
import conusviz.options.ConfigOptions.Config
import conusviz.options.LayoutOptions.Layout
import conusviz.traces.XYZTrace
import spire.random.rng.Serial
import ujson.{Obj, Value}
import upickle.default._
import upickle.default.{macroRW, ReadWriter => RW}


object Example extends App {

  val data = List(1, 2, 3, 4, 5)

  for ((x, i) <- data.view.zipWithIndex) {
    println(x, i)
  }


//  implicit val rw: RW[List[(String, Object)]] = macroRW
//
//
//  case class example(a: String, b: String) {
//    val fields = {this.getClass.getDeclaredFields.toList.map(i => {
//        i.getName -> i.get(this)
//      })}
//  }


  // some dummy data
//  val col1 = Col(List("a", "b", "c", "d", "e"))
//  val col2 = Col(List(12, 200, 80900, 201200, 124420000))
//  val col3 = Col(List(121, 12121, 71240000, 44356, 845))
//
//  val header: Col[String] = Col(List("a1", "b1", "c1"))
//  val data = List(col1, col2, col3)
//
//  val table = Table(header, data)
//  table.plot()
  
}

//  val runnedModel = myFirstModel.run

//  Thread.sleep(1000)

//  val r: Seq[SingleModelResults] = runnedModel.results

// total number of steps
//  println(r(0).getModelVariablesForStep(0))


//println(createCoordinateRange(2, 2))

//  val x = getSeriesFromSingleModel(r(0), depth, Seq(0, 0), 100)
//  val y = getSeriesFromSingleModel(r(0), burialTemperature, Seq(0, 0), 100)
//  val z: List[List[Double]] = getSeriesFromSingleModel(r(0), SampleTemp, Seq(0, 0), 100).grouped(4).toList
//
//  val trace = XYZTrace(z, "example", "surface")
//  val layout = Layout("XYZ.Heatmap", true)
//  val config: Config = Config(responsive=false, scrollZoom=true)
//  val chart = new XYZChart(List(trace), layout, config)
//  chart.plot()
