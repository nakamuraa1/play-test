package controllers

import models.Person
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
class PersonApi extends Controller {

  import formatters.Formatter.PersonJsonFormatter

  def personRegister = Action(parse.json) {
    _.body.validate[Person].map { p =>
      Ok(Json.toJson(p))
    }.recoverTotal { e =>
      BadRequest(JsError.toJson(e))
    }
  }
}
