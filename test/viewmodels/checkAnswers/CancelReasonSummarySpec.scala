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
import fixtures.messages.CancelReasonMessages
import models.CancelReason.Other
import models.CheckMode
import org.scalatest.matchers.must.Matchers
import pages.CancelReasonPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Value
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

class CancelReasonSummarySpec extends SpecBase with Matchers {

  lazy val app = applicationBuilder().build()

  ".row" - {

    Seq(CancelReasonMessages.English).foreach { messagesForLanguage =>

      s"when being rendered in lang code of '${messagesForLanguage.lang.code}'" - {

        implicit lazy val msgs: Messages = messages(app, messagesForLanguage.lang)

        "when there's no answer" - {

          "must output the expected data" in {

            CancelReasonSummary.row(showActionLink = true)(emptyUserAnswers, msgs) mustBe None
          }
        }
        "when there's an answer" - {

          "when showActionLink boolean is true" - {

            "must output the expected row" in {

              CancelReasonSummary.row(showActionLink = true)(emptyUserAnswers.set(CancelReasonPage, Other), msgs) mustBe Some(
                SummaryListRowViewModel(
                  key = messagesForLanguage.cyaLabel,
                  value = Value(HtmlContent(messagesForLanguage.radio5)),
                  actions = Seq(
                    ActionItemViewModel(
                      content = messagesForLanguage.change,
                      href = controllers.routes.CancelReasonController.onPageLoad(testErn, testArc, CheckMode).url,
                      id = CancelReasonPage
                    ).withVisuallyHiddenText(messagesForLanguage.cyaChangeHidden)
                  )
                )
              )
            }
          }

          "when showActionLink boolean is false" - {

            "must output the expected row without action links" in {

              CancelReasonSummary.row(showActionLink = false)(emptyUserAnswers.set(CancelReasonPage, Other), msgs) mustBe Some(
                SummaryListRowViewModel(
                  key = messagesForLanguage.cyaLabel,
                  value = Value(HtmlContent(messagesForLanguage.radio5)),
                  actions = Seq()
                )
              )
            }
          }
        }
      }
    }
  }
}
