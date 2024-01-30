package dtos

import play.api.libs.json.{Json, Writes, Reads}

case class CreateTaskDTO(name: String)

trait CreateTaskDTOJson {
	implicit val writer: Writes[CreateTaskDTO] = Json.writes
	implicit val reads: Reads[CreateTaskDTO] = Json.reads
}

object CreateTaskDTO extends CreateTaskDTOJson {}