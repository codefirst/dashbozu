package bz

import models._

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
      case _ =>
        None
    }
}
