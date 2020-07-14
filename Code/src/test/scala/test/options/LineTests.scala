package org.carbonateresearch.picta

import org.carbonateresearch.picta.options.Line
import org.scalatest.funsuite.AnyFunSuite
import upickle.default.write

class LineTests extends AnyFunSuite {
  test("Line.Constructor.Basic") {
    val line = Line() setColor "rgb(255, 255, 255, 1)"
    val test = """{"width":0.5,"color":"rgb(255, 255, 255, 1)"}"""
    assert(test == write(line.serialize))
  }
  test("Line.Constructor.Advanced") {
    val line = Line() setWidth 0.1 setColor List("rgb(255, 255, 255, 1)", "rgb(255, 255, 255, 1)")
    val test = """{"width":0.1,"color":["rgb(255, 255, 255, 1)","rgb(255, 255, 255, 1)"]}"""
    assert(test == write(line.serialize))
  }
}