import org.carbonateresearch.picta._


object TestDriveApp extends App {

//  val x = XY(x = 2, series_name = "a") asType PIE


}

//import org.carbonateresearch.picta.conus.Utils._
//import org.carbonateresearch.picta.{Canvas, Chart, XY}
//import org.carbonateresearch.conus.common.SingleModelResults
//import org.carbonateresearch.conus.modelzoo.GeneralGeology._
//import org.carbonateresearch.conus.modelzoo.PasseyHenkesClumpedDiffusionModel._
//import org.carbonateresearch.conus.{AllCells, ModelVariable, PerCell, Simulator, Step, SteppedModel}
//
//import math._


//  val modelWarehouse = Simulator
//  // a few constants
//  val rhocal:Double = 2.71 //Density of carbonates
//  val cOf=889000 //concentration of O in fluid
//  val cCf=200 //concentration of C in fluid
//  val cOcal = 480000 //concentration of O in stoichiometric calcite
//  val cCcal = 120000 //concentration of C in stoichiometric calcite
//  val d13Ctdc = 0 //carbon isotope composition of total dissolved carbon in fluid
//
//  // setting model variables
//  val initialAge:ModelVariable[Double] = ModelVariable("Initial age",96.0,"Ma")
//  val finalAge:ModelVariable[Double] = ModelVariable("Final age",0.0,"Ma")
//  val d18Of:ModelVariable[Double] = ModelVariable("Fluid d18O",-1.0,"‰")
//  val TmaxSample:ModelVariable[Double] = ModelVariable("Tmax",0.0,"˚C")
//  val WFF:ModelVariable[Double] = ModelVariable("Weight fraction of fluid",0.0,"")
//  val CWRR:ModelVariable[Double] = ModelVariable("Cumulative water/rock ratio",0.0,"")
//  val FluidMass:ModelVariable[Double] = ModelVariable("Mass of fluid",0.2,"")
//  val d18Occ:ModelVariable[Double] = ModelVariable("d18Occ",1.0,"‰")
//  val d13Ccc:ModelVariable[Double] = ModelVariable("d13Ccc",1.0,"‰")
//  val D47r:ModelVariable[Double] = ModelVariable("D47 with partial recrystallization",.5,"‰")
//  val fractionRec:ModelVariable[Double] = ModelVariable("Fraction of recrystallization",0.01,"‰")
//  val initalBurialAtModelStart:ModelVariable[Double] = ModelVariable("Initial burial at model start",0.0,"meters")
//
//  // Initialise model conditions as lists
//  val burialHistory = List((96.0,0.0), (39.0,2000.0), (0.0,-70.0))
//  val geothermalGradientHistory= List((96.0,30.0),(0.0,30.0))
//  val surfaceTemperaturesHistory = List((96.0,30.0),(0.0,30.0))
//  val numberOfSteps = 10
//  val ageList:List[Double] = List(97.0,96.0,95.0)
//  val finalAgeList:List[Double] = List(0.0)
//  val rangeOfFluidMasses:List[Double] = List(0.2)
//  val rangeOfPartialRecrystallization = List(1.0,.5,.1,.01,0.0)
//
//  // Bulk isotope calculation equations
//  val d18OccFunction = (s:Step) => {
//    val cO=(WFF(s)*cOf)+(1-WFF(s))*cOcal
//    val alpha = math.exp((18.03*(1000*pow((burialTemperature(s)+273.15), -1)) - 32.42)/1000)
//    val d18O = ((d18Of(s) * WFF(s) * cOf) + d18Occ(s) * (1-WFF(s)) * cOf)/ cO
//    ((d18O * cOf * alpha) - (1000 * cOf * WFF(s) * (1-alpha))) / ((cOcal * (1-WFF(s)) * alpha) + cOf * WFF(s))
//  }
//
//  val d13CccFunction = (s:Step) => {
//    val cC=(WFF(s)*cCf)+(1-WFF(s))*cCcal
//    val alpha = exp((-2.4612+(7666.3/100) - (2.9880*pow(10,3)/pow((burialTemperature(s)+273.15),6)))/1000)
//    val d13C = ((d13Ctdc * WFF(s) * d13Ctdc) + (d13Ccc(s) * (1-WFF(s)) * cCcal))/cC
//    ((d13C * cC * alpha) - (1000 * cCf * WFF(s) * (1-alpha))) / (cCcal * (1-WFF(s) * alpha) + cCf * WFF(s))
//  }
//
//  val eaglefordModel = new SteppedModel(numberOfSteps,"Eagleford recrystallization")
//    .setGrid(19)
//    .defineMathematicalModel(
//      age =>> ageOfStep(initialAge,finalAge),
//      depth =>> {(s:Step) => {burialDepthFromAgeModel(age,burialHistory).apply(s)+initalBurialAtModelStart(s)}},
//      surfaceTemperature =>> surfaceTemperaturesAtAge(age, surfaceTemperaturesHistory),
//      geothermalGradient =>> geothermalGradientAtAge(age,geothermalGradientHistory),
//      burialTemperature =>> burialTemperatureFromGeothermalGradient(surfaceTemperature,depth,geothermalGradient),
//      dT =>> dTFun,
//      D47eq =>> D47eqFun,
//      D47i =>> D47iFun,
//      SampleTemp =>> davies19_T,
//      FluidMass =>> {(s:Step) => 0.2},
//      d18Of =>> {(s:Step) => -1.0},
//      WFF =>> {(s:Step) => FluidMass(s) / (FluidMass(s) + rhocal)},
//      CWRR =>> {(s:Step) => s.stepNumber * WFF(s)/(1-WFF(s))},
//      d18Occ =>> d18OccFunction,
//      d13Ccc =>> d13CccFunction,
//      D47r =>> {(s:Step) => {if(burialTemperature(s)-burialTemperature(s-1) >= 0){D47r(s-1) * (1.0-fractionRec(s)) + D47eq(s) * fractionRec(s)}
//      else {D47r(s-1)}}}
//    )
//    .defineInitialModelConditions(
//      AllCells(initialAge,ageList),
//      AllCells(finalAge,finalAgeList),
//      AllCells(fractionRec,rangeOfPartialRecrystallization),
//      AllCells(D47i,List(0.670,0.680,0.690)),
//      AllCells(D47r,List(0.670,0.680,0.690)),
//      PerCell(initalBurialAtModelStart,List(
//        (List(0.0),Seq(0)),
//        (List(-0.91),Seq(1)),
//        (List(-1.22),Seq(2)),
//        (List(-2.90),Seq(3)),
//        (List(-4.57),Seq(4)),
//        (List(-4.88),Seq(5)),
//        (List(-4.90),Seq(6)),
//        (List(-6.10),Seq(7)),
//        (List(-6.40),Seq(8)),
//        (List(-6.71),Seq(9)),
//        (List(-25.90),Seq(10)),
//        (List(-28.96),Seq(11)),
//        (List(-29.87),Seq(12)),
//        (List(-35.05),Seq(13)),
//        (List(-35.36),Seq(14)),
//        (List(-35.97),Seq(15)),
//        (List(-36.58),Seq(16)),
//        (List(-37.19),Seq(17)),
//        (List(-38.90),Seq(18)))))
//    .defineCalibration(
//      D47r.isBetween(0.511,0.683).atCells(Seq(0)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(1)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(2)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(3)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(4)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(5)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(6)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(7)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(8)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(9)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(10)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(11)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(12)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(13)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(14)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(15)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(16)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(17)),
//      D47r.isBetween(0.511,0.683).atCells(Seq(18))
//    )
//
//  modelWarehouse.evaluate(eaglefordModel)
//  Thread.sleep(1000)
//
////  modelWarehouse.save(eaglefordModel)
//
//  val model: SingleModelResults = modelWarehouse(eaglefordModel)(0)
//
//  val series1: List[Double] = getDataFromSingleModel(model, age, List(0), numberOfSteps)
//
//  val series2: List[Double] = getDataFromSingleModel(model, d18Occ, List(0), numberOfSteps)
//
//  val series = XY(series1, series2)
//
////  val series = getXYSeriesFromSingleModel(model, (age, d18Occ), List(0), numberOfSteps)
//
//  val chart = Chart() addSeries series setTitle("x against y")
//
//  val canvas = Canvas() addCharts chart
//
//  canvas.plot()