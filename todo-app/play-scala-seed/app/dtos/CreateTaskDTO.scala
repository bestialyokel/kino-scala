package dtos

import play.api.libs.json.{Json, Reads, Writes}
import io.scalaland.chimney.dsl._
import models.{Task, TaskStatus}

case class CreateTaskDTO(name: String) {
  def transform: Task = Task(0, "", TaskStatus.Incompleted, None).patchUsing(this)
}

trait CreateTaskDTOJson {
  implicit val writer: Writes[CreateTaskDTO] = Json.writes
  implicit val reads: Reads[CreateTaskDTO] = Json.reads
}

object CreateTaskDTO extends CreateTaskDTOJson
