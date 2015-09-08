package formatters

import models.{Name, Person}
import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

object Formatter {
  def age_check(age: Int): Boolean = {
    if (age >= 30) false else true
  }
  val under30Validate = Reads.IntReads.filter(ValidationError("age must be under 30"))(age_check)

  implicit val NameJsonFormatter: Format[Name] = (
      (__ \ "first").format[String] and
      (__ \ "last").format[String]
    )(Name.apply, unlift(Name.unapply))

  implicit val PersonJsonFormatter: Format[Person] = (

    (__ \ "age").format[Int](under30Validate) and
      (__ \ "name").format[Name] and
      (__ \ "bloodType").formatNullable[String] and
      (__ \ "favoriteNumbers").format[Seq[Int]]
    )(Person.apply, unlift(Person.unapply))
}