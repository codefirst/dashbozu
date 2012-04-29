package models

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.Play.current
import java.util.Date
import java.net._

class ActivitySpec extends Specification {
  val activity = Activity("id0001",
                          "title0001",
                          "body0001",
                          new Date(),
                          "source0001",
                          "project0001",
                          Some(new URI("http://example.com/hoge")),
                          Some(new URI("http://example.com/icon.png")))

  "a activity" should {
    "have id 'id0001'" in {
      activity.id must_== "id0001"
    }
  }

  def testDatabase(name: String = "default"): Map[String, String] = {
    Map(
      ("db." + name + ".driver") -> "org.h2.Driver",
      ("db." + name + ".url") -> ("jdbc:h2:mem:play-test-"+scala.util.Random.nextInt+";MODE=PostgreSQL;IGNORECASE=true")
    )
  }
  "model" should {
    "findAll" in {
      running(FakeApplication(additionalConfiguration = testDatabase())) {
        ActivityDB.findAll must_== List()
      }
    }
    "be inserted" in {
      running(FakeApplication(additionalConfiguration = testDatabase())) {
        ActivityDB.addAll(List(activity)) must_== Some(1)
        ActivityDB.findAll.toSet must_== Set(activity)
      }
    }
    "be inserted only if not in DB" in {
      running(FakeApplication(additionalConfiguration = testDatabase())) {
        val a2 = activity.copy(id="id0002")
        ActivityDB.addAll(List(activity, a2))// must_== Some(1)
        ActivityDB.findAll.toSet must_== Set(activity, a2)
      }
    }
  }
}
