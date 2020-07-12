package picta

import org.scalatest.funsuite.AnyFunSuite
import picta.options.Axis
import upickle.default.write

class AxisTests extends AnyFunSuite {
  test("Axis.Constructor") {
    val axis = Axis(position = "x2") setTitle "second x-axis" setDomain(0.85, 1.0)
    val test = """{"xaxis2":{"title":{"text":"second x-axis"},"showgrid":true,"zeroline":false,"showline":false,"domain":[0.85,1]}}"""
    assert(test == write(axis.serialize))
  }
}
