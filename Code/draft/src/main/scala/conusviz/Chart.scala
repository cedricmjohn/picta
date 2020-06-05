package conusviz

import java.io._
import scalatags.Text.all._

object Chart {

  def generatePlotlyFunction(jsonString: String):String = {
      s"""
         | <script>
         | var Json = ${jsonString};
         | //var Json = JSON.parse(myJson)
         | window.onload = function() {
         |   Plotly.newPlot("graph", Json.data);
         | }
         | </script>
         |""".stripMargin
  }

  def generateHTMLChart(plotlyFunction: String):String = {
    html(
      head(
        script(src:="https://cdnjs.cloudflare.com/ajax/libs/plotly.js/1.33.1/plotly-basic.min.js"),
        script(plotlyFunction)
      ),
      body(
        div(id:="graph"),
      )
    ).toString()
  }

  def writeHTMLChartToFile(html: String):Unit = {
    // TODO - MAKE NAME DYNAMIC
    val fout = new File("example.html")
    val bufferWriter = new BufferedWriter(new FileWriter(fout))
    bufferWriter.write(html)
    bufferWriter.close()
  }
}
