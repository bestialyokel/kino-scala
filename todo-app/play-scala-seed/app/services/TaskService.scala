package services

import daos.{TaskDAO, TaskDAODatabaseImpl}
import dtos.{CreateTaskDTO, PatchTaskDTO}
import models.{Task, TaskStatus}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

trait TaskService {
	def getAll(): Future[Seq[Task]]
	def create(dto: CreateTaskDTO): Future[Option[Task]]
	def delete(id: Int): Future[Option[Task]]
	def update(id: Int, dto: PatchTaskDTO): Future[Option[Task]]
	def deleteCompleted(): Future[Unit]
	def setStatusForAll(status: TaskStatus): Future[TaskStatus]
}

@Singleton
class TaskServiceImpl @Inject()(taskDAO: TaskDAO) extends TaskService {
	def getAll(): Future[Seq[Task]] = taskDAO.getAll()

	def create(dto: CreateTaskDTO): Future[Option[Task]] = taskDAO.create(dto)

	def delete(id: Int): Future[Option[Task]] = taskDAO.deleteById(id)

	def update(id: Int, dto: PatchTaskDTO): Future[Option[Task]] = taskDAO.updateById(id, dto)

	def deleteCompleted(): Future[Unit] = taskDAO.deleteCompleted()

	def setStatusForAll(status: TaskStatus): Future[TaskStatus] = taskDAO.setStatusForAll(status)

}

