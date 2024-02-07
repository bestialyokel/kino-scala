package dtos

import play.api.libs.json.{Json, Reads, Writes}

case class UpdateTaskNameDTO(name: String)

trait UpdateTaskNameDTOJson {
  implicit val writer: Writes[UpdateTaskNameDTO] = Json.writes
  implicit val reads: Reads[UpdateTaskNameDTO] = Json.reads
}

object UpdateTaskNameDTO extends UpdateTaskNameDTOJson
