package models

import enums.TaskStatus

import java.time.OffsetDateTime
import play.api.libs.json.{Json, Reads, Writes}

case class Task(id: Int, name: String, status: TaskStatus, deletedO: Option[OffsetDateTime])

trait TaskJson {
  implicit val writer: Writes[Task] = Json.writes
  implicit val reads: Reads[Task] = Json.reads
}

object Task extends TaskJson
