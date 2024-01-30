package util

import com.google.inject.{AbstractModule, Inject, Provides}
import com.typesafe.config.ConfigFactory
import daos.{TaskDAO, TaskDAODatabaseImpl}
import io.sentry.Sentry
import play.api.Configuration
import services.{TaskService, TaskServiceImpl}

class ProductionModule extends AbstractModule {

	override def configure() = {
		bind(classOf[TaskDAO])
		 .to(classOf[TaskDAODatabaseImpl])
		 .asEagerSingleton()

		bind(classOf[TaskService])
		  .to(classOf[TaskServiceImpl])
		  .asEagerSingleton()

		val config = new Configuration(ConfigFactory.load())
		val uri = config.get[String]("sentry.uri")
		Sentry.init(uri)

	}
}