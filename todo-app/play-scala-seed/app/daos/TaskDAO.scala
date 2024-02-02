package daos

import com.google.inject.Inject
import dtos.{CreateTaskDTO, PatchTaskDTO}
import enumeratum.SlickEnumSupport
import models.{Task, TaskStatus}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.sql.Timestamp
import java.time.{OffsetDateTime}
import javax.inject.Singleton
import scala.concurrent.{ExecutionContext, Future}

trait TaskDAO {
	def getAll(): Future[Seq[Task]]
	def create(dto: CreateTaskDTO): Future[Option[Task]]
	def updateById(id: Int, dto: PatchTaskDTO): Future[Option[Task]]
	def deleteById(id: Int): Future[Option[Task]]
	def deleteCompleted(): Future[Unit]
	def setStatusForAll(status: TaskStatus): Future[TaskStatus]
}

@Singleton
class TaskDAODatabaseImpl @Inject()
  (val dbConfigProvider: DatabaseConfigProvider)
  (implicit ec: ExecutionContext)
  extends TaskDAO
    with SlickEnumSupport {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  override val profile = dbConfig.profile

  import dbConfig._
  import profile.api._

  implicit lazy val taskStatus = mappedColumnTypeForEnum(TaskStatus)

	private class TasksTable(tag: Tag) extends Table[Task](tag, "tasks") {
		def id = column[Int]("task_id", O.PrimaryKey, O.AutoInc)
		def name = column[String]("name")
		def status = column[TaskStatus]("status")
    // OffsetDateTime and ZonedDateTime not currently supportable natively by the backend
    // org.postgresql
		def deleted = column[Option[Timestamp]]("deleted_at")

		def * = (id, name, status, deleted).mapTo[Task]
	}

	private val Tasks = TableQuery[TasksTable]

	def create(dto: CreateTaskDTO): Future[Option[Task]] = db.run {
    (Tasks.returning(Tasks) += getTask(0, dto))
		.map(Some(_))
	}

	def getAll(): Future[Seq[Task]] = db.run {
    Tasks.filter(_.deleted.isEmpty).result
	}

	def setStatusForAll(status: TaskStatus): Future[TaskStatus] = db.run {
    Tasks.map(_.status).update(status).map(_ => status)
	}

	// TODO: как-то прикрутить returning
	def deleteCompleted(): Future[Unit] = db.run {
    val now = Some(getNowTimestamp())
    Tasks
      .filter(_.status === (TaskStatus.Completed: TaskStatus))
      .map(_.deleted).update(now)
      .map(_ => ())
	}

	// TODO: как-то прикрутить returning
	def updateById(id: Int, dto: PatchTaskDTO): Future[Option[Task]] = db.run {
		val q = Tasks.filter(_.id === id)

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
	def deleteById(id: Int): Future[Option[Task]] = db.run {
		val q = Tasks.filter(_.id === id)
    val now = Some(getNowTimestamp())
		q.map(_.deleted).update(now).flatMap { _ =>
			q.result.headOption
		}.transactionally
	}

  protected def getNowTimestamp(): Timestamp = {
    Timestamp.from(OffsetDateTime.now().toInstant)
  }

  protected def patchTask(task: Task, dto: PatchTaskDTO): Task = {
    task.copy(
      name = dto.nameO.getOrElse(task.name),
      status = dto.completedO.getOrElse(task.status),
      deleted = dto.deletedO.flatMap {
        case true => Some(getNowTimestamp())
        case _ => None
      }
    )
  }

  protected def getTask(id: Int, dto: CreateTaskDTO) = Task(id, dto.name, TaskStatus.Incompleted, Some(getNowTimestamp()))
}



