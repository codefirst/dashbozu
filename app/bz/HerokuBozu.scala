package bz

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
      url       = params("url").headOption.map(new java.net.URI(_)),
      iconUrl   = None,
      author    = params("user").headOption,
      status    = Success
    ))
  }
}
