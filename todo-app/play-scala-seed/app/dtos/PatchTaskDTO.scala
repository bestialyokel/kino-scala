package dtos

import play.api.libs.json.{Json, Writes, Reads}

case class PatchTaskDTO(
                         name: Option[String] = None,
                         completed: Option[Boolean] = None,
                         deleted: Option[Boolean] = None
                       )

trait PatchTaskDTOJson {
	implicit val writer: Writes[PatchTaskDTO] = Json.writes
	implicit val reads: Reads[PatchTaskDTO] = Json.reads
}

object PatchTaskDTO extends PatchTaskDTOJson {}