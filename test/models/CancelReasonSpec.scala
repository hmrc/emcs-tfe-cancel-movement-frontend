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

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json, Writes}

class CancelReasonSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "CancelReason" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(CancelReason.values)

      forAll(gen) {
        cancelReason =>

          JsString(cancelReason.toString).validate[CancelReason].asOpt.value mustEqual cancelReason
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!CancelReason.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[CancelReason] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(CancelReason.values)

      forAll(gen) {
        cancelReason =>

          Json.toJson(cancelReason) mustEqual JsString(cancelReason.toString)
      }
    }

    "have submissionWrites with the correct mapping" in {

      implicit val writes: Writes[CancelReason] = CancelReason.submissionWrites

      Json.toJson[CancelReason](CancelReason.Other) mustBe JsString("0")
      Json.toJson[CancelReason](CancelReason.ContainsError) mustBe JsString("1")
      Json.toJson[CancelReason](CancelReason.TransactionInterrupted) mustBe JsString("2")
      Json.toJson[CancelReason](CancelReason.Duplicate) mustBe JsString("3")
      Json.toJson[CancelReason](CancelReason.DifferentDispatchDate) mustBe JsString("4")
    }
  }
}
