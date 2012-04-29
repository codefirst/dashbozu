package models

import org.specs2.mutable._

class SubscribeSpec extends Specification {
  class Stub extends Subscribe[Int] {
    def action {
      notify(42)
    }
  }

  "subscriber" should {
    "recive value" in {
      val stub = new Stub
      var ans : Option[Int] = None
      stub.subscribe { x =>
        ans = Some(x)
      }
      stub.action
      ans must_== Some(42)
    }
  }

  "each subscriber" should {
    "recieve value" in {
      val stub = new Stub
      var ans1 : Option[Int] = None
      var ans2 : Option[Int] = None

      stub.subscribe { x =>
        ans1 = Some(x+1)
      }

      stub.subscribe { x =>
        ans2 = Some(x+2)
      }

      stub.action

      ans1 must_== Some(43)
      ans2 must_== Some(44)
    }
  }
}
