package daos

import java.time.OffsetDateTime
import javax.inject.Singleton
import scala.concurrent.{ExecutionContext, Future}
import com.google.inject.Inject
import dtos.{CreateTaskDTO, UpdateTaskNameDTO, UpdateTaskStatusDTO}
import models.{Task, TaskStatus}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import utils.slick.CustomPostgresProfile
import cats.implicits._

trait TaskDAO {
  def getAll(): Future[Seq[Task]]
  def getOneById(id: Int): Future[Option[Task]]
  def create(dto: CreateTaskDTO): Future[Option[Task]]
  def updateNameById(id: Int, dto: UpdateTaskNameDTO): Future[Unit]
  def updateStatusById(id: Int, dto: UpdateTaskStatusDTO): Future[Unit]
  def setStatusForAll(status: TaskStatus): Future[Unit]
  def deleteById(id: Int): Future[Unit]
  def deleteCompleted(): Future[Unit]
}

trait TaskComponent {
  self: HasDatabaseConfig[CustomPostgresProfile] =>
}

@Singleton
class TaskDAODatabaseImpl @Inject() (val dbConfigProvider: DatabaseConfigProvider)(implicit
  ec: ExecutionContext
) extends TaskDAO
      with TaskComponent
      with HasDatabaseConfigProvider[CustomPostgresProfile] {

  import profile.api._

  private class TasksTable(tag: Tag) extends Table[Task](tag, "tasks") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def status = column[TaskStatus]("status")
    def deleted = column[Option[OffsetDateTime]]("deleted")

    def * = (id, name, status, deleted).mapTo[Task]
  }

  private val Tasks = TableQuery[TasksTable]

  def getAll(): Future[Seq[Task]] = db.run {
    Tasks.filter(_.deleted.isEmpty).result
  }

  def getOneById(id: Int): Future[Option[Task]] = db.run {
    Tasks.filter(_.id === id).result.headOption
  }

  def create(dto: CreateTaskDTO): Future[Option[Task]] = db.run {
    (Tasks.returning(Tasks) += dto.transform).map(Some(_))
  }

  def updateNameById(id: Int, dto: UpdateTaskNameDTO): Future[Unit] = db
    .run {
      Tasks.filter(_.id === id).map(_.name).update(dto.name)
    }
    .void

  def updateStatusById(id: Int, dto: UpdateTaskStatusDTO): Future[Unit] = db
    .run {
      Tasks.filter(_.id === id).map(_.status).update(dto.status)
    }
    .void

  def setStatusForAll(status: TaskStatus): Future[Unit] = db
    .run {
      Tasks.map(_.status).update(status)
    }
    .void

  def deleteById(id: Int): Future[Unit] = db
    .run {
      Tasks.filter(_.id === id).map(_.deleted).update(OffsetDateTime.now().some)
    }
    .void

  def deleteCompleted(): Future[Unit] = db
    .run {
      Tasks
        .filter(_.status === (TaskStatus.Completed: TaskStatus))
        .map(_.deleted)
        .update(OffsetDateTime.now().some)
    }
    .void

}
