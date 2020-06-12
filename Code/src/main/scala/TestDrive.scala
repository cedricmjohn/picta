package conusviz

//import scala.util.Random
//import Data._
//import ujson.{Obj, Value}
import ujson.{Arr, Obj, Value}
import upickle.default._

import scala.reflect.ClassTag
import scala.util.Random

object Example extends App {

  val rand: Random.type = scala.util.Random

  // raw data
  val x = (1 to 1000).map { e => e*100*rand.nextDouble() }.toSeq
  val y = x.map(_*rand.nextDouble())
  val z = y.map(_+rand.nextDouble()*100)

  val layout = Layout("Chart", true)
  val config = Config(true, true)
  val trace = Trace(List(x, y), trace_name = "test", trace_type = "scattergl", mode = "markers")

  val xychart = new XYChart(List(trace), layout, config)
  xychart.plot()
}

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