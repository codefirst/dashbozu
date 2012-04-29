package models
import scala.collection.mutable._

trait Subscribe[T] {
  type action = T => Unit
  private val subscribers =
    ListBuffer[action]()

  def subscribe(f : action) {
    subscribers += f
  }

  def notify(x : T) {
    subscribers.foreach(_.apply(x))
  }
}
