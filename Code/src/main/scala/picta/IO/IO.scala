package picta.IO

import java.io.File
import com.github.tototoshi.csv._
import scala.collection.mutable

/** TODO - need to make sure working directory is also used */
object IO {
  def get_wd: String = os.pwd.toString

  def read_csv(wd: String, filepath: String): mutable.Map[String, List[Any]] = {
    val reader = CSVReader.open(new File(filepath)).allWithHeaders()
    val headers = reader(0).keys
    var map: mutable.Map[String, List[Any]] = collection.mutable.Map()
    headers.foreach(h => map += (h -> Nil))
    reader.foreach(line => {
      line.foreach(x => { map(x._1) = x._2 :: map(x._1) })
    })
    map
  }
}
