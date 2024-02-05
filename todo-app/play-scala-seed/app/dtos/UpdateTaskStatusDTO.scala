package dtos

import models.TaskStatus
import play.api.libs.json.{Json, Reads, Writes}

case class UpdateTaskStatusDTO(status: TaskStatus)

trait UpdateTaskStatusDTOJson {
  implicit val writer: Writes[UpdateTaskStatusDTO] = Json.writes
  implicit val reads: Reads[UpdateTaskStatusDTO] = Json.reads
}

object UpdateTaskStatusDTO extends UpdateTaskStatusDTOJson {}
