package util


import controllers.TodoListController
import org.apache.pekko.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

import javax.inject.Inject

class TodoListRouter @Inject()(val controller: TodoListController) extends SimpleRouter {
	override def routes: Routes = {

		case GET(p"/") =>
			controller.getAllTasks()

		case POST(p"/") =>
			controller.createTask()

		case PATCH(p"/$id") =>
			controller.updateTask(id.toIntOption)

		case DELETE(p"/$id>") =>
			controller.deleteTask(id.toIntOption)

		case DELETE(p"/delete-completed") =>
			controller.deleteCompleted()

		case PATCH(p"/toggle-completed/$completed") =>
			controller.toggleCompleted(completed.toBooleanOption)

	}

}