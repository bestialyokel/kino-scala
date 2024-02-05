package dtos

import play.api.libs.json.{Json, Reads, Writes}

case class CreateTaskDTO(name: String)

trait CreateTaskDTOJson {
  implicit val writer: Writes[CreateTaskDTO] = Json.writes
  implicit val reads: Reads[CreateTaskDTO] = Json.reads
}

object CreateTaskDTO extends CreateTaskDTOJson {}
