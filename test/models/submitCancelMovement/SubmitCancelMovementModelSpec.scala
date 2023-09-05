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

import base.SpecBase
import fixtures.GetMovementResponseFixtures
import models.CancelReason.Duplicate
import models.DestinationType.TaxWarehouse
import models.common.{AddressModel, TraderModel}
import pages.{CancelReasonPage, ChooseGiveMoreInformationPage, MoreInformationPage}
import play.api.libs.json.Json

class SubmitCancelMovementModelSpec extends SpecBase with GetMovementResponseFixtures {

  "SubmitCancelMovementModel" - {

    val userAnswers = emptyUserAnswers
      .set(CancelReasonPage, Duplicate)
      .set(ChooseGiveMoreInformationPage, true)
      .set(MoreInformationPage, Some("more information"))

    "can be constructed from combination of UserAnswers and MovementDetails as expected" in {

      SubmitCancelMovementModel(getMovementResponseModel)(userAnswers) mustBe
        SubmitCancelMovementModel(
          arc = testArc,
          sequenceNumber = 1,
          cancelReason = Duplicate,
          additionalInformation = Some("more information"),
          consigneeTrader = None,
          destinationType = TaxWarehouse,
          memberStateCode = Some("GB")
        )
    }

    "should write to JSON as expected to match Submission API" - {

      "for max values" in {

        Json.toJson(SubmitCancelMovementModel(getMovementResponseModel.copy(
          consigneeTrader = Some(TraderModel(
            traderExciseNumber = Some(testErn),
            traderName = Some("TraderName"),
            address = Some(AddressModel(
              streetNumber = Some("1"),
              street = Some("Street Name"),
              postcode = Some("TF3 4ER"),
              city = Some("Telford")
            )),
            eoriNumber = None
          ))
        ))(userAnswers)) mustBe
          Json.obj(
            "exciseMovement" -> Json.obj(
              "arc" -> testArc,
              "sequenceNumber" -> 1
            ),
            "cancellationReason" -> Json.obj(
              "reason" -> "3",
              "complementaryInformation" -> "more information"
            ),
            "consigneeTrader" -> Json.obj(
              "traderExciseNumber" -> testErn,
              "traderName" -> "TraderName",
              "address" -> Json.obj(
                "streetNumber" -> "1",
                "street" -> "Street Name",
                "postcode" -> "TF3 4ER",
                "city" -> "Telford"
              )
            ),
            "destinationType" -> "1",
            "memberStateCode" -> "GB"
          )
      }

      "for min values" in {

        Json.toJson(SubmitCancelMovementModel(getMovementResponseModel.copy(memberStateCode = None))(userAnswers.remove(MoreInformationPage))) mustBe
          Json.obj(
            "exciseMovement" -> Json.obj(
              "arc" -> testArc,
              "sequenceNumber" -> 1
            ),
            "cancellationReason" -> Json.obj(
              "reason" -> "3"
            ),
            "destinationType" -> "1"
          )
      }
    }
  }
}
