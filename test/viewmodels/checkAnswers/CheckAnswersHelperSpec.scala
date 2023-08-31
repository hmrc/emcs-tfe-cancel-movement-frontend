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

package viewmodels.checkAnswers

import base.SpecBase
import models.CancelReason.Other
import org.scalatest.matchers.must.Matchers
import pages.{CancelReasonPage, MoreInformationPage}
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList

class CheckAnswersHelperSpec extends SpecBase with Matchers {

  lazy val app = applicationBuilder().build()
  lazy val cyaHelper = app.injector.instanceOf[CheckAnswersHelper]
  lazy val moreInformationSummary = app.injector.instanceOf[MoreInformationSummary]
  implicit lazy val msgs: Messages = messages(app)

  ".summaryList()" - {

    "when there's no answers" - {

      "must output only the More Information row with the option to provide more information" in {

        implicit val answers = emptyUserAnswers

        cyaHelper.summaryList() mustBe SummaryList(Seq(moreInformationSummary.row(showActionLink = true)).flatten)
      }
    }

    "when there are answers" - {

      "must output all expected Summary Rows" in {

        implicit val answers = emptyUserAnswers
          .set(CancelReasonPage, Other)
          .set(MoreInformationPage, Some("Reason"))

        val result = cyaHelper.summaryList()

        result mustBe
          SummaryList(Seq(
            CancelReasonSummary.row(showActionLink = true),
            moreInformationSummary.row(showActionLink = true)
          ).flatten)
      }
    }
  }
}
