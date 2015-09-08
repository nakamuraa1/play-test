import org.specs2.mutable.Specification
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, WithApplication}

class PersonAPISpec extends Specification {

  "PersonAPI#register" should {

    "register person" in new WithApplication {
      val Some(result) = route(FakeRequest(POST, "/api/v1/person",
        FakeHeaders(Seq(CONTENT_TYPE -> "application/json")),
        Json.parse( """{"age":24, "name":{"first":"FirstName", "last":"LastName"}, "bloodType":"AB", "favoriteNumbers":[1]}""")))
      status(result) mustEqual OK
      contentAsString(result) mustEqual """{"age":24,"name":{"first":"FirstName","last":"LastName"},"bloodType":"AB","favoriteNumbers":[1]}"""
    }

    "display json parse error caused by PersonJsonFormatter age" in new WithApplication {
      val Some(result) = route(FakeRequest(POST, "/api/v1/person",
        FakeHeaders(Seq(CONTENT_TYPE -> "application/json")),
        Json.parse( """{"typo!!!":24, "name":{"first":"FirstName", "last":"LastName"}, "bloodType":"B", "favoriteNumbers":[1,2]}""")))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) mustEqual """{"obj.age":[{"msg":["error.path.missing"],"args":[]}]}"""
    }

    "display json parse error caused by NameJsonFormatter" in new WithApplication {
      val Some(result) = route(FakeRequest(POST, "/api/v1/person",
        FakeHeaders(Seq(CONTENT_TYPE -> "application/json")),
        Json.parse( """{"age":24, "name":{"first":"FirstName", "typo!!!":"LastName"}, "bloodType":"B", "favoriteNumbers":[1,2]}""")))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) mustEqual """{"obj.name.last":[{"msg":["error.path.missing"],"args":[]}]}"""
    }

    "resisterPerson bloodType null" in new WithApplication {
      val Some(result) = route(FakeRequest(POST, "/api/v1/person",
        FakeHeaders(Seq(CONTENT_TYPE -> "application/json")),
        Json.parse( """{"age":24, "name":{"first":"FirstName", "last":"LastName"}, "favoriteNumbers":[1,2]}""")))
      status(result) mustEqual OK
      contentAsString(result) mustEqual """{"age":24,"name":{"first":"FirstName","last":"LastName"},"favoriteNumbers":[1,2]}"""
    }

    "display json parse error caused by NameJsonFormatter favoriteNumbers" in new WithApplication {
      val Some(result) = route(FakeRequest(POST, "/api/v1/person",
        FakeHeaders(Seq(CONTENT_TYPE -> "application/json")),
        Json.parse( """{"age":24, "name":{"first":"FirstName", "last":"LastName"}, "bloodType":"B", "favoriteNumbers":["a",2]}""")))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) mustEqual """{"obj.favoriteNumbers[0]":[{"msg":["error.expected.jsnumber"],"args":[]}]}"""
    }
    "display json parse error caused by age must be under 30" in new WithApplication {
      val Some(result) = route(FakeRequest(POST, "/api/v1/person",
        FakeHeaders(Seq(CONTENT_TYPE -> "application/json")),
        Json.parse( """{"age":35, "name":{"first":"FirstName", "last":"LastName"}, "bloodType":"B", "favoriteNumbers":[0,2]}""")))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) mustEqual """{"obj.age":[{"msg":["age must be under 30"],"args":[]}]}"""
    }
  }
}