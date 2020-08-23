package org.carbonateresearch.picta

import org.carbonateresearch.picta.options._
import org.carbonateresearch.picta.UnitTestUtils._
import org.carbonateresearch.picta.conus.Utils.getDataFromSingleModel

import org.scalatest.funsuite.AnyFunSuite
import org.carbonateresearch.conus._
import org.carbonateresearch.conus.common.SingleModelResults

/**
 * This tests the CoNuS model integration. It runs a simple rat population model
 */
class ConusTests extends AnyFunSuite {

  val plotFlag = false

  ignore("Conus.2DCategory") {
    val simulator = new BasicSimulator
    val nbRats:ModelVariable[Int] = ModelVariable("Number of Rats",2,"Individuals") //Notice this is an Int
    val deathRate:ModelVariable[Double] = ModelVariable("Death rate",0.0,"%")

    // Let's initialise a few model conditions
    val numberOfSteps = 10

    // And let's create a function that, given a rat population and a deathRate, calculates a new population

    def survivingRats(initialPopulation:Int, deathRate:Double): Int = {
      initialPopulation-math.floor(initialPopulation.toDouble*deathRate).toInt
    }

    val ratPopulation = new SteppedModel(numberOfSteps,"Simplified rat population dynamics")
      .setGrid(3,3) // 9 cells
      .defineMathematicalModel( // In this super simple model we do only two things at each step
        deathRate =>> {(s:Step) => scala.util.Random.nextDouble()*0.9}, // calculate a death rate
        nbRats =>> {(s:Step) => {survivingRats(nbRats(s-1)+(nbRats(s-1)/2*10),deathRate(s))}} // calcuate the nb rats
      )
      .defineInitialModelConditions( // Now we need to determine the inital size of the population at each model grid
        PerCell(nbRats,List(
          (List(2),Seq(0,0)),
          (List(2),Seq(0,1)),
          (List(4),Seq(0,2)),
          (List(4),Seq(1,0)),
          (List(2),Seq(1,1)),
          (List(6),Seq(1,2)),
          (List(2),Seq(2,0)),
          (List(4),Seq(2,1)),
          (List(6),Seq(2,2)))))

    simulator.evaluate(ratPopulation)
    Thread.sleep(1000)

    // grab the results from the Conus model
    val model: SingleModelResults = simulator(ratPopulation)(0)

    val generation = (0 until numberOfSteps-1).map(x=>x.toDouble).toList

    // we can use the utility function to grab the series for a single variable
    val deathRateSeries: List[Double] = getDataFromSingleModel(model, deathRate, List(0,0), numberOfSteps)

    val xy1 = XY(generation, deathRateSeries) setName("Death rate")

    // alternatively we can quickly get the same data for XY using the function below
    //  val xy1 = getXYSeriesFromSingleModel(model, (age, d18Occ), List(0), numberOfSteps)

    // lets also plot a second y variable
    val yaxis2 = Axis(
      Y,
      position = 2,
      title = "Nb rats",
      overlaying = Axis(Y), // this ensures that the axis sits on a seperate axis
      side = RIGHT_SIDE, // this ensures the axis is on the right hand side
      tickformat = "0.0f" // this will keep formatting reasonable for display purposes
    )

    // we construct the second y variable;
    val nbRatsSeries: List[Double] = getDataFromSingleModel(model, nbRats, List(0,0), numberOfSteps).map(x => x.toDouble)
    val xy2 = XY(generation, nbRatsSeries) setAxis yaxis2 setName("Nb of rats")

    // finally we can combine in a single chart
    val chart = (
      Chart()
        addSeries xy1
        addSeries xy2
        setTitle("Date rate vs nb of rats per generation for cell (0,0)")
        addAxes(Axis(X, title="Generation"), Axis(Y, title="Death rate"), yaxis2)
      )

    val canvas = Canvas() addCharts chart

    assert(validateJson(chart.serialize.toString))

    // When we plot the result, we can see the legend is in the wrong place and overlaying the axis - we can overcome this
    // in the next example
    if (plotFlag) canvas.plot

    // finally we can combine in a single chart
    val chart2 = (
      Chart()
        addSeries xy1
        addSeries xy2
        setTitle("Death rate vs Nb of rats for cell (0,0)")
        addAxes(Axis(X, title="Generation"), Axis(Y, title="Death rate"), yaxis2)
        setLegend(x = 0.5, y = -0.5, orientation = HORIZONTAL, xanchor = AUTO, yanchor = AUTO)
      )

    val canvas2 = Canvas() addCharts chart2

    if (plotFlag) canvas2.plot

    assert(validateJson(chart2.serialize.toString))

    val xaxis = Axis(X, title = "Generation") setLimits (0.0, 9.0)
    val yaxis = Axis(Y, title = "Nb of rats") setLimits (0.0, 10000.0)
    val animation: List[XY[Double, Double, Double, Double]] = (0 to generation.size-1).map(x => XY(generation.take(x+1), nbRatsSeries.take(x+1)) setName "nbRats").toList

    // we can also specifiy the underlying layout directly - sometimes this can be useful
    val layout = ChartLayout("Animation XY") setAxes(xaxis, yaxis)

    val chart3 = Chart(animated = true, transition_duration=100) setChartLayout layout addSeries animation

    if (plotFlag) chart3.plot


    assert(validateJson(chart3.serialize.toString))

    val nbCol = (0 to 2).toList
    val mySeries:List[Double] = nbCol.flatMap(r => {
      nbCol.map{c => getDataFromSingleModel(model, nbRats, List(r,c), numberOfSteps).last.toDouble}})

    val series = XYZ(z=mySeries,n=3) asType HEATMAP

    val chart4 = Chart().addSeries(series).setTitle("Nb of rats at time step 10")

    if (plotFlag) chart4.plot

    assert(validateJson(chart4.serialize.toString))

    def createSeries:List[List[Double]] = {
      val nbCol = (0 to 2).toList
      val nestedList:List[List[Double]] = nbCol.flatMap(r => {
        nbCol.map{c => getDataFromSingleModel(model, nbRats, List(r,c), numberOfSteps).map(x=>x.toDouble)}})

      (0 to 9).map(x => (0 to 8).map(y => nestedList(y)(x)).toList).toList
    }

    val ratsAsSurface = createSeries.map(s => XYZ(z=s,n=3) asType SURFACE setColorBar("Rat Population", RIGHT_SIDE))

    val ratsChart = Chart(animated = true, transition_duration=100) addSeries ratsAsSurface setTitle "Surface"

    if (plotFlag) ratsChart.plot

    assert(validateJson(ratsChart.serialize.toString))
  }
}

