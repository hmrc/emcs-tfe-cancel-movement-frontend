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

package models.submitCancelMovement

import models.common.TraderModel
import models.response.emcsTfe.GetMovementResponse
import models.{CancelReason, DestinationType, UserAnswers}
import pages.{CancelReasonPage, MoreInformationPage}
import play.api.libs.json.{Json, Writes}
import utils.{JsonOptionFormatter, ModelConstructorHelpers}

case class SubmitCancelMovementModel(arc: String,
                                     sequenceNumber: Int,
                                     cancelReason: CancelReason,
                                     additionalInformation: Option[String],
                                     consigneeTrader: Option[TraderModel],
                                     destinationType: DestinationType,
                                     memberStateCode: Option[String])

object SubmitCancelMovementModel extends JsonOptionFormatter with ModelConstructorHelpers {

  def apply(movementDetails: GetMovementResponse)(implicit userAnswers: UserAnswers): SubmitCancelMovementModel =
    SubmitCancelMovementModel(
      arc = movementDetails.arc,
      sequenceNumber = movementDetails.sequenceNumber,
      cancelReason = mandatoryPage(CancelReasonPage),
      additionalInformation = userAnswers.get(MoreInformationPage).flatten,
      consigneeTrader = movementDetails.consigneeTrader,
      destinationType = movementDetails.destinationType,
      memberStateCode = movementDetails.memberStateCode
    )

  implicit val fmt: Writes[SubmitCancelMovementModel] = Writes { model =>
    jsonObjNoNulls(
      "exciseMovement" -> Json.obj(
        "arc" -> model.arc,
        "sequenceNumber" -> model.sequenceNumber
      ),
      "cancellationReason" -> jsonObjNoNulls(
        "reason" -> Json.toJson(model.cancelReason)(CancelReason.submissionWrites),
        "complementaryInformation" -> Json.toJson(model.additionalInformation)
      ),
      "consigneeTrader" -> Json.toJson(model.consigneeTrader),
      "destinationType" -> Json.toJson(model.destinationType),
      "memberStateCode" -> Json.toJson(model.memberStateCode)
    )
  }

}
