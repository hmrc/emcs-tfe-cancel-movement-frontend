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

package models.audit

import models.ErrorResponse
import models.requests.DataRequest
import models.response.emcsTfe.SubmitCancelMovementResponse
import models.submitCancelMovement.SubmitCancelMovementModel
import play.api.libs.json.{JsValue, Json}
import utils.JsonOptionFormatter

case class SubmitCancelMovementAuditModel(
                                           submissionRequest: SubmitCancelMovementModel,
                                           submissionResponse: Either[ErrorResponse, SubmitCancelMovementResponse]
                                         )(implicit request: DataRequest[_]) extends AuditModel with JsonOptionFormatter {

  override val auditType: String = "cancelMovementSubmission"

  override val detail: JsValue = jsonObjNoNulls(fields =
    "credentialId" -> request.credId,
    "internalId" -> request.internalId,
    "ern" -> request.ern,
    "arc" -> submissionRequest.arc,
    "sequenceNumber" -> submissionRequest.sequenceNumber,
    "consigneeTrader" -> Json.toJson(submissionRequest.consigneeTrader),
    "destinationType" -> Json.toJson(submissionRequest.destinationType),
    "memberStateCode" -> Json.toJson(submissionRequest.memberStateCode),
    "cancelReason" -> Json.toJson(submissionRequest.cancelReason),
    "additionalInformation" -> Json.toJson(submissionRequest.additionalInformation)
  ) ++ {
    submissionResponse match {
      case Right(success) =>
        Json.obj(fields =
          "status" -> "success",
          "receipt" -> success.receipt
        )
      case Left(failedMessage) =>
        Json.obj(fields =
          "status" -> "failed",
          "failedMessage" -> failedMessage.message
        )
    }
  }
}