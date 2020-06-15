package conusviz

import java.io.{BufferedWriter, File, FileWriter}
import scalatags.Text.all._
import ujson.{Value}
import almond.interpreter.api.{DisplayData, OutputHandler}
import scala.io.Source

object Chart {

  // TODO CHANGE TO SCRIPT
  val minJs : String = {
    val is = getClass.getClassLoader.getResourceAsStream("plotly.min.js")
    scala.io.Source.fromInputStream(is).mkString
  }

  def init_notebook_mode()(implicit publish: OutputHandler): Unit = {
    val html = s"""
      |<script type='text/javascript'>
      |define( 'plotly', function(require, exports, module) {
      |$minJs
      |})
      |require( ['plotly'], function(Plotly) {
      |window.Plotly = Plotly;
      |})
      |</script>
      |""".stripMargin
    publish.html(html)
  }

  def generatePlotlyFunction(traces: Value, layout: Value, config: Value): String = {
      s"""
         |<script>
         | var traces = ${traces};
         | var layout = ${layout};
         | var config = ${config};
         | Plotly.newPlot("graph", traces, layout, config);
         |</script>
         |""".stripMargin
  }

  def generateHTMLChart(plotlyFunction: String):String = {
    html(
      body(
        div(id:="graph"),
        raw(plotlyFunction)
      )
    ).toString()
  }

  def writeHTMLToJupyter(html: String)(implicit publish: OutputHandler): Unit = {
    publish.html(html)
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
