import org.carbonateresearch.picta._
import org.carbonateresearch.picta.XY
import org.carbonateresearch.picta.options.{Subplot, XAxis, YAxis}
import org.carbonateresearch.picta.options.ColorOptions.Color


object Example extends App {

  def createXYSeries[T: Color]
  (numberToCreate: Int, count: Int = 0, length: Int = 10): List[XYSeries[Int, Double, T, T]] = {

    if (count == numberToCreate) Nil
    else {
      val xs = List.range(0, length)
      val ys = xs.map(x => scala.util.Random.nextDouble() * x)
      val trace = XY(x = xs, y = ys, name = "trace" + count)
      trace :: createXYSeries(numberToCreate, count + 1, length)
    }
  }

  val grid = Subplot(1, 2)

  grid(0, 0) = Chart() addSeries XY(List(1,2,3), List(3, 5, 6))

  val xaxis = XAxis(title = "X Variable", range = (0.0, 10.0))
  val yaxis = YAxis(title = "Y Variable", range = (0.0, 10.0))
  val layout1 = Layout("Animation.XY", height=300, width=300) setAxes(xaxis, yaxis)
  val layout2 = Layout("Animation.XY", height=300, width=300) setAxes(xaxis, yaxis)
  val data = createXYSeries(numberToCreate = 50, length = 30)
  val chart = Chart(animated = true) setLayout layout1 addSeries data
  val chart2 = Chart(animated = true) setLayout layout2 addSeries data

  grid(0, 0) = chart
  grid(0, 1) = chart2

  val canvas = Canvas(1, 2)

  canvas.plot()

}
