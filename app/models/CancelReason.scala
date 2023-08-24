/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait CancelReason

object CancelReason extends Enumerable.Implicits {

  case object TransactionInterrupted extends WithName("transactionInterrupted") with CancelReason
  case object Duplicate extends WithName("duplicate") with CancelReason
  case object DifferentDispatchDate extends WithName("differentDispatchDate") with CancelReason
  case object ContainsError extends WithName("containsError") with CancelReason
  case object Other extends WithName("other") with CancelReason

  val values: Seq[CancelReason] = Seq(
    TransactionInterrupted,
    Duplicate,
    DifferentDispatchDate,
    ContainsError,
    Other
  )

  def options(implicit messages: Messages): Seq[RadioItem] = values.zipWithIndex.map {
    case (value, index) =>
      RadioItem(
        content = Text(messages(s"cancelReason.${value.toString}")),
        value   = Some(value.toString),
        id      = Some(s"value_$index")
      )
  }

  implicit val enumerable: Enumerable[CancelReason] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
