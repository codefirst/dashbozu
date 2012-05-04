package bz

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.Play.current

class BozuSpec extends Specification {
  "hash" should {
    "return 32 length string from 0-indexed hash" in {
      val hash = Bozu.hash("a") // 0-indexed 32bit char
      hash.length must_==  32
    }

    "return 32 length string from non 0-indexed hash" in {
      val hash = Bozu.hash("b") // non 0-indexed 32bit char
      hash.length must_==  32
    }
  }
}
