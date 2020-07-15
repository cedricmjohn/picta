package org.carbonateresearch.picta.charts

import java.io.{BufferedWriter, File, FileWriter}
import java.net.{HttpURLConnection, URL}

import almond.api.JupyterApi
import almond.interpreter.api.OutputHandler
import org.carbonateresearch.picta.OptionWrapper.{Blank, Opt}
import org.carbonateresearch.picta.common.Utils.genRandomText
import os.Path
import ujson.Value

private[picta] object Html {
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
  def initNotebook()(implicit publish: OutputHandler, kernel: JupyterApi): Unit = {
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
  private[picta] def plotChart(traces: List[Value], frames: Opt[Value] = Blank, labels: Opt[Value] = Blank,
                transition_duration: Opt[Int] = Blank, layout: Value, config: Value): Unit = {

    val graph_id = genRandomText()

    val html: String = (frames.option, labels.option, transition_duration.option) match {
      case (Some(x), Some(y), Some(z)) =>
        generateHTML(traces = traces, frames = x, labels = y, transition_duration = z, layout = layout, config = config,
          includeScript = true, graph_id = graph_id)
      case _ => generateHTML(traces = traces, layout = layout, config = config, includeScript = true, includeStyle = true,
        graph_id = graph_id)
    }

    writeHTMLToFile(html, graph_id)
  }

  /**
   * This plots the chart inside a Jupyter notebook
   *
   * @param traces  : a list of trace data we wish to plot
   * @param layout  : the layout case class specifying layout options
   * @param config  : the config case class specifying the chart config options
   * @param publish (implicit): required to render the HTML in the almond notebook
   */
  private[picta] def plotChartInline(traces: List[Value], frames: Opt[Value] = Blank, labels: Opt[Value] = Blank,
                                      transition_duration: Opt[Int] = Blank, layout: Value, config: Value)(implicit publish: OutputHandler): Unit = {

    val graph_id = genRandomText() + genRandomText()

    val html: String = (frames.option, labels.option, transition_duration.option) match {
      case (Some(x), Some(y), Some(z)) => generateHTML(traces = traces, frames = x, labels = y, transition_duration = z,
        layout = layout, config = config, includeScript = false, includeStyle = false, graph_id = graph_id)
      case _ => generateHTML(traces = traces, layout = layout, config = config, includeScript = false, includeStyle = false,
        graph_id = graph_id)
    }

    writeHTMLToJupyter(html, graph_id)
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

    val graph_html = frames.option match {
      case Some(_) =>
        s"""
           |<style>$cssStyle</style>
           |<div align="center">
           |<div id='graph_${graph_id}' class="graph"></div>
           |<div class='animationInterface'>
           |<button id='play_${graph_id}'>Play</button>
           |<button id='pause_${graph_id}'>Pause</button>
           |<div id="sliderContainer_${graph_id}" class='progressBar'></div>
           |<div>
           |<div>
           |   <div id='counterContainer_${graph_id}' class='inline-div'>
           |      <h3>Frame: </h3><h3 id="value_${graph_id}">0</h3>
           |   </div>
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
      (frames.option, labels.option, transition_duration.option) match {
        case (Some(x), Some(y), Some(z)) => createAnimationHTML(traces = traces, frames = x, labels = y, layout = layout,
          transition_duration = z, graph_id)

        case _ =>
          s"""|<script>
              | var traces_${graph_id} = ${traces};
              | var layout_${graph_id} = ${layout};
              | var config_${graph_id} = ${config};
              | Plotly.newPlot("graph_${graph_id}", traces_${graph_id}, layout_${graph_id}, config_${graph_id});
              |</script>
              |""".stripMargin
      }
    }

    includeScript match {
      case true => (script ++= graph_html ++= function_html).mkString
      case false => graph_html + function_html
    }
  }

  private def createAnimationHTML(traces: Value, frames: Value, labels: Value, layout: Value,
                          transition_duration: Int, graph_id: String): String = {

    s"""
       |<script>
       |//var graph_id = 'graph_${graph_id}'
       |var graph_${graph_id} = document.getElementById('graph_${graph_id}')
       |var traces_${graph_id} = $traces
       |var layout_${graph_id} = $layout
       |var frames_${graph_id} = $frames
       |var labels_${graph_id} = $labels
       |var duration_${graph_id} = $transition_duration
       |
       |var animation_settings_${graph_id} = {
       |    mode: "immediate",
       |    direction: {},
       |    fromcurrent: true,
       |    frame: [
       |      {duration: duration_${graph_id}},
       |      {duration: duration_${graph_id}},
       |      {redraw: true}
       |    ],
       |    transition: [
       |      {duration: duration_${graph_id}, easing: 'cubic-in-out'},
       |      {duration: duration_${graph_id}, easing: 'cubic-in-out'},
       |    ],
       |    ordering: "layout first"
       |}
       |
       |var slider_${graph_id} = document.createElement("input")
       |var play_${graph_id} = document.getElementById("play_${graph_id}")
       |var pause_${graph_id} = document.getElementById("pause_${graph_id}")
       |slider_${graph_id}.id = "slider_${graph_id}"
       |slider_${graph_id}.type = "range"
       |slider_${graph_id}.min = 0
       |slider_${graph_id}.max = frames_${graph_id}.length - 1
       |slider_${graph_id}.value = 0
       |document.getElementById("sliderContainer_${graph_id}").appendChild(slider_${graph_id});
       |
       |Plotly.newPlot(graph_${graph_id}, traces_${graph_id}, layout_${graph_id})
       |.then(function () { Plotly.addFrames(graph_${graph_id}, frames_${graph_id}) })
       |
       |var trigger_${graph_id} = true
       |var reset_count_${graph_id} = false
       |
       |
       |play_${graph_id}.addEventListener('click', function() {
       |   trigger_${graph_id} = true
       |   if (reset_count_${graph_id}) {
       |      slider_${graph_id}.value = 0
       |      reset_count_${graph_id} = false
       |   }
       |
       |  const start_index = slider_${graph_id}.value == labels_${graph_id}.length - 1 ? 0 : slider_${graph_id}.value
       |  const end_index = labels_${graph_id}.length
       |  Plotly.animate(graph_${graph_id}, labels_${graph_id}.slice(start_index, end_index), animation_settings_${graph_id})
       |})
       |
       |pause_${graph_id}.addEventListener('click', function() {
       |  Plotly.animate(graph_${graph_id}, [null], animation_settings_${graph_id})
       |})
       |
       |graph_${graph_id}.on('plotly_redraw', () => {
       |   value_${graph_id}.innerHTML = slider_${graph_id}.value;
       |   if (trigger_${graph_id} & !reset_count_${graph_id}) ++slider_${graph_id}.value
       |   if (slider_${graph_id}.value == labels_${graph_id}.length - 1) reset_count_${graph_id} = true
       |});
       |
       |slider_${graph_id}.oninput = function() {
       |   Plotly.animate(graph_${graph_id}, frames_${graph_id}[this.value], animation_settings_${graph_id})
       |   trigger_${graph_id} = false
       |   slider_${graph_id}.value = this.value
       |   value_${graph_id}.innerHTML = this.value;
       |   if (this.value == 0) reset_count_${graph_id} = false
       |   if (this.value == labels_${graph_id}.length - 1) reset_count_${graph_id} = true
       |   reset_count_${graph_id} = false
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
    val save_directory = System.getProperty("user.home") + "/Picta/"
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