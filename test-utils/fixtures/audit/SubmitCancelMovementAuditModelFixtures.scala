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

package fixtures.audit

import fixtures.{BaseFixtures, GetMovementResponseFixtures, SubmitCancelMovementFixtures}
import models.UnexpectedDownstreamResponseError
import models.audit.SubmitCancelMovementAuditModel
import models.requests.{DataRequest, MovementRequest, UserRequest}
import models.submitCancelMovement.SubmitCancelMovementModel
import play.api.libs.json.{JsValue, Json}
import play.api.test.FakeRequest
import utils.JsonOptionFormatter.jsonObjNoNulls

trait SubmitCancelMovementAuditModelFixtures extends BaseFixtures
  with SubmitCancelMovementFixtures
  with GetMovementResponseFixtures {

  def submitCancelMovementAuditSuccessful(model: SubmitCancelMovementModel = submitCancelMovementModel): SubmitCancelMovementAuditModel =
    SubmitCancelMovementAuditModel(
      submissionRequest = model,
      submissionResponse = Right(successResponseChRIS)
    )(DataRequest(MovementRequest(UserRequest(FakeRequest(), testErn, testInternalId, testCredId, false), testArc, getMovementResponseModel), emptyUserAnswers, testMinTraderKnownFacts))

  def submitCancelMovementAuditSuccessfulJSON(model: SubmitCancelMovementModel = submitCancelMovementModel): JsValue = jsonObjNoNulls(
    "credentialId" -> testCredId,
    "internalId" -> testInternalId,
    "ern" -> testErn,
    "arc" -> testArc,
    "sequenceNumber" -> 1,
    "consigneeTrader" -> Json.toJson(model.consigneeTrader),
    "destinationType" -> Json.toJson(model.destinationType.auditValue),
    "memberStateCode" -> Json.toJson(model.memberStateCode),
    "cancelReason" -> Json.toJson(model.cancelReason),
    "additionalInformation" -> Json.toJson(model.additionalInformation),
    "status" -> "success",
    "receipt" -> testConfirmationReference
  )

  def submitCancelMovementAuditFailed(model: SubmitCancelMovementModel = submitCancelMovementModel): SubmitCancelMovementAuditModel =
    SubmitCancelMovementAuditModel(
      submissionRequest = model,
      submissionResponse = Left(UnexpectedDownstreamResponseError)
    )(DataRequest(
      MovementRequest(
        UserRequest(FakeRequest(), testErn, testInternalId, testCredId, false),
        testArc,
        getMovementResponseModel
      ),
      emptyUserAnswers,
      testMinTraderKnownFacts
    ))

  def submitCancelMovementAuditFailedJSON(model: SubmitCancelMovementModel = submitCancelMovementModel): JsValue = jsonObjNoNulls(
    "credentialId" -> testCredId,
    "internalId" -> testInternalId,
    "ern" -> testErn,
    "arc" -> testArc,
    "sequenceNumber" -> 1,
    "consigneeTrader" -> Json.toJson(model.consigneeTrader),
    "destinationType" -> Json.toJson(model.destinationType.auditValue),
    "memberStateCode" -> Json.toJson(model.memberStateCode),
    "cancelReason" -> Json.toJson(model.cancelReason),
    "additionalInformation" -> Json.toJson(model.additionalInformation),
    "status" -> "failed",
    "failedMessage" -> "Unexpected downstream response status"
  )
}
