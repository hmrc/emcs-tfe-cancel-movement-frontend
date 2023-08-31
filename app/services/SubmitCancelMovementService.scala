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

import connectors.emcsTfe.SubmitCancelMovementConnector
import models.audit.SubmitCancelMovementAuditModel
import models.submitCancelMovement.SubmitCancelMovementModel
import models.requests.DataRequest
import models.response.emcsTfe.SubmitCancelMovementResponse
import models.{ErrorResponse, SubmitCancelMovementException}
import uk.gov.hmrc.http.HeaderCarrier
import utils.Logging

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SubmitCancelMovementService @Inject()(submitShortageOrExcessConnector: SubmitCancelMovementConnector,
                                            auditingService: AuditingService)
                                           (implicit ec: ExecutionContext) extends Logging {

  def submit(ern: String, arc: String)(implicit hc: HeaderCarrier, dataRequest: DataRequest[_]): Future[SubmitCancelMovementResponse] = {

    val submissionRequest = SubmitCancelMovementModel(dataRequest.movementDetails)(dataRequest.userAnswers)

    submitShortageOrExcessConnector.submit(ern, submissionRequest).map {
      withAuditEvent(submissionRequest, _)
        .getOrElse(throw SubmitCancelMovementException(s"Failed to submit Cancel Movement to emcs-tfe for ern: '$ern' & arc: '$arc'"))
    }
  }

  private def withAuditEvent(submissionRequest: SubmitCancelMovementModel,
                             submissionResponse: Either[ErrorResponse, SubmitCancelMovementResponse])
                            (implicit hc: HeaderCarrier, dataRequest: DataRequest[_]): Either[ErrorResponse, SubmitCancelMovementResponse] = {
    if(submissionResponse.isLeft) {
      logger.warn(s"[submit] Failed to submit Cancel Movement to emcs-tfe for ern: '${dataRequest.ern}' and arc: '${dataRequest.arc}'")
    }
    auditingService.audit(
      SubmitCancelMovementAuditModel(
        submissionRequest = submissionRequest,
        submissionResponse = submissionResponse
      )
    )
    submissionResponse
  }
}
