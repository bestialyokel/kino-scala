package controllers

import dtos.{CreateTaskDTO, PatchTaskDTO}
import models.TaskStatus
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.InjectedController
import services.TaskService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}

@Singleton
class TaskController @Inject()
(
  val taskService: TaskService
)(implicit val ec: ExecutionContext)
  extends InjectedController with Logging {

  def getAll() = Action.async {
    logger.debug("getAllTasks called")

    taskService.getAll()
      .map { tasks => Ok(Json.toJson(tasks)) }
  }

  def create() = Action.async(parse.json[CreateTaskDTO]) { implicit request =>
    logger.debug("createTask called")

    taskService.create(request.body)
      .map {
        case Some(task) => Ok(Json.toJson(task))
        case None => ImATeapot
      }
  }

  def delete(id: Int) = Action.async {
    logger.debug(s"deleteTask called id=${id}")

    taskService.delete(id)
      .map {
        case Some(task) => Ok(Json.toJson(task))
        case None => NotFound
      }

  }

  def update(id: Int) = Action.async(parse.json[PatchTaskDTO]) { implicit request =>
    logger.debug(s"updateTask called id=${id}")

    taskService.update(id, request.body)
      .map {
        case Some(task) => Ok(Json.toJson(task))
        case None => NotFound
      }
  }

  def deleteCompleted() = Action.async {
    logger.debug(s"deleteCompleted called")

    taskService.deleteCompleted()
      .map { _ => Ok }
  }

  def setStatusForAll(status: String) = Action.async {
    logger.debug(s"toggleCompleted(${status}) called")

    // withNameOption матчит имена классов?
    Try(TaskStatus.withName(status)) match {
      case Success(s) =>
        taskService.setStatusForAll(s)
          .map { task => Ok(Json.toJson(task)) }
      case _ => Future.successful(BadRequest)
    }
  }
}