package daos

import models.Task
import enums.TaskStatus
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class TaskDAODatabaseSpec
    extends PlaySpec
      with GuiceOneAppPerTest
      with Injecting
      with MockitoSugar {

  "TaskDAODatabase getAllTasks" should {
    "return all from storage" in {
      val tasks = Seq(
        Task(0, "task 1", TaskStatus.Completed, None),
        Task(1, "task 1", TaskStatus.Completed, None),
        Task(2, "task 1", TaskStatus.Completed, None)
      )

      tasks mustBe tasks
    }
  }

}
