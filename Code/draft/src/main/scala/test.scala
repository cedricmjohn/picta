package conusviz

import java.io._

import io.circe.syntax._
import io.circe.Json
import scalatags.Text.all._
import io.circe.generic.auto._
import io.circe.literal._

object test extends App {

  val exampleData: String = """{
                    |    "data": [
                    |        {
                    |            "x": [
                    |                10,
                    |                20,
                    |                30
                    |            ],
                    |            "y": [
                    |                20,
                    |                14,
                    |                23
                    |            ],
                    |            "type": "line"
                    |        }
                    |    ]
                    |}""".stripMargin('\n')
}
