package org.carbonateresearch.picta

import java.io.File

import com.github.tototoshi.csv.CSVReader

import scala.collection.mutable

/** Contains helper methods to work with common IO tasks. */
object IO {
  /** this grabs the working directory path as a string */
  def getWorkingDirectory: String = os.pwd.toString

  /** Reads a CSV file (with-headers) into a Map(header -> column of data)
   * @param filepath : A string containing the path of the file that is to be read
   */
  def readCSV(filepath: String): mutable.Map[String, List[String]] = {
    val reader = CSVReader.open(new File(filepath)).allWithHeaders()
    val headers = reader(0).keys
    var map: mutable.Map[String, List[String]] = collection.mutable.Map()
    headers.foreach(h => map += (h -> Nil))
    reader.foreach(line => {
      line.foreach(x => {
        map(x._1) = x._2 :: map(x._1)
      })
    })
    map
  }
}