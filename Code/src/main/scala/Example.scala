import org.carbonateresearch.picta._


object Example extends App {


  val series = XY(List(1, 2, 3), List(4, 3 ,5), series_type = SCATTER, series_mode = MARKERS)
  val chart = Chart() setData series setLayout picta.Layout("XY.Scatter.Int")
  if (plotFlag) chart.plot


}
