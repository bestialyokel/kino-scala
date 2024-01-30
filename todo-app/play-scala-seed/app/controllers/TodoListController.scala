package controllers

import dtos.{CreateTaskDTO, PatchTaskDTO}
import io.sentry.Sentry
import play.api.Logging
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.{BaseController, ControllerComponents}
import services.TaskService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TodoListController @Inject()
(
  val controllerComponents: ControllerComponents,
  val taskService: TaskService,
  implicit val ec: ExecutionContext

) extends BaseController with Logging {

  private val createTaskDTOForm: Form[CreateTaskDTO] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "name" -> nonEmptyText
      )(CreateTaskDTO.apply)(CreateTaskDTO.unapply))
  }

  private val patchTaskDTOForm: Form[PatchTaskDTO] = {
    import play.api.data.Forms._

    def atLeastOneFieldPresented(dto: PatchTaskDTO): Option[PatchTaskDTO] = {
      if (dto.deleted.isDefined || dto.completed.isDefined || dto.name.isDefined) {
        Some(dto)
      } else {
        None
      }
    }

    Form(
      mapping(
        "name" -> optional(nonEmptyText),
        "completed" -> optional(boolean),
        "deleted" -> optional(boolean)
      )(PatchTaskDTO.apply)(PatchTaskDTO.unapply)
        verifying(
          "Failed form constraints!",
          dto => atLeastOneFieldPresented(dto).isDefined
        )
    )

  }

  def getAllTasks() = Action.async {
    logger.debug("getAllTasks called")

    taskService.getAllTasks()
      .map { tasks => Ok(Json.toJson(tasks)) }
      .recover {
        case e: Exception =>
          Sentry.captureException(e)
          InternalServerError
      }
  }

  def createTask() = Action.async { implicit request =>
    logger.debug("createTask called")

    def process(dto: CreateTaskDTO) = {
      taskService.createTask(dto)
        .map {
          case Some(task) => Ok(Json.toJson(task))
          case None => BadRequest
        }
        .recover {
          case e: Exception =>
            Sentry.captureException(e)
            InternalServerError
      }
    }

    createTaskDTOForm.bindFromRequest().fold(
      formWithErrors => {
        val msg = formWithErrors.errors.map(e => s"${e.key}: ${e.message}").mkString(";")
        Future.successful(BadRequest(msg))
      },
      process
    )
  }

  def deleteTask(idO: Option[Int]) = Action.async {
    logger.debug(s"deleteTask called id=${idO}")

    idO match {
      case None => Future.successful(BadRequest)
      case Some(id) =>
        taskService.deleteTask(id)
          .map {
            case Some(task) => Ok(Json.toJson(task))
            case None => NotFound
          }
          .recover {
            case e: Exception =>
              Sentry.captureException(e)
              InternalServerError
          }
    }
  }

  def updateTask(idO: Option[Int]) = Action.async { implicit request =>
    logger.debug(s"updateTask called id=${idO}")

    def process(dto: PatchTaskDTO) = {
      idO match {
        case None => Future.successful(BadRequest)
        case Some(id) =>
          taskService.updateTask(id, dto)
            .map {
              case Some(task) => Ok(Json.toJson(task))
              case None => NotFound
            }
            .recover {
              case e: Exception => Sentry.captureException(e)
              InternalServerError
            }
      }
    }

    patchTaskDTOForm.bindFromRequest().fold(
      formWithErrors => {
          val msg = formWithErrors.errors.map(e => s"${e.key}: ${e.message}").mkString(";")
          Future.successful(BadRequest(msg))
      },
      process
    )
  }

  def deleteCompleted() = Action.async {
    logger.debug(s"deleteCompleted called")

    taskService.deleteCompleted()
      .map { task => Ok(Json.toJson(task)) }
      .recover {
        case e: Exception => Sentry.captureException(e)
        InternalServerError
      }
  }

  def toggleCompleted(completedO: Option[Boolean]) = Action.async {
    logger.debug(s"toggleCompleted(${completedO}) called")

    completedO match {
      case None => Future.successful(BadRequest)
      case Some(completed) =>
        taskService.toggleCompleted(completed)
          .map { task => Ok(Json.toJson(task)) }
          .recover {
            case e: Exception =>
              Sentry.captureException(e)
              InternalServerError
          }
    }
  }

}