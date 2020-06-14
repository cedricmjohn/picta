package conusviz

import org.scalatest.FunSuite
import upickle.default._
import upickle.default.{ReadWriter => RW}

class UnitTests extends FunSuite {

  val x_int = List(1, 2, 3)
  val y_int = List(1, 2, 3)
  val z_int = List(1, 2, 3)

  val x_double = List(1.5, 2.5, 3.5)
  val y_double = List(1.5, 2.5, 3.5)
  val z_double = List(1.5, 2.5, 3.5)

  val x_str = List("1", "2", "3")
  val y_str = List("a", "b", "c")
  val z_str = List("my", "name", "is")

  /*
  * XY Chart tests
  * */
  test("Trace.createTrace.Int.Scatter.2D") {
    val trace = XYTrace(x_int, y_int, "test", "scatter", "lines")
    val test = """{"name":"test","type":"scatter","mode":"lines","x":[1,2,3],"y":[1,2,3]}"""
    assert(test == write(trace.value))
  }

  test("Trace.createTrace.String.Scatter.2D") {
    val trace = XYTrace(x_str, y_str, "test", "scatter", "lines")
    val test = """{"name":"test","type":"scatter","mode":"lines","x":["1","2","3"],"y":["a","b","c"]}"""
    assert(test == write(trace.value))
  }

  test("Trace.createTrace.Double.Scatter.2D") {
    val trace = XYTrace(x_double, y_double, "test", "scatter", "lines")
    val test = """{"name":"test","type":"scatter","mode":"lines","x":[1.5,2.5,3.5],"y":[1.5,2.5,3.5]}"""
    assert(test == write(trace.value))
  }

  test("Data.InvalidTrace.XY") {
    val chart_types = Seq("contour", "heatmap", "scatter3d")
    chart_types.map(t => {
      assertThrows[IllegalArgumentException] {
        XYTrace(x_int, y_int, "test", t, "lines")
      }
    })
  }

  /*
  * XYZ Chart tests
  * */




//  test("Data.InvalidTrace.XYZ") {
//    val cases = Seq("contour", "heatmap", "scatter3d")
//    cases.map(str => {
//      assertThrows[IllegalArgumentException] {
//        Trace(List(x, y), "test", str, "lines").value
//      }
//    })
//  }
}
