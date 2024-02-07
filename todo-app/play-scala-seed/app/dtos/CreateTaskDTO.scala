package dtos

import play.api.libs.json.{Json, Reads, Writes}
import io.scalaland.chimney.dsl._
import models.Task
import enums.TaskStatus

case class CreateTaskDTO(name: String) {

  def transform: Task = this
    .into[Task]
    .withFieldConst(_.deletedO, None)
    .withFieldConst(_.id, 0)
    .withFieldConst(_.status, TaskStatus.Incompleted)
    .transform

}

trait CreateTaskDTOJson {
  implicit val writer: Writes[CreateTaskDTO] = Json.writes
  implicit val reads: Reads[CreateTaskDTO] = Json.reads
}

object CreateTaskDTO extends CreateTaskDTOJson
