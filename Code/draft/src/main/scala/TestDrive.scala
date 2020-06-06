package conusviz

import java.io._
import scalatags.Text.all._

import io.circe.syntax._
import io.circe.{Json, Encoder}
import io.circe.generic.auto._

object TestDrive {

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


