package bz
import java.net.URI
import models._
import scala.util.parsing.json.JSON

class TravisCIBozu extends Bozu {
  def get(params : Map[String, Seq[String]]) : Seq[Activity] = {

    val jsonString:String = params("payload").head
    val json:Option[Any] = JSON.parseFull(jsonString)
    val map:Map[String,Any] = json.get.asInstanceOf[Map[String, Any]]
    val id:Int= map.get("id").get.asInstanceOf[Double].toInt
    val number:String= map.get("number").get.asInstanceOf[String]
    val statusId:Int= map.get("status").get.asInstanceOf[Double].toInt
    val message:String = map.get("message").get.asInstanceOf[String]
    val repoMap:Map[String,Any] = map.get("repository").get.asInstanceOf[Map[String, Any]]
    val ownerName:String = repoMap.get("owner_name").get.asInstanceOf[String]
    val projectName:String = repoMap.get("name").get.asInstanceOf[String]
    val committerEmail:String = map.get("committer_email").get.asInstanceOf[String]
    val committerName:String = map.get("committer_name").get.asInstanceOf[String]
    val buildURL:String = "http://travis-ci.org/#!/" + ownerName + "/" + projectName + "/builds/" + number

    val status =
      statusId match {
        case 0 =>
          Success
        case _ =>
          Error
      }

    Seq(Activity(
      id        = "travisci-" + id.toString,
      title     = projectName + " - Travis CI #" + number,
      body      = message,
      createdAt = new java.util.Date(),
      source    = "travisci",
      project   = projectName,
      url       = Some(new URI(buildURL)),
      iconUrl   = Some(new URI(Bozu.getIcon(committerEmail).toString)),
      author    = Some(committerName),
      status    = status
    ))
  }
}
