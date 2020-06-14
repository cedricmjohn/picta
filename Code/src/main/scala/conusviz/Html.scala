package conusviz

import scala.io.Source
import java.io._

import io.circe.Json
import scalatags.Text.all._
import ujson.{Obj, Value}

object Chart {

  // TODO CHANGE TO SCRIPT
  val readmeText : Iterator[String] = Source.fromResource("example.txt").getLines

  def generatePlotlyFunction(traces: Value, layout: Value, config: Value):String = {
      s"""
         | <script>
         | var traces = ${traces};
         | var layout = ${layout};
         | var config = ${config};
         | //var Json = JSON.parse(myJson)
         | window.onload = function() {
         |   Plotly.newPlot("graph", traces, layout, config);
         | }
         | </script>
         |""".stripMargin
  }

  def generateHTMLChart(plotlyFunction: String):String = {
    html(
      head(
        script(src:="https://cdnjs.cloudflare.com/ajax/libs/plotly.js/1.33.1/plotly.min.js"),
        raw(plotlyFunction)
      ),
      body(
        div(id:="graph"),
      )
    ).toString()
  }

  def writeHTMLChartToFile(html: String): Unit = {
    // TODO - MAKE NAME DYNAMIC
    val fout = new File("example.html")
    val bufferWriter = new BufferedWriter(new FileWriter(fout))
    bufferWriter.write(html)
    bufferWriter.close()
  }
}
