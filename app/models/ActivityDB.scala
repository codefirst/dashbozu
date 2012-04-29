package models

import play.api.db._
import play.api.Play.current
import org.scalaquery.session._
import org.scalaquery.ql._
import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql.basic.{ BasicTable => Table }
import org.scalaquery.ql.basic.BasicDriver.Implicit._
import org.scalaquery.ql.basic.BasicProfile
import org.scalaquery.ql.basic.BasicInsertInvoker

import java.net.URI
import java.util.{ Date => UtilDate }
import java.sql.{ Date => SQLDate }

object ActivityDB extends Table[Activity]("ACTIVITY") {
  def wrap[S,T](delegate : TypeMapperDelegate[S])(apply : S => T)(unapply : T => S) : TypeMapperDelegate[T] = {
    new TypeMapperDelegate[T] {
      def zero :T =
        apply(delegate.zero)

      def sqlType =
        delegate.sqlType

      def setValue(v: T, p: PositionedParameters) =
        delegate.setValue(unapply(v), p)

      def setOption(v: Option[T], p: PositionedParameters) =
        delegate.setOption(v.map(unapply(_)), p)

      def nextValue(r: PositionedResult) =
        apply(delegate.nextValue(r))

      def updateValue(v: T, r: PositionedResult) =
        delegate.updateValue(unapply(v), r)

      override def valueToSQLLiteral(v: T) =
        delegate.valueToSQLLiteral(unapply(v))
    }
  }

  implicit object URITypeMapper extends BaseTypeMapper[URI] {
    def apply(profile: BasicProfile) =
      wrap(profile.typeMapperDelegates.stringTypeMapperDelegate)(new URI(_))(_.toString)
  }

  implicit object JavaDateTypeMapper extends BaseTypeMapper[UtilDate] {
    def apply(profile: BasicProfile) =
      wrap[SQLDate,UtilDate](profile.typeMapperDelegates.dateTypeMapperDelegate)((date:SQLDate) => date)((d:UtilDate) => new SQLDate(d.getTime()))
  }

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

  def findAll : List[Activity] = db.withSession { implicit db : Session =>
    (for (t <- this) yield t.*).list
  }
  def addAll(as : List[Activity]) : Option[Int] = db.withSession { implicit db : Session =>
    ActivityDB.insertAll(as.toSeq:_*)
  }
}

