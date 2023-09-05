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

package mocks.connectors

import connectors.emcsTfe.SubmitCancelMovementConnector
import models.ErrorResponse
import models.response.emcsTfe.SubmitCancelMovementResponse
import models.submitCancelMovement.SubmitCancelMovementModel
import org.scalamock.handlers.CallHandler4
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockSubmitCancelMovementConnector extends MockFactory {

  lazy val mockSubmitCancelMovementConnector: SubmitCancelMovementConnector = mock[SubmitCancelMovementConnector]

  object MockSubmitCancelMovementConnector {

    def submit(ern: String,
               submission: SubmitCancelMovementModel): CallHandler4[String, SubmitCancelMovementModel, HeaderCarrier, ExecutionContext, Future[Either[ErrorResponse, SubmitCancelMovementResponse]]] =
      (mockSubmitCancelMovementConnector.submit(_: String, _: SubmitCancelMovementModel)(_: HeaderCarrier, _: ExecutionContext))
        .expects(ern, submission, *, *)
  }
}
