package org.carbonateresearch.picta.charts

import java.io.{BufferedWriter, File, FileWriter}
import java.net.{HttpURLConnection, URL}

import almond.api.JupyterApi
import almond.interpreter.api.OutputHandler
import org.carbonateresearch.picta.Chart
import os.Path
import ujson.Value
import upickle.default.transform

object Html {
  /** this is the plotly.min.js script that is used to render the plots */
  private val plotlyJs: String = readFile("plotly.min.js")
  private val macyJs: String = readFile("macy.min.js")
  private val requireJs: String = readFile("require.min.js")
  private val cssStyle: String = readFile("style.css")
  private val useCDN: Boolean = testNetworkConnection()

  /** A function to read files from the src resources folder */
  private def readFile(file_name: String): String = {
    val is = getClass.getClassLoader.getResourceAsStream(file_name)
    scala.io.Source.fromInputStream(is).mkString
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

  /**
   * This sets the charts to be inline inside a Jupyter notebook.
   *
   * @param publish (implicit): required to render the HTML in the almond notebook.
   * @param kernel  (implicit): required to interact with the underlying Jupyter kernel instance.
   */
  def initNotebook()(implicit publish: OutputHandler, kernel: JupyterApi): Unit = {
    kernel.silent(true)

    var html = new StringBuilder()

    html ++=
      s"""
         |<script> $requireJs </script>
         |<script>
         | require.config({
         |   paths: {
         |     'plotly': "https://cdn.plot.ly/plotly-latest.min",
         |     'macy': "https://cdn.jsdelivr.net/npm/macy@2.5.1/dist/macy.min"
         |   },
         | })
         |require( ['plotly', 'macy'], function(Plotly, Macy) {
         | window.Plotly = Plotly;
         | window.Macy = Macy;
         |})
         |</script>
         |""".stripMargin

    publish.html(html.toString)
  }

    /** Creates the necessary headers when plotting in jvm mode */
  private def createHeader(useCDN: Boolean, includeStyle: Boolean) = {
    var header = new StringBuilder()

    useCDN match {
      case true =>
        header ++= s"""<script src="https://cdn.plot.ly/plotly-latest.min.js"></script> \n"""
      case false =>
        header ++= s"""<script> ${plotlyJs} </script> \n"""
    }

    header ++= s"""<script> ${macyJs} </script> \n"""

    includeStyle match {
      case true => header ++= s"""<style>$cssStyle</style> \n"""
      case false => ()
    }

    s"""<head> \n""" + header.mkString + s"""\n</head>\n"""
  }

  private def createGridHTML(rows: Int, columns: Int, grid: Array[Chart], id: String) = {
    var html = new StringBuilder()

    html ++= s"""<div id="grid_${id}" align="center"> \n"""

    /** Build body of the page */
    for (i <- 0 until rows) {
      for (j <- 0 until columns) {
        val chart = grid(i * columns + j)
        val graph_id = chart.id

        if (chart.animated) {
          html ++=
            s"""
               |<div align="center">
               |  <div id='graph_${graph_id}' class="graph"></div>
               |  <div class='animationInterface'>
               |    <button id='play_${graph_id}'>Play</button>
               |    <button id='pause_${graph_id}'>Pause</button>
               |    <div id="sliderContainer_${graph_id}" class='progressBar'></div>
               |    <div id='counterContainer_${graph_id}' class='inline-div'>
               |        <h3>Frame: </h3> <h3 id="value_${graph_id}">0</h3>
               |    </div>
               |   </div>
               |</div> \n
               |""".stripMargin
        }
        else html ++= s"""<div id='graph_${graph_id}' class='graph'></div> \n"""
      }
    }

    html ++= s"""</div> \n"""

    html.mkString
  }


  private[picta] def plotChart(rows: Int, cols: Int, grid: Array[Chart], id: String) = {
    var html = new StringBuilder()

    /* create the html headers and add them to the page */
    html ++= createHeader(useCDN, true)

    /* create the grid, with the individual chart html inside. This takes into account whether the chart is animated or not */
    html ++= createGridHTML(rows, cols, grid, id)

    /* create the javascript that corresponds to each of these charts */
    html ++= createJsScripts(rows, cols, grid, id)

    writeHTMLToFile(html.toString, id)
  }

  private[picta] def plotChartInline(rows: Int, columns: Int, grid: Array[Chart], id: String)(implicit publish: OutputHandler): Unit = {
    var html = new StringBuilder()

    /* create the grid, with the individual chart html inside. This takes into account whether the chart is animated or not */
    html ++= createGridHTML(rows, columns, grid, id)

    /* create the javascript that corresponds to each of these charts */
    html ++= createJsScripts(rows, columns, grid, id)

    publish.html(html.toString)
  }

  private def createJsScripts(rows: Int, columns: Int, grid: Array[Chart], id: String) = {
    var html = new StringBuilder()

    /** create js for this batch */
    html ++= s"""<script>"""

    val masonry_js =
      s"""
         |var masonry = new Macy({
         |    container: '#grid_${id}',
         |    debug: true,
         |    mobileFirst: true,
         |    columns: ${columns},
         |  });
         |""".stripMargin

    html ++= masonry_js

    /** Now construct the relevant js for each chart */
    for (i <- 0 until rows) {
      for (j <- 0 until columns) {
        val chart = grid(i * columns + j)
        val graph_id = chart.id

        if (chart.animated) {
          val traces: Value = transform(List(chart.data_(0))).to(Value)
          val frames: Value = chart.frames
          val labels: Value = transform(chart.labels).to(Value)
          val layout: Value = chart.layout_
          val transition_duration = chart.transition_duration
          val config: Value = chart.config_

          html ++= createAnimationHTML(traces: Value, frames: Value, labels: Value, layout: Value, config=config,
            transition_duration: Int, graph_id: String)
        }

        else {
          val traces: Value = transform(chart.data_).to(Value)
          val layout: Value = chart.layout_
          val config: Value = chart.config_

          html ++=
            s"""
               | var traces_${graph_id} = ${traces};
               | var layout_${graph_id} = ${layout};
               | var config_${graph_id} = ${config};
               | Plotly.newPlot("graph_${graph_id}", traces_${graph_id}, layout_${graph_id}, config_${graph_id});
               |""".stripMargin
        }
      }
    }

    html ++= s"""</script> \n"""
    html.mkString
  }

  private def createAnimationHTML(traces: Value, frames: Value, labels: Value, layout: Value, config: Value,
                          transition_duration: Int, graph_id: String): String = {

    s"""
       |var graph_${graph_id} = document.getElementById('graph_${graph_id}')
       |var traces_${graph_id} = $traces
       |var layout_${graph_id} = $layout
       |var frames_${graph_id} = $frames
       |var labels_${graph_id} = $labels
       |var duration_${graph_id} = $transition_duration
       |var config_${graph_id} = $config
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
       |Plotly.newPlot(graph_${graph_id}, traces_${graph_id}, layout_${graph_id}, config_${graph_id})
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
}