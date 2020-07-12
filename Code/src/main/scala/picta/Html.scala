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
  private val cssStyle: String = readFile("style.css")
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
    val body = {
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

    val html = s"""<style>$cssStyle</style>""" + body

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
  def plotChart(traces: List[Value], frames: Opt[Value] = Blank, labels: Opt[Value] = Blank,
                transition_duration: Opt[Int] = Blank, layout: Value, config: Value): Unit = {

    val graph_id = System.currentTimeMillis().toString

    val html: String = (frames.asOption, labels.asOption, transition_duration.asOption) match {
      case (Some(x), Some(y), Some(z)) =>
        generateHTML(traces = traces, frames = x, labels = y, transition_duration = z, layout = layout, config = config,
          includeScript = true, graph_id = graph_id)
      case _ => generateHTML(traces = traces, layout = layout, config = config, includeScript = true, includeStyle = true,
        graph_id = graph_id)
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
  private def generateHTML(traces: Value, frames: Opt[Value] = Blank, labels: Opt[Value] = Blank, layout: Value, config: Value,
                           includeScript: Boolean = false, includeStyle: Boolean = true, graph_id: String, transition_duration: Opt[Int] = Blank): String = {

    var script = new StringBuilder()

    useCDN match {
      case true => script ++= """<script src="https://cdn.plot.ly/plotly-latest.min.js"></script>"""
      case false => script ++= s"""<script> ${plotlyJs} </script>"""
    }

    includeStyle match {
      case true => script ++= s"""<style>$cssStyle</style>"""
      case false => ()
    }

    val graph_html = frames.asOption match {
      case Some(_) => s"""
                         |<style>$cssStyle</style>
                         |<div align="center">
                         |<div id='graph_${graph_id}' class="graph"></div>
                         |<button id='play'>Play</button>
                         |<button id='pause'>Pause</button>
                         |<div id="sliderContainer"></div>
                         |<div>
                         |   <div id='counterContainer'><h3>Frame:</h3> <h3 id="value">0</h3> </div>
                         |</div>
                         |</div>
                         |""".stripMargin
      case _ =>
        s"""
           |<div align="center">
           |<div id='graph_${graph_id}' class='graph'></div>
           |</div>
           |""".stripMargin
    }

    val function_html = {
      (frames.asOption, labels.asOption, transition_duration.asOption) match {
        case (Some(x), Some(y), Some(z)) => createAnimationHTML(traces = traces, frames = x, labels = y, layout = layout,
          transition_duration = z, graph_id)

        case _ =>
          s"""|<script>
              | var traces = ${traces};
              | var layout = ${layout};
              | var config = ${config};
              | Plotly.newPlot("graph_${graph_id}", traces, layout, config);
              |</script>
              |""".stripMargin
      }
    }

    includeScript match {
      case true => (script ++= graph_html ++= function_html).mkString
      case false => graph_html + function_html
    }
  }

  def createAnimationHTML(traces: Value, frames: Value, labels: Value, layout: Value,
                          transition_duration: Int, graph_id: String): String = {

    s"""
       |<script>
       |const graph_id = 'graph_${graph_id}'
       |const graph = document.getElementById(graph_id)
       |const button = document.getElementById('play')
       |const traces = $traces
       |const layout = $layout
       |const frames = $frames
       |const labels = $labels
       |const duration = $transition_duration
       |
       |const animation_settings = {
       |    mode: "immediate",
       |    direction: {},
       |    fromcurrent: true,
       |    frame: [
       |      {duration: duration},
       |      {duration: duration},
       |      {redraw: true}
       |    ],
       |    transition: [
       |      {duration: duration, easing: 'cubic-in-out'},
       |      {duration: duration, easing: 'cubic-in-out'},
       |    ],
       |    ordering: "layout first"
       |}
       |
       |var slider = document.createElement("input")
       |slider.id = "slider"
       |slider.type = "range"
       |slider.min = 0
       |slider.max = frames.length - 1
       |slider.value = 0
       |document.getElementById("sliderContainer").appendChild(slider);
       |
       |Plotly.newPlot(graph_id, traces, layout)
       |.then(function () { Plotly.addFrames(graph_id, frames) })
       |
       |function startAnimation() {
       |  const start_index = slider.value == labels.length - 1 ? 0 : slider.value
       |  const end_index = labels.length
       |  Plotly.animate(graph_id, labels.slice(start_index, end_index), animation_settings)
       |}
       |
       |var trigger = true
       |var reset_count = false
       |
       |play.onclick = function() {
       |   trigger = true
       |   if (reset_count) {
       |      slider.value = 0
       |      reset_count = false
       |   }
       |   startAnimation()
       |}
       |
       |pause.onclick = function() {
       |   Plotly.animate(graph_id, [null], animation_settings)
       |}
       |
       |graph.on('plotly_redraw', () => {
       |   value.innerHTML = slider.value;
       |   if (trigger & !reset_count) ++slider.value
       |   if (slider.value == labels.length - 1) reset_count = true
       |});
       |
       |slider.oninput = function() {
       |   Plotly.animate(graph_id, frames[this.value], animation_settings)
       |   trigger = false
       |   slider.value = this.value
       |   value.innerHTML = this.value;
       |   if (this.value == 0) reset_count = false
       |   if (this.value == labels.length - 1) reset_count = true
       |   reset_count = false
       |}
       |</script>
       |""".stripMargin
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
  def plotChart_inline(traces: List[Value], frames: Opt[Value] = Blank, labels: Opt[Value] = Blank,
                       transition_duration: Opt[Int] = Blank, layout: Value, config: Value)(implicit publish: OutputHandler): Unit = {

    val graph_id = System.currentTimeMillis().toString

    val html: String = (frames.asOption, labels.asOption, transition_duration.asOption) match {
      case (Some(x), Some(y), Some(z)) => generateHTML(traces = traces, frames = x, labels = x, transition_duration = z,
        layout = layout, config = config, includeScript = false, includeStyle = false, graph_id = graph_id)
      case _ => generateHTML(traces = traces, layout = layout, config = config, includeScript = false, includeStyle = false
        , graph_id = graph_id)
    }
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