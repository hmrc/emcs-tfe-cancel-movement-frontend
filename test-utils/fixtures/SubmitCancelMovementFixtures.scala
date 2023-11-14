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

package fixtures

import models.CancelReason.Other
import models.DestinationType.TaxWarehouse
import models.response.emcsTfe.SubmitCancelMovementResponse
import models.submitCancelMovement.SubmitCancelMovementModel
import play.api.libs.json.Json

trait SubmitCancelMovementFixtures extends BaseFixtures {

  val submitCancelMovementModel = SubmitCancelMovementModel(
    arc = testArc,
    sequenceNumber = 1,
    cancelReason = Other,
    additionalInformation = Some("more information about explaining the delay"),
    consigneeTrader = None,
    destinationType = TaxWarehouse,
    memberStateCode = Some("GB")
  )

  val successResponseChRIS = SubmitCancelMovementResponse(receipt = testConfirmationReference, "ChRIS")

  val successResponseEIS = SubmitCancelMovementResponse(receipt = testConfirmationReference, "EIS")

  val successResponseChRISJson = Json.obj("receipt" -> testConfirmationReference)

  val successResponseEISJson = Json.parse(
    s"""{
       | "status": "OK",
       | "message": "$testConfirmationReference",
       | "emcsCorrelationId": "3e8dae97-b586-4cef-8511-68ac12da9028"
       |}""".stripMargin)

  val successResponseJson = Json.obj("receipt" -> testConfirmationReference)
}
