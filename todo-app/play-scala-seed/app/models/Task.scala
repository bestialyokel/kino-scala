package models

import play.api.libs.json.{JsPath, Json, JsonConfiguration, Reads, Writes, __}
import play.api.libs.functional.syntax._
import enumeratum._
import enumeratum.values._

import java.sql.Timestamp
import java.time.{Instant, LocalDateTime, ZoneId, ZoneOffset}
import java.time.format.DateTimeFormatter

sealed abstract class TaskStatus(override val entryName: String) extends EnumEntry

case object TaskStatus extends Enum[TaskStatus] with PlayJsonEnum[TaskStatus] {
  val values = findValues
  case object Completed   extends TaskStatus("completed")
  case object Incompleted extends TaskStatus("incompleted")
}

case class Task(id: Int, name: String, status: TaskStatus, deleted: Option[Timestamp])

trait TaskJson {
  val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC))

  implicit val timestampReads: Reads[Timestamp] = {
    implicitly[Reads[String]].map { s =>
        val dt = LocalDateTime.parse(s, formatter)
        Timestamp.valueOf(dt)
    }
  }

  implicit val timestampWrites: Writes[Timestamp] = {
    implicitly[Writes[String]].contramap(t => formatter.format(t.toInstant))
  }

	implicit val writer: Writes[Task] = Json.writes
	implicit val reads: Reads[Task] = Json.reads
}

object Task extends TaskJson {}
