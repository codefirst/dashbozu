package bz

import models._
import java.security.MessageDigest

trait Bozu {
  def get(params : Map[String, Seq[String]]) : Seq[Activity]
}

object Bozu {
  def apply(name : String) : Option[Bozu] =
    name match {
      case "jenkins" =>
        Some(new JenkinsBozu)
      case "redmine" =>
        Some(new RedmineBozu)
      case "git" =>
        Some(new GitBozu)
      case "heroku" =>
        Some(new HerokuBozu)
      case "travisci" =>
        Some(new TravisCIBozu)
      case "github" =>
        Some(new GitHubBozu)
      case "newrelic" =>
        Some(new NewRelicBozu)
      case "webhook" =>
        Some(new WebhookBozu)
      case _ =>
        None
    }

  def hash(s: String) : String = {
    val m = java.security.MessageDigest.getInstance("MD5")
    val b = s.getBytes("UTF-8")
    m.update(b, 0, b.length)
    val h = new java.math.BigInteger(1, m.digest()).toString(16)
    "0" * (32 - h.length) + h
  }

  def getIcon(emailaddress : String, size : Int = 80) : String = {
    "http://www.gravatar.com/avatar/"+hash(emailaddress)+"?s="+size.toString
  }
}
