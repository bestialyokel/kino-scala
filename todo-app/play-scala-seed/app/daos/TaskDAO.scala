package daos

import com.google.inject.Inject
import dtos.{CreateTaskDTO, PatchTaskDTO}
import models.Task
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Singleton
import scala.concurrent.{ExecutionContext, Future}

trait TaskDAO {
	def getAllTasks(): Future[Seq[Task]]
	def createTask(dto: CreateTaskDTO): Future[Option[Task]]
	def updateTaskById(id: Int, dto: PatchTaskDTO): Future[Option[Task]]
	def deleteTaskById(id: Int): Future[Option[Task]]
	def deleteCompleted(): Future[Seq[Task]]
	def toggleCompleted(completed: Boolean): Future[Boolean]

	protected def patchTask(task: Task, dto: PatchTaskDTO): Task = {
		task.copy(
			name = dto.name.getOrElse(task.name),
			completed = dto.completed.getOrElse(task.completed),
			deleted = dto.deleted.getOrElse(task.deleted)
		)
	}

	protected def getTask(id: Int, dto: CreateTaskDTO) = Task(id, dto.name, false, false)
}

@Singleton
class TaskDAODatabaseImpl @Inject()
  (val dbConfigProvider: DatabaseConfigProvider)
  (implicit ec: ExecutionContext)
  extends TaskDAO with HasDatabaseConfigProvider[JdbcProfile] {
	import dbConfig.profile.api._

	private class TasksTable(tag: Tag) extends Table[Task](tag, "tasks") {
		def id = column[Int]("task_id", O.PrimaryKey, O.AutoInc)
		def name = column[String]("name")
		def completed = column[Boolean]("completed")
		def deleted = column[Boolean]("deleted")

		def * = (id, name, completed, deleted).mapTo[Task]
	}

	private val tasks = TableQuery[TasksTable]

	def createTask(dto: CreateTaskDTO): Future[Option[Task]] = db.run {
		val q = tasks.returning(tasks) += getTask(0, dto)
		q.map(Some(_))
	}

	def getAllTasks(): Future[Seq[Task]] = db.run {
		tasks.filter(!_.deleted).result
	}

	def toggleCompleted(completed: Boolean): Future[Boolean] = db.run {
		tasks.map(_.completed).update(completed).map(_ => completed)
	}

	// TODO: как-то прикрутить returning
	def deleteCompleted(): Future[Seq[Task]] = db.run {
		val q = tasks.filter(_.completed === true)
		q.result.flatMap { forDelete =>
			q.map(_.deleted).update(true).flatMap { _ =>
				DBIO.successful(forDelete.map(_.copy(deleted = true)))
			}
		}.transactionally
	}

	// TODO: как-то прикрутить returning
	def updateTaskById(id: Int, dto: PatchTaskDTO): Future[Option[Task]] = db.run {
		val q = tasks.filter(_.id === id)

		q.result
		  .headOption
		  .flatMap {
			  case Some(task) =>
				  q.update(patchTask(task, dto))
					.andThen(q.result.headOption)
			  case None => DBIO.successful(None)
		  }
		  .transactionally
	}

	// TODO: как-то прикрутить returning
	def deleteTaskById(id: Int): Future[Option[Task]] = db.run {
		val q = tasks.filter(_.id === id)

		q.map(_.deleted).update(true).flatMap { _ =>
			q.result.headOption
		}.transactionally

	}
}



