package enums

import enumeratum.EnumEntry.Snakecase
import enumeratum.{Enum, EnumEntry, PlayEnum}

sealed trait TaskStatus extends EnumEntry with Snakecase

case object TaskStatus extends Enum[TaskStatus] with PlayEnum[TaskStatus] {
  val values = findValues
  case object Completed extends TaskStatus
  case object Incompleted extends TaskStatus
}
