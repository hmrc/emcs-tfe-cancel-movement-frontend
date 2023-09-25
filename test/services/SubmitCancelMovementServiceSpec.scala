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

package services

import base.SpecBase
import fixtures.SubmitCancelMovementFixtures
import mocks.connectors.MockSubmitCancelMovementConnector
import mocks.services.MockAuditingService
import models.CancelReason.Other
import models.audit.SubmitCancelMovementAuditModel
import models.submitCancelMovement.SubmitCancelMovementModel
import models.{SubmitCancelMovementException, UnexpectedDownstreamResponseError}
import pages.{CancelReasonPage, MoreInformationPage}
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class SubmitCancelMovementServiceSpec extends SpecBase
  with MockSubmitCancelMovementConnector
  with SubmitCancelMovementFixtures
  with MockAuditingService {

  implicit val hc = HeaderCarrier()
  implicit val ec = ExecutionContext.global

  lazy val testService = new SubmitCancelMovementService(mockSubmitCancelMovementConnector, mockAuditingService)

  val userAnswers = emptyUserAnswers
    .set(CancelReasonPage, Other)
    .set(MoreInformationPage, Some("reason"))

  ".submit(ern: String, submission: SubmitCancelMovementModel)" - {

    "should submit, audit and return a success response" - {

      "when connector receives a success from downstream" in {

        val request = dataRequest(FakeRequest(), userAnswers)

        val submission = SubmitCancelMovementModel(getMovementResponseModel)(userAnswers)

        MockSubmitCancelMovementConnector.submit(testErn, submission).returns(Future.successful(Right(successResponse)))

        MockAuditingService.audit(
          SubmitCancelMovementAuditModel(
            submission,
            Right(successResponse)
          )(dataRequest(FakeRequest()))
        ).noMoreThanOnce()

        testService.submit(testErn, testArc)(hc, request).futureValue mustBe successResponse
      }
    }

    "should submit, audit and return a failure response" - {

      "when connector receives a failure from downstream" in {

        val request = dataRequest(FakeRequest(), userAnswers)
        val submission = SubmitCancelMovementModel(getMovementResponseModel)(userAnswers)

        MockSubmitCancelMovementConnector.submit(testErn, submission).returns(Future.successful(Left(UnexpectedDownstreamResponseError)))

        MockAuditingService.audit(
          SubmitCancelMovementAuditModel(
            submission,
            Left(UnexpectedDownstreamResponseError)
          )(dataRequest(FakeRequest()))
        ).noMoreThanOnce()

        intercept[SubmitCancelMovementException](await(testService.submit(testErn, testArc)(hc, request))).getMessage mustBe
          s"Failed to submit Cancel Movement to emcs-tfe for ern: '$testErn' & arc: '$testArc'"
      }
    }
  }
}