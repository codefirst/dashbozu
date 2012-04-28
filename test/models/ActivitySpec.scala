package models

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import java.util._
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
}
