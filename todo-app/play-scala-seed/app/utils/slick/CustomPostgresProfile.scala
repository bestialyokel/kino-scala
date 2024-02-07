package utils.slick

import com.github.tminglei.slickpg._
import enumeratum.SlickEnumSupport
import models.TaskStatus
import slick.ast.BaseTypedType
import slick.jdbc

trait CustomPostgresProfile extends ExPostgresProfile with PgDate2Support with SlickEnumSupport {

  override val api = CustomAPI

  object CustomAPI extends API with DateTimeImplicits with Date2DateTimePlainImplicits {

    implicit lazy val taskStatus: jdbc.JdbcType[TaskStatus] with BaseTypedType[TaskStatus] =
      mappedColumnTypeForEnum(TaskStatus)

  }

}

object CustomPostgresProfile extends CustomPostgresProfile
