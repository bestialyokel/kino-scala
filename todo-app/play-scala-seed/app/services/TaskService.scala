package services

import daos.TaskDAO
import dtos.{CreateTaskDTO, UpdateTaskNameDTO, UpdateTaskStatusDTO}
import models.{Task, TaskStatus}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

trait TaskService {
	def getAll(): Future[Seq[Task]]
	def create(dto: CreateTaskDTO): Future[Option[Task]]
	def delete(id: Int): Future[Unit]
  def updateName(id: Int, dto: UpdateTaskNameDTO): Future[Option[Task]]
  def updateStatus(id: Int, dto: UpdateTaskStatusDTO): Future[Option[Task]]
	def deleteCompleted(): Future[Unit]
	def setStatusForAll(status: TaskStatus): Future[TaskStatus]
}

@Singleton
class TaskServiceImpl @Inject()(taskDAO: TaskDAO) extends TaskService {
	def getAll(): Future[Seq[Task]] = taskDAO.getAll()

	def create(dto: CreateTaskDTO): Future[Option[Task]] = taskDAO.create(dto)

	def delete(id: Int): Future[Unit] = taskDAO.deleteById(id)

  def updateName(id: Int, dto: UpdateTaskNameDTO): Future[Option[Task]] = taskDAO.updateNameById(id, dto)

  def updateStatus(id: Int, dto: UpdateTaskStatusDTO): Future[Option[Task]] = taskDAO.updateStatusById(id, dto)

	def deleteCompleted(): Future[Unit] = taskDAO.deleteCompleted()

	def setStatusForAll(status: TaskStatus): Future[TaskStatus] = taskDAO.setStatusForAll(status)

}

