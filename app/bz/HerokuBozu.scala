package bz
import java.net.URI
import models._

class HerokuBozu extends Bozu {
  def get(params : Map[String, Seq[String]]) : Seq[Activity] = {
    Seq(Activity(
      id        = "heroku-" + params("head_long").head,
      title     = "Deploying %s".format(params("app").head),
      body      = params("git_log").head,
      createdAt = new java.util.Date(),
      source    = "heroku",
      project   = params("app").head,
      url       = params("url").headOption.map(new URI(_)),
      iconUrl   = params("user").headOption.map((s : String) => new URI(Bozu.getIcon(s))),
      author    = params("user").headOption,
      status    = Success
    ))
  }
}
