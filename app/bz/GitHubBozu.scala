package bz
import java.net.URI
import models._
import scala.util.parsing.json.JSON

class GitHubBozu extends Bozu {
  def get(params : Map[String, Seq[String]]) : Seq[Activity] = {

    val jsonString:String = params("payload").head
    val json:Option[Any] = JSON.parseFull(jsonString)
    val map:Map[String,Any] = json.get.asInstanceOf[Map[String, Any]]
    val id:String= map.get("after").get.asInstanceOf[String]
    val ref:String= map.get("ref").get.asInstanceOf[String]

    val repoMap:Map[String,Any] = map.get("repository").get.asInstanceOf[Map[String, Any]]
    val projectName:String = repoMap.get("name").get.asInstanceOf[String]

    val commits = map.get("commits").get.asInstanceOf[List[Any]]
    val lastCommit = commits.last.asInstanceOf[Map[String, Any]]
    var url = lastCommit.get("url").get.asInstanceOf[String]

    val author = lastCommit.get("author").get.asInstanceOf[Map[String, Any]]
    val committerEmail:String = author.get("email").get.asInstanceOf[String]
    val committerName:String = author.get("name").get.asInstanceOf[String]

    val message:String = commits.map((x:Any) =>
          x.asInstanceOf[Map[Any, String]].get("message").get.asInstanceOf[String]
        ).mkString("<br>")

    Seq(Activity(
      id        = "github-" + id,
      title     = "commit " + id.take(8) + " to " + ref.split("/").last,
      body      = message,
      createdAt = new java.util.Date(),
      source    = "github",
      project   = projectName,
      url       = Some(new URI(url)),
      iconUrl   = Some(new URI(Bozu.getIcon(committerEmail).toString)),
      author    = Some(committerName),
      status    = Info
    ))
  }
}
