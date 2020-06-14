package conusviz

import java.io.{BufferedWriter, File, FileWriter}

//import java.io._
import scala.io.Source
import scalatags.Text.all._
import ujson.{Obj, Value}

object Chart {

  // TODO CHANGE TO SCRIPT
//  val readmeText : Iterator[String] = Source.fromResource("example.txt").getLines

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

    val osName = (System.getProperty("os.name") match {
      case name if name.startsWith("Linux") => "linux"
      case name if name.startsWith("Mac") => "mac"
      case name if name.startsWith("Windows") => "win"
      case _ => throw new Exception("Unknown platform!")
    })

    // TODO - MAKE NAME DYNAMIC
    val model_name = System.currentTimeMillis().toString
    val dir = System.getProperty("user.home")+"/Conus/"+model_name+".html"
    val fout = new File(dir)
    val bufferWriter = new BufferedWriter(new FileWriter(fout))
    bufferWriter.write(html)
    bufferWriter.close()

    val command = osName match {
      case "linux" => Some(Seq("xdg-open", fout.getAbsolutePath))
      case "mac" => Some(Seq("open", fout.getAbsolutePath))
      case "win" => Some(Seq("cmd", s"start ${fout.getAbsolutePath}"))
      case _ => None
    }

    command match {
      case Some(c) => sys.process.Process(c).run
      case None => Console.err.println(s"Chart could not be opened")
    }
  }
}
