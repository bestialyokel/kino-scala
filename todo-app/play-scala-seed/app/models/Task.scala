package models

import enumeratum.EnumEntry.Snakecase
import java.time.OffsetDateTime
import enumeratum._
import play.api.libs.json.{Json, Reads, Writes}

sealed trait TaskStatus extends EnumEntry with Snakecase

case object TaskStatus extends Enum[TaskStatus] with PlayEnum[TaskStatus] {
  val values = findValues
  case object Completed extends TaskStatus
  case object Incompleted extends TaskStatus
}

case class Task(id: Int, name: String, status: TaskStatus, deletedO: Option[OffsetDateTime])

trait TaskJson {
  implicit val writer: Writes[Task] = Json.writes
  implicit val reads: Reads[Task] = Json.reads
}

object Task extends TaskJson
