package controllers

import javax.inject.{Inject, Singleton}

import scala.concurrent.ExecutionContext
import dtos.{CreateTaskDTO, UpdateTaskNameDTO, UpdateTaskStatusDTO}
import models.TaskStatus
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.InjectedController
import services.TaskService

@Singleton
class TaskController @Inject() (val taskService: TaskService)(implicit
  val ec: ExecutionContext
) extends InjectedController
      with Logging {

  def all() = Action.async {
    logger.debug("getAllTasks called")

    taskService.all().map(tasks => Ok(Json.toJson(tasks)))
  }

  def create() = Action.async(parse.json[CreateTaskDTO]) { implicit request =>
    logger.debug("createTask called")

    taskService
      .create(request.body)
      .map {
        case Some(task) => Ok(Json.toJson(task))
        case None       => InternalServerError
      }
  }

  def setName(id: Int) = Action.async(parse.json[UpdateTaskNameDTO]) { implicit request =>
    logger.debug(s"updateTask called id=$id")

    taskService
      .setName(id, request.body)
      .map {
        case Some(task) => Ok(Json.toJson(task))
        case None       => NotFound
      }
  }

  def setStatus(id: Int) = Action.async(parse.json[UpdateTaskStatusDTO]) { implicit request =>
    logger.debug(s"updateTask called id=$id")

    taskService
      .setStatus(id, request.body)
      .map {
        case Some(task) => Ok(Json.toJson(task))
        case None       => NotFound
      }
  }

  def setStatusForAll(status: TaskStatus) = Action.async {
    logger.debug(s"toggleCompleted($status) called")

    taskService.setStatusForAll(status).map(_ => Ok)
  }

  def delete(id: Int) = Action.async {
    logger.debug(s"deleteTask called id=$id")

    taskService.delete(id).map(_ => NoContent)
  }

  def deleteCompleted() = Action.async {
    logger.debug(s"deleteCompleted called")

    taskService.deleteCompleted().map(_ => NoContent)
  }

}
