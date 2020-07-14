package org.carbonateresearch.picta

import org.carbonateresearch.picta.options.Axis
import org.scalatest.funsuite.AnyFunSuite
import upickle.default.write

class AxisTests extends AnyFunSuite {
  test("Axis.Constructor") {
    val axis = Axis(position = "x2") setTitle "second x-axis" setDomain(0.85, 1.0)
    val test = """{"xaxis2":{"title":{"text":"second x-axis"},"showgrid":true,"zeroline":false,"showline":false,"domain":[0.85,1]}}"""
    assert(test == write(axis.serialize))
  }
}
