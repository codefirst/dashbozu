import models._

trait Bozu {
  def get(params : Map[String, String]) : List[Activity]
}
