package daos

import java.time.OffsetDateTime

import javax.inject.Singleton

import scala.concurrent.{ExecutionContext, Future}

import com.google.inject.Inject
import dtos.{CreateTaskDTO, UpdateTaskNameDTO, UpdateTaskStatusDTO}
import enumeratum.SlickEnumSupport
import models.{Task, TaskStatus}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait TaskDAO {
  def getAll(): Future[Seq[Task]]
  def create(dto: CreateTaskDTO): Future[Option[Task]]
  def updateNameById(id: Int, dto: UpdateTaskNameDTO): Future[Option[Task]]
  def updateStatusById(id: Int, dto: UpdateTaskStatusDTO): Future[Option[Task]]
  def deleteById(id: Int): Future[Unit]
  def deleteCompleted(): Future[Unit]
  def setStatusForAll(status: TaskStatus): Future[Unit]
}

@Singleton
class TaskDAODatabaseImpl @Inject() (val dbConfigProvider: DatabaseConfigProvider)(implicit
  ec: ExecutionContext
) extends TaskDAO
      with SlickEnumSupport {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  override val profile = dbConfig.profile

  import dbConfig._
  import profile.api._

  implicit lazy val taskStatus: this.profile.BaseColumnType[TaskStatus] =
    mappedColumnTypeForEnum(TaskStatus)

  private class TasksTable(tag: Tag) extends Table[Task](tag, "tasks") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def status = column[TaskStatus]("status")
    // OffsetDateTime and ZonedDateTime not currently supportable natively by the backend
    // org.postgresql
    def deleted = column[Option[OffsetDateTime]]("deleted_at")

    def * = (id, name, status, deleted).mapTo[Task]
  }

  private val Tasks = TableQuery[TasksTable]

  def create(dto: CreateTaskDTO): Future[Option[Task]] = db.run {
    val q = Tasks.returning(Tasks) += getTask(0, dto)
    q.map(Some(_))
  }

  def getAll(): Future[Seq[Task]] = db.run {
    Tasks.filter(_.deleted.isEmpty).result
  }

  def setStatusForAll(status: TaskStatus): Future[Unit] = db.run {
    Tasks.map(_.status).update(status).map(_ => ())
  }

  // TODO: как-то прикрутить returning
  def deleteCompleted(): Future[Unit] = db.run {
    val now = Some(getNowTimestamp())
    Tasks
      .filter(_.status === (TaskStatus.Completed: TaskStatus))
      .map(_.deleted)
      .update(now)
      .map(_ => ())
  }

  // TODO: как-то прикрутить returning
  def updateNameById(id: Int, dto: UpdateTaskNameDTO): Future[Option[Task]] = db.run {
    val q = Tasks.filter(_.id === id)

    q.map(_.name)
      .update(dto.name)
      .flatMap { _ =>
        q.result.headOption
      }
      .transactionally
  }

  def updateStatusById(id: Int, dto: UpdateTaskStatusDTO): Future[Option[Task]] = db.run {
    val q = Tasks.filter(_.id === id)

    q.map(_.status)
      .update(dto.taskStatus)
      .flatMap { _ =>
        q.result.headOption
      }
      .transactionally
  }

  // TODO: как-то прикрутить returning
  def deleteById(id: Int): Future[Unit] = db.run {
    Tasks.filter(_.id === id).map(_.deleted).update(Some(getNowTimestamp())).map(_ => ())
  }

  protected def getNowTimestamp(): OffsetDateTime = OffsetDateTime.now()

  protected def getTask(id: Int, dto: CreateTaskDTO) =
    Task(id, dto.name, TaskStatus.Incompleted, None)

}
