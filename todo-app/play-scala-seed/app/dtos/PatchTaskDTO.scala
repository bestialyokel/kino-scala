package dtos

import models.TaskStatus
import play.api.libs.json.{Json, Reads, Writes}

case class PatchTaskDTO(
                         nameO: Option[String] = None,
                         completedO: Option[TaskStatus] = None,
                         deletedO: Option[Boolean] = None
                       )

trait PatchTaskDTOJson {
	implicit val writer: Writes[PatchTaskDTO] = Json.writes
	implicit val reads: Reads[PatchTaskDTO] = Json.reads
}

object PatchTaskDTO extends PatchTaskDTOJson {}