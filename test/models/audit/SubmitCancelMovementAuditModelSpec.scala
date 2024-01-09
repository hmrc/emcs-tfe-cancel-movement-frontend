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

import base.SpecBase
import fixtures.audit.SubmitCancelMovementAuditModelFixtures
import models.DestinationType
import models.submitCancelMovement.SubmitCancelMovementModel

class SubmitCancelMovementAuditModelSpec extends SpecBase with SubmitCancelMovementAuditModelFixtures {

  "SubmitCancelMovementAuditModel" - {

    "should write a correct audit json" - {

      DestinationType.values.foreach { destinationType =>

        val model: SubmitCancelMovementModel = submitCancelMovementModel.copy(destinationType = destinationType)

        s"for destination type of: $destinationType" - {
          "when a successful submission has occurred" in {
            submitCancelMovementAuditSuccessful(model).auditType mustBe "CancelMovementSubmission"
            submitCancelMovementAuditSuccessful(model).detail mustBe submitCancelMovementAuditSuccessfulJSON(model)
          }

          "when a failed to submit has occurred" in {
            submitCancelMovementAuditFailed(model).auditType mustBe "CancelMovementSubmission"
            submitCancelMovementAuditFailed(model).detail mustBe submitCancelMovementAuditFailedJSON(model)
          }
        }
      }
    }
  }
}
