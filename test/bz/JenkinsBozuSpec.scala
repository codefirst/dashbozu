package bz

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.Play.current

class JenkinsBozuSpec extends Specification {
  "an empty url" should {
    "return List()" in {
      val activities = (new JenkinsBozu).get(Map())
      print(activities)
      activities must_== List()
    }
  }
}
