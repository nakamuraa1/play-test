package formatters

import models.{Name, Person}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

object Formatter {

  implicit val NameJsonFormatter: Format[Name] = (
    (__ \ "first").format[String](minLength[String](1)) and
      (__ \ "last").format[String]
    )(Name.apply, unlift(Name.unapply))

  implicit val PersonJsonFormatter: Format[Person] = (

    (__ \ "age").format[Int] and
      (__ \ "name").format[Name]
    )(Person.apply, unlift(Person.unapply))

}