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
import featureswitch.core.config.EnableNRS
import fixtures.{NRSBrokerFixtures, SubmitCancelMovementFixtures}
import mocks.MockAppConfig
import mocks.connectors.MockSubmitCancelMovementConnector
import mocks.services.{MockAuditingService, MockNRSBrokerService}
import models.CancelReason.Other
import models.audit.SubmitCancelMovementAuditModel
import models.submitCancelMovement.SubmitCancelMovementModel
import models.{SubmitCancelMovementException, UnexpectedDownstreamResponseError}
import pages.{CancelReasonPage, MoreInformationPage}
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class SubmitCancelMovementServiceSpec extends SpecBase
  with MockSubmitCancelMovementConnector
  with SubmitCancelMovementFixtures
  with MockAuditingService
  with MockNRSBrokerService
  with MockAppConfig
  with NRSBrokerFixtures {

  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  lazy val testService = new SubmitCancelMovementService(mockSubmitCancelMovementConnector, mockNRSBrokerService, mockAuditingService, mockAppConfig)

  val userAnswers = emptyUserAnswers
    .set(CancelReasonPage, Other)
    .set(MoreInformationPage, Some("reason"))

  class Fixture(isNRSEnabled: Boolean) {
    MockAppConfig.getFeatureSwitchValue(EnableNRS).returns(isNRSEnabled)
  }

  ".submit(ern: String, submission: SubmitCancelMovementModel)" - {

    Seq(true, false).foreach { nrsEnabled =>

      s"when NRS enabled is '$nrsEnabled'" - {

        "should submit, audit and return a success response" - {

          "when connector receives a success from downstream" in new Fixture(nrsEnabled) {

            val request = dataRequest(FakeRequest(), userAnswers)

            val submission = SubmitCancelMovementModel(getMovementResponseModel)(userAnswers)

            MockSubmitCancelMovementConnector.submit(testErn, submission).returns(Future.successful(Right(successResponseChRIS)))

            MockAuditingService.audit(
              SubmitCancelMovementAuditModel(
                submission,
                Right(successResponseChRIS)
              )(dataRequest(FakeRequest()))
            ).noMoreThanOnce()

            if (nrsEnabled) {
              MockNRSBrokerService.submitPayload(submission, testErn)
                .returns(Future.successful(Right(nrsBrokerResponseModel)))
            } else {
              MockNRSBrokerService.submitPayload(submission, testErn).never()
            }

            testService.submit(testErn, testArc)(hc, request).futureValue mustBe successResponseChRIS
          }
        }

        "should submit, audit and return a failure response" - {

          "when connector receives a failure from downstream" in new Fixture(nrsEnabled) {

            val request = dataRequest(FakeRequest(), userAnswers)
            val submission = SubmitCancelMovementModel(getMovementResponseModel)(userAnswers)

            MockSubmitCancelMovementConnector.submit(testErn, submission).returns(Future.successful(Left(UnexpectedDownstreamResponseError)))

            MockAuditingService.audit(
              SubmitCancelMovementAuditModel(
                submission,
                Left(UnexpectedDownstreamResponseError)
              )(dataRequest(FakeRequest()))
            ).noMoreThanOnce()

            if (nrsEnabled) {
              MockNRSBrokerService.submitPayload(submission, testErn)
                .returns(Future.successful(Right(nrsBrokerResponseModel)))
            } else {
              MockNRSBrokerService.submitPayload(submission, testErn).never()
            }

            intercept[SubmitCancelMovementException](await(testService.submit(testErn, testArc)(hc, request))).getMessage mustBe
              s"Failed to submit Cancel Movement to emcs-tfe for ern: '$testErn' & arc: '$testArc'"
          }
        }
      }
    }
  }
}
