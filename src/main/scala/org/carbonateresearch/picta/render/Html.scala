package org.carbonateresearch.picta.render

import java.io.{BufferedWriter, File, FileWriter}
import java.net.{HttpURLConnection, URL}

import almond.api.JupyterApi
import almond.interpreter.api.OutputHandler
import org.carbonateresearch.picta.Chart
import org.carbonateresearch.picta.OptionWrapper.{Opt, Blank}
import os.Path
import ujson.Value
import upickle.default.transform

/** this object provides methods that generate the HTML that the user sees */
object Html {
  private val plotlyJs: String = readFile("plotly.min.js")
  private val macyJs: String = readFile("macy.min.js")
  private val domToImageJs: String = readFile("dom-to-image.min.js")
  private val requireJs: String = readFile("require.min.js")

  private val cssStyle: String = readFile("style.css")
  private val useCDN: Boolean = testNetworkConnection()

  /** A function to read files from the src resources folder
   *
   * @param file_name: A string that denotes the name of the file in the resources folder.
   * @return: A string containing the contents of the file.
   */
  private def readFile(file_name: String): String = {
    val is = getClass.getClassLoader.getResourceAsStream(file_name)
    scala.io.Source.fromInputStream(is).mkString
  }

  /** This function checks if an active network connection is available. It returns true if this is the case, false
   *  otherwise.
   *
   * @return: A boolean that indicates whether an active internet connection is available
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

  /** This initializes the library to work with an Almond Jupyter Kernel.
   *
   * @param publish: required to render the HTML in the almond notebook.
   * @param kernel: required to interact with the underlying Jupyter kernel instance.
   */
  def initNotebook()(implicit publish: OutputHandler, kernel: JupyterApi): Unit = {
    kernel.silent(true)

//    ${ if (rows > 1 || cols > 1) "\n .svg-container {width: 100% !important;} \n"}
//    ${ if (rows > 1 || cols > 1) "\n .main-svg {width: 100% !important;} \n"}

    var html = new StringBuilder()

    html ++=
      s"""
         |<style> $cssStyle </style>
         |<script> $requireJs </script>
         |<script> $domToImageJs </script>
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

  /** This function creates the necessary headers when plotting in JVM mode. *
   *
   * @param useCDN: A boolean to specify whether to use a CDN or read the libraries from the resources folder.
   * @param includeStyle: A boolean to specify whether to use the custom CSS file.
   * @return: A string containing the relevant HTML section to be injected into the rendered page.
   */
  private def createHeader(useCDN: Boolean, includeStyle: Boolean, rows: Int, cols: Int): String = {
    var header = new StringBuilder()

    useCDN match {
      case true =>
        header ++= s"""<script src="https://cdnjs.cloudflare.com/ajax/libs/dom-to-image/2.6.0/dom-to-image.min.js"></script> \n"""
        header ++= s"""<script src="https://cdn.plot.ly/plotly-latest.min.js"></script> \n"""
      case false =>
        header ++= s"""<script> $domToImageJs </script>"""
        header ++= s"""<script> ${plotlyJs} </script> \n"""
    }

    header ++= s"""<script> ${macyJs} </script> \n"""

    includeStyle match {
      case true => header ++= s"""<style> $cssStyle </style> \n""".stripMargin
      case false => ()
    }

    s"""<head> \n""" + header.mkString + s"""\n</head>\n"""
  }


  /** A function that generates the HTML grid from the Canvas.
   *
   * @param rows: The number of rows in the Canvas subplot grid.
   * @param cols: The number of columns in the Canvas subplot grid.
   * @param grid: The grid containing the subplots we want to plot.
   * @param id: The id of the canvas.
   * @return
   */
  private def createGridHTML(rows: Int, cols: Int, grid: Array[Chart], id: String, title: Opt[String] = Blank) = {
    var html = new StringBuilder()

    html ++= s"""<div id="grid-container_$id" class="grid-container" align="center"> \n"""

    title.option match {
      case Some(x) => html ++= s"""<div class="grid-title" align="center"> <h1>$x</h1> </div> \n"""
      case _ => ()
    }

    html ++= s"""<div id="grid_${id}" class="grid" align="center"> \n"""

    /** Build body of the page */
    for (i <- 0 until rows) {
      for (j <- 0 until cols) {
        val chart = grid(i * cols + j)
        val graph_id = chart.id

        if (chart.animated) {
          html ++=
            s"""
               |<div align="center">
               |  <div id="graph_${graph_id}" class="graph"></div>
               |  <div class="animationInterface">
               |    <button id="play_${graph_id}" class="picta-button">Play</button>
               |    <button id="pause_${graph_id}" class="picta-button">Pause</button>
               |    <div id="sliderContainer_${graph_id}" class="progressBar"></div>
               |    <div id="counterContainer_${graph_id}" class="inline-div">
               |        <h3>Frame: </h3> <h3 id="value_${graph_id}">0</h3>
               |    </div>
               |   </div>
               |</div> \n
               |""".stripMargin
        }
        else html ++= s"""<div id="graph_${graph_id}" class="graph"></div> \n"""
      }
    }

    html ++= s"""</div> \n""" // for the grid
    html ++= s"""</div> \n""" // for grid-container

    // if we need the download Canvas as PNG button
    if (rows > 1 || cols > 1) {
      html ++=
        s"""
           |<div align="center">
           |<button id = "saveAsPNG_$id" class="picta-button">Download Canvas as PNG</button>
           |</div>
           |""".stripMargin
    }

    html.mkString
  }

  /** Creates the associated javascript for each Canvas.
   *
   * @param rows: Number of rows in the Canvas subplot grid.
   * @param cols: Number of columns in the Canvas subplot.
   * @param grid: The Canvas subplot grid.
   * @param id: The unique id of the Canvas this grid corresponds to.
   * @return
   */
  private def createJsScripts(rows: Int, cols: Int, grid: Array[Chart], id: String) = {
    var html = new StringBuilder()

    /** create js for this batch */
    html ++= s"""<script>"""

    val masonry_js =
      s"""
         |var masonry = new Macy({
         |    container: '#grid_${id}',
         |    debug: true,
         |    mobileFirst: true,
         |    columns: ${cols},
         |    margin: {
         |        x: 0,
         |        y: 0
         |    }
         |  });
         |""".stripMargin

    html ++= masonry_js

    /** Now construct the relevant js for each chart */
    for (i <- 0 until rows) {
      for (j <- 0 until cols) {
        val chart = grid(i * cols + j)
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

    /* if we have a grid with more than one image, we need to allow a Canvas save as PNG button*/
    if (rows > 1 || cols > 1) {
      html ++= s"""
                  |var save = document.getElementById("saveAsPNG_$id")
                  |save.onclick = function() {
                  |    const grid = document.getElementById("grid-container_$id")
                  |    const svg_containers = grid.getElementsByClassName('svg-container')
                  |    const main_svgs = grid.getElementsByClassName('main-svg')
                  |
                  |    const svg_containers_styles = []
                  |    const main_svgs_styles = []
                  |
                  |    const modebars = document.getElementsByClassName("modebar-container")
                  |
                  |    for (var i = 0; i < modebars.length; i++) {
                  |        modebars[i].style.display = "none"
                  |    }
                  |
                  |    domtoimage.toPng(grid)
                  |    .then(function (dataUrl) {
                  |        var img = new Image();
                  |        img.src = dataUrl;
                  |        var link = document.createElement('a');
                  |        link.download = 'Picta-image.png';
                  |        link.href = dataUrl;
                  |        link.click()
                  |    })
                  |    .catch(function (error) {
                  |        console.error('oops, something went wrong!', error);
                  |    });
                  |}
                  |""".stripMargin
    }
    html ++= s"""</script> \n"""
    html.mkString
  }

  /** A function that creates the HTML associated with an animated chart.
   *
   * @param series: A json representation of the data series.
   * @param frames: A json representation of the sequence of frames.
   * @param labels: A json representation of the labels that are associated with each of the frames -
   *              for tracking purposes.
   * @param layout: A json representation of the Layout for the corresponding Canvas.
   * @param config: A json representation of the Config for the corresponding Canvas
   * @param transition_duration: The duration between transitioning frame to frame.
   * @param graph_id: A unique id associated with a particular Chart.
   * @return: A string representation of the HTML.
   */
  private def createAnimationHTML(series: Value, frames: Value, labels: Value, layout: Value, config: Value,
                                  transition_duration: Int, graph_id: String): String = {

    s"""
       |var graph_${graph_id} = document.getElementById('graph_${graph_id}')
       |var traces_${graph_id} = $series
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

  /** This function assembles the various bits of HTML and writes it to an individual page.
   *
   * @param rows: Number of rows in the Canvas subplot grid.
   * @param cols: Number of columns in the Canvas subplot.
   * @param grid: The Canvas subplot grid.
   * @param id: The unique id of the Canvas this grid corresponds to.
   */
  private[picta] def plotChart(rows: Int, cols: Int, grid: Array[Chart], id: String, title: Opt[String] = Blank) = {
    var html = new StringBuilder()

    /* create the html headers and add them to the page */
    html ++= createHeader(useCDN, true, rows, cols)

    /* create the grid, with the individual chart html inside. This takes into account whether the chart is animated or not */
    title.option match {
      case Some(x) => html ++= createGridHTML(rows, cols, grid, id, x)
      case _ => html ++= createGridHTML(rows, cols, grid, id)
    }

    /* create the javascript that corresponds to each of these charts */
    html ++= createJsScripts(rows, cols, grid, id)

    writeHTMLToFile(html.toString, id)
  }

  /** This function assembles the various bits of HTML and writes it to a Jupyter display.
   *
   * @param rows: Number of rows in the Canvas subplot grid.
   * @param cols: Number of columns in the Canvas subplot.
   * @param grid: The Canvas subplot grid.
   * @param id: The unique id of the Canvas this grid corresponds to.
   * @param publish: Implicit parameter that exposes API when Almond kernel is in scope.
   */
  private[picta] def plotChartInline(rows: Int, cols: Int, grid: Array[Chart], id: String, title: Opt[String] = Blank)
                                    (implicit publish: OutputHandler): Unit = {

    var html = new StringBuilder()

    /* create the grid, with the individual chart html inside. This takes into account whether the chart is animated or not */
    title.option match {
      case Some(x) => html ++= createGridHTML(rows, cols, grid, id, x)
      case _ => html ++= createGridHTML(rows, cols, grid, id)
    }

    /* create the javascript that corresponds to each of these charts */
    html ++= createJsScripts(rows, cols, grid, id)

    publish.html(html.toString)
  }
}