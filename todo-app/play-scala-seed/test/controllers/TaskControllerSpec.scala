package controllers

import java.sql.Timestamp
import java.time.OffsetDateTime

import scala.concurrent.{ExecutionContext, Future}

import dtos.{CreateTaskDTO, UpdateTaskNameDTO}
import models.{Task, TaskStatus}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Play.materializer
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import play.api.test._
import play.api.test.Helpers._
import services.TaskService

class TaskControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  protected def getNow(): Timestamp = Timestamp.from(OffsetDateTime.now().toInstant)

  protected def getController(service: TaskService): TaskController =
    new TaskController(service)(ExecutionContext.global) {
      override def controllerComponents: ControllerComponents = stubControllerComponents()
    }

  "TodoListControllerSpec GET /tasks" should {
    "return all from storage" in {
      val mockTaskService = mock[TaskService]
      val tasks = Seq(
        Task(0, "task 1", TaskStatus.Completed, None),
        Task(1, "task 1", TaskStatus.Completed, None),
        Task(2, "task 1", TaskStatus.Completed, None)
      )
      when(mockTaskService.all()).thenReturn(Future.successful(tasks))

      val controller = getController(mockTaskService)

      val tasksF = controller.all().apply(FakeRequest(GET, "/tasks"))

      status(tasksF) mustBe OK
      contentType(tasksF) mustBe Some("application/json")
      contentAsJson(tasksF).as[Seq[Task]] mustBe tasks
    }
  }

  "TodoListControllerSpec POST /tasks" should {
    "create task" in {
      val mockTaskService = mock[TaskService]

      val dto = CreateTaskDTO("task 1")
      val task = Task(0, dto.name, TaskStatus.Incompleted, None)

      when(mockTaskService.create(dto)).thenReturn(Future.successful(Some(task)))

      val controller = getController(mockTaskService)

      val req = FakeRequest(POST, "/tasks")
        .withHeaders("content-type" -> "application/json")
        .withBody(dto)

      val tasksF = controller.create().apply(req)

      status(tasksF) mustBe OK
      contentType(tasksF) mustBe Some("application/json")
      contentAsJson(tasksF).as[Task] mustBe task
    }

    "return Bad Request for invalid input" in {
      val mockTaskService = mock[TaskService]

      val dto = CreateTaskDTO("task 1")
      val task = Task(0, dto.name, TaskStatus.Incompleted, None)

      when(mockTaskService.create(dto)).thenReturn(Future.successful(Some(task)))
      val controller = getController(mockTaskService)

      val req = FakeRequest(POST, "/tasks")
        .withHeaders("content-type" -> "application/json")
        .withBody("name_" -> "abc")

      val tasksF = controller.create().apply(req)

      status(tasksF) mustBe BAD_REQUEST
    }

  }

  // TODO: All above
  "TodoListControllerSpec DELETE /tasks/$id" should {
    "return OK if sucessfully deleted" in {
      val mockTaskService = mock[TaskService]

      val task = Task(1, "", TaskStatus.Completed, None)
      when(mockTaskService.delete(0)).thenReturn(Future.successful(Some(task)))

      val controller = getController(mockTaskService)

      val req = FakeRequest(DELETE, "/tasks/0")
      val tasksF = controller.delete(0).apply(req)

      status(tasksF) mustBe NO_CONTENT
    }

  }

  "TodoListControllerSpec PATCH /tasks/toggle-completed" should {
    "toggle completed field of tasks and return flag" in {
      val mockTaskService = mock[TaskService]

      when(mockTaskService.setStatusForAll(TaskStatus.Completed))
        .thenReturn(Future.successful(TaskStatus.Completed))

      val controller = getController(mockTaskService)

      val req = FakeRequest(PATCH, s"/tasks/set-status/${TaskStatus.Completed.entryName}")
      val tasksF = controller.setStatusForAll(TaskStatus.Completed).apply(req)

      status(tasksF) mustBe OK
    }
  }

  "TodoListControllerSpec DELETE /tasks/delete-completed" should {
    "delete all completed tasks and return it" in {
      val mockTaskService = mock[TaskService]

      when(mockTaskService.deleteCompleted()).thenReturn(Future.successful((): Unit))

      val controller = getController(mockTaskService)

      val req = FakeRequest(DELETE, "/tasks/delete-completed")
      val tasksF = controller.deleteCompleted().apply(req)

      status(tasksF) mustBe NO_CONTENT
    }

  }

  "TodoListControllerSpec PATCH /tasks/$id/update-name" should {
    "update task" in {
      val mockTaskService = mock[TaskService]

      val dto = UpdateTaskNameDTO("abc")

      val task = Task(0, "abc", TaskStatus.Completed, None)

      when(mockTaskService.setName(0, dto)).thenReturn(Future.successful(Some(task)))

      val controller = getController(mockTaskService)

      val req = FakeRequest(PATCH, "/tasks/0")
        .withHeaders("content-type" -> "application/json")
        .withBody(dto)
      val tasksF = controller.setName(0).apply(req)

      status(tasksF) mustBe OK
      contentType(tasksF) mustBe Some("application/json")
      contentAsJson(tasksF).as[Task] mustBe task
    }

    "return Bad Request for invalid input" in {
      val mockTaskService = mock[TaskService]

      val controller = getController(mockTaskService)

      val req = FakeRequest(PATCH, "/tasks/0")
        .withHeaders("content-type" -> "application/json")
        .withJsonBody(Json.toJson("name_" -> "abc"))
      val tasksF = controller.setName(0).apply(req)

      status(tasksF) mustBe BAD_REQUEST
    }

    "return Not Found for non existing id" in {
      val mockTaskService = mock[TaskService]
      val dto = UpdateTaskNameDTO("abc")

      when(mockTaskService.setName(0, dto)).thenReturn(Future.successful(None))

      val controller = getController(mockTaskService)

      val req = FakeRequest(PATCH, "/tasks/0")
        .withHeaders("content-type" -> "application/json")
        .withBody(dto)

      val tasksF = controller.setName(0).apply(req)

      status(tasksF) mustBe NOT_FOUND
    }

  }

}
