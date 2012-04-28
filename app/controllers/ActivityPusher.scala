package controllers
import lib.pusher.Pusher
import play.api._
import play.api.mvc._
import play.api.Play.current
import models.Activity
import java.net.URI
import dispatch.json._


object ActivityPusher {
  for {
    id <- Play.configuration.getString("pusher.id")
    key <- Play.configuration.getString("pusher.key")
    secret <- Play.configuration.getString("pusher.secret")
  } Pusher.init(id, key, secret)

  val act = Activity(
    id = "https://codefirst.org/redmine/issues/935#change-2142",
    title = "機能 #935 (担当): 検索機能の高速化",
    body = "igo-ruby + ipa 辞書で実装中<br />実測でDBの使用容量が2倍、検索速度が1.5倍<br /><a class='https://codefirst.org/redmine/users/6'>下村/翔</a>",
    createdAt = new java.util.Date(),
    source = "jenkins",
    project = "AsakusaSatellite",
    url = Some(new URI("https://codefirst.org/redmine/issues/935#change-2142")),
    iconUrl = Some(new URI("http://dashbozu.herokuapp.com/assets/images/icons/redmine.png"))
  )


  def publish(activity : Activity) = {
    val html =
      views.html.activity(activity)

    val json =
      JsObject(Map(
        JsString("html") -> JsString(html)
      ))
    println(Pusher.triggerPush("activity", "new", json.toString))
  }
}
