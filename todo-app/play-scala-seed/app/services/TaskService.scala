package services

import daos.{TaskDAO, TaskDAODatabaseImpl}
import dtos.{CreateTaskDTO, PatchTaskDTO}
import models.Task

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

trait TaskService {
	def getAllTasks(): Future[Seq[Task]]
	def createTask(dto: CreateTaskDTO): Future[Option[Task]]
	def deleteTask(id: Int): Future[Option[Task]]
	def updateTask(id: Int, dto: PatchTaskDTO): Future[Option[Task]]
	def deleteCompleted(): Future[Seq[Task]]
	def toggleCompleted(completed: Boolean): Future[Boolean]
}

@Singleton
class TaskServiceImpl @Inject()(val taskDAO: TaskDAO) extends TaskService {
	def getAllTasks(): Future[Seq[Task]] = taskDAO.getAllTasks()

	def createTask(dto: CreateTaskDTO): Future[Option[Task]] = taskDAO.createTask(dto)

	def deleteTask(id: Int): Future[Option[Task]] = taskDAO.deleteTaskById(id)

	def updateTask(id: Int, dto: PatchTaskDTO): Future[Option[Task]] = taskDAO.updateTaskById(id, dto)

	def deleteCompleted(): Future[Seq[Task]] = taskDAO.deleteCompleted()

	def toggleCompleted(completed: Boolean): Future[Boolean] = taskDAO.toggleCompleted(completed)

}

