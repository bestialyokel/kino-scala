package controllers

import dtos.{CreateTaskDTO, PatchTaskDTO}
import models.Task
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import services.TaskService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import play.api.libs.json.Json

class TodoListControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

	"TodoListControllerSpec GET /tasks" should {
		"return all from storage" in {
			val mockTaskService = mock[TaskService]
			val tasks = Seq(
				Task(0, "task 1", true, false),
				Task(1, "task 1", true, false),
				Task(2, "task 1", true, false),
			)
			when(mockTaskService.getAllTasks).thenReturn(Future.successful(tasks))

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val tasksF = controller.getAllTasks().apply(FakeRequest(GET, "/tasks"))

			status(tasksF) mustBe OK
			contentType(tasksF) mustBe Some("application/json")
			contentAsJson(tasksF).as[Seq[Task]] mustBe tasks
		}
	}

	"TodoListControllerSpec POST /tasks" should {
		"create task" in {
			val mockTaskService = mock[TaskService]

			val dto = CreateTaskDTO("task 1")
			val task = Task(0, dto.name, false, false)

			when(mockTaskService.createTask(dto)).thenReturn(
				Future.successful(Some(task))
			)

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val req = FakeRequest(POST, "/tasks").withJsonBody(Json.toJson(dto))
			val tasksF = controller.createTask.apply(req)

			status(tasksF) mustBe OK
			contentType(tasksF) mustBe Some("application/json")
			contentAsJson(tasksF).as[Task] mustBe task
		}

		"return Bad Request for invalid input" in {
			val mockTaskService = mock[TaskService]

			val dto = CreateTaskDTO("task 1")
			val task = Task(0, dto.name, false, false)

			when(mockTaskService.createTask(dto)).thenReturn(
				Future.successful(Some(task))
			)

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val req = FakeRequest(POST, "/tasks").withJsonBody(Json.toJson("name_" -> "abc"))
			val tasksF = controller.createTask.apply(req)

			status(tasksF) mustBe BAD_REQUEST
		}


	}

	// TODO: All above
	"TodoListControllerSpec DELETE /tasks/$id" should {
		"return OK if sucessfully deleted" in {
			val mockTaskService = mock[TaskService]

			val task = Task(0, "test 1", false, false)

			when(mockTaskService.deleteTask(0)).thenReturn(
				Future.successful(Some(task))
			)

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val req = FakeRequest(DELETE, "/tasks/0")
			val tasksF = controller.deleteTask(Some(0)).apply(req)

			status(tasksF) mustBe OK
			contentType(tasksF) mustBe Some("application/json")
			contentAsJson(tasksF).as[Task] mustBe task
		}

		"return Not Found for non existing" in {
			val mockTaskService = mock[TaskService]

			when(mockTaskService.deleteTask(0)).thenReturn(
				Future.successful(None)
			)

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val req = FakeRequest(DELETE, "/tasks")
			val tasksF = controller.deleteTask(Some(0)).apply(req)

			status(tasksF) mustBe NOT_FOUND
		}


	}

	"TodoListControllerSpec PATCH /tasks/toggle-completed" should {
		"toggle completed field of tasks and return flag" in {
			val mockTaskService = mock[TaskService]

			val dto = CreateTaskDTO("task 1")
			val task = Task(0, dto.name, false, false)

			when(mockTaskService.toggleCompleted(true)).thenReturn(
				Future.successful(true)
			)

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val req = FakeRequest(POST, "/tasks/toggle-completed")
			val tasksF = controller.toggleCompleted(Some(true)).apply(req)

			status(tasksF) mustBe OK
			contentAsString(tasksF) must include ("true")
		}
	}

	"TodoListControllerSpec DELETE /tasks/delete-completed" should {
		"delete all completed tasks and return it" in {
			val mockTaskService = mock[TaskService]

			val tasks = Seq(
				Task(0, "task1", true, true),
				Task(1, "task2", true, true),
				Task(2, "task3", true, true),
			)

			when(mockTaskService.deleteCompleted).thenReturn(
				Future.successful(tasks)
			)

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val req = FakeRequest(DELETE, "/tasks/delete-completed")
			val tasksF = controller.deleteCompleted().apply(req)

			status(tasksF) mustBe OK
			contentType(tasksF) mustBe Some("application/json")
			contentAsJson(tasksF).as[Seq[Task]] mustBe tasks
		}

		"return Bad Request for invalid input" in {
			val mockTaskService = mock[TaskService]

			val dto = CreateTaskDTO("task 1")
			val task = Task(0, dto.name, false, false)

			when(mockTaskService.createTask(dto)).thenReturn(
				Future.successful(Some(task))
			)

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val req = FakeRequest(POST, "/tasks").withJsonBody(Json.toJson("name_" -> "abc"))
			val tasksF = controller.createTask.apply(req)

			status(tasksF) mustBe BAD_REQUEST
		}


	}

	"TodoListControllerSpec PATCH /tasks/$id" should {
		"update task" in {
			val mockTaskService = mock[TaskService]

			val dto = PatchTaskDTO(Some("abc"), Some(true))

			val task = Task(0, "abc", true, false)

			when(mockTaskService.updateTask(0, dto)).thenReturn(
				Future.successful(Some(task))
			)

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val req = FakeRequest(PATCH, "/tasks/0").withJsonBody(Json.toJson(dto))
			val tasksF = controller.updateTask(Some(0)).apply(req)

			status(tasksF) mustBe OK
			contentType(tasksF) mustBe Some("application/json")
			contentAsJson(tasksF).as[Task] mustBe task
		}

		"return Bad Request for invalid input" in {
			val mockTaskService = mock[TaskService]

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val req = FakeRequest(PATCH, "/tasks/0").withJsonBody(Json.toJson("name_" -> "abc"))
			val tasksF = controller.updateTask(Some(0)).apply(req)

			status(tasksF) mustBe BAD_REQUEST
		}

		"return Not Found for non existing id" in {
			val mockTaskService = mock[TaskService]
			val dto = PatchTaskDTO(Some("abc"), Some(true))

			when(mockTaskService.updateTask(0, dto)).thenReturn(
				Future.successful(None)
			)

			val controller = new TodoListController(
				stubControllerComponents(),
				mockTaskService,
				ExecutionContext.global
			)

			val req = FakeRequest(PATCH, "/tasks/0").withJsonBody(Json.toJson(dto))
			val tasksF = controller.updateTask(Some(0)).apply(req)

			status(tasksF) mustBe NOT_FOUND
		}


	}


}
