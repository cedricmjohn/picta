package conusviz

import java.io.{BufferedWriter, File, FileWriter}

import almond.api.JupyterApi
import almond.api.helpers.Display
import scalatags.Text.all._
import ujson.Value
import almond.interpreter.api.{DisplayData, OutputHandler}

import scala.io.Source

object Chart {

  // TODO CHANGE TO SCRIPT
  val minJs : String = {
    val is = getClass.getClassLoader.getResourceAsStream("plotly.min.js")
    scala.io.Source.fromInputStream(is).mkString
  }

  def generateHTML(traces: Value, layout: Value, config: Value, plotlyJs: String): String = {
      s"""
         |<div id='graph'></div>
         |<script>
         |$plotlyJs
         | var traces = ${traces};
         | var layout = ${layout};
         | var config = ${config};
         | Plotly.newPlot("graph", traces, layout, config);
         |</script>
         |""".stripMargin
  }

  def writeHTMLToJupyter(html: String)(implicit publish: OutputHandler): Unit = {
    publish.html(html)
  }

  def writeHTMLToFile(html: String): Unit = {
    val osName = (System.getProperty("os.name") match {
      case name if name.startsWith("Linux") => "linux"
      case name if name.startsWith("Mac") => "mac"
      case name if name.startsWith("Windows") => "win"
      case _ => throw new Exception("Unknown platform!")
    })

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

  /*
  * This sets the charts to be inline inside a Jupyter notebook.
  * */
  def init_notebook_mode()(implicit publish: OutputHandler, kernel: JupyterApi): Unit = {
    kernel.silent(true)
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

  // simply inject traces, layout and config into the the function and generate the HTML
  def plotChart(traces: List[Value], layout: Value, config: Value, js: String): Unit = {
    val html: String = generateHTML(traces, layout, config, js)
    writeHTMLToFile(html)
  }

  def plotChart_inline(traces: List[Value], layout: Value, config: Value)
                      (implicit publish: OutputHandler): Unit = {
    val html: String = generateHTML(traces, layout, config, "")
    writeHTMLToJupyter(html)
  }
}
