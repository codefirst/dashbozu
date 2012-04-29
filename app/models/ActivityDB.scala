package models

import play.api._
import play.api.db._
import play.api.Play.current
import org.scalaquery.session._
import org.scalaquery.ql._
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql.basic.{ BasicTable => Table }
import org.scalaquery.ql.basic.BasicDriver.Implicit._
import org.scalaquery.ql.basic.BasicProfile
import org.scalaquery.ql.basic.BasicInsertInvoker
import org.scalaquery.ql.extended.PostgresDriver.Implicit._
import org.scalaquery.ql.extended.H2Driver.Implicit._

import java.net.URI
import java.util.{ Date => UtilDate }
import java.sql.{ Timestamp => SQLTimestamp }

object ActivityDB extends Table[Activity]("ACTIVITY") with Subscribe[Activity] {
  import ActivityMapper._

  def id        = column[String]("ID", O.PrimaryKey)
  def title     = column[String]("TITLE", O.NotNull)
  def body      = column[String]("BODY", O.NotNull)
  def createdAt = column[UtilDate]("CREATED_AT", O.NotNull)
  def source    = column[String]("SOURCE", O.NotNull)
  def project   = column[String]("PROJECT", O.NotNull)
  def url       = column[Option[URI]]("URL")
  def iconUrl   = column[Option[URI]]("ICON_URL")
  def * = id~title~body~createdAt~source~project~url~iconUrl <> (Activity, Activity.unapply _)

  def db = Database.forDataSource(DB.getDataSource())

  def findAll(limit:Int=0) : List[Activity] = db.withSession { implicit db : Session =>
    val l = if (limit == 0) Play.configuration.getInt("activity.fetch_limit").getOrElse(20)
            else limit
    (for (t <- this; _ <- Query orderBy Ordering.Desc(t.createdAt)) yield t.*).take(l).list
  }

  def tee[A]( x : A)(action : A => Unit) : A = {
    action(x)
    x
  }

  def addAll(as : Seq[Activity]) : Option[Int] = db.withSession { implicit db : Session =>
    println(as)
    val ys = ActivityDB.where( x => x.id inSet as.map(_.id)).map(_.id).list
    val zs = as filterNot ( (a : Activity) => ys.contains(a.id) )
    tee(ActivityDB.insertAll(zs:_*)) { _ =>
      zs.foreach( z => notify(z))
    }
  }
}

