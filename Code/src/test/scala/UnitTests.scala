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

  val k = List(
    List(8.83,8.89,8.81,8.87,8.9,8.87),
    List(8.89,8.94,8.85,8.94,8.96,8.92),
    List(8.84,8.9,8.82,8.92,8.93,8.91),
    List(8.79,8.85,8.79,8.9,8.94,8.92),
    List(8.79,8.88,8.81,8.9,8.95,8.92),
    List(8.8,8.82,8.78,8.91,8.94,8.92),
    List(8.75,8.78,8.77,8.91,8.95,8.92),
    List(8.8,8.8,8.77,8.91,8.95,8.94),
    List(8.74,8.81,8.76,8.93,8.98,8.99),
    List(8.89,8.99,8.92,9.1,9.13,9.11),
    List(8.97,8.97,8.91,9.09,9.11,9.11),
    List(9.04,9.08,9.05,9.25,9.28,9.27),
    List(9,9.01,9,9.2,9.23,9.2),
    List(8.99,8.99,8.98,9.18,9.2,9.19),
    List(8.93,8.97,8.97,9.18,9.2,9.18)
  )

    test("Trace.InvalidTrace.XYZ") {
      val chart_types = Seq("scatter", "scattergl", "bar")
      chart_types.map(t => {
        assertThrows[IllegalArgumentException] {
          XYZTrace(x_int, y_int, z_int, "test", t, "lines").value
        }
      })
    }

    test("Trace.PlotlyExample.Surface") {
      val trace = SurfaceTrace(k, "trace")
      val test ="""{"name":"trace","type":"surface","mode":"","z":[[8.83,8.89,8.81,8.87,8.9,8.87,8.89,8.94,8.85,8.94,8.96,8.92,8.84,8.9,8.82],[8.92,8.93,8.91,8.79,8.85,8.79,8.9,8.94,8.92,8.79,8.88,8.81,8.9,8.95,8.92],[8.8,8.82,8.78,8.91,8.94,8.92,8.75,8.78,8.77,8.91,8.95,8.92,8.8,8.8,8.77],[8.91,8.95,8.94,8.74,8.81,8.76,8.93,8.98,8.99,8.89,8.99,8.92,9.1,9.13,9.11],[8.97,8.97,8.91,9.09,9.11,9.11,9.04,9.08,9.05,9.25,9.28,9.27,9,9.01,9],[9.2,9.23,9.2,8.99,8.99,8.98,9.18,9.2,9.19,8.93,8.97,8.97,9.18,9.2,9.18]]}"""
      assert(test == write(trace.value))
    }












}
