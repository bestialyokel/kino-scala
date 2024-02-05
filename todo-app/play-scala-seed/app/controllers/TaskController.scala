package controllers

import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}

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

  def getAll() = Action.async {
    logger.debug("getAllTasks called")

    taskService
      .getAll()
      .map { tasks =>
        Ok(Json.toJson(tasks))
      }
  }

  def create() = Action.async(parse.json[CreateTaskDTO]) { implicit request =>
    logger.debug("createTask called")

    taskService
      .create(request.body)
      .map {
        case Some(task) => Ok(Json.toJson(task))
        case None       => ImATeapot
      }
  }

  def delete(id: Int) = Action.async {
    logger.debug(s"deleteTask called id=$id")

    taskService
      .delete(id)
      .map { _ =>
        NoContent
      }

  }

  def updateName(id: Int) = Action.async(parse.json[UpdateTaskNameDTO]) { implicit request =>
    logger.debug(s"updateTask called id=$id")

    taskService
      .updateName(id, request.body)
      .map {
        case Some(task) => Ok(Json.toJson(task))
        case None       => NotFound
      }
  }

  def updateStatus(id: Int) = Action.async(parse.json[UpdateTaskStatusDTO]) { implicit request =>
    logger.debug(s"updateTask called id=$id")

    taskService
      .updateStatus(id, request.body)
      .map {
        case Some(task) => Ok(Json.toJson(task))
        case None       => NotFound
      }
  }

  def deleteCompleted() = Action.async {
    logger.debug(s"deleteCompleted called")

    taskService
      .deleteCompleted()
      .map { _ =>
        NoContent
      }
  }

  def setStatusForAll(status: String) = Action.async {
    logger.debug(s"toggleCompleted($status) called")

    // withNameOption матчит имена классов?
    // хотя должен по идее entryName
    Try(TaskStatus.withName(status)) match {
      case Success(s) => taskService
          .setStatusForAll(s)
          .map { _ =>
            Ok
          }
      case _ => Future.successful(BadRequest)
    }
  }

}
