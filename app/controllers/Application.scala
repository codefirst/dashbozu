package controllers

import play.api._
import play.api.mvc._
import models._
import bz._
import java.net.URI

object Application extends Controller {

  def index = Action {
    val activities =
      (((new JenkinsBozu).get(Map(("url",Seq("http://dev.codefirst.org/jenkins/job/dashbozu/api/xml?depth=2"))))) ++
      ((new RedmineBozu).get(Map(("url",Seq("https://codefirst.org/redmine/activity.atom?key=sSZ7xhAzqkKeaTHiicSOwuPI5DvUFGZ1smQRQQpC")))))).sortWith((x,y) => x.createdAt.getTime > y.createdAt.getTime)
      
    // val activities = List(
    //   // TODO remove mock
    //   Activity(
    //     id = "https://codefirst.org/redmine/issues/935#change-2142",
    //     title = "機能 #935 (担当): 検索機能の高速化",
    //     body = "igo-ruby + ipa 辞書で実装中<br />実測でDBの使用容量が2倍、検索速度が1.5倍<br /><a class='https://codefirst.org/redmine/users/6'>下村/翔</a>",
    //     createdAt = null,
    //     source = "redmine",
    //     project = "AsakusaSatellite",
    //     url = Some(new URI("https://codefirst.org/redmine/issues/935#change-2142")),
    //     iconUrl = Some(new URI("http://dashbozu.herokuapp.com/assets/images/icons/redmine-shimo.png"))
    //   ),
    //   Activity(
    //     id = "https://codefirst.org/redmine/issues/505#change-2141",
    //     title = "機能 #505 (却下): Skypeブリッジ",
    //     body = " Skype はオワコンなのでとりあえず却下します<br />",
    //     createdAt = null,
    //     source = "redmine",
    //     project = "AsakusaSatellite",
    //     url = Some(new URI("https://codefirst.org/redmine/issues/505#change-2141")),
    //     iconUrl = Some(new URI("http://dashbozu.herokuapp.com/assets/images/icons/redmine-shimo.png"))
    //   ),
    //   Activity(
    //     id = "https://codefirst.org/redmine/issues/505#change-2141",
    //     title = "ビルド #599 (2012/04/25 23:41:54)",
    //     body = " Changes <ol>  <li>Removed: img border on IE refs #1125 (detail / redmineweb)</li> </ol> リモートホスト127.0.0.1が実行(git)",
    //     createdAt = null,
    //     source = "jenkins",
    //     project = "AsakusaSatellite",
    //     url = Some(new URI("https://codefirst.org/redmine/issues/505#change-2141")),
    //     iconUrl = Some(new URI("http://dashbozu.herokuapp.com/assets/images/icons/jenkins-success.png"))
    //   ),
    //   Activity(
    //     id = "https://codefirst.org/redmine/issues/505#change-2141",
    //     title = "ビルド #65 (2012/04/25 22:18:57)",
    //     body = "Changes <ol> <li>テスト整備 refs #1116 (detail)</li> <li>push/pullに--forceオプションんを追加 refs #1116 (detail)</li> </ol> リモートホスト127.0.0.1が実行(git)",
    //     createdAt = null,
    //     source = "jenkins",
    //     project = "AtsutaKatze",
    //     url = Some(new URI("https://codefirst.org/redmine/issues/505#change-2141")),
    //     iconUrl = Some(new URI("http://dashbozu.herokuapp.com/assets/images/icons/jenkins-success.png"))
    //   ),
    //   Activity(
    //     id = "76b91763",
    //     title = "commit 76b91763",
    //     body = "bugfix: disable redmine ticket link",
    //     createdAt = null,
    //     source = "git",
    //     project = "AsakusaSatellite",
    //     url = Some(new URI("https://codefirst.org/redmine/issues/505#change-2141")),
    //     iconUrl = Some(new URI("http://dashbozu.herokuapp.com/assets/images/icons/redmine-shimo.png"))
    //   )
    // )
    Ok(views.html.index(activities.toList))
  }

  def hookPost(name : String) = Action { request =>
    Bozu(name) map { bz =>
      request.body match {
        case AnyContentAsFormUrlEncoded(body) => Bozu(name) map { bz =>
          bz.get(body).foreach {
            ActivityPusher.publish(_)
          }
        }
        case _ => {}
      }
      Ok("ok")
    } getOrElse {
      BadRequest("no such hook: " + name)
    }
  }

  def hookGet(name : String) = Action { request =>
    Bozu(name) map { bz =>
      bz.get(request.queryString).foreach {
        ActivityPusher.publish(_)
      }
      Ok("ok")
    } getOrElse {
      BadRequest("no such hook: " + name)
    }
  }
}
