package controllers

import dtos.{CreateTaskDTO, PatchTaskDTO}
import models.{Task, TaskStatus}
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Play.materializer
import play.api.test._
import play.api.test.Helpers._
import services.TaskService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents

import java.sql.Timestamp
import java.time.OffsetDateTime

class TaskControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  protected def getNow(): Timestamp = {
    Timestamp.from(OffsetDateTime.now().toInstant)
  }

  protected def getController(service: TaskService): TaskController = {
    new TaskController(
      service,
    )(ExecutionContext.global) {
      override def controllerComponents: ControllerComponents = stubControllerComponents()
    }
  }

  "TodoListControllerSpec GET /tasks" should {
    "return all from storage" in {
			val mockTaskService = mock[TaskService]
			val tasks = Seq(
				Task(0, "task 1", TaskStatus.Completed, None),
				Task(1, "task 1", TaskStatus.Completed, None),
				Task(2, "task 1", TaskStatus.Completed, None),
			)
			when(mockTaskService.getAll()).thenReturn(Future.successful(tasks))

			val controller = getController(mockTaskService)

			val tasksF = controller.getAll().apply(FakeRequest(GET, "/tasks"))

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

			when(mockTaskService.create(dto)).thenReturn(
				Future.successful(Some(task))
			)

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

			when(mockTaskService.create(dto)).thenReturn(
				Future.successful(Some(task))
			)
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

			val task = Task(0, "test 1", TaskStatus.Incompleted, None)

			when(mockTaskService.delete(0)).thenReturn(
				Future.successful(Some(task))
			)

      val controller = getController(mockTaskService)

			val req = FakeRequest(DELETE, "/tasks/0")
			val tasksF = controller.delete(0).apply(req)

			status(tasksF) mustBe OK
			contentType(tasksF) mustBe Some("application/json")
			contentAsJson(tasksF).as[Task] mustBe task
		}

		"return Not Found for non existing" in {
			val mockTaskService = mock[TaskService]

			when(mockTaskService.delete(0)).thenReturn(
				Future.successful(None)
			)

      val controller = getController(mockTaskService)

			val req = FakeRequest(DELETE, "/tasks")
			val tasksF = controller.delete(0).apply(req)

			status(tasksF) mustBe NOT_FOUND
		}


	}

	"TodoListControllerSpec PATCH /tasks/toggle-completed" should {
		"toggle completed field of tasks and return flag" in {
			val mockTaskService = mock[TaskService]

			val dto = CreateTaskDTO("task 1")
			val task = Task(0, dto.name, TaskStatus.Incompleted, None)

			when(mockTaskService.setStatusForAll(TaskStatus.Completed)).thenReturn(
				Future.successful(TaskStatus.Completed)
			)

      val controller = getController(mockTaskService)

			val req = FakeRequest(PATCH, s"/tasks/set-status/${TaskStatus.Completed.entryName}")
			val tasksF = controller.setStatusForAll(TaskStatus.Completed.toString).apply(req)

			status(tasksF) mustBe OK
			contentAsString(tasksF) mustBe TaskStatus.Completed
		}
	}

	"TodoListControllerSpec DELETE /tasks/delete-completed" should {
		"delete all completed tasks and return it" in {
			val mockTaskService = mock[TaskService]

      val now = Some(getNow())
			val tasks = Seq(
				Task(0, "task1", TaskStatus.Completed, now),
				Task(1, "task2", TaskStatus.Completed, now),
				Task(2, "task3", TaskStatus.Completed, now),
			)

			when(mockTaskService.deleteCompleted()).thenReturn(
				Future.successful(tasks)
			)

      val controller = getController(mockTaskService)

			val req = FakeRequest(DELETE, "/tasks/delete-completed")
			val tasksF = controller.deleteCompleted().apply(req)

			status(tasksF) mustBe OK
			contentType(tasksF) mustBe Some("application/json")
			contentAsJson(tasksF).as[Seq[Task]] mustEqual tasks
		}

		"return Bad Request for invalid input" in {
			val mockTaskService = mock[TaskService]

			val dto = CreateTaskDTO("task 1")
			val task = Task(0, dto.name, TaskStatus.Incompleted, None)

			when(mockTaskService.create(dto)).thenReturn(
				Future.successful(Some(task))
			)

      val controller = getController(mockTaskService)

			val req = FakeRequest(POST, "/tasks").withJsonBody(Json.toJson("name_" -> "abc"))
			val tasksF = controller.create().apply(req)

			status(tasksF) mustBe BAD_REQUEST
		}


	}

	"TodoListControllerSpec PATCH /tasks/$id" should {
		"update task" in {
			val mockTaskService = mock[TaskService]

			val dto = PatchTaskDTO(Some("abc"), Some(TaskStatus.Completed))

			val task = Task(0, "abc", TaskStatus.Completed, None)

			when(mockTaskService.update(0, dto)).thenReturn(
				Future.successful(Some(task))
			)

      val controller = getController(mockTaskService)

			val req = FakeRequest(PATCH, "/tasks/0")
        .withHeaders("content-type" -> "application/json")
        .withBody(dto)
			val tasksF = controller.update(0).apply(req)

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
			val tasksF = controller.update(0).apply(req)

			status(tasksF) mustBe BAD_REQUEST
		}

		"return Not Found for non existing id" in {
			val mockTaskService = mock[TaskService]
			val dto = PatchTaskDTO(Some("abc"), Some(TaskStatus.Completed))

			when(mockTaskService.update(0, dto)).thenReturn(
				Future.successful(None)
			)

      val controller = getController(mockTaskService)

			val req = FakeRequest(PATCH, "/tasks/0")
        .withHeaders("content-type" -> "application/json")
        .withBody(dto)

			val tasksF = controller.update(0).apply(req)

			status(tasksF) mustBe NOT_FOUND
		}


	}


}
