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
import org.scalaquery.ql.extended.PostgresDriver.Implicit._
import org.scalaquery.ql.extended.H2Driver.Implicit._

import java.net.URI
import java.util.{ Date => UtilDate }
import java.util.TimeZone
import java.sql.{ Timestamp => SQLTimestamp }

object ActivityMapper {
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
      wrap(profile.typeMapperDelegates.stringTypeMapperDelegate){ s =>
        if(s == null) null
        else new URI(s)
      } { u =>
        if(u == null) null
        else u.toString
      }
  }

  implicit object JavaDateTypeMapper extends BaseTypeMapper[UtilDate] {
    def apply(profile: BasicProfile) =
      wrap[SQLTimestamp,UtilDate](profile.typeMapperDelegates.timestampTypeMapperDelegate)((date:SQLTimestamp) => new UtilDate(date.getTime + TimeZone.getDefault.getRawOffset))((d:UtilDate) => new SQLTimestamp(d.getTime - TimeZone.getDefault.getRawOffset))
  }

  implicit object StatusTypeMapper extends BaseTypeMapper[Status] {
    def apply(profile: BasicProfile) =
      wrap[String,Status](profile.typeMapperDelegates.stringTypeMapperDelegate){ s =>
        if(s == null)
          null
        else
          Status(s)
      } { s =>
        if(s == null) null
        else s.toString
      }
  }
}
