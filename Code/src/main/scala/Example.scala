//package conusviz

//import Trace._
//import ujson.Obj
import conusviz.{Config, Layout, SurfaceTrace, Trace, XYChart, XYTrace}
import ujson.{Obj, Value}
import upickle.default.{ReadWriter => RW}
import upickle.default._

import scala.reflect.ClassTag
import conusviz.XYTrace._

object Example extends App {
  val x = List("a", "b", "c")
  val y = List(1, 2, 3, 4)
  val z = List(10, 20, 30)

  val k = List.range(1, 100).grouped(10).toList

  val data = SurfaceTrace(k,"trace0", "scatter", "marker")

  val layout = Layout("Chart", true)
  val config = Config(true, true)

//  val xychart = new S(List(data), layout, config)

//  xychart.plot()
}



//  val trace: Trace[Any] = Trace(List(x, y), trace_name = "test", trace_type = "surface", mode = "lines")

//val layout = Layout("Chart", true)
//val config = Config(true, true)

//  val layout = Layout("Chart", true)
//  val config = Config(true, true)
//  val trace = Trace(List(x, y), trace_name = "test", trace_type = "surface", mode = "lines")
//
//  println(trace)
//  val xychart = new XYChart(List(trace), layout, config)
//  xychart.plot()

//  val sum: ModelVariable[Double] = ModelVariable("Sum", 1.0)
//  val interestRate: ModelVariable[Double] = ModelVariable("Interest Rate", 0.1)
//
//  def interestCalculator = (s: Step) => sum(s - 1) + sum(s - 1) * interestRate(s)
//
//  val model = new SteppedModel(10, "Finance").setGrid(3)
//    .defineMathematicalModel(sum =>> interestCalculator)
//    .defineInitialModelConditions(
//      PerCell(sum, List(
//        (List(1.0), Seq(0)),
//        (List(10.0), Seq(1)),
//        (List(100.0), Seq(2))
//      )),
//      AllCells(interestRate, List(0.10))
//    )

//  val runnedModel = model.run

//  Thread.sleep(1000)