package models

import play.api.libs.json.{JsPath, Json, JsonConfiguration, Reads, Writes, __}
import play.api.libs.functional.syntax._

case class Task(id: Int, name: String, completed: Boolean, deleted: Boolean)

trait TaskJson {
	/*
	implicit val writer: Writes[Task] = (
	  (__ \ implicitly[JsonConfiguration].naming("title")).write[String] and
		(__ \ implicitly[JsonConfiguration].naming("deleted")).write[Boolean] and
			(__ \ implicitly[JsonConfiguration].naming("checked")).write[Boolean]
	  )(unlift(Task.unapply))

	implicit val reads: Reads[Task] = (
	  (JsPath \ "title").read[String] and
		(JsPath \ "deleted").read[Boolean] and
		(JsPath \ "checked'").read[Boolean]
	  )(Task.apply _)
	 */

	implicit val writer: Writes[Task] = Json.writes
	implicit val reads: Reads[Task] = Json.reads
}

object Task extends TaskJson {}
