package backlog4s.formatters

import backlog4s.datas._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.json._

/**
  * Define Enum type for encoding and decoding json
  * It tell how to format enumeration in json
  * If it should be a string or a number
  */
sealed trait EnumType
case object IntEnum extends EnumType
case object StringEnum extends EnumType

object SprayJsonFormats extends DefaultJsonProtocol {


  implicit object DateTimeFormat extends RootJsonFormat[DateTime] {

    val formatter = ISODateTimeFormat.basicDateTimeNoMillis

    def write(obj: DateTime): JsValue = {
      JsString(formatter.print(obj))
    }

    def read(json: JsValue): DateTime = json match {
      case JsString(s) => try {
        formatter.parseDateTime(s)
      }
      catch {
        case t: Throwable => error(s)
      }
      case _ =>
        error(json.toString())
    }

    def error(v: Any): DateTime = {
      val example = formatter.print(0)
      deserializationError(f"'$v' is not a valid date value. Dates must be in compact ISO-8601 format, e.g. '$example'")
    }
  }

  class EnumFormat[E <: Enumeration](enu: E, enumType: EnumType) extends RootJsonFormat[E#Value] {
    override def read(json: JsValue): E#Value = json match {
      case JsNumber(num) => enu.apply(num.toInt)
      case JsString(name) => enu.withName(name)
      case _ => throw DeserializationException(s"Unexpected input ${json.prettyPrint}")
    }

    override def write(obj: E#Value): JsValue = enumType match {
      case IntEnum => JsNumber(obj.id)
      case StringEnum => JsString(obj.toString)
    }
  }

  class IdFormat[A]() extends RootJsonFormat[Id[A]] {
    override def read(json: JsValue): Id[A] = json match {
      case JsNumber(idVal) => Id(idVal.toLong)
      case _ =>
        throw DeserializationException(s"Expected a js number got ${json.prettyPrint}")
    }

    override def write(obj: Id[A]): JsValue = JsNumber(obj.value)
  }

  class KeyFormat[A]() extends RootJsonFormat[Key[A]] {
    override def read(json: JsValue): Key[A] = json match {
      case JsString(keyVal) => Key(keyVal)
      case _ =>
        throw DeserializationException(s"Expected a js string got ${json.prettyPrint}")
    }

    override def write(obj: Key[A]): JsValue = JsString(obj.value)
  }

  implicit val userIdFormat = new IdFormat[User]
  implicit val roleFormat = new EnumFormat(Role, IntEnum)
  implicit val langFormat = new EnumFormat(Lang, StringEnum)
  implicit val errorCodeFormat = new EnumFormat(ApiErrorCode, IntEnum)
  implicit val orderFormat = new EnumFormat(Order, StringEnum)
  implicit val errorFormat = jsonFormat3(ApiError)
  implicit val errorsFormat = jsonFormat1(ApiErrors)
  implicit val userFormat: JsonFormat[User] = jsonFormat6(User)
  implicit val addUserFormFormat = jsonFormat5(AddUserForm)
  implicit val updateUserFormFormat = jsonFormat4(UpdateUserForm)
  implicit val idGroupFormat = new IdFormat[Group]
  implicit val group = jsonFormat8(Group)
  implicit val addGroupFormFormat = jsonFormat1(AddGroupForm)
  implicit val updateGroupFormFormat = jsonFormat2(UpdateGroupForm)
  implicit val idProjectFormat = new IdFormat[Project]
  implicit val keyProjectFormat = new KeyFormat[Project]
  implicit val projectFormat = jsonFormat8(Project)
  implicit val addProjectFormFormat = jsonFormat5(AddProjectForm)
  implicit val updateProjectFormFormat = jsonFormat7(UpdateProjectForm)
}