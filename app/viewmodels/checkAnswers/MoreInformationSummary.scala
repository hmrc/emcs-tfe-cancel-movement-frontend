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

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.MoreInformationPage
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._
import views.html.components.link
import utils.JsonOptionFormatter._

import javax.inject.Inject


class MoreInformationSummary @Inject()(link: link) {
  def row(showActionLink: Boolean)(implicit answers: UserAnswers, messages: Messages): Option[SummaryListRow] = {

    answers.get(MoreInformationPage).flatten match {

      case Some(answer) =>
        Some(SummaryListRowViewModel(
          key = "moreInformation.checkYourAnswers.label",
          value = ValueViewModel(HtmlContent(
            HtmlFormat.escape(answer)
          )),
          actions = if(!showActionLink) Seq() else Seq(
            ActionItemViewModel(
              content = "site.change",
              href = routes.MoreInformationController.onPageLoad(answers.ern, answers.arc, CheckMode).url,
              id = MoreInformationPage
            ).withVisuallyHiddenText(messages("moreInformation.checkYourAnswers.change.hidden"))
          )
        ))
      case _ if showActionLink =>
        Some(SummaryListRowViewModel(
          key = "moreInformation.checkYourAnswers.label",
          value = ValueViewModel(HtmlContent(
            link(
              routes.MoreInformationController.onPageLoad(answers.ern, answers.arc, CheckMode).url,
              "moreInformation.checkYourAnswers.addInformation"
            )
          ))
        ))
      case _ => None
    }
  }
}