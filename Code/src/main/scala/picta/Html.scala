package picta

import java.io.{BufferedWriter, File, FileWriter}
import java.net.{HttpURLConnection, URL}

import almond.api.JupyterApi
import almond.interpreter.api.OutputHandler
import os.Path
import picta.common.OptionWrapper._
import ujson.Value

object Html {

  /** this is the plotly.min.js script that is used to render the plots */
  private val plotlyJs: String = readFile("plotly.min.js")
  private val useCDN: Boolean = testNetworkConnection()

  /**
   * This sets the charts to be inline inside a Jupyter notebook.
   *
   * @param publish (implicit): required to render the HTML in the almond notebook.
   * @param kernel  (implicit): required to interact with the underlying Jupyter kernel instance.
   */
  def init_notebook_mode()(implicit publish: OutputHandler, kernel: JupyterApi): Unit = {
    kernel.silent(true)

    /** if internet connection; grab from cdn otherwise just inject the raw javascript */
    val html = {
      val requirejs_CDN_path = """paths: {'plotly': "https://cdn.plot.ly/plotly-latest.min"},"""

      val requirejs =
        if (useCDN) s"""<script type='text/javascript'>${readFile("require.min.js")}</script>""" else ""

      s"""
         |${if (useCDN) requirejs}
         |<script type='text/javascript'>
         |require.config({
         |${if (useCDN) requirejs_CDN_path else plotlyJs}
         |})
         |require( ['plotly'], function(Plotly) {
         |window.Plotly = Plotly;
         |})
         |</script>
         |""".stripMargin
    }

    publish.html(html)
  }

  /** A function to read files from the src resources folder */
  private def readFile(file_name: String): String = {
    val is = getClass.getClassLoader.getResourceAsStream(file_name)
    scala.io.Source.fromInputStream(is).mkString
  }

  /**
   * This plots the chart in the browser
   *
   * @param traces : a list of trace data we wish to plot
   * @param layout : the layout case class specifying layout options
   * @param config : the config case class specifying the chart config options
   */
  def plotChart(traces: List[Value], frames: Opt[Value]=Blank, layout: Value, config: Value): Unit = {
    val graph_id = System.currentTimeMillis().toString

    val html: String = frames.asOption match {
      case Some(x) =>
        generateHTML(traces=traces, frames=x,layout=layout, config=config, includeScript = true, graph_id=graph_id)
      case _ => generateHTML(traces=traces, layout=layout, config=config, includeScript = true, graph_id=graph_id)
    }

    writeHTMLToFile(html, graph_id)
  }

  /**
   * A function to generate the HTML corresponding to the Plotly plotting function.
   *
   * @param traces   : This is the trace data. It should be serialized as a json list.
   * @param layout   : This should be the layout case class instance.
   * @param config   : This should be the config case class instance.
   * @param graph_id : This is an internal id that allows the Plotly functions to find the chart element in the HTML.
   */
  private def generateHTML(traces: Value, frames: Opt[Value]=Blank, layout: Value, config: Value, includeScript: Boolean, graph_id: String): String = {

    var script = new StringBuilder()

    useCDN match {
      case true => script ++= """<script src="https://cdn.plot.ly/plotly-latest.min.js"></script>"""
      case false => script ++= s"""<script> ${plotlyJs} </script>"""
    }

    val graph_html =
      s"""
       |<div align="center">
       |<div id='graph_${graph_id}' style="width:100%; margin:0 auto;"></div>
       |</div>
       |""".stripMargin

    val function_html = frames.asOption match {
      case Some(frames_) =>
        s"""
           |<script>
           |Plotly.newPlot("graph_${graph_id}",{
           |data: ${traces},
           |layout: ${layout},
           |frames: ${frames_},
           |});
           |</script>
           |""".stripMargin
      case _ =>
        s"""|<script>
            | var traces = ${traces};
            | var layout = ${layout};
            | var config = ${config};
            | Plotly.newPlot("graph_${graph_id}", traces, layout, config);
            |</script>
            |""".stripMargin
    }

    includeScript match {
      case true => (script ++= graph_html ++= function_html).mkString
      case false => graph_html + function_html
    }
  }

  /**
   * A function to write the chart to a .html and open a browser displaying the chart.
   *
   * @param html     : This is the html represented as a string.
   * @param graph_id : This is required in order to generate a unique file name for the chart.
   */
  private def writeHTMLToFile(html: String, graph_id: String): Unit = {
    val osName = System.getProperty("os.name") match {
      case name if name.startsWith("Linux") => "linux"
      case name if name.startsWith("Mac") => "mac"
      case name if name.startsWith("Windows") => "win"
      case _ => throw new Exception("Unknown platform!")
    }

    /** Folders where images are saved is relative to the home folder */
    val save_directory = System.getProperty("user.home") + "/Conus/"
    if (!os.exists(Path(save_directory))) os.makeDir(Path(save_directory))
    val file_directory = save_directory + graph_id + ".html"
    val fout = new File(file_directory)
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

  /**
   * This plots the chart inside a Jupyter notebook
   *
   * @param traces  : a list of trace data we wish to plot
   * @param layout  : the layout case class specifying layout options
   * @param config  : the config case class specifying the chart config options
   * @param publish (implicit): required to render the HTML in the almond notebook
   */
  def plotChart_inline(traces: List[Value], layout: Value, config: Value)(implicit publish: OutputHandler): Unit = {
    val graph_id = System.currentTimeMillis().toString
    val html: String = generateHTML(traces=traces, layout=layout, config=config, includeScript = false, graph_id = graph_id)
    writeHTMLToJupyter(html, graph_id)
  }

  /**
   * A function to inject the HTML into the Jupyter notebook.
   *
   * @param html     : This is the html represented as a string.
   * @param graph_id : This is required in order to generate a unique div id for the plotly chart to render in the right
   *                 place.
   */
  private def writeHTMLToJupyter(html: String, graph_id: String)(implicit publish: OutputHandler): Unit = {
    publish.html(html)
  }

  /** This function checks if an active network connection is available. It returns true if this is the case, false
   * otherwise.
   */
  private def testNetworkConnection(): Boolean = {
    var activeConnection: Boolean = true
    val url: URL = new URL("https://www.google.com")
    val urlConn: HttpURLConnection = url.openConnection().asInstanceOf[HttpURLConnection]

    try {
      urlConn.connect()
      urlConn.setConnectTimeout(1)
      urlConn.setReadTimeout(1)
    } catch {
      case e: Throwable => activeConnection = false
    } finally urlConn.disconnect()

    activeConnection
  }
}