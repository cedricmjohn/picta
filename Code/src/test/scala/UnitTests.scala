package conusviz

import org.scalatest.FunSuite
import upickle.default._
import upickle.default.{ReadWriter => RW}

import Trace._


class UnitTests extends FunSuite {

//  test("Data.createSeries.2D") {
//    val x = Seq(1, 2, 3)
//    val y = Seq(1, 2, 3)
//    val series = Trace.createSeries(List(x, y))
//    val test: String = """{"x":[1,2,3],"y":[1,2,3]}"""
//    assert(write(series) == test)
//  }
//
//  test("Data.createSeries.3D") {
//    val x = Seq(1, 2, 3)
//    val y = Seq(1, 2, 3)
//    val z = Seq(1, 2, 3)
//    val series = Trace.createSeries(List(x, y, z))
//    val test: String = """{"x":[1,2,3],"y":[1,2,3],"z":[1,2,3]}"""
//    assert(write(series) == test)
//  }
//
//  test("Data.zipSeries.3D") {
//    val x = Seq(1, 2, 3)
//    val y = Seq(1, 2, 3)
//    val z = Seq(1, 2, 3)
//    val test = List((1, 1, 1), (2, 2, 2), (3, 3, 3))
//    assert(test == Trace.zipSeries(List(x, y, z)))
//  }
//
//  test("Data.createTrace.Scatter.2D") {
//    val x = Seq(1, 2, 3)
//    val y = Seq(1, 2, 3)
//    val trace = createTrace(List(x, y), "test", "scatter", "lines")
//    val test = """{"name":"test","type":"scatter","mode":"lines","x":[1,2,3],"y":[1,2,3]}"""
//    assert(test == write(trace))
//  }

//  test("Data.createTrace.2D") {
//    val x = Seq(1, 2, 3)
//    val y = Seq(1, 2, 3)
//    val trace = Trace(List(x, y), "test", "scatter", "lines")
//    val test = """{"name":"test","type":"scatter","mode":"lines","x":[1,2,3],"y":[1,2,3]}"""
//    assert(test == write(trace.value))
//  }

  test("Data.createTrace.3D") {
    val x = Seq(1, 2, 3)
    val y = Seq(10, 20, 30)
    val z = Seq(30, 20, 10)
    val trace1 = Trace(List(x, y, z), "test", "scatter", "lines").value
    val trace2 = Trace(List(x, y), "test", "scatter", "lines").value
    val data = List(trace1, trace2)

    println(write(data))
  }







}
