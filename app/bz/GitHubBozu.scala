package bz
import java.net.URI
import models._
import scala.util.parsing.json.JSON
import play.api._
import play.api.mvc._

class GitHubBozu extends Bozu {
  case class Entry(id : String, title : String, author : (String, URI), message : String, url : String)

  private val log = Logger(getClass())

  private def projectName(json : Map[String,Any]) : String = {
    val repoMap:Map[String, String] = json.get("repository").get.asInstanceOf[Map[String, String]]
    repoMap.get("name").getOrElse("unknown project")
  }

  private def getStr(map : Map[String, Any], key : String) : String =
    map.getOrElse(key, "").asInstanceOf[String]

  private def getNum(map : Map[String, Any], key : String) : Double =
    map.getOrElse(key, 0).asInstanceOf[Double]

  private def onPushEvent(json : Map[String, Any]) : Entry = {
    // push event has old style payload
    val id  = getStr(json, "after")
    val ref = getStr(json, "ref")

    val commits = json.getOrElse("commits", Map()).asInstanceOf[List[Map[String, String]]]

    val lastCommit = commits.last
    var url = getStr(lastCommit, "url")

    val author = lastCommit.getOrElse("author", Map()).asInstanceOf[Map[String, String]]

    val message:String = commits.map{ x =>
      x.getOrElse("message", "-")
    }.mkString("<br>")

    Entry(
      id      = id,
      url     = url,
      title   = "commit " + id.take(8) + " to " + ref.split("/").last,
      author  = (author.getOrElse("name", "-"), new URI(Bozu.getIcon(author.getOrElse("email","-")))),
      message = message)
  }

  private def onGenericEvent(json : Map[String, Any]) : Entry = {
    if( json.contains("comment") ) {
      val comment = json.getOrElse("comment", Map()).asInstanceOf[Map[String, Any]]
      val user  = comment.getOrElse("user",Map()).asInstanceOf[Map[String, String]]
      val issue = json.getOrElse("issue", Map()).asInstanceOf[Map[String, Any]]

      Entry(
        id      = getNum(comment, "id").toInt.toString,
        url     = getStr(issue, "html_url"),
        title   = "comment " + getStr(json, "action") + ": " + getStr(issue, "title"),
        author  = (getStr(user, "login"), new URI(getStr(user, "avatar_url"))),
        message = getStr(comment, "body"))
    } else if( json.contains("issue") ) {
      val issue = json.getOrElse("issue", Map()).asInstanceOf[Map[String, Any]]
      val user  = issue.getOrElse("user",Map()).asInstanceOf[Map[String, String]]

      Entry(
        id      = getNum(issue, "id").toInt.toString,
        url     = getStr(issue, "html_url"),
        title   = "issue " + getStr(json, "action") + ": " + getStr(issue, "title"),
        author  = (getStr(user, "login"), new URI(getStr(user, "avatar_url"))),
        message = getStr(issue, "body"))
    } else if ( json.contains("pull_request") ) {
      val pull_req = json.getOrElse("pull_request", Map()).asInstanceOf[Map[String, Any]]
      val user  = pull_req.getOrElse("user",Map()).asInstanceOf[Map[String, String]]

      Entry(
        id      = getNum(pull_req, "id").toInt.toString,
        url     = getStr(pull_req, "html_url"),
        title   = "pull request " + getStr(json, "action") + ": " + getStr(pull_req, "title"),
        author  = (getStr(user, "login"), new URI(getStr(user, "avatar_url"))),
        message = getStr(pull_req, "body"))
    } else {
      null
    }
  }


  def get(params : Map[String, Seq[String]]) : Seq[Activity] = {
    val jsonString:String = params("payload").head
    log.warn(jsonString)
    val json:Option[Any] = JSON.parseFull(jsonString)
    val map:Map[String,Any] = json.get.asInstanceOf[Map[String, Any]]

    val entry = if(map.contains("after")) {
      onPushEvent(map)
    } else {
      onGenericEvent(map)
    }

    val (name, icon) = entry.author

    Seq(Activity(
      id        = "github-" + entry.id,
      title     = entry.title,
      body      = entry.message,
      createdAt = new java.util.Date(),
      source    = "github",
      project   = projectName(map),
      url       = Some(new URI(entry.url)),
      iconUrl   = Some(icon),
      author    = Some(name),
      status    = Info
    ))
  }
}
