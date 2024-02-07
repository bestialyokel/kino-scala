package services

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import daos.TaskDAO
import dtos.{CreateTaskDTO, UpdateTaskNameDTO, UpdateTaskStatusDTO}
import models.{Task, TaskStatus}

trait TaskService {
  def all(): Future[Seq[Task]]
  def create(dto: CreateTaskDTO): Future[Option[Task]]
  def setName(id: Int, dto: UpdateTaskNameDTO): Future[Option[Task]]
  def setStatus(id: Int, dto: UpdateTaskStatusDTO): Future[Option[Task]]
  def setStatusForAll(status: TaskStatus): Future[TaskStatus]
  def delete(id: Int): Future[Option[Task]]
  def deleteCompleted(): Future[Unit]
}

@Singleton
class TaskServiceImpl @Inject() (taskDAO: TaskDAO)(implicit
  val ec: ExecutionContext
) extends TaskService {
  def all(): Future[Seq[Task]] = taskDAO.getAll()

  def create(dto: CreateTaskDTO): Future[Option[Task]] = taskDAO.create(dto)

  def delete(id: Int): Future[Option[Task]] = taskDAO
    .deleteById(id)
    .flatMap(_ => taskDAO.getOneById(id))

  def setName(id: Int, dto: UpdateTaskNameDTO): Future[Option[Task]] = taskDAO
    .updateNameById(id, dto)
    .flatMap(_ => taskDAO.getOneById(id))

  def setStatus(id: Int, dto: UpdateTaskStatusDTO): Future[Option[Task]] = taskDAO
    .updateStatusById(id, dto)
    .flatMap(_ => taskDAO.getOneById(id))

  def deleteCompleted(): Future[Unit] = taskDAO.deleteCompleted()

  def setStatusForAll(status: TaskStatus): Future[TaskStatus] =
    taskDAO.setStatusForAll(status).map(_ => status)

}
