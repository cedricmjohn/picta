package conusviz.charts

import almond.interpreter.api.OutputHandler
import conusviz.common.Component

/*
* Interface for Chart
* */
trait Chart extends Component {
  def plot(): Unit
  def plot_inline()(implicit publish: OutputHandler): Unit
}